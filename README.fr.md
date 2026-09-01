![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=seo-plugin-seo-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-seo&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-seo)

# Plugin SEO

## Introduction

Ce plugin rassemble ce dont un site a besoin pour être correctement indexé par les moteurs de recherche : URL explicites, URL canoniques, sitemap et règles de réécriture d'URL. Le tout se gère depuis une seule fonctionnalité du back office, *Gestion SEO*, et vit dans deux tables : `seo_friendly_url` et `seo_rule`.

## URL explicites

Le module des URL explicites vous permet d'exposer une URL lisible pour n'importe quelle ressource de votre site.

Par exemple l'URL technique :

`http://mysite/jsp/site/Portal.jsp?page_id=122`

devient accessible sous une forme bien plus conviviale :

`http://mysite/pages/my_topic.html`

Elles ne sont pas saisies à la main. Des générateurs parcourent le contenu du site et dérivent une URL par ressource à partir de son nom : *Budget 2024*, page 122, donne `/budget-2024`. Trois options en modulent la forme — hériter du chemin des pages parentes, ajouter un suffixe `.html`, et écraser les URL déjà générées. Tout plugin peut fournir son propre générateur en implémentant `FriendlyUrlGenerator`.

Les pages continuent de pointer vers l'URL technique tant que le post-processeur de contenu n'est pas activé : il réécrit les liens de chaque page servie, de sorte que visiteurs et moteurs ne voient jamais que les URL explicites.

## URL canoniques

Lorsqu'une même ressource est joignable sous plusieurs URL, il faut indiquer aux moteurs laquelle indexer, sans quoi ils dispersent son classement ou en choisissent une d'eux-mêmes. Ce module ajoute aux pages servies un élément `<link rel="canonical">` pointant vers l'URL explicite marquée comme canonique.

Le mécanisme s'active depuis le back office, et chaque URL explicite porte son propre marqueur de canonicité.

## Sitemap

Un sitemap est la liste des URL d'un site, au format XML défini par [sitemaps.org](https://www.sitemaps.org), destinée aux **seuls** robots d'indexation — il n'est jamais présenté aux visiteurs, contrairement au plan du site. Il contient toutes les URL explicites marquées comme devant y figurer, avec la date de dernière modification de la ressource correspondante.

Le fichier est généré **hors de la webapp**, et servi par le plugin lui-même sur `/sitemap-seo.xml` — le chemin à déclarer dans la directive `Sitemap` du `robots.txt` du site. Voir *Fichiers générés* ci-dessous.

Les localisations qu'il publie doivent être des URL absolues, comme l'exige le protocole et comme le vérifient les moteurs : elles sont construites à partir de `lutece.prod.url`, une propriété de lutece-core que le site renseigne par environnement. **Laissée vide, le sitemap est généré avec des localisations relatives, que les moteurs rejettent**, et chaque génération journalise une erreur en ce sens.

## Réécriture d'URL

Le module de réécriture d'URL vous permet de créer des règles associant une URL publique à l'URL technique qui la sert. Par exemple :

`http://mysite/app/wiki`

est servie par

`http://mysite/jsp/site/Portal.jsp?page=wiki`

La réécriture a lieu au sein de l'application, sous forme de *forward* : le navigateur conserve l'URL publique dans sa barre d'adresse, et aucune redirection n'est émise. Deux natures de règles coexistent — les règles génériques de `seo_rule`, qui emploient des expressions régulières et des groupes de capture pour couvrir d'un coup toute une famille d'URL, et une règle par ligne de `seo_friendly_url`, ancrée aux deux extrémités.

Le filtre lit les règles dans ces deux tables et les recharge lorsqu'elles changent : une publication depuis le back office atteint donc tous les nœuds d'un cluster. Aucun fichier de configuration n'intervient.

## Page de diagnostic de la réécriture d'URL

Le filtre UrlRewriteFilter embarque une page de diagnostic qui affiche les règles de réécriture réellement chargées en mémoire, leurs erreurs d'analyse et les informations de la requête courante. Elle permet de savoir si une règle publiée par le plugin a bien été relue par le filtre, ce que la base seule ne dit pas.

Elle est **désactivée par défaut**. Pour l'activer sur un environnement de développement, poser `seo.urlrewriter.statusEnabled=true` dans `WEB-INF/conf/plugins/seo.properties` ; la page est alors servie sur `<context-path>/status` (valeur de `seo.urlrewriter.statusPath`).

Tout paramètre d'initialisation du filtre `seo_urlrewriter` déclaré dans `WEB-INF/plugins/seo.xml` peut être surchargé de cette manière, sous le préfixe `seo.urlrewriter.`. La résolution passant par l'API de configuration du core, la valeur peut tout aussi bien venir d'une propriété système ou d'une variable d'environnement telle que `SEO_URLREWRITER_STATUSENABLED`.

À ne pas laisser active en production : l'accès est filtré sur l'en-tête `Host` (`seo.urlrewriter.statusEnabledOnHosts`), pas sur l'adresse du client.

## Fichiers générés

Le sitemap est écrit dans `sitemap-seo.xml`, au sein du répertoire désigné par la propriété `seo.generatedFilesPath` de `WEB-INF/conf/plugins/seo.properties`. Ce répertoire est situé hors de la webapp : une webapp est un artefact de build, remplacé à chaque déploiement, donc un fichier généré qui y réside est perdu — et sur un site qui livre son propre `sitemap.xml`, écrasé.

Laissée indéfinie, la propriété fait retomber sur un sous-répertoire du répertoire temporaire système. Ce défaut est toujours inscriptible, et sans conséquence puisque le daemon reconstruit le sitemap chaque heure et que la servlet le génère à la demande s'il manque. Pointer la propriété sur un volume persistant conserve le fichier entre les redémarrages.

Aucun conteneur de servlets ne servant un fichier situé hors de la webapp, `SitemapServlet` le publie, et une règle de réécriture de ce plugin l'expose sur `/sitemap-seo.xml`.

**Migration.** Les versions antérieures écrivaient le sitemap dans la webapp, sous le nom `sitemap.xml` puis `sitemap-seo.xml`. Supprimer tout fichier de ce genre resté à la racine de la webapp, et faire pointer la directive `Sitemap` du `robots.txt` sur `/sitemap-seo.xml` : tant qu'il en subsiste un, le serveur web continue de servir ce fichier gelé aux moteurs qui l'ont découvert, et chaque génération journalise un rappel. Noter que le `robots.txt` appartient au dépôt de chaque site, non à ce plugin, et qu'il accepte plusieurs directives `Sitemap`.

Les règles de réécriture ne sont plus écrites dans un fichier. La propriété `seo.configFilePath` et le paramètre `confPath` du filtre ont été supprimés.

## Configuration

| Propriété | Défaut | Rôle |
|-----------|--------|------|
| `seo.generatedFilesPath` | un sous-répertoire du répertoire temporaire système | Répertoire des fichiers générés, hors webapp |
| `daemon.seoFriendlyUrlGenerator.interval` | `3600` | Secondes entre deux exécutions du générateur d'URL explicites |
| `daemon.seoSitemapGenerator.interval` | `3600` | Secondes entre deux générations du sitemap |
| `seo.urlrewriter.*` | la valeur déclarée dans `seo.xml` | Surcharge un paramètre d'initialisation du filtre de réécriture |

`lutece.prod.url` est lue également, mais appartient à lutece-core : le site la renseigne par environnement, dans `conf/override/config.properties`. Ce plugin se contente de la lire.

## Daemons

`seoFriendlyUrlGenerator` régénère les URL explicites à partir du contenu du site, puis publie les règles de réécriture. `seoSitemapGenerator` régénère le sitemap. Les deux s'exécutent une fois au démarrage puis toutes les heures, et chacun peut être désactivé depuis le back office, indépendamment de l'intervalle.


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-seo/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*
