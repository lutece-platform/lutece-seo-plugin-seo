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
package fr.paris.lutece.plugins.seo.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import fr.paris.lutece.plugins.seo.service.RuleFileService;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * UrlRewriteFilter whose init parameters can be overridden through the configuration API used by lutece-core
 * (MicroProfile Config, exposed by {@link AppPropertiesService}).
 *
 * <p>
 * For an init parameter named <code>xxx</code> declared in <code>seo.xml</code>, the configuration property
 * <code>seo.urlrewriter.xxx</code> takes precedence whenever it is defined and not blank. Since the resolution goes
 * through MicroProfile Config, the value may come from a plugin properties file, a system property, an environment
 * variable (<code>SEO_URLREWRITER_XXX</code>) or any other config source registered in the application.
 * </p>
 *
 * <p>
 * The filter is instantiated by reflection by the core FilterService, outside of the CDI container : the programmatic
 * configuration API is used instead of <code>&#64;ConfigProperty</code> injection.
 * </p>
 *
 * <p>
 * The rewrite rules are read from the database rather than from a configuration file. They are a pure derivative of the
 * <code>seo_rule</code> and <code>seo_friendly_url</code> tables, so a file would only be a copy of data already
 * persisted, and one local to each node : in a multi node deployment, generating it from the back office wrote it on the
 * node that served the request while the others kept the former rules indefinitely. Every node now builds its rules from
 * the same tables, and reloads them when the version stamped in the datastore changes.
 * </p>
 */
public class SeoUrlRewriteFilter extends UrlRewriteFilter
{
    /** Prefix of the configuration properties overriding the filter init parameters */
    public static final String PREFIX_CONFIG_PARAM = "seo.urlrewriter.";

    /** Label identifying the origin of the rules in the logs and on the status page of the filter */
    private static final String CONF_ORIGIN = "seo rules (database)";

    private ServletContext _context;

    /** Version of the rules currently loaded, as stamped in the datastore when they were last published */
    private volatile String _strLoadedVersion;

    /** Instant of the last reload check, this filter keeping its own since the one of the parent tracks a file */
    private volatile long _lLastReloadCheck;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        _context = filterConfig.getServletContext( );
        super.init( new ConfigurableFilterConfig( filterConfig ) );
    }

    /**
     * Loads the rules from the database instead of the configuration file the parent class expects. Called by the parent
     * during {@link #init(FilterConfig)}.
     *
     * {@inheritDoc}
     */
    @Override
    protected void loadUrlRewriter( FilterConfig filterConfig ) throws ServletException
    {
        _lLastReloadCheck = System.currentTimeMillis( );
        loadRules( );
    }

    /**
     * The parent returns false as soon as the configuration did not come from a file, which is always the case here, and
     * tracks the interval with a private field. Both are replaced by this filter.
     *
     * <p>
     * Beware of the unit : the parent stores the interval in milliseconds but its getter returns seconds.
     * </p>
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isTimeToReloadConf( )
    {
        long lIntervalMillis = getConfReloadCheckInterval( ) * 1000L;

        return isConfReloadCheckEnabled( ) && ( System.currentTimeMillis( ) - _lLastReloadCheck ) > lIntervalMillis;
    }

    /**
     * The parent compares the last modified date of the configuration file, which no longer exists : the version stamped
     * in the datastore when the rules were last published is compared instead, so a publication from any node is picked
     * up by all of them.
     *
     * {@inheritDoc}
     */
    @Override
    public void reloadConf( )
    {
        _lLastReloadCheck = System.currentTimeMillis( );

        String strVersion = getPublishedVersion( );

        if ( strVersion.equals( _strLoadedVersion ) )
        {
            return;
        }

        AppLogService.info( "SEO : rewrite rules changed, reloading them from the database - version : '{}'", strVersion );
        loadRules( );
    }

    /**
     * Builds the rewrite configuration from the rules held in the database and hands it to the parent class. A failure
     * leaves the rules currently in memory untouched : the filter is initialised while the webapp starts, and letting the
     * exception out would abort that startup.
     */
    private void loadRules( )
    {
        String strVersion = getPublishedVersion( );

        try
        {
            byte [ ] bytesRules = RuleFileService.getRulesXml( ).getBytes( StandardCharsets.UTF_8 );

            try ( InputStream isRules = new ByteArrayInputStream( bytesRules ) )
            {
                checkConf( new Conf( _context, isRules, CONF_ORIGIN, CONF_ORIGIN ) );
            }

            _strLoadedVersion = strVersion;
        }
        catch( Exception e )
        {
            AppLogService.error( "SEO : unable to load the rewrite rules from the database, the rules currently in memory are kept : {}", e.getMessage( ), e );
        }
    }

    /**
     * Gets the version stamped in the datastore when the rules were last published.
     *
     * @return The version, an empty string when the rules have never been published
     */
    private static String getPublishedVersion( )
    {
        return DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" );
    }

    /**
     * FilterConfig wrapper resolving every init parameter against the configuration before falling back on the value
     * declared in the plugin descriptor.
     */
    private static final class ConfigurableFilterConfig implements FilterConfig
    {
        private final FilterConfig _delegate;

        ConfigurableFilterConfig( FilterConfig delegate )
        {
            _delegate = delegate;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getInitParameter( String strName )
        {
            String strDeclared = _delegate.getInitParameter( strName );
            String strConfigured = AppPropertiesService.getProperty( PREFIX_CONFIG_PARAM + strName );

            if ( strConfigured == null || strConfigured.isBlank( ) )
            {
                return strDeclared;
            }

            if ( !strConfigured.equals( strDeclared ) )
            {
                AppLogService.info( "SEO : init parameter '{}' overridden by property '{}{}' - value : '{}'", strName, PREFIX_CONFIG_PARAM, strName,
                        strConfigured );
            }

            return strConfigured;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Enumeration<String> getInitParameterNames( )
        {
            Set<String> setNames = new LinkedHashSet<>( );
            Enumeration<String> enumDeclared = _delegate.getInitParameterNames( );

            while ( enumDeclared.hasMoreElements( ) )
            {
                setNames.add( enumDeclared.nextElement( ) );
            }

            // parameters set only through the configuration, without any counterpart in the descriptor
            for ( String strKey : AppPropertiesService.getKeys( PREFIX_CONFIG_PARAM ) )
            {
                setNames.add( strKey.substring( PREFIX_CONFIG_PARAM.length( ) ) );
            }

            return Collections.enumeration( setNames );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFilterName( )
        {
            return _delegate.getFilterName( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ServletContext getServletContext( )
        {
            return _delegate.getServletContext( );
        }
    }
}
