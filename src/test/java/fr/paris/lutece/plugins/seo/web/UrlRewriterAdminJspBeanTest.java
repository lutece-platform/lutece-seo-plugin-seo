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

import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.test.LuteceTestCase;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * UrlRewriterAdminJspBeanTest
 */
public class UrlRewriterAdminJspBeanTest extends LuteceTestCase
{

    @Test
    public void testUrlRewriterAdminJspBean( )
    {
        for ( CommonsInclude ci : CommonsService.getCommonsIncludes( ) )
        {
            CommonsService.activateCommons( ci.getKey( ) );
            System.out.println( "############################ Tests using " + ci.getName( ) + " " + ci.getDescription( ) );
            String strRuleId = String.valueOf( UrlRewriterRuleHome.getLastId( ) );
            getCreateRule( );
            doCreateRule( );
            getManageUrlRewriterRules( );
            getModifyRule( strRuleId );
            doModifyRule( strRuleId );
            deleteRule( strRuleId );
            doDeleteRule( strRuleId );
        }
    }

    /**
     * Test of getManageUrlRewriterRules method, of class UrlRewriterAdminJspBean.
     */
    private void getManageUrlRewriterRules( )
    {
        System.out.println( "getManageUrlRewriterRules" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getManageUrlRewriterRules( request );
    }

    /**
     * Test of getCreateRule method, of class UrlRewriterAdminJspBean.
     */
    private void getCreateRule( )
    {
        System.out.println( "getCreateRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getCreateRule( request );
    }

    /**
     * Test of doCreateRule method, of class UrlRewriterAdminJspBean.
     */
    private void doCreateRule( )
    {
        System.out.println( "doCreateRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_FROM, "http://from.url" );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_TO, "http://to.url" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.doCreateRule( request );
    }

    /**
     * Test of getModifyRule method, of class UrlRewriterAdminJspBean.
     */
    private void getModifyRule( String strID )
    {
        System.out.println( "getModifyRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getModifyRule( request );
    }

    /**
     * Test of doModifyRule method, of class UrlRewriterAdminJspBean.
     */
    private void doModifyRule( String strID )
    {
        System.out.println( "doModifyRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_FROM, "http://from.url" );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_TO, "http://to.url" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.doModifyRule( request );
    }

    /**
     * Test of deleteRule method, of class UrlRewriterAdminJspBean.
     */
    private void deleteRule( String strID )
    {
        System.out.println( "deleteRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.deleteRule( request );
    }

    /**
     * Test of doDeleteRule method, of class UrlRewriterAdminJspBean.
     */
    private void doDeleteRule( String strID )
    {
        System.out.println( "doDeleteRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.doDeleteRule( request );
    }

    /**
     * Test of doGenerate method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testDoGenerate( )
    {
        System.out.println( "doGenerate" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.doGenerate( request );
    }

    /**
     * Test of getPanelTitle method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelTitle( )
    {
        System.out.println( "getPanelTitle" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getPanelTitle( );
    }

    /**
     * Test of getPanelContent method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelContent( )
    {
        System.out.println( "getPanelContent" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getPanelContent( );
    }

    /**
     * Test of getPanelOrder method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelOrder( )
    {
        System.out.println( "getPanelOrder" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getPanelOrder( );
    }

    /**
     * Test of getPanelKey method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelKey( )
    {
        System.out.println( "getPanelKey" );
        UrlRewriterAdminJspBean instance = new UrlRewriterAdminJspBean( );
        instance.getPanelKey( );
    }

}
