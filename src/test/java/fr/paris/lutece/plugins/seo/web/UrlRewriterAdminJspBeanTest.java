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

import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

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
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.getManageUrlRewriterRules( request );
    }

    /**
     * Test of getCreateRule method, of class UrlRewriterAdminJspBean.
     */
    private void getCreateRule( )
    {
        System.out.println( "getCreateRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
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
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, UrlRewriterAdminJspBean.ACTION_CREATE_RULE ) );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        try
        {
            instance.doCreateRule( request );
        }
        catch( AccessDeniedException e )
        {
            fail( "a valid token must be accepted" );
        }
    }

    /**
     * Test of getModifyRule method, of class UrlRewriterAdminJspBean.
     */
    private void getModifyRule( String strID )
    {
        System.out.println( "getModifyRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
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
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, UrlRewriterAdminJspBean.ACTION_MODIFY_RULE ) );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        try
        {
            instance.doModifyRule( request );
        }
        catch( AccessDeniedException e )
        {
            fail( "a valid token must be accepted" );
        }
    }

    /**
     * Test of deleteRule method, of class UrlRewriterAdminJspBean.
     */
    private void deleteRule( String strID )
    {
        System.out.println( "deleteRule" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, strID );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
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
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, UrlRewriterAdminJspBean.ACTION_DELETE_RULE ) );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        try
        {
            instance.doDeleteRule( request );
        }
        catch( AccessDeniedException e )
        {
            fail( "a valid token must be accepted" );
        }
    }

    /**
     * Creates a rule for a test.
     *
     * @return the rule
     */
    private static UrlRewriterRule createRule( )
    {
        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/unchanged" );
        rule.setRuleTo( "/to" );
        UrlRewriterRuleHome.create( rule );

        return rule;
    }

    /**
     * Creating a rule requires the token of the creation form.
     */
    @Test
    public void testDoCreateRuleWithoutToken( )
    {
        int nCount = UrlRewriterRuleHome.findAll( ).size( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_FROM, "/created-without-token" );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_TO, "/to" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );

        assertThrows( AccessDeniedException.class, ( ) -> instance.doCreateRule( request ) );
        assertEquals( nCount, UrlRewriterRuleHome.findAll( ).size( ) );
    }

    /**
     * Modifying a rule requires the token of the modification form.
     */
    @Test
    public void testDoModifyRuleWithoutToken( )
    {
        UrlRewriterRule rule = createRule( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, String.valueOf( rule.getIdRule( ) ) );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_FROM, "/changed" );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_TO, "/to" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );

        assertThrows( AccessDeniedException.class, ( ) -> instance.doModifyRule( request ) );
        assertEquals( "/unchanged", UrlRewriterRuleHome.findByPrimaryKey( rule.getIdRule( ) ).getRuleFrom( ) );
        UrlRewriterRuleHome.remove( rule.getIdRule( ) );
    }

    /**
     * Deleting a rule requires the token given with the confirmation.
     */
    @Test
    public void testDoDeleteRuleWithoutToken( )
    {
        UrlRewriterRule rule = createRule( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, String.valueOf( rule.getIdRule( ) ) );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );

        assertThrows( AccessDeniedException.class, ( ) -> instance.doDeleteRule( request ) );
        assertNotNull( UrlRewriterRuleHome.findByPrimaryKey( rule.getIdRule( ) ) );
        UrlRewriterRuleHome.remove( rule.getIdRule( ) );
    }

    /**
     * The creation and modification forms carry a token.
     */
    @Test
    public void testFormsCarryToken( )
    {
        UrlRewriterRule rule = createRule( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( UrlRewriterAdminJspBean.PARAMETER_RULE_ID, String.valueOf( rule.getIdRule( ) ) );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );

        assertTrue( instance.getCreateRule( request ).contains( "name=\"token\"" ), "create form" );
        assertTrue( instance.getModifyRule( request ).contains( "name=\"token\"" ), "modify form" );
        UrlRewriterRuleHome.remove( rule.getIdRule( ) );
    }

    /**
     * The alias field of the create and modify rule forms shows its own help text, not the label followed by ".help".
     */
    @Test
    public void testRuleFormsHelpText( )
    {
        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/from" );
        rule.setRuleTo( "/to" );
        Map<String, Object> model = new HashMap<>( );
        model.put( "rule", rule );
        model.put( SecurityTokenService.MARK_TOKEN, "token" );

        for ( String strTemplate : new String [ ] {
                "admin/plugins/seo/create_urlrewriterrule.html", "admin/plugins/seo/modify_urlrewriterrule.html"
        } )
        {
            String strHtml = AppTemplateService.getTemplate( strTemplate, Locale.ENGLISH, model ).getHtml( );
            assertFalse( strHtml.contains( ".help" ), strTemplate );
            assertTrue( strHtml.contains( I18nService.getLocalizedString( "seo.create_urlrewriterrule.labelRuleFrom.help", Locale.ENGLISH ) ), strTemplate );
        }
    }

    /**
     * Test of doGenerate method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testDoGenerate( )
    {
        System.out.println( "doGenerate" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.doGenerate( request );
    }

    /**
     * Test of getPanelTitle method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelTitle( )
    {
        System.out.println( "getPanelTitle" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.getPanelTitle( );
    }

    /**
     * Test of getPanelContent method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelContent( )
    {
        System.out.println( "getPanelContent" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.getPanelContent( );
    }

    /**
     * Test of getPanelOrder method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelOrder( )
    {
        System.out.println( "getPanelOrder" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.getPanelOrder( );
    }

    /**
     * Test of getPanelKey method, of class UrlRewriterAdminJspBean.
     */
    @Test
    public void testGetPanelKey( )
    {
        System.out.println( "getPanelKey" );
        UrlRewriterAdminJspBean instance = CDI.current( ).select( UrlRewriterAdminJspBean.class ).get( );
        instance.getPanelKey( );
    }

}
