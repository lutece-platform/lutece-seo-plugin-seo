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
 * This class provides Data Access methods for FriendlyUrl objects
 */
public final class FriendlyUrlDAO implements IFriendlyUrlDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_url ) FROM seo_friendly_url";
    private static final String SQL_QUERY_SELECT = "SELECT id_url, friendly_url, technical_url, date_creation, date_modification, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority FROM seo_friendly_url WHERE id_url = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO seo_friendly_url ( id_url, friendly_url, technical_url, date_creation, date_modification, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM seo_friendly_url WHERE id_url = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE seo_friendly_url SET id_url = ?, friendly_url = ?, technical_url = ?, date_creation = ?, date_modification = ?, is_canonical = ?, is_sitemap = ?, sitemap_lastmod = ? , sitemap_changefreq = ? , sitemap_priority = ? WHERE id_url = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_url, friendly_url, technical_url, date_creation, date_modification, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority  FROM seo_friendly_url";

    /**
     * Generates a new primary key
     *
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey(Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_NEW_PK, plugin);
        daoUtil.executeQuery();

        int nKey;

        if (!daoUtil.next())
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt(1) + 1;
        daoUtil.free();

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param friendlyUrl instance of the FriendlyUrl object to insert
     * @param plugin The plugin
     */
    public void insert(FriendlyUrl friendlyUrl, Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_INSERT, plugin);

        friendlyUrl.setId(newPrimaryKey(plugin));

        daoUtil.setInt(1, friendlyUrl.getId());
        daoUtil.setString(2, friendlyUrl.getFriendlyUrl());
        daoUtil.setString(3, friendlyUrl.getTechnicalUrl());
        daoUtil.setTimestamp(4, friendlyUrl.getDateCreation());
        daoUtil.setTimestamp(5, friendlyUrl.getDateModification());
        daoUtil.setBoolean(6, friendlyUrl.isCanonical());
        daoUtil.setBoolean(7, friendlyUrl.isSitemap());
        daoUtil.setString(8, friendlyUrl.getSitemapLastmod());
        daoUtil.setString(9, friendlyUrl.getSitemapChangeFreq());
        daoUtil.setString(10, friendlyUrl.getSitemapPriority());

        daoUtil.executeUpdate();
        daoUtil.free();
    }

    /**
     * Load the data of the friendlyUrl from the table
     *
     * @param nId The identifier of the friendlyUrl
     * @param plugin The plugin
     * @return the instance of the FriendlyUrl
     */
    public FriendlyUrl load(int nId, Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT, plugin);
        daoUtil.setInt(1, nId);
        daoUtil.executeQuery();

        FriendlyUrl friendlyUrl = null;

        if (daoUtil.next())
        {
            friendlyUrl = new FriendlyUrl();

            friendlyUrl.setId(daoUtil.getInt(1));
            friendlyUrl.setFriendlyUrl(daoUtil.getString(2));
            friendlyUrl.setTechnicalUrl(daoUtil.getString(3));
            friendlyUrl.setDateCreation(daoUtil.getTimestamp(4));
            friendlyUrl.setDateModification(daoUtil.getTimestamp(5));
            friendlyUrl.setCanonical(daoUtil.getBoolean(6));
            friendlyUrl.setSitemap(daoUtil.getBoolean(7));
            friendlyUrl.setSitemapLastmod(daoUtil.getString(8));
            friendlyUrl.setSitemapChangeFreq(daoUtil.getString(9));
            friendlyUrl.setSitemapPriority(daoUtil.getString(10));
        }

        daoUtil.free();
        return friendlyUrl;
    }

    /**
     * Delete a record from the table
     *
     * @param nFriendlyUrlId The identifier of the friendlyUrl
     * @param plugin The plugin
     */
    public void delete(int nFriendlyUrlId, Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_DELETE, plugin);
        daoUtil.setInt(1, nFriendlyUrlId);
        daoUtil.executeUpdate();
        daoUtil.free();
    }

    /**
     * Update the record in the table
     *
     * @param friendlyUrl The reference of the friendlyUrl
     * @param plugin The plugin
     */
    public void store(FriendlyUrl friendlyUrl, Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_UPDATE, plugin);

        daoUtil.setInt(1, friendlyUrl.getId());
        daoUtil.setString(2, friendlyUrl.getFriendlyUrl());
        daoUtil.setString(3, friendlyUrl.getTechnicalUrl());
        daoUtil.setTimestamp(4, friendlyUrl.getDateCreation());
        daoUtil.setTimestamp(5, friendlyUrl.getDateModification());
        daoUtil.setBoolean(6, friendlyUrl.isCanonical());
        daoUtil.setBoolean(7, friendlyUrl.isSitemap());
        daoUtil.setString(8, friendlyUrl.getSitemapLastmod());
        daoUtil.setString(9, friendlyUrl.getSitemapChangeFreq());
        daoUtil.setString(10, friendlyUrl.getSitemapPriority());
        daoUtil.setInt(11, friendlyUrl.getId());

        daoUtil.executeUpdate();
        daoUtil.free();
    }

    /**
     * Load the data of all the friendlyUrls and returns them as a List
     *
     * @param plugin The plugin
     * @return The List which contains the data of all the friendlyUrls
     */
    public List<FriendlyUrl> selectFriendlyUrlsList(Plugin plugin)
    {
        List<FriendlyUrl> friendlyUrlList = new ArrayList<FriendlyUrl>();
        DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECTALL, plugin);
        daoUtil.executeQuery();

        while (daoUtil.next())
        {
            FriendlyUrl friendlyUrl = new FriendlyUrl();

            friendlyUrl.setId(daoUtil.getInt(1));
            friendlyUrl.setFriendlyUrl(daoUtil.getString(2));
            friendlyUrl.setTechnicalUrl(daoUtil.getString(3));
            friendlyUrl.setDateCreation(daoUtil.getTimestamp(4));
            friendlyUrl.setDateModification(daoUtil.getTimestamp(5));
            friendlyUrl.setCanonical(daoUtil.getBoolean(6));
            friendlyUrl.setSitemap(daoUtil.getBoolean(7));
            friendlyUrl.setSitemapLastmod(daoUtil.getString(8));
            friendlyUrl.setSitemapChangeFreq(daoUtil.getString(9));
            friendlyUrl.setSitemapPriority(daoUtil.getString(10));

            friendlyUrlList.add(friendlyUrl);
        }

        daoUtil.free();
        return friendlyUrlList;
    }
}
