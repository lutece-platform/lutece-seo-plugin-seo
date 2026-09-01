/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.seo.service.sitemap;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;

import java.io.File;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SitemapService Test
 */
public class SitemapServiceTest extends LuteceTestCase
{
    /**
     * The whole point of the file living outside the webapp : a webapp is a build artefact, replaced at every deployment.
     */
    @Test
    public void testSitemapFileIsOutsideTheWebapp( )
    {
        String strSitemap = SitemapService.getSitemapFile( ).getAbsolutePath( );
        String strWebApp = new File( AppPathService.getWebAppPath( ) ).getAbsolutePath( );

        assertFalse( strSitemap.startsWith( strWebApp + File.separator ), strSitemap + " must not be inside " + strWebApp );
        assertTrue( strSitemap.endsWith( File.separator + "sitemap-seo.xml" ), "unexpected sitemap file name : " + strSitemap );
    }

    /**
     * Generating writes the file, whatever the state of the directory beforehand : it is created when missing, and the
     * default one does not survive a restart.
     */
    @Test
    public void testGenerateSitemapWritesTheFile( )
    {
        File fileSitemap = SitemapService.getSitemapFile( );
        fileSitemap.delete( );

        SitemapService.generateSitemap( );

        assertTrue( fileSitemap.isFile( ), fileSitemap + " should have been written" );
        assertTrue( fileSitemap.length( ) > 0, fileSitemap + " should not be empty" );
    }
}
