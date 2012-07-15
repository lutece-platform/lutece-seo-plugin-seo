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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for SEOProperty objects
 */
public final class SEOPropertyDAO implements ISEOPropertyDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT property_key, property_value FROM seo_properties WHERE property_key = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO seo_properties ( property_key, property_value ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM seo_properties WHERE property_key = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE seo_properties SET property_value = ? WHERE property_key = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT property_key, property_value FROM seo_properties";

    /**
     * Insert a new record in the table.
     * @param property instance of the SEOProperty object to insert
     * @param plugin The plugin
     */
    public void insert( SEOProperty property, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setString( 1, property.getPropertyKey(  ) );
        daoUtil.setString( 2, property.getPropertyValue(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the property from the table
     * @param strKey The identifier of the property
     * @param plugin The plugin
     * @return the instance of the SEOProperty
     */
    public SEOProperty load( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery(  );

        SEOProperty property = null;

        if ( daoUtil.next(  ) )
        {
            property = new SEOProperty(  );

            property.setPropertyKey( daoUtil.getString( 1 ) );
            property.setPropertyValue( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return property;
    }

    /**
     * Delete a record from the table
     * @param strKey The identifier of the property
     * @param plugin The plugin
     */
    public void delete( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param property The reference of the property
     * @param plugin The plugin
     */
    public void store( SEOProperty property, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, property.getPropertyValue(  ) );
        daoUtil.setString( 2, property.getPropertyKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the propertys and returns them as a List
     * @param plugin The plugin
     * @return The List which contains the data of all the propertys
     */
    public List<SEOProperty> selectSEOPropertysList( Plugin plugin )
    {
        List<SEOProperty> propertyList = new ArrayList<SEOProperty>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            SEOProperty property = new SEOProperty(  );

            property.setPropertyKey( daoUtil.getString( 1 ) );
            property.setPropertyValue( daoUtil.getString( 2 ) );

            propertyList.add( property );
        }

        daoUtil.free(  );

        return propertyList;
    }
}
