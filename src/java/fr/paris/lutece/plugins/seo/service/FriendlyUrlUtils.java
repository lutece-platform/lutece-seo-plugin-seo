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
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import java.util.Map;

/**
 * Friendly URL Utils
 */
public final class FriendlyUrlUtils
{
    private static final String ANCHOR = "<a ";
    private static final String HREF = "href=\"";
    private static final String END_URL = "\"";
    private static final String SLASH = "/";

    /**
     * Private constructor
     */
    private FriendlyUrlUtils( )
    {
    }

    /**
     * Normalize a string to a friendly URL
     * 
     * @param strSource
     *            The source
     * @return The converted string
     */
    public static String convertToFriendlyUrl( String strSource )
    {
        return Normalizer.normalize( strSource.toLowerCase( ), Form.NFD ).replaceAll( "\\p{InCombiningDiacriticalMarks}+", "" ).replaceAll( "[^\\p{Alnum}]+",
                "-" );
    }

    /**
     * Replace in the source all URL found in the map
     * 
     * @param strSource
     *            The source
     * @param map
     *            The Map that contains Friendly URL mapping
     * @param strBaseUrl
     *            The base URL
     * @return The source with found URL replaced
     */
    public static String replaceByFriendlyUrl( String strSource, Map<String, String> map, String strBaseUrl )
    {
        StringBuilder sbOutput = new StringBuilder( );
        String strCurrent = strSource;
        String strUrl;
        String strFriendlyUrl;
        int nPosBeginUrl;
        int nPosEndUrl;

        int nPos = strCurrent.indexOf( ANCHOR );

        while ( nPos >= 0 )
        {
            String strEnd = strCurrent.substring( nPos );
            nPosBeginUrl = strEnd.indexOf( HREF ) + HREF.length( );
            sbOutput.append( strCurrent.substring( 0, nPos + nPosBeginUrl ) );
            strCurrent = strEnd.substring( nPosBeginUrl );
            nPosEndUrl = strCurrent.indexOf( END_URL );
            strUrl = strCurrent.substring( 0, nPosEndUrl );
            strUrl = strUrl.trim( );
            strUrl = removeBaseUrl( strUrl, strBaseUrl );
            strUrl = strUrl.replaceAll( "&amp;", "&" );
            strFriendlyUrl = map.get( strUrl );
            sbOutput.append( ( strFriendlyUrl != null ) ? strFriendlyUrl : strUrl );

            if ( strFriendlyUrl != null )
            {
                AppLogService.debug( "Url : " + strUrl + " replaced by : " + strFriendlyUrl );
            }

            strCurrent = strCurrent.substring( nPosEndUrl );
            nPos = strCurrent.indexOf( ANCHOR );
        }

        sbOutput.append( strCurrent );

        return sbOutput.toString( );
    }

    /**
     * Clean leading slash character
     * 
     * @param strUrl
     *            The source
     * @return The cleaned URL
     */
    public static String cleanUrl( String strUrl )
    {
        String strClean = strUrl.startsWith( SLASH ) ? strUrl.substring( 1, strUrl.length( ) ) : strUrl;
        strClean = strClean.replaceAll( "&amp;", "&" );

        return strClean;
    }

    /**
     * Remove base URL
     * 
     * @param strUrl
     *            The absolute URL
     * @param strBaseUrl
     *            The base URL
     * @return The relative URL
     */
    private static String removeBaseUrl( String strUrl, String strBaseUrl )
    {
        return ( strUrl.startsWith( strBaseUrl ) ? strUrl.substring( strBaseUrl.length( ), strUrl.length( ) ) : strUrl );
    }
}
