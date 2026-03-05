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
package fr.paris.lutece.plugins.seo.business;

import fr.paris.lutece.plugins.seo.service.FriendlyUrlService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import jakarta.enterprise.inject.spi.CDI;

import java.util.Collections;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for FriendlyUrl objects
 */
public final class FriendlyUrlHome
{
    private static final String PLUGIN_NAME = "seo";

    /**
     * Private constructor - this class need not be instantiated
     */
    private FriendlyUrlHome( )
    {
    }

    private static IFriendlyUrlDAO getDAO( )
    {
        return CDI.current( ).select( IFriendlyUrlDAO.class ).get( );
    }

    private static Plugin getPlugin( )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }

    /**
     * Create an instance of the friendlyUrl class
     *
     * @param friendlyUrl
     *            The instance of the FriendlyUrl which contains the informations to store
     * @return The instance of friendlyUrl which has been created with its primary key.
     */
    public static FriendlyUrl create( FriendlyUrl friendlyUrl )
    {
        getDAO( ).insert( friendlyUrl, getPlugin( ) );
        notifyUpdate( );

        return friendlyUrl;
    }

    /**
     * Update of the friendlyUrl data specified in parameter
     *
     * @param friendlyUrl
     *            The instance of the FriendlyUrl which contains the data to store
     * @return The instance of the friendlyUrl which has been updated
     */
    public static FriendlyUrl update( FriendlyUrl friendlyUrl )
    {
        getDAO( ).store( friendlyUrl, getPlugin( ) );
        notifyUpdate( );

        return friendlyUrl;
    }

    /**
     * Remove the friendlyUrl whose identifier is specified in parameter
     *
     * @param nFriendlyUrlId
     *            The friendlyUrl Id
     */
    public static void remove( int nFriendlyUrlId )
    {
        getDAO( ).delete( nFriendlyUrlId, getPlugin( ) );
        notifyUpdate( );
    }

    /**
     * Remove all URL
     */
    public static void removeAll( )
    {
        getDAO( ).deleteAll( getPlugin( ) );
        notifyUpdate( );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a friendlyUrl whose identifier is specified in parameter
     *
     * @param nKey
     *            The friendlyUrl primary key
     * @return an instance of FriendlyUrl
     */
    public static FriendlyUrl findByPrimaryKey( int nKey )
    {
        return getDAO( ).load( nKey, getPlugin( ) );
    }

    /**
     * Load the data of all the friendlyUrl objects and returns them in form of a collection
     *
     * @return the list which contains the data of all the friendlyUrl objects
     */
    public static List<FriendlyUrl> findAll( )
    {
        List<FriendlyUrl> listFriendlyUrls = getDAO( ).selectFriendlyUrlsList( getPlugin( ) );
        Collections.sort( listFriendlyUrls );

        return listFriendlyUrls;
    }

    /**
     * On change
     */
    private static void notifyUpdate( )
    {
        CDI.current( ).select( FriendlyUrlService.class ).get( ).resetCache( );
    }

}
