/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for FriendlyUrl objects
 */
public final class FriendlyUrlHome
{
    private static final String PLUGIN_NAME = "seo";
    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );

    // Static variable pointed at the DAO instance
    private static IFriendlyUrlDAO _dao = (IFriendlyUrlDAO) SpringContextService.getBean( "seo.friendlyUrlDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private FriendlyUrlHome(  )
    {
    }

    /**
     * Create an instance of the friendlyUrl class
     * @param friendlyUrl The instance of the FriendlyUrl which contains the informations to store
     * @return The  instance of friendlyUrl which has been created with its primary key.
     */
    public static FriendlyUrl create( FriendlyUrl friendlyUrl )
    {
        _dao.insert( friendlyUrl, _plugin );

        return friendlyUrl;
    }

    /**
     * Update of the friendlyUrl data specified in parameter
     * @param friendlyUrl The instance of the FriendlyUrl which contains the data to store
     * @return The instance of the  friendlyUrl which has been updated
     */
    public static FriendlyUrl update( FriendlyUrl friendlyUrl )
    {
        _dao.store( friendlyUrl, _plugin );

        return friendlyUrl;
    }

    /**
     * Remove the friendlyUrl whose identifier is specified in parameter
     * @param nFriendlyUrlId The friendlyUrl Id
     */
    public static void remove( int nFriendlyUrlId )
    {
        _dao.delete( nFriendlyUrlId, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a friendlyUrl whose identifier is specified in parameter
     * @param nKey The friendlyUrl primary key
     * @return an instance of FriendlyUrl
     */
    public static FriendlyUrl findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the friendlyUrl objects and returns them in form of a collection
     * @return the list which contains the data of all the friendlyUrl objects
     */
    public static List<FriendlyUrl> findAll(  )
    {
        return _dao.selectFriendlyUrlsList( _plugin );
    }
}
