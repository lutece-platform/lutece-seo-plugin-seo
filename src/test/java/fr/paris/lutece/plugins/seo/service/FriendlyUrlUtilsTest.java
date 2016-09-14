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
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.test.LuteceTestCase;

import org.junit.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pierre
 */
public class FriendlyUrlUtilsTest extends LuteceTestCase
{
    private static final String SOURCE = "page.html";
    private static final String BASE_URL = "http://lutece.paris.fr/mywebapp/";

    /**
     * Test of convertToAlias method, of class GeneratorUtils.
     */
    @Test
    public void testConvertToFriendlyUrl( )
    {
        System.out.println( "convertToFriendlyUrl" );

        String strSource = "L'élément à intégrer";
        String expResult = "l-element-a-integrer";
        String result = FriendlyUrlUtils.convertToFriendlyUrl( strSource );
        assertEquals( expResult, result );
    }

    /**
     * Test of replaceLink method, of class GeneratorUtils.
     */
    @Test
    public void testReplaceByFriendlyUrl( ) throws IOException
    {
        System.out.println( "replaceByFriendlyUrl" );

        String strSource = getFileContent( SOURCE );
        Map<String, String> map = new HashMap<String, String>( );
        map.put( "toto", "replaced" );
        map.put( "tutu", "replaced" );

        String result = FriendlyUrlUtils.replaceByFriendlyUrl( strSource, map, BASE_URL );

        int nReplacementCount = 0;
        int nPos = result.indexOf( "replaced" );

        while ( nPos != -1 )
        {
            nReplacementCount++;
            nPos = result.indexOf( "replaced", nPos + 1 );
        }

        assertEquals( nReplacementCount, 3 );
        System.out.println( result );
    }

    public String getFileContent( String strResource ) throws IOException
    {
        InputStream is = getClass( ).getResourceAsStream( "/" + strResource );
        InputStreamReader isr = new InputStreamReader( is );
        BufferedReader in = new BufferedReader( isr );
        Writer writer = new StringWriter( );

        if ( in != null )
        {
            char [ ] buffer = new char [ 1024];

            try
            {
                int n;

                while ( ( n = in.read( buffer ) ) != -1 )
                {
                    writer.write( buffer, 0, n );
                }
            }
            finally
            {
                isr.close( );
            }

            return writer.toString( );
        }
        else
        {
            return "";
        }
    }
}
