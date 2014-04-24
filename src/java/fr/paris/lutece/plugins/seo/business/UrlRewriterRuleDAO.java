/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
import java.util.Collection;


/**
 * This class provides Data Access methods for UrlRewriterRule objects
 */
public final class UrlRewriterRuleDAO implements IUrlRewriterRuleDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_rule ) FROM seo_rule";
    private static final String SQL_QUERY_SELECT = "SELECT id_rule, rule_from, rule_to FROM seo_rule WHERE id_rule = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO seo_rule ( id_rule, rule_from, rule_to ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM seo_rule WHERE id_rule = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE seo_rule SET id_rule = ?, rule_from = ?, rule_to = ? WHERE id_rule = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_rule, rule_from, rule_to FROM seo_rule";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     * @param urlRewriterRule instance of the UrlRewriterRule object to insert
     * @param plugin The plugin
     */
    public void insert( UrlRewriterRule urlRewriterRule, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        urlRewriterRule.setIdRule( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, urlRewriterRule.getIdRule(  ) );
        daoUtil.setString( 2, urlRewriterRule.getRuleFrom(  ) );
        daoUtil.setString( 3, urlRewriterRule.getRuleTo(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the urlRewriterRule from the table
     * @param nId The identifier of the urlRewriterRule
     * @param plugin The plugin
     * @return the instance of the UrlRewriterRule
     */
    public UrlRewriterRule load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        UrlRewriterRule urlRewriterRule = null;

        if ( daoUtil.next(  ) )
        {
            urlRewriterRule = new UrlRewriterRule(  );

            urlRewriterRule.setIdRule( daoUtil.getInt( 1 ) );
            urlRewriterRule.setRuleFrom( daoUtil.getString( 2 ) );
            urlRewriterRule.setRuleTo( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return urlRewriterRule;
    }

    /**
     * Delete a record from the table
     * @param nUrlRewriterRuleId The identifier of the urlRewriterRule
     * @param plugin The plugin
     */
    public void delete( int nUrlRewriterRuleId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nUrlRewriterRuleId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param urlRewriterRule The reference of the urlRewriterRule
     * @param plugin The plugin
     */
    public void store( UrlRewriterRule urlRewriterRule, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, urlRewriterRule.getIdRule(  ) );
        daoUtil.setString( 2, urlRewriterRule.getRuleFrom(  ) );
        daoUtil.setString( 3, urlRewriterRule.getRuleTo(  ) );
        daoUtil.setInt( 4, urlRewriterRule.getIdRule(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the urlRewriterRules and returns them as a collection
     * @param plugin The plugin
     * @return The Collection which contains the data of all the urlRewriterRules
     */
    public Collection<UrlRewriterRule> selectUrlRewriterRulesList( Plugin plugin )
    {
        Collection<UrlRewriterRule> urlRewriterRuleList = new ArrayList<UrlRewriterRule>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            UrlRewriterRule urlRewriterRule = new UrlRewriterRule(  );

            urlRewriterRule.setIdRule( daoUtil.getInt( 1 ) );
            urlRewriterRule.setRuleFrom( daoUtil.getString( 2 ) );
            urlRewriterRule.setRuleTo( daoUtil.getString( 3 ) );

            urlRewriterRuleList.add( urlRewriterRule );
        }

        daoUtil.free(  );

        return urlRewriterRuleList;
    }
}
