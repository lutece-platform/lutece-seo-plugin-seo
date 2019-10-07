/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.plugins.seo.web;

import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.test.LuteceTestCase;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 * @author levy
 */
public class FriendlyUrlJspBeanTest extends LuteceTestCase
{
    private static final String DATA_FROM = "http://friendly.url/";
    private static final String DATA_TO = "http://technical.url/";
    private static final String DATA_CANONICAL = "1";
    private static final String DATA_SITEMAP = "1";
    private static final String DATA_CHANGE_FREQ = "weekly";
    private static final String DATA_PRIORITY = "0.5";

    private static final String [ ] TOGGLES = {
            FriendlyUrlJspBean.TOGGLE_CANONICAL_URLS, FriendlyUrlJspBean.TOGGLE_FRIENDLY_URL_DAEMON, FriendlyUrlJspBean.TOGGLE_REPLACE_URL
    };

    @Test
    public void testSEO( )
    {
        for ( CommonsInclude ci : CommonsService.getCommonsIncludes( ) )
        {
            CommonsService.activateCommons( ci.getKey( ) );
            System.out.println( "############################ Tests using " + ci.getName( ) + " " + ci.getDescription( ) );

            doDeleteAllUrls( );
            getCreateUrl( );
            doCreateUrl( );
            getManageFriendlyUrls( );
            getModifyUrl( );
            doModifyUrl( );
            deleteUrl( );
            doDeleteUrl( );
        }
    }

    /**
     * Test of getManageFriendlyUrls method, of class FriendlyUrlJspBean.
     */
    private void getManageFriendlyUrls( )
    {
        System.out.println( "getManageFriendlyUrls" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getManageFriendlyUrls( request );
    }

    /**
     * Test of getCreateUrl method, of class FriendlyUrlJspBean.
     */
    private void getCreateUrl( )
    {
        System.out.println( "getCreateUrl" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getCreateUrl( request );
    }

    /**
     * Test of doCreateUrl method, of class FriendlyUrlJspBean.
     */
    private void doCreateUrl( )
    {
        System.out.println( "doCreateUrl" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_FROM, DATA_FROM );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_TO, DATA_TO );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_CANONICAL, DATA_CANONICAL );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_SITEMAP, DATA_SITEMAP );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_CHANGE_FREQ, DATA_CHANGE_FREQ );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_PRIORITY, DATA_PRIORITY );

        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doCreateUrl( request );
    }

    /**
     * Test of getModifyUrl method, of class FriendlyUrlJspBean.
     */
    private void getModifyUrl( )
    {
        System.out.println( "getModifyUrl" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_URL_ID, "1" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getModifyUrl( request );
    }

    /**
     * Test of doModifyUrl method, of class FriendlyUrlJspBean.
     */
    private void doModifyUrl( )
    {
        System.out.println( "doModifyUrl" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_URL_ID, "1" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doModifyUrl( request );
    }

    /**
     * Test of deleteUrl method, of class FriendlyUrlJspBean.
     */
    private void deleteUrl( )
    {
        System.out.println( "deleteUrl" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.deleteUrl( request );
    }

    /**
     * Test of doDeleteUrl method, of class FriendlyUrlJspBean.
     */
    private void doDeleteUrl( )
    {
        System.out.println( "doDeleteUrl" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( FriendlyUrlJspBean.PARAMETER_URL_ID, "1" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doDeleteUrl( request );
    }

    /**
     * Test of doDeleteAllUrls method, of class FriendlyUrlJspBean.
     */

    private void doDeleteAllUrls( )
    {
        System.out.println( "doDeleteAllUrls" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doDeleteAllUrls( request );
    }

    /**
     * Test of doGenerate method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testDoGenerate( )
    {
        System.out.println( "doGenerate" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doGenerate( request );
    }

    /**
     * Test of getGenerateAliasRules method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testGetGenerateAliasRules( )
    {
        System.out.println( "getGenerateAliasRules" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getGenerateAliasRules( request );
    }

    /**
     * Test of doGenerateAliasRules method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testDoGenerateAliasRules( )
    {
        System.out.println( "doGenerateAliasRules" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doGenerateAliasRules( request );
    }

    /**
     * Test of doToggle method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testDoToggle( )
    {
        System.out.println( "doToggle" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        for ( String strToggle : TOGGLES )
        {
            request.setParameter( FriendlyUrlJspBean.PARAMETER_TOGGLE, strToggle );
            instance.doToggle( request );
        }
    }

    /**
     * Test of doUpdateGeneratorSettings method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testDoUpdateGeneratorSettings( )
    {
        System.out.println( "doUpdateGeneratorSettings" );
        HttpServletRequest request = new MockHttpServletRequest( );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.doUpdateGeneratorSettings( request );
    }

    /**
     * Test of getPanelTitle method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testGetPanelTitle( )
    {
        System.out.println( "getPanelTitle" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getPanelTitle( );
    }

    /**
     * Test of getPanelContent method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testGetPanelContent( )
    {
        System.out.println( "getPanelContent" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getPanelContent( );
    }

    /**
     * Test of getPanelOrder method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testGetPanelOrder( )
    {
        System.out.println( "getPanelOrder" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getPanelOrder( );
    }

    /**
     * Test of getPanelKey method, of class FriendlyUrlJspBean.
     */
    @Test
    public void testGetPanelKey( )
    {
        System.out.println( "getPanelKey" );
        FriendlyUrlJspBean instance = new FriendlyUrlJspBean( );
        instance.getPanelKey( );
    }

}
