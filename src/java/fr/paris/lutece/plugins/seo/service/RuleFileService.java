/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.tuckey.web.filters.urlrewrite.Conf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Builds the URL rewriting rules from the database, and publishes them so that every node reloads them.
 *
 * <p>
 * The rules used to be written to a file read by the rewrite filter. They are a pure derivative of the
 * <code>seo_rule</code> and <code>seo_friendly_url</code> tables, so that file was a copy of data already persisted, and
 * one local to each node. The filter now builds its configuration from {@link #getRulesXml()}, and
 * {@link #publishRules()} only stamps a new version in the datastore.
 * </p>
 *
 * <p>
 * The document is assembled in plain Java, not through a FreeMarker template. The filter renders it during its
 * <code>init</code>, which the core runs before it registers the FreeMarker auto-includes : a template processed at that
 * point builds the FreeMarker configuration without the macros, and that configuration is cached for the whole life of
 * the webapp. Every template rendered afterwards then fails on its first macro.
 * </p>
 */
public final class RuleFileService
{
    private static final String PROPERTY_REWRITE_CONFIG_LOG = "seo.config.log";
    private static final String CONF_NAME = "seo rules (database)";

    /** Path of the sitemap as declared in robots.txt, rewritten to the servlet of the plugin */
    private static final String SITEMAP_RULE_FROM = "^/sitemap-seo\\.xml$";
    private static final String SITEMAP_RULE_TO = "/servlet/plugins/seo/sitemap-seo.xml";

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<!DOCTYPE urlrewrite PUBLIC \"-//tuckey.org//DTD UrlRewrite 4.0//EN\" \"http://www.tuckey.org/res/dtds/urlrewrite4.0.dtd\">\n"
            + "<!--\n\n" + "    Configuration of the UrlRewriteFilter\n" + "    http://tuckey.org/urlrewrite/\n\n"
            + "    GENERATED DOCUMENT - not meant to be edited.\n"
            + "    Produced by fr.paris.lutece.plugins.seo.service.RuleFileService from the\n"
            + "    URL rewriter rules (seo_rule) and the friendly URLs (seo_friendly_url).\n\n" + "-->\n";
    private static final String TAG_URLREWRITE_OPEN = "<urlrewrite>\n";
    private static final String TAG_URLREWRITE_CLOSE = "</urlrewrite>\n";
    private static final String TAG_RULE_OPEN = "    <rule>\n";
    private static final String TAG_RULE_CLOSE = "    </rule>\n";
    private static final String TAG_FROM_OPEN = "        <from>";
    private static final String TAG_FROM_CLOSE = "</from>\n";
    private static final String TAG_TO_OPEN = "        <to>";
    private static final String TAG_TO_CLOSE = "</to>\n";
    private static final String COMMENT_SITEMAP = "    <!-- Sitemap generated outside the webapp : served by SitemapServlet -->\n";
    private static final String REGEX_START = "^";
    private static final String REGEX_END = "$";

    /**
     * Private constructor
     */
    private RuleFileService( )
    {
    }

    /**
     * Publishes the rules held in the database : checks that they form a configuration the rewrite filter accepts, then
     * stamps a new version, which every node compares to the one it has loaded, and records the log shown in the back
     * office.
     *
     * <p>
     * The check is what the administrator gets as feedback. A rule holding an invalid regular expression used to break
     * the rewriting silently, the failure only appearing in the log of each node at the next reload : nothing is
     * published unless the rules parse.
     * </p>
     *
     * @return <code>true</code> when the rules were published, <code>false</code> when they were rejected
     */
    public static boolean publishRules( )
    {
        Collection<UrlRewriterRule> listRules = UrlRewriterRuleHome.findAll( );
        List<FriendlyUrl> listUrl = FriendlyUrlHome.findAll( );

        if ( !areRulesValid( ) )
        {
            return false;
        }

        String strDate = DateFormat.getDateTimeInstance( ).format( new Date( ) );
        Object [ ] args = {
                strDate, listRules.size( ) + listUrl.size( ), "OK"
        };
        String strLogFormat = I18nService.getLocalizedString( PROPERTY_REWRITE_CONFIG_LOG, Locale.getDefault( ) );

        DatastoreService.setDataValue( SEODataKeys.KEY_REWRITE_CONFIG_UPDATE, MessageFormat.format( strLogFormat, args ) );
        DatastoreService.setDataValue( SEODataKeys.KEY_RULES_VERSION, String.valueOf( System.currentTimeMillis( ) ) );
        DatastoreService.setDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, DatastoreService.VALUE_TRUE );

        return true;
    }

    /**
     * Checks that the rules held in the database render a configuration the rewrite filter is able to load.
     *
     * @return <code>true</code> when the rules are valid
     */
    private static boolean areRulesValid( )
    {
        try ( InputStream isRules = new ByteArrayInputStream( getRulesXml( ).getBytes( StandardCharsets.UTF_8 ) ) )
        {
            Conf conf = new Conf( isRules, CONF_NAME );

            if ( !conf.isOk( ) )
            {
                AppLogService.error( "SEO : the rewrite rules were rejected by the filter configuration parser, nothing has been published" );

                return false;
            }
        }
        catch( Exception e )
        {
            AppLogService.error( "SEO : unable to build the rewrite rules, nothing has been published : {}", e.getMessage( ), e );

            return false;
        }

        return true;
    }

    /**
     * Renders the rewrite rules held in the database as the XML configuration expected by the rewrite filter. Free of any
     * side effect : the filter calls it on every reload.
     *
     * <p>
     * Every URL rewriter rule is written as it was entered, anchored at the start of the path. Every friendly URL is
     * written as an exact match of the whole path. The rule exposing the sitemap comes first.
     * </p>
     *
     * @return The rules, as an urlrewrite configuration document
     */
    public static String getRulesXml( )
    {
        Collection<UrlRewriterRule> listRules = UrlRewriterRuleHome.findAll( );
        List<FriendlyUrl> listUrl = FriendlyUrlHome.findAll( );

        StringBuilder sbRules = new StringBuilder( XML_HEADER.length( ) + 128 * ( 1 + listRules.size( ) + listUrl.size( ) ) );
        sbRules.append( XML_HEADER ).append( TAG_URLREWRITE_OPEN );

        sbRules.append( COMMENT_SITEMAP );
        appendRule( sbRules, SITEMAP_RULE_FROM, SITEMAP_RULE_TO );

        for ( UrlRewriterRule rule : listRules )
        {
            appendRule( sbRules, REGEX_START + rule.getRuleFrom( ), rule.getRuleTo( ) );
        }

        for ( FriendlyUrl url : listUrl )
        {
            appendRule( sbRules, REGEX_START + url.getFriendlyUrl( ) + REGEX_END, url.getTechnicalUrl( ) );
        }

        sbRules.append( TAG_URLREWRITE_CLOSE );

        return sbRules.toString( );
    }

    /**
     * Appends a rule element.
     *
     * @param sbRules
     *            The document under construction
     * @param strFrom
     *            The regular expression matched against the request path
     * @param strTo
     *            The path the request is rewritten to
     */
    private static void appendRule( StringBuilder sbRules, String strFrom, String strTo )
    {
        sbRules.append( TAG_RULE_OPEN );
        sbRules.append( TAG_FROM_OPEN ).append( escapeXml( strFrom ) ).append( TAG_FROM_CLOSE );
        sbRules.append( TAG_TO_OPEN ).append( escapeXml( strTo ) ).append( TAG_TO_CLOSE );
        sbRules.append( TAG_RULE_CLOSE );
    }

    /**
     * Escapes the characters XML reserves, so that a rule is read back exactly as it was entered : a regular expression
     * or a target URL may well hold an ampersand, and the filter unescapes the text content when it parses the document.
     *
     * @param strValue
     *            The value, possibly <code>null</code>
     * @return The escaped value, empty when the value is <code>null</code>
     */
    private static String escapeXml( String strValue )
    {
        if ( strValue == null )
        {
            return "";
        }

        StringBuilder sbEscaped = new StringBuilder( strValue.length( ) + 16 );

        for ( int i = 0; i < strValue.length( ); i++ )
        {
            char c = strValue.charAt( i );

            switch( c )
            {
                case '&':
                    sbEscaped.append( "&amp;" );
                    break;
                case '<':
                    sbEscaped.append( "&lt;" );
                    break;
                case '>':
                    sbEscaped.append( "&gt;" );
                    break;
                case '"':
                    sbEscaped.append( "&quot;" );
                    break;
                case '\'':
                    sbEscaped.append( "&apos;" );
                    break;
                default:
                    sbEscaped.append( c );
            }
        }

        return sbEscaped.toString( );
    }
}
