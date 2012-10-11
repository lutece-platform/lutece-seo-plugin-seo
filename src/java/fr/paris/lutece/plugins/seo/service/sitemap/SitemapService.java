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
package fr.paris.lutece.plugins.seo.service.sitemap;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.service.FriendlyUrlUtils;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Sitemap Service
 */
public final class SitemapService
{
    private static final String TEMPLATE_SITEMAP_XML = "/admin/plugins/seo/sitemap.xml";
    private static final String MARK_URLS_LIST = "urls_list";
    private static final String FILE_SITEMAP = "/sitemap.xml";
    private static final String PROPERTY_SITEMAP_LOG = "seo.sitemap.log";

    /**
     * Private Constructor
     */
    private SitemapService(  )
    {
    }

    /**
     * Generate Sitemap
     * @return The sitemap content
     */
    public static String generateSitemap(  )
    {
        List<FriendlyUrl> list = getSitemapUrls(  );
        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_URLS_LIST, list );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_SITEMAP_XML, Locale.getDefault(  ), model );

        String strXmlSitemap = templateList.getHtml(  );
        String strSiteMapFilePath = AppPathService.getWebAppPath(  ) + FILE_SITEMAP;
        File fileSiteMap = new File( strSiteMapFilePath );

        String strResult = "OK";

        try
        {
            FileUtils.writeStringToFile( fileSiteMap, strXmlSitemap );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error writing Sitemap file : " + e.getMessage(  ), e.getCause(  ) );
            strResult = "Error : " + e.getMessage(  );
        }

        String strDate = DateFormat.getDateTimeInstance(  ).format( new Date(  ) );
        Object[] args = { strDate, list.size(  ), strResult };
        String strLogFormat = I18nService.getLocalizedString( PROPERTY_SITEMAP_LOG, Locale.getDefault(  ) );
        String strLog = MessageFormat.format( strLogFormat, args );
        DatastoreService.setDataValue( SEODataKeys.KEY_SITEMAP_UPDATE_LOG, strLog );

        return strLog;
    }

    /**
     * Get sitemap URLs
     * @return The list of URL to add to sitemap
     */
    private static List<FriendlyUrl> getSitemapUrls(  )
    {
        List<FriendlyUrl> list = new ArrayList<FriendlyUrl>(  );

        for ( FriendlyUrl url : FriendlyUrlHome.findAll(  ) )
        {
            if ( url.isSitemap(  ) )
            {
                url.setFriendlyUrl( FriendlyUrlUtils.cleanSlash( url.getFriendlyUrl(  ) ) );
                list.add( url );
            }
        }

        return list;
    }
}
