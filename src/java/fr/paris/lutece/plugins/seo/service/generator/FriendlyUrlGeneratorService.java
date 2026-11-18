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
package fr.paris.lutece.plugins.seo.service.generator;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Alias Generator Service
 */
@ApplicationScoped
public class FriendlyUrlGeneratorService
{
    private static final String SUFFIX_HTML = ".html";
    private static final int NOT_FOUND = -1;

    @Inject
    private Instance<FriendlyUrlGenerator> _generatorInstances;

    private List<FriendlyUrlGenerator> _listGenerators;

    @PostConstruct
    public void init( )
    {
        _listGenerators = new ArrayList<>( );
        _generatorInstances.forEach( _listGenerators::add );
    }

    /**
     * Generate Alias rules
     *
     * @param options
     *            Options
     */
    public void generate( GeneratorOptions options )
    {
        // Holds also the friendly URLs created during the run, so that two pages of the same name do not get the same friendly URL
        Collection<FriendlyUrl> listExisting = new ArrayList<>( FriendlyUrlHome.findAll( ) );

        List<FriendlyUrl> listRules = new ArrayList<>( );

        for ( FriendlyUrlGenerator generator : _listGenerators )
        {
            generator.generate( listRules, options );
        }

        processRuleList( listRules, listExisting, options );
        DatastoreService.setDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, DatastoreService.VALUE_FALSE );
    }

    /**
     * Gets the generators list
     *
     * @return The generators list
     */
    public List<GeneratorSettings> getGenerators( )
    {
        List<GeneratorSettings> list = new ArrayList<>( );

        for ( FriendlyUrlGenerator generator : _listGenerators )
        {
            GeneratorSettings gs = new GeneratorSettings( );
            String strKey = generator.getKey( );
            gs.setKey( strKey );
            gs.setName( generator.getName( ) );

            String strPrefix = SEODataKeys.PREFIX_GENERATOR + strKey;
            gs.setDefaultChangeFreq( DatastoreService.getDataValue( strPrefix + SEODataKeys.SUFFIX_CHANGE_FREQ, "" ) );
            gs.setDefaultPriority( DatastoreService.getDataValue( strPrefix + SEODataKeys.SUFFIX_PRIORITY, "" ) );
            list.add( gs );
        }

        return list;
    }

    /**
     * Process rules list
     *
     * @param listRules
     *            The rule list
     * @param listExisting
     *            The existing rules
     * @param options
     *            Options
     */
    // package visibility for the tests
    void processRuleList( List<FriendlyUrl> listRules, Collection<FriendlyUrl> listExisting, GeneratorOptions options )
    {
        AppLogService.info( "Processing Url rewriting Alias rules" );
        AppLogService.info( "* Option Force update existing rules : {}", options.isForceUpdate( ) ? "on" : "off" );
        AppLogService.info( "* Option Add path : {}", options.isAddPath( ) ? "on" : "off" );
        AppLogService.info( "* Option Html suffix : {}", options.isHtmlSuffix( ) ? "on" : "off" );

        for ( FriendlyUrl url : listRules )
        {
            String strSuffix = options.isHtmlSuffix( ) ? SUFFIX_HTML : "";
            String strFriendlyUrl = url.getFriendlyUrl( );
            url.setFriendlyUrl( strFriendlyUrl + strSuffix );

            FriendlyUrl existingUrl = getExistingUrl( listExisting, url );

            if ( existingUrl != null )
            {
                if ( options.isForceUpdate( ) )
                {
                    // update the existing alias
                    url.setId( existingUrl.getId( ) );
                    makeFriendlyUrlUnique( listExisting, url, strFriendlyUrl, strSuffix );
                    FriendlyUrlHome.update( url );
                    existingUrl.setFriendlyUrl( url.getFriendlyUrl( ) );
                    AppLogService.info( "Updated : {} -> {}", url.getFriendlyUrl( ), url.getTechnicalUrl( ) );
                }
                else
                {
                    AppLogService.info( "Ignored : {} -> {}", url.getFriendlyUrl( ), url.getTechnicalUrl( ) );
                }
            }
            else
            {
                // create a new alias
                url.setId( NOT_FOUND );
                makeFriendlyUrlUnique( listExisting, url, strFriendlyUrl, strSuffix );
                FriendlyUrlHome.create( url );
                listExisting.add( url );
                AppLogService.info( "Created : {} -> {}", url.getFriendlyUrl( ), url.getTechnicalUrl( ) );
            }
        }
    }

    /**
     * Adds a number to a friendly URL used by another technical URL (pages of the same name), until it is unique
     *
     * @param listExisting
     *            The existing rules
     * @param url
     *            The URL
     * @param strFriendlyUrl
     *            The friendly URL without suffix
     * @param strSuffix
     *            The suffix
     */
    private void makeFriendlyUrlUnique( Collection<FriendlyUrl> listExisting, FriendlyUrl url, String strFriendlyUrl, String strSuffix )
    {
        int nIndex = 2;

        while ( isFriendlyUrlUsedByAnother( listExisting, url ) )
        {
            url.setFriendlyUrl( strFriendlyUrl + "-" + nIndex++ + strSuffix );
        }
    }

    /**
     * Tells if the friendly URL of a URL is used by another rule
     *
     * @param listExisting
     *            The existing rules
     * @param url
     *            The URL
     * @return true if another rule uses the friendly URL
     */
    private boolean isFriendlyUrlUsedByAnother( Collection<FriendlyUrl> listExisting, FriendlyUrl url )
    {
        return listExisting.stream( ).anyMatch( u -> u.getId( ) != url.getId( ) && u.getFriendlyUrl( ).equals( url.getFriendlyUrl( ) ) );
    }

    /**
     * Get the existing rule of the technical URL of a URL
     *
     * @param listExisting
     *            The list of existing rules
     * @param url
     *            The URL
     * @return The existing rule, or null
     */
    private FriendlyUrl getExistingUrl( Collection<FriendlyUrl> listExisting, FriendlyUrl url )
    {
        for ( FriendlyUrl u : listExisting )
        {
            if ( u.getTechnicalUrl( ).equals( url.getTechnicalUrl( ) ) )
            {
                return u;
            }
        }

        return null;
    }
}
