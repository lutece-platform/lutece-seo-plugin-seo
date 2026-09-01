![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=seo-plugin-seo-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)

# Plugin SEO

## Introduction

This plugin gathers the features a site needs to be indexed properly by search engines : friendly URLs, canonical URLs, a sitemap, and URL rewriting rules. All of them are managed from a single back office feature, *SEO management*, and stored in two tables : `seo_friendly_url` and `seo_rule`.

## Friendly URLs

The friendly URLs module lets you expose a readable URL for any resource of your site.

For example the technical URL :

`http://mysite/jsp/site/Portal.jsp?page_id=122`

becomes accessible under a much friendlier one :

`http://mysite/pages/my_topic.html`

They are not written by hand. Generators walk the content of the site and derive one URL per resource from its name : *Budget 2024*, page 122, yields `/budget-2024`. Three options shape the result — inherit the path of the parent pages, append a `.html` suffix, and overwrite the URLs already generated. Any plugin may contribute its own generator by implementing `FriendlyUrlGenerator`.

Pages keep linking to their technical URL unless the content post processor is enabled : it rewrites the links of every page served, so that visitors and search engines only ever see the friendly ones.

## Canonical URLs

When the same resource is reachable under several URLs, search engines have to be told which one to index, otherwise they split its ranking or pick one themselves. This module adds a `<link rel="canonical">` element to the pages served, pointing at the friendly URL flagged as canonical.

It is switched on and off from the back office, and each friendly URL carries its own canonical flag.

## Sitemap

A sitemap is the list of the URLs of a site, in the XML format defined by [sitemaps.org](https://www.sitemaps.org), meant for search engine crawlers only — it is never shown to visitors, unlike the site map page of the site. It holds every friendly URL flagged for the sitemap, with the last modification date of the underlying resource.

The file is generated **outside the webapp**, and served by the plugin itself on `/sitemap-seo.xml` — the path to declare in the `Sitemap` directive of the `robots.txt` of the site. See *Generated files* below.

The locations it publishes have to be absolute URLs, which the protocol requires and search engines enforce : they are built from `lutece.prod.url`, a property of lutece-core the site declares per environment. **Left undefined, the sitemap is generated with relative locations, which search engines reject**, and every generation logs an error saying so.

## URL rewriting

The URL rewriting module lets you create rules mapping a public URL onto the technical URL that serves it. For example :

`http://mysite/app/wiki`

is served by

`http://mysite/jsp/site/Portal.jsp?page=wiki`

The rewriting happens inside the application, as a forward : the browser keeps the public URL in its address bar, and no redirection is sent. Two kinds of rules coexist — the generic rules of `seo_rule`, which use regular expressions and capture groups to cover a whole family of URLs at once, and one rule per row of `seo_friendly_url`, anchored at both ends.

The filter reads the rules from those two tables and reloads them when they change, so a publication from the back office reaches every node of a cluster. No configuration file is involved.

## URL rewriting diagnostic page

UrlRewriteFilter ships a diagnostic page displaying the rewrite rules actually loaded in memory, their parsing errors and the current request info. It tells whether a rule published by the plugin has really been read by the filter, which the database alone cannot tell.

It is **disabled by default**. To enable it on a development environment, set `seo.urlrewriter.statusEnabled=true` in `WEB-INF/conf/plugins/seo.properties` ; the page is then served on `<context-path>/status` (value of `seo.urlrewriter.statusPath`).

Every init parameter of the `seo_urlrewriter` filter declared in `WEB-INF/plugins/seo.xml` can be overridden that way, under the `seo.urlrewriter.` prefix. Resolution goes through the configuration API of the core, so a value may equally come from a system property or an environment variable such as `SEO_URLREWRITER_STATUSENABLED`.

Do not leave the page enabled in production : access is filtered on the `Host` header (`seo.urlrewriter.statusEnabledOnHosts`), not on the client address.

## Generated files

The sitemap is written to `sitemap-seo.xml` in the directory given by the `seo.generatedFilesPath` property of `WEB-INF/conf/plugins/seo.properties`. That directory lies outside the webapp : a webapp is a build artefact, replaced at every deployment, so a generated file kept inside it is lost — and on a site shipping its own `sitemap.xml`, overwritten.

Left undefined, a subdirectory of the system temporary directory is used. That default is always writable, and harmless, since the daemon rebuilds the sitemap every hour and the servlet generates it on demand when the file is missing. Point the property at a persistent volume to keep the file across restarts.

Since no servlet container serves a file living outside the webapp, `SitemapServlet` publishes it, and a rewrite rule of this plugin exposes it on `/sitemap-seo.xml`.

**Migration.** Former versions wrote the sitemap inside the webapp, as `sitemap.xml` then `sitemap-seo.xml`. Remove any such file left at the webapp root and point the `Sitemap` directive of `robots.txt` at `/sitemap-seo.xml` : as long as one is present, the web server keeps serving that frozen file to the search engines that discovered it, and every generation logs a reminder. Note that `robots.txt` belongs to the repository of each site, not to this plugin, and that it accepts several `Sitemap` directives.

The rewrite rules are no longer written to a file. The `seo.configFilePath` property and the `confPath` parameter of the filter have been removed.

## Configuration

| Property | Default | Purpose |
|----------|---------|---------|
| `seo.generatedFilesPath` | a subdirectory of the system temporary directory | Directory of the generated files, outside the webapp |
| `daemon.seoFriendlyUrlGenerator.interval` | `3600` | Seconds between two runs of the friendly URL generator |
| `daemon.seoSitemapGenerator.interval` | `3600` | Seconds between two generations of the sitemap |
| `seo.urlrewriter.*` | the value declared in `seo.xml` | Overrides an init parameter of the rewrite filter |

`lutece.prod.url` is read as well, but belongs to lutece-core : the site declares it per environment, in `conf/override/config.properties`. This plugin only reads it.

## Daemons

`seoFriendlyUrlGenerator` regenerates the friendly URLs from the content of the site, then publishes the rewrite rules. `seoSitemapGenerator` regenerates the sitemap. Both run once at startup and then every hour, and each one can be switched off from the back office, independently of the interval.


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-seo/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*
