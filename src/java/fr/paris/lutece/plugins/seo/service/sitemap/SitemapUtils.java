/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;


/**
 * Sitemap Utils
 */
public final class SitemapUtils
{
    public static final String[] CHANGE_FREQ_VALUES = 
        {
            "always", "hourly", "daily", "weekly", "monthly", "yearly", "never",
        };
    public static final String[] PRIORITY_VALUES = 
        {
            "1.0", "0.9", "0.8", "0.7", "0.6", "0.5", "0.4", "0.3", "0.2", "0.1", "0.0",
        };
    private static SimpleDateFormat _formater = new SimpleDateFormat( "yyyy-MM-dd" );

    /**
     * Private constructor
     */
    private SitemapUtils(  )
    {
    }

    /**
     * Date formater using Sitemaps standard
     * @param date The Date
     * @return The formated date
     */
    public static String formatDate( Timestamp date )
    {
        return _formater.format( date );
    }

    /**
     * Get Change Frequency values
     * @return The list of values
     */
    public static ReferenceList getChangeFrequencyValues(  )
    {
        ReferenceList list = new ReferenceList(  );

        for ( int i = 0; i < CHANGE_FREQ_VALUES.length; i++ )
        {
            list.addItem( CHANGE_FREQ_VALUES[i], CHANGE_FREQ_VALUES[i] );
        }

        return list;
    }

    /**
     * Get priority values
     * @return The list of values
     */
    public static ReferenceList getPriorityValues(  )
    {
        ReferenceList list = new ReferenceList(  );

        for ( int i = 0; i < PRIORITY_VALUES.length; i++ )
        {
            list.addItem( PRIORITY_VALUES[i], PRIORITY_VALUES[i] );
        }

        return list;
    }
}
