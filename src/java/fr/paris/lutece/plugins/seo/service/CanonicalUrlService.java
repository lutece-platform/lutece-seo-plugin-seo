/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Canonical Url Service
 */
public class CanonicalUrlService
{
    private static final String HEAD = "<head>";
    private static final String COMMENT = "\n\t\t<!-- Canonical URL added by SEO plugin --> ";
    private static final String CANONICAL_TAG_BEGIN = "\n\t\t<link rel=\"canonical\" href=\"";
    private static final String CANONICAL_TAG_END = "\" />\n";
    private static CanonicalUrlService _singleton = new CanonicalUrlService(  );
    private static boolean _bCanonical;

    private CanonicalUrlService(  )
    {
        _bCanonical = DatastoreService.getDataValue( SEODataKeys.KEY_CANONICAL_URLS_ENABLED, "" )
                                      .equals( DatastoreService.VALUE_TRUE );
    }

    public static CanonicalUrlService instance(  )
    {
        return _singleton;
    }

    /**
     * Tells if canonical urls are enabled
     * @return  True if canonical urls are enabled, otherwise false
     */
    public boolean isCanonicalUrlsEnabled(  )
    {
        return _bCanonical;
    }

    /**
     * Sets the status of Canonical URLs (Enabled  or Disabled)
     * @param bEnabled
     */
    public void setCanonicalUrlsEnabled( boolean bEnabled )
    {
        _bCanonical = bEnabled;
    }

    /**
     * Add canonical URL into an HTML page's content
     * @param strContent The HTML page's content
     * @param request The HTTP request
     * @param mapFriendlyUrls The map of friendly URLS
     * @param strBaseUrl The Base URL
     * @return The HTML page content with the canonical URL inserted
     */
    public String addCanonicalUrl( String strContent, HttpServletRequest request, Map<String, String> mapFriendlyUrls,
        String strBaseUrl )
    {
        StringBuilder sbUrl = new StringBuilder(  );
        sbUrl.append( request.getRequestURI(  ).substring( request.getContextPath(  ).length(  ) + 1 ) );
        sbUrl.append( "?" ).append( request.getQueryString(  ) );

        String strFriendlyUrl = mapFriendlyUrls.get( sbUrl.toString(  ) );

        if ( strFriendlyUrl != null )
        {
            return insertCanonicalUrl( strContent, strBaseUrl + strFriendlyUrl );
        }

        return strContent;
    }

    /**
     * Add canonical URL into an HTML page's content
     * @param strContent The HTML page's content
     * @param strUrl The canonical URL to insert
     * @return The HTML page content with the canonical URL inserted
     */
    private String insertCanonicalUrl( String strContent, String strUrl )
    {
        StringBuilder sb = new StringBuilder(  );
        int nPos = strContent.indexOf( HEAD );

        if ( nPos < 0 )
        {
            AppLogService.error( "CanonicalUrl Service : no HEAD tag found in " + strUrl );

            return strContent;
        }

        sb.append( strContent.substring( 0, nPos + HEAD.length(  ) ) );
        sb.append( COMMENT );
        sb.append( CANONICAL_TAG_BEGIN );
        sb.append( strUrl );
        sb.append( CANONICAL_TAG_END );
        sb.append( strContent.substring( nPos + HEAD.length(  ) ) );

        return sb.toString(  );
    }
}
