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
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;

import java.util.HashMap;
import java.util.Map;


/**
 * Friendly Url Service
 */
public class FriendlyUrlService extends AbstractCacheableService
{
    private static final String CACHE_KEY = "friendly_url_cache_key";
    private static final String NAME = "SEO Friendly Url Cache Service";

    private static FriendlyUrlService _singleton = new FriendlyUrlService();

    private FriendlyUrlService()
    {
        initCache();
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public String getName(  )
    {
        return NAME;
    }

    public static FriendlyUrlService instance()
    {
        return _singleton;
    }
    
    /**
     * Returns the map of Friendly URL
     * @return The map
     */
    public Map<String, String> getFriendlyUrlMap(  )
    {
        Map<String,String> map = (Map<String,String>) getFromCache( CACHE_KEY );
        if( map == null )
        {
            map = new HashMap<String, String>(  );

            for ( FriendlyUrl url : FriendlyUrlHome.findAll(  ) )
            {
                map.put( FriendlyUrlUtils.cleanSlash( url.getTechnicalUrl(  ) ),
                FriendlyUrlUtils.cleanSlash( url.getFriendlyUrl(  ) ) );
            }

            putInCache( CACHE_KEY, map );
        }
        return map;
    }
}
