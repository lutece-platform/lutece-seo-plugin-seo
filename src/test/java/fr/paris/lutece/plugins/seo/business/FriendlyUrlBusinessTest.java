/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Timestamp;


public class FriendlyUrlBusinessTest extends LuteceTestCase
{
    private final static int IDURL1 = 1;
    private final static int IDURL2 = 2;
    private final static String FRIENDLYURL1 = "FriendlyUrl1";
    private final static String FRIENDLYURL2 = "FriendlyUrl2";
    private final static String TECHNICALURL1 = "TechnicalUrl1";
    private final static String TECHNICALURL2 = "TechnicalUrl2";
    private final static Timestamp DATECREATION1 = new Timestamp( 0 );
    private final static Timestamp DATECREATION2 = new Timestamp( 1 );
    private final static Timestamp DATEMODIFICATION1 = new Timestamp( 2 );
    private final static Timestamp DATEMODIFICATION2 = new Timestamp( 3 );
    private final static boolean ISCANONICAL1 = true;
    private final static boolean ISCANONICAL2 = false;
    private final static boolean ISSITEMAP1 = true;
    private final static boolean ISSITEMAP2 = false;
    private final static String SITEMAPLASTMOD1 = "SitemapLastmod1";
    private final static String SITEMAPLASTMOD2 = "SitemapLastmod2";

    public void testBusiness(  )
    {
        // Initialize an object
        FriendlyUrl friendlyUrl = new FriendlyUrl(  );
        friendlyUrl.setId( IDURL1 );
        friendlyUrl.setFriendlyUrl( FRIENDLYURL1 );
        friendlyUrl.setTechnicalUrl( TECHNICALURL1 );
        friendlyUrl.setDateCreation( DATECREATION1 );
        friendlyUrl.setDateModification( DATEMODIFICATION1 );
        friendlyUrl.setCanonical( ISCANONICAL1 );
        friendlyUrl.setSitemap( ISSITEMAP1 );
        friendlyUrl.setSitemapLastmod( SITEMAPLASTMOD1 );

        // Create test
        FriendlyUrlHome.create( friendlyUrl );

        FriendlyUrl friendlyUrlStored = FriendlyUrlHome.findByPrimaryKey( friendlyUrl.getId(  ) );
        assertEquals( friendlyUrlStored.getId(  ), friendlyUrl.getId(  ) );
        assertEquals( friendlyUrlStored.getFriendlyUrl(  ), friendlyUrl.getFriendlyUrl(  ) );
        assertEquals( friendlyUrlStored.getTechnicalUrl(  ), friendlyUrl.getTechnicalUrl(  ) );
        assertEquals( friendlyUrlStored.getDateCreation(  ), friendlyUrl.getDateCreation(  ) );
        assertEquals( friendlyUrlStored.getDateModification(  ), friendlyUrl.getDateModification(  ) );
        assertEquals( friendlyUrlStored.isCanonical(  ), friendlyUrl.isCanonical(  ) );
        assertEquals( friendlyUrlStored.isSitemap(  ), friendlyUrl.isSitemap(  ) );
        assertEquals( friendlyUrlStored.getSitemapLastmod(  ), friendlyUrl.getSitemapLastmod(  ) );

        // Update test
        friendlyUrl.setId( IDURL2 );
        friendlyUrl.setFriendlyUrl( FRIENDLYURL2 );
        friendlyUrl.setTechnicalUrl( TECHNICALURL2 );
        friendlyUrl.setDateCreation( DATECREATION2 );
        friendlyUrl.setDateModification( DATEMODIFICATION2 );
        friendlyUrl.setCanonical( ISCANONICAL2 );
        friendlyUrl.setSitemap( ISSITEMAP2 );
        friendlyUrl.setSitemapLastmod( SITEMAPLASTMOD2 );
        FriendlyUrlHome.update( friendlyUrl );
        friendlyUrlStored = FriendlyUrlHome.findByPrimaryKey( friendlyUrl.getId(  ) );
        assertEquals( friendlyUrlStored.getId(  ), friendlyUrl.getId(  ) );
        assertEquals( friendlyUrlStored.getFriendlyUrl(  ), friendlyUrl.getFriendlyUrl(  ) );
        assertEquals( friendlyUrlStored.getTechnicalUrl(  ), friendlyUrl.getTechnicalUrl(  ) );
        assertEquals( friendlyUrlStored.getDateCreation(  ), friendlyUrl.getDateCreation(  ) );
        assertEquals( friendlyUrlStored.getDateModification(  ), friendlyUrl.getDateModification(  ) );
        assertEquals( friendlyUrlStored.isCanonical(  ), friendlyUrl.isCanonical(  ) );
        assertEquals( friendlyUrlStored.isSitemap(  ), friendlyUrl.isSitemap(  ) );
        assertEquals( friendlyUrlStored.getSitemapLastmod(  ), friendlyUrl.getSitemapLastmod(  ) );

        // List test
        FriendlyUrlHome.findAll(  );

        // Delete test
        FriendlyUrlHome.remove( friendlyUrl.getId(  ) );
        friendlyUrlStored = FriendlyUrlHome.findByPrimaryKey( friendlyUrl.getId(  ) );
        assertNull( friendlyUrlStored );
    }
}
