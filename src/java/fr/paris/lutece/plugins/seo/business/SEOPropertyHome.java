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
 
package fr.paris.lutece.plugins.seo.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;



/**
 * This class provides instances management methods (create, find, ...) for SEOProperty objects
 */

public final class SEOPropertyHome
{
    private static final String PLUGIN_NAME = "seo";
    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );

    // Static variable pointed at the DAO instance
    private static ISEOPropertyDAO _dao = ( ISEOPropertyDAO ) SpringContextService.getBean( "seo.propertyDAO" );


    /**
     * Private constructor - this class need not be instantiated
     */

    private SEOPropertyHome(  )
    {
    }

    /**
     * Create an instance of the property class
     * @param property The instance of the SEOProperty which contains the informations to store
     * @return The  instance of property which has been created with its primary key.
     */

    public static SEOProperty create( SEOProperty property )
    {
        _dao.insert( property , _plugin );

        return property;
    }


    /**
     * Update of the property data specified in parameter
     * @param property The instance of the SEOProperty which contains the data to store
     * @return The instance of the  property which has been updated
     */

    public static SEOProperty update( SEOProperty property )
    {
        _dao.store( property , _plugin );

        return property;
    }


    /**
     * Remove the property whose identifier is specified in parameter
     * @param strKey The property Id
     */


    public static void remove( String strKey )
    {
        _dao.delete( strKey , _plugin );
    }


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a property whose identifier is specified in parameter   
     * @param strKey The property primary key
     * @return an instance of SEOProperty
     */

    public static SEOProperty findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey , _plugin );
    }


    /**
     * Load the data of all the property objects and returns them in form of a collection
     * @return the list which contains the data of all the property objects
     */

    public static List<SEOProperty> findAll()
    {
        return _dao.selectSEOPropertysList( _plugin );
    }

}