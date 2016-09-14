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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for UrlRewriterRule objects
 */
public final class UrlRewriterRuleHome
{
    // Static variable pointed at the DAO instance
    private static final String BEAN_DAO = "seo.urlRewriterRuleDAO";
    private static IUrlRewriterRuleDAO _dao = (IUrlRewriterRuleDAO) SpringContextService.getBean( BEAN_DAO );
    private static Plugin _plugin = PluginService.getPlugin( "seo" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private UrlRewriterRuleHome( )
    {
    }

    /**
     * Create an instance of the urlRewriterRule class
     * 
     * @param urlRewriterRule
     *            The instance of the UrlRewriterRule which contains the informations to store
     * @return The instance of urlRewriterRule which has been created with its primary key.
     */
    public static UrlRewriterRule create( UrlRewriterRule urlRewriterRule )
    {
        _dao.insert( urlRewriterRule, _plugin );

        return urlRewriterRule;
    }

    /**
     * Update of the urlRewriterRule which is specified in parameter
     * 
     * @param urlRewriterRule
     *            The instance of the UrlRewriterRule which contains the data to store
     * @return The instance of the urlRewriterRule which has been updated
     */
    public static UrlRewriterRule update( UrlRewriterRule urlRewriterRule )
    {
        _dao.store( urlRewriterRule, _plugin );

        return urlRewriterRule;
    }

    /**
     * Remove the urlRewriterRule whose identifier is specified in parameter
     * 
     * @param nUrlRewriterRuleId
     *            The urlRewriterRule Id
     */
    public static void remove( int nUrlRewriterRuleId )
    {
        _dao.delete( nUrlRewriterRuleId, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a urlRewriterRule whose identifier is specified in parameter
     * 
     * @param nKey
     *            The urlRewriterRule primary key
     * @return an instance of UrlRewriterRule
     */
    public static UrlRewriterRule findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the urlRewriterRule objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the urlRewriterRule objects
     */
    public static Collection<UrlRewriterRule> findAll( )
    {
        return _dao.selectUrlRewriterRulesList( _plugin );
    }
}
