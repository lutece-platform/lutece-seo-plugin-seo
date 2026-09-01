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
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.tuckey.web.filters.urlrewrite.Conf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
 */
public final class RuleFileService
{
    private static final String TEMPLATE_FILE = "/admin/plugins/seo/urlrewrite.xml";
    private static final String MARK_RULES_LIST = "rules_list";
    private static final String MARK_URL_LIST = "url_list";
    private static final String PROPERTY_REWRITE_CONFIG_LOG = "seo.config.log";
    private static final String TAG_XML_DECLARATION = "<?xml";
    private static final String CONF_NAME = "seo rules (database)";

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
     * @return The rules, as an urlrewrite configuration document
     */
    public static String getRulesXml( )
    {
        HashMap model = new HashMap( );
        model.put( MARK_RULES_LIST, UrlRewriterRuleHome.findAll( ) );
        model.put( MARK_URL_LIST, FriendlyUrlHome.findAll( ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_FILE, Locale.getDefault( ), model );

        String strRules = t.getHtml( );

        // the template may be preceded by whitespace, which is not allowed before an XML declaration
        int nStartXml = strRules.indexOf( TAG_XML_DECLARATION );
        if ( nStartXml > 0 )
        {
            strRules = strRules.substring( nStartXml );
        }

        return strRules;
    }
}
