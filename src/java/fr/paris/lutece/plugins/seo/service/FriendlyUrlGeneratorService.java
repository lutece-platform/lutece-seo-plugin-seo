/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Alias Generator Service
 */
public class FriendlyUrlGeneratorService
{
    private static final String SUFFIX_HTML = ".html";
    private static final int NOT_FOUND = -1;
    private static List<FriendlyUrlGenerator> _listGenerators = new ArrayList<FriendlyUrlGenerator>(  );
    private static FriendlyUrlGeneratorService _singleton;

    /**
     * private constructore
     */
    private FriendlyUrlGeneratorService(  )
    {
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static synchronized FriendlyUrlGeneratorService instance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new FriendlyUrlGeneratorService(  );

            _listGenerators = SpringContextService.getBeansOfType( FriendlyUrlGenerator.class );
        }

        return _singleton;
    }

    /**
     * Generate Alias rules
     * @param options Options
     */
    public void generate( GeneratorOptions options )
    {
        Collection<FriendlyUrl> listExisting = FriendlyUrlHome.findAll(  );

        List<FriendlyUrl> listRules = new ArrayList<FriendlyUrl>(  );

        for ( FriendlyUrlGenerator generator : _listGenerators )
        {
            generator.generate( listRules, options );
        }

        processRuleList( listRules, listExisting, options );
    }

    /**
     * Gets the generators list
     * @return The generators list
     */
    public List<FriendlyUrlGenerator> getGenerators(  )
    {
        return _listGenerators;
    }

    private void processRuleList( List<FriendlyUrl> listRules, Collection<FriendlyUrl> listExisting,
        GeneratorOptions options )
    {
        AppLogService.info( "Processing Url rewriting Alias rules" );
        AppLogService.info( "* Option Force update existing rules : " + ( options.isForceUpdate(  ) ? "on" : "off" ) );
        AppLogService.info( "* Option Add path : " + ( options.isAddPath(  ) ? "on" : "off" ) );
        AppLogService.info( "* Option Html suffix : " + ( options.isHtmlSuffix(  ) ? "on" : "off" ) );

        for ( FriendlyUrl url : listRules )
        {
            if ( options.isHtmlSuffix(  ) )
            {
                url.setFriendlyUrl( url.getFriendlyUrl() + SUFFIX_HTML );
            }

            int nExistingRuleId = getExistingRuleId( listExisting, url );

            if ( nExistingRuleId != NOT_FOUND )
            {
                if ( options.isForceUpdate(  ) )
                {
                    // update the existing alias
                    url.setId( nExistingRuleId );
                    FriendlyUrlHome.update( url );
                    AppLogService.info( "Updated : " + url.getFriendlyUrl() + " -> " + url.getTechnicalUrl() );
                }
                else
                {
                    AppLogService.info( "Ignored : " + url.getFriendlyUrl() + " -> " + url.getTechnicalUrl() );
                }
            }
            else
            {
                // create a new alias
                FriendlyUrlHome.create( url );
                AppLogService.info( "Created : " + url.getFriendlyUrl() + " -> " + url.getTechnicalUrl() );
            }
        }
    }

    private int getExistingRuleId( Collection<FriendlyUrl> listExisting, FriendlyUrl url )
    {
        for ( FriendlyUrl u : listExisting )
        {
            if ( u.getTechnicalUrl().equals( url.getTechnicalUrl() ) )
            {
                return u.getId(  );
            }
        }

        return NOT_FOUND;
    }
}
