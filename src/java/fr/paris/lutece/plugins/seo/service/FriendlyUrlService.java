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
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import javax.cache.CacheException;

import java.util.HashMap;
import java.util.Map;

/**
 * Friendly Url Service
 */
@ApplicationScoped
public class FriendlyUrlService extends AbstractCacheableService<String, Object>
{
    private static final String CACHE_KEY = "friendly_url_cache_key";
    private static final String CACHE_KEY_CANONICAL = "canonical_url_cache_key";
    private static final String NAME = "SEO Friendly Url Cache Service";
    private boolean _bReplaceUrl;

    @PostConstruct
    public void init( )
    {
        initCache( NAME, String.class, Object.class );
        _bReplaceUrl = DatastoreService.getDataValue( SEODataKeys.KEY_URL_REPLACE_ENABLED, "" ).equals( DatastoreService.VALUE_TRUE );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName( )
    {
        return NAME;
    }

    /**
     * Returns the map of Friendly URL
     *
     * @return The map
     */
    @SuppressWarnings( "unchecked" )
    public Map<String, String> getFriendlyUrlMap( )
    {
        Map<String, String> map = (Map<String, String>) get( CACHE_KEY );

        if ( map == null )
        {
            map = new HashMap<>( );

            for ( FriendlyUrl url : FriendlyUrlHome.findAll( ) )
            {
                map.put( FriendlyUrlUtils.cleanUrl( url.getTechnicalUrl( ) ), FriendlyUrlUtils.cleanUrl( url.getFriendlyUrl( ) ) );
            }

            put( CACHE_KEY, map );
        }

        return map;
    }

    /**
     * Returns the map of Canonical URL
     *
     * @return The map
     */
    @SuppressWarnings( "unchecked" )
    Map<String, String> getCanonicalUrlMap( )
    {
        Map<String, String> map = (Map<String, String>) get( CACHE_KEY_CANONICAL );

        if ( map == null )
        {
            map = new HashMap<>( );

            for ( FriendlyUrl url : FriendlyUrlHome.findAll( ) )
            {
                if ( url.isCanonical( ) )
                {
                    map.put( FriendlyUrlUtils.cleanUrl( url.getTechnicalUrl( ) ), FriendlyUrlUtils.cleanUrl( url.getFriendlyUrl( ) ) );
                }
            }

            put( CACHE_KEY_CANONICAL, map );
        }

        return map;
    }

    /**
     * Is the URL replace service enabled
     *
     * @return True if enabled, otherwise false
     */
    public boolean isUrlReplaceEnabled( )
    {
        return _bReplaceUrl;
    }

    /**
     * Set enabled or disabled the Url Replace Service
     *
     * @param bEnabled
     *            The service status
     */
    public void setUrlReplaceEnabled( boolean bEnabled )
    {
        _bReplaceUrl = bEnabled;
    }

    @Override
    public void put( String key, Object value )
    {
        if ( isCacheEnable( ) && isCacheAvailable( ) )
        {
            try
            {
                super.put( key, value );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "Cache put error for key {}", key, e );
            }
        }
    }

    @Override
    public Object get( String key )
    {
        if ( isCacheEnable( ) && isCacheAvailable( ) )
        {
            try
            {
                return super.get( key );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "Cache get error for key {}", key, e );
            }
        }
        return null;
    }

    @Override
    public boolean remove( String key )
    {
        if ( isCacheEnable( ) && isCacheAvailable( ) )
        {
            try
            {
                return super.remove( key );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "Cache remove error for key {}", key, e );
            }
        }
        return false;
    }

    private boolean isCacheAvailable( )
    {
        return _cache != null && !_cache.isClosed( );
    }
}
