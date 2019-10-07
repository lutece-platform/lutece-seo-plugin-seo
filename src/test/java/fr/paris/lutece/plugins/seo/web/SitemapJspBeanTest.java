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

import fr.paris.lutece.test.LuteceTestCase;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * SitemapJspBeanTest
 */
public class SitemapJspBeanTest extends LuteceTestCase
{

    /**
     * Test of doGenerateSitemap method, of class SitemapJspBean.
     */
    @Test
    public void testDoGenerateSitemap( )
    {
        System.out.println( "doGenerateSitemap" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.doGenerateSitemap( request );
    }

    /**
     * Test of doSitemapDaemonToggle method, of class SitemapJspBean.
     */
    @Test
    public void testDoSitemapDaemonToggle( )
    {
        System.out.println( "doSitemapDaemonToggle" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.doSitemapDaemonToggle( request );
    }

    /**
     * Test of getPanelTitle method, of class SitemapJspBean.
     */
    @Test
    public void testGetPanelTitle( )
    {
        System.out.println( "getPanelTitle" );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.getPanelTitle( );
    }

    /**
     * Test of getPanelContent method, of class SitemapJspBean.
     */
    @Test
    public void testGetPanelContent( )
    {
        System.out.println( "getPanelContent" );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.getPanelContent( );
    }

    /**
     * Test of getPanelOrder method, of class SitemapJspBean.
     */
    @Test
    public void testGetPanelOrder( )
    {
        System.out.println( "getPanelOrder" );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.getPanelOrder( );
    }

    /**
     * Test of getPanelKey method, of class SitemapJspBean.
     */
    @Test
    public void testGetPanelKey( )
    {
        System.out.println( "getPanelKey" );
        SitemapJspBean instance = new SitemapJspBean( );
        instance.getPanelKey( );
    }

}
