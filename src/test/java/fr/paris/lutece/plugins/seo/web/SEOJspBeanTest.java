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
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SEOJspBeanTest
 */
public class SEOJspBeanTest extends LuteceTestCase
{

    /**
     * Test of getManageSEO method, of class SEOJspBean.
     */
    @Test
    public void testGetManageSEO( )
    {
        System.out.println( "getManageSEO" );
        HttpServletRequest request = new MockHttpServletRequest( );
        SEOJspBean instance = CDI.current( ).select( SEOJspBean.class ).get( );

        for ( CommonsInclude ci : CommonsService.getCommonsIncludes( ) )
        {
            CommonsService.activateCommons( ci.getKey( ) );
            System.out.println( "############################ Tests using " + ci.getName( ) + " " + ci.getDescription( ) );
            String strHtml = instance.getManageSEO( request );

            // the panels are HTML rendered by their own templates, written as markup by manage_seo.html
            assertTrue( strHtml.contains( "id=\"doGenerateSitemap\"" ), "the sitemap panel must be rendered as HTML" );
            assertFalse( strHtml.contains( "&lt;form" ), "the content of a panel must not be escaped" );
        }
    }

    /**
     * The forms of the panels of the dashboard (rule file, toggles, sitemap) carry a security token.
     */
    @Test
    public void testPanelFormsCarryToken( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        SEOJspBean instance = CDI.current( ).select( SEOJspBean.class ).get( );

        String strHtml = instance.getManageSEO( request );
        assertFormsCarryToken( strHtml );

        // one form per panel action: the token fields keep the ids of the page unique
        Set<String> setIds = new HashSet<>( );
        Matcher matcher = Pattern.compile( "\\sid=\"([^\"]+)\"" ).matcher( strHtml );
        while ( matcher.find( ) )
        {
            assertTrue( setIds.add( matcher.group( 1 ) ), "duplicate id " + matcher.group( 1 ) );
        }
    }

    /**
     * Asserts that every form of a page carries a security token.
     *
     * @param strHtml
     *            the page
     */
    static void assertFormsCarryToken( String strHtml )
    {
        Matcher matcher = Pattern.compile( "(?s)<form\\b.*?</form>" ).matcher( strHtml );
        int nForms = 0;
        while ( matcher.find( ) )
        {
            nForms++;
            assertTrue( matcher.group( ).contains( "name=\"token\"" ), "form without token: " + matcher.group( ) );
        }
        assertTrue( nForms > 0, "no form found" );
    }

}
