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
package fr.paris.lutece.plugins.seo.service.generator;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.test.LuteceTestCase;

import jakarta.enterprise.inject.spi.CDI;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FriendlyUrlGeneratorService Test
 */
public class FriendlyUrlGeneratorServiceTest extends LuteceTestCase
{
    /**
     * The settings saved from the generation screen are read by the generator under the same key : the class of the generator,
     * not the class of its CDI proxy.
     */
    @Test
    public void testGeneratorKeyIsTheGeneratorClass( )
    {
        FriendlyUrlGeneratorService service = CDI.current( ).select( FriendlyUrlGeneratorService.class ).get( );
        List<GeneratorSettings> listGenerators = service.getGenerators( );

        assertTrue( listGenerators.stream( ).anyMatch( gs -> PageFriendlyUrlGenerator.class.getName( ).equals( gs.getKey( ) ) ),
                "the page generator must be listed under its class name : " + listGenerators.stream( ).map( GeneratorSettings::getKey ).toList( ) );
    }

    /**
     * Two pages of the same name get two different friendly URLs
     */
    @Test
    public void testPagesOfTheSameNameGetDifferentFriendlyUrls( )
    {
        FriendlyUrlGeneratorService service = CDI.current( ).select( FriendlyUrlGeneratorService.class ).get( );
        List<FriendlyUrl> listRules = new ArrayList<>( );
        listRules.add( newUrl( "/seo-same-name", "/jsp/site/Portal.jsp?page_id=98701" ) );
        listRules.add( newUrl( "/seo-same-name", "/jsp/site/Portal.jsp?page_id=98702" ) );
        listRules.add( newUrl( "/seo-same-name", "/jsp/site/Portal.jsp?page_id=98703" ) );
        GeneratorOptions options = new GeneratorOptions( );
        options.setHtmlSuffix( true );

        try
        {
            service.processRuleList( listRules, new ArrayList<>( FriendlyUrlHome.findAll( ) ), options );

            assertEquals( "/seo-same-name.html", findFriendlyUrl( "/jsp/site/Portal.jsp?page_id=98701" ) );
            assertEquals( "/seo-same-name-2.html", findFriendlyUrl( "/jsp/site/Portal.jsp?page_id=98702" ) );
            assertEquals( "/seo-same-name-3.html", findFriendlyUrl( "/jsp/site/Portal.jsp?page_id=98703" ) );
        }
        finally
        {
            FriendlyUrlHome.findAll( ).stream( ).filter( u -> u.getFriendlyUrl( ).startsWith( "/seo-same-name" ) )
                    .forEach( u -> FriendlyUrlHome.remove( u.getId( ) ) );
        }
    }

    private static FriendlyUrl newUrl( String strFriendlyUrl, String strTechnicalUrl )
    {
        FriendlyUrl url = new FriendlyUrl( );
        url.setFriendlyUrl( strFriendlyUrl );
        url.setTechnicalUrl( strTechnicalUrl );
        return url;
    }

    private static String findFriendlyUrl( String strTechnicalUrl )
    {
        return FriendlyUrlHome.findAll( ).stream( ).filter( u -> strTechnicalUrl.equals( u.getTechnicalUrl( ) ) ).map( FriendlyUrl::getFriendlyUrl )
                .findFirst( ).orElse( null );
    }
}
