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

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.content.ContentPostProcessor;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import javax.cache.CacheException;

/**
 *
 * @author pierre
 */
@ApplicationScoped
@Named( "seo.canonicalUrlContentPostProcessor" )
public class CanonicalUrlContentPostProcessor extends AbstractCacheableService<String, Object> implements ContentPostProcessor
{
    private static final String NAME = "SEO Canonical Url replacer";

    @Inject
    private CanonicalUrlService _canonicalUrlService;

    @Inject
    private FriendlyUrlService _friendlyUrlService;

    @PostConstruct
    public void init( )
    {
        initCache( NAME, String.class, Object.class );
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
     * {@inheritDoc }
     */
    @Override
    public String process( HttpServletRequest request, String strContent )
    {
        if ( _canonicalUrlService.isCanonicalUrlsEnabled( ) )
        {
            String strBaseUrl = AppPathService.getBaseUrl( request );

            return _canonicalUrlService.addCanonicalUrl( strContent, request, _friendlyUrlService.getCanonicalUrlMap( ), strBaseUrl );
        }

        return strContent;
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
