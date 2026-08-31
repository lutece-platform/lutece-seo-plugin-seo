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

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

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
 */
public class SeoUrlRewriteFilter extends UrlRewriteFilter
{
    /** Prefix of the configuration properties overriding the filter init parameters */
    public static final String PREFIX_CONFIG_PARAM = "seo.urlrewriter.";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        super.init( new ConfigurableFilterConfig( filterConfig ) );
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
