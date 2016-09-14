/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
 * This class provides Data Access methods for FriendlyUrl objects
 */
public final class FriendlyUrlDAO implements IFriendlyUrlDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_url ) FROM seo_friendly_url";
    private static final String SQL_QUERY_SELECT = "SELECT id_url, friendly_url, technical_url, date_creation, date_modification, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority FROM seo_friendly_url WHERE id_url = ?";
    private static final String SQL_QUERY_INSERT_COLUMNS_PREFIX = "INSERT INTO seo_friendly_url ( id_url, friendly_url, technical_url, ";
    private static final String SQL_QUERY_INSERT_COLUMNS_SUFFIX = "is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority )";
    private static final String SQL_QUERY_INSERT_VALUES_PREFIX = " VALUES ( ?, ?, ?, ";
    private static final String SQL_QUERY_INSERT_VALUES_SUFFIX = "?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM seo_friendly_url WHERE id_url = ? ";
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM seo_friendly_url";
    private static final String SQL_QUERY_UPDATE_PREFIX = "UPDATE seo_friendly_url SET id_url = ?, friendly_url = ?, technical_url = ?,";
    private static final String SQL_QUERY_UPDATE_DATE_CREATION_FRAGMENT = " date_creation = ?, ";
    private static final String SQL_QUERY_UPDATE_DATE_MODIFICATION_FRAGMENT = " date_modification = ?, ";
    private static final String SQL_QUERY_UPDATE_SUFFIX = "is_canonical = ?, is_sitemap = ?, sitemap_lastmod = ? , sitemap_changefreq = ? , sitemap_priority = ? WHERE id_url = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_url, friendly_url, technical_url, date_creation, date_modification, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority  FROM seo_friendly_url";

    /**
     * {@inheritDoc }
     */ 
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey;

        daoUtil.next( );
        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public void insert( FriendlyUrl friendlyUrl, Plugin plugin )
    {
        StringBuilder query_columns = new StringBuilder( SQL_QUERY_INSERT_COLUMNS_PREFIX );
        StringBuilder query_values = new StringBuilder( SQL_QUERY_INSERT_VALUES_PREFIX );

        if ( friendlyUrl.getDateCreation( ) != null )
        {
            query_columns.append( "date_creation, " );
            query_values.append( "?, " );
        }
        if ( friendlyUrl.getDateModification( ) != null )
        {
            query_values.append( "date_modification, " );
            query_columns.append( "?, " );
        }
        query_columns.append( SQL_QUERY_INSERT_COLUMNS_SUFFIX );
        query_values.append( SQL_QUERY_INSERT_VALUES_SUFFIX );
        String query = query_columns.toString( ) + query_values.toString( );

        DAOUtil daoUtil = new DAOUtil( query, plugin );

        friendlyUrl.setId( newPrimaryKey( plugin ) );

        int argIdx = 1;

        daoUtil.setInt( argIdx++, friendlyUrl.getId( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getFriendlyUrl( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getTechnicalUrl( ) );
        if ( friendlyUrl.getDateCreation( ) != null )
        {
            daoUtil.setTimestamp( argIdx++, friendlyUrl.getDateCreation( ) );
        }
        if ( friendlyUrl.getDateModification( ) != null )
        {
            daoUtil.setTimestamp( argIdx++, friendlyUrl.getDateModification( ) );
        }
        daoUtil.setBoolean( argIdx++, friendlyUrl.isCanonical( ) );
        daoUtil.setBoolean( argIdx++, friendlyUrl.isSitemap( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapLastmod( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapChangeFreq( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapPriority( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public FriendlyUrl load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        FriendlyUrl friendlyUrl = null;

        if ( daoUtil.next( ) )
        {
            friendlyUrl = new FriendlyUrl( );

            friendlyUrl.setId( daoUtil.getInt( 1 ) );
            friendlyUrl.setFriendlyUrl( daoUtil.getString( 2 ) );
            friendlyUrl.setTechnicalUrl( daoUtil.getString( 3 ) );
            friendlyUrl.setDateCreation( daoUtil.getTimestamp( 4 ) );
            friendlyUrl.setDateModification( daoUtil.getTimestamp( 5 ) );
            friendlyUrl.setCanonical( daoUtil.getBoolean( 6 ) );
            friendlyUrl.setSitemap( daoUtil.getBoolean( 7 ) );
            friendlyUrl.setSitemapLastmod( daoUtil.getString( 8 ) );
            friendlyUrl.setSitemapChangeFreq( daoUtil.getString( 9 ) );
            friendlyUrl.setSitemapPriority( daoUtil.getString( 10 ) );
        }

        daoUtil.free( );

        return friendlyUrl;
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public void delete( int nFriendlyUrlId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nFriendlyUrlId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public void store( FriendlyUrl friendlyUrl, Plugin plugin )
    {
        StringBuilder query = new StringBuilder( SQL_QUERY_UPDATE_PREFIX );
        if ( friendlyUrl.getDateCreation( ) != null )
        {
            query.append( SQL_QUERY_UPDATE_DATE_CREATION_FRAGMENT );
        }
        if ( friendlyUrl.getDateModification( ) != null )
        {
            query.append( SQL_QUERY_UPDATE_DATE_MODIFICATION_FRAGMENT );
        }
        query.append( SQL_QUERY_UPDATE_SUFFIX );

        DAOUtil daoUtil = new DAOUtil( query.toString( ), plugin );

        int argIdx = 1;

        daoUtil.setInt( argIdx++, friendlyUrl.getId( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getFriendlyUrl( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getTechnicalUrl( ) );
        if ( friendlyUrl.getDateCreation( ) != null )
        {
            daoUtil.setTimestamp( argIdx++, friendlyUrl.getDateCreation( ) );
        }
        if ( friendlyUrl.getDateModification( ) != null )
        {
            daoUtil.setTimestamp( argIdx++, friendlyUrl.getDateModification( ) );
        }
        daoUtil.setBoolean( argIdx++, friendlyUrl.isCanonical( ) );
        daoUtil.setBoolean( argIdx++, friendlyUrl.isSitemap( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapLastmod( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapChangeFreq( ) );
        daoUtil.setString( argIdx++, friendlyUrl.getSitemapPriority( ) );
        daoUtil.setInt( argIdx++, friendlyUrl.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public List<FriendlyUrl> selectFriendlyUrlsList( Plugin plugin )
    {
        List<FriendlyUrl> friendlyUrlList = new ArrayList<FriendlyUrl>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            FriendlyUrl friendlyUrl = new FriendlyUrl( );

            friendlyUrl.setId( daoUtil.getInt( 1 ) );
            friendlyUrl.setFriendlyUrl( daoUtil.getString( 2 ) );
            friendlyUrl.setTechnicalUrl( daoUtil.getString( 3 ) );
            friendlyUrl.setDateCreation( daoUtil.getTimestamp( 4 ) );
            friendlyUrl.setDateModification( daoUtil.getTimestamp( 5 ) );
            friendlyUrl.setCanonical( daoUtil.getBoolean( 6 ) );
            friendlyUrl.setSitemap( daoUtil.getBoolean( 7 ) );
            friendlyUrl.setSitemapLastmod( daoUtil.getString( 8 ) );
            friendlyUrl.setSitemapChangeFreq( daoUtil.getString( 9 ) );
            friendlyUrl.setSitemapPriority( daoUtil.getString( 10 ) );

            friendlyUrlList.add( friendlyUrl );
        }

        daoUtil.free( );

        return friendlyUrlList;
    }

    /**
     * {@inheritDoc }
     */ 
    @Override
    public void deleteAll( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL, plugin );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
