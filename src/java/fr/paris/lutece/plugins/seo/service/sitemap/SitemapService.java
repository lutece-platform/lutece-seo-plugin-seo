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

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.service.FriendlyUrlUtils;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

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
    private static final String MARK_BASE_URL = "base_url";
    private static final String PROPERTY_GENERATED_FILES_PATH = "seo.generatedFilesPath";
    private static final String SYSTEM_TMP_DIR = "java.io.tmpdir";
    private static final String DEFAULT_GENERATED_FILES_DIR = "lutece-seo";
    private static final String SITEMAP_FILE_NAME = "sitemap-seo.xml";
    private static final String LEGACY_SITEMAP_FILE_PATH = "/sitemap.xml";
    private static final String [ ] OBSOLETE_WEBAPP_SITEMAPS = {
            "/sitemap-seo.xml", LEGACY_SITEMAP_FILE_PATH
    };
    private static final String PLUGIN_SITEMAP = "sitemap";
    private static final String PROPERTY_LUTECE_PROD_URL = "lutece.prod.url";
    private static final String PROPERTY_SITEMAP_LOG = "seo.sitemap.log";

    /**
     * Private Constructor
     */
    private SitemapService( )
    {
    }

    /**
     * Generate Sitemap
     * 
     * @return The sitemap content
     */
    public static String generateSitemap( )
    {
        List<FriendlyUrl> list = getSitemapUrls( );
        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_URLS_LIST, list );
        model.put( MARK_BASE_URL, getSitemapBaseUrl( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_SITEMAP_XML, Locale.getDefault( ), model );

        String strXmlSitemap = templateList.getHtml( );
        File fileSiteMap = getSitemapFile( );

        String strResult = "OK";

        try
        {
            writeAtomically( fileSiteMap, strXmlSitemap );
        }
        catch( IOException e )
        {
            AppLogService.error( "Error writing Sitemap file : " + e.getMessage( ), e.getCause( ) );
            strResult = "Error : " + e.getMessage( );
        }

        warnObsoleteSitemap( );

        String strDate = DateFormat.getDateTimeInstance( ).format( new Date( ) );
        Object [ ] args = {
                strDate, list.size( ), strResult
        };
        String strLogFormat = I18nService.getLocalizedString( PROPERTY_SITEMAP_LOG, Locale.getDefault( ) );
        String strLog = MessageFormat.format( strLogFormat, args );
        DatastoreService.setDataValue( SEODataKeys.KEY_SITEMAP_UPDATE_LOG, strLog );

        return strLog;
    }

    /**
     * Gets the base URL the sitemap locations are prefixed with, ending with a slash. Sitemaps require absolute URLs, so an undefined
     * lutece.prod.url leaves a sitemap search engines reject. That property belongs to the site, which declares it per environment, and this
     * plugin has no sensible default to offer : the case is logged rather than left silent.
     * 
     * @return The base URL, empty when the property is not set
     */
    private static String getSitemapBaseUrl( )
    {
        String strBaseUrl = AppPropertiesService.getProperty( PROPERTY_LUTECE_PROD_URL, "" ).trim( );

        if ( strBaseUrl.isEmpty( ) )
        {
            AppLogService.error( "SEO : " + PROPERTY_LUTECE_PROD_URL
                    + " is not set, the sitemap is generated with relative URLs, which search engines reject. Declare it in the configuration of the site." );

            return strBaseUrl;
        }

        return strBaseUrl.endsWith( "/" ) ? strBaseUrl : ( strBaseUrl + "/" );
    }

    /**
     * Gets the file the sitemap is written to. It lives outside the webapp : the webapp is a build artefact, replaced at
     * every deployment, and a generated file kept there is lost - or, on a site shipping its own sitemap, overwritten.
     * The directory defaults to a subdirectory of the system temporary directory, always writable and harmless since the
     * daemon rebuilds the sitemap anyway ; point seo.generatedFilesPath at a persistent volume to keep it across
     * restarts.
     * 
     * @return The sitemap file, whose parent directory may not exist yet
     */
    public static File getSitemapFile( )
    {
        String strPath = AppPropertiesService.getProperty( PROPERTY_GENERATED_FILES_PATH, "" ).trim( );

        File dir = strPath.isEmpty( ) ? new File( System.getProperty( SYSTEM_TMP_DIR ), DEFAULT_GENERATED_FILES_DIR ) : new File( strPath );

        return new File( dir, SITEMAP_FILE_NAME );
    }

    /**
     * Writes the sitemap through a temporary file renamed onto the target, so that a search engine reading it never gets
     * a truncated document. Plain writing truncates the file before filling it again, and the daemon runs on every node,
     * which means several writers as soon as the directory sits on a shared volume.
     * 
     * @param file
     *            The target file
     * @param strContent
     *            The content to write
     * @throws IOException
     *             If the file cannot be written
     */
    private static void writeAtomically( File file, String strContent ) throws IOException
    {
        Path pathDir = file.toPath( ).getParent( );
        Files.createDirectories( pathDir );

        Path pathTemp = Files.createTempFile( pathDir, file.getName( ), ".tmp" );
        Files.write( pathTemp, strContent.getBytes( StandardCharsets.UTF_8 ) );
        relaxTemporaryFilePermissions( pathTemp );

        try
        {
            Files.move( pathTemp, file.toPath( ), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE );
        }
        catch( AtomicMoveNotSupportedException e )
        {
            AppLogService.debug( "SEO : atomic move unsupported on {}, falling back on a plain move", pathDir, e );
            Files.move( pathTemp, file.toPath( ), StandardCopyOption.REPLACE_EXISTING );
        }
    }

    /**
     * Gives the temporary file the permissions a plain write would have produced. Files.createTempFile creates it
     * readable by its owner only, and the rename keeps that : on a shared volume, a node running under another user would
     * no longer be able to read the sitemap.
     * 
     * @param path
     *            The temporary file
     */
    private static void relaxTemporaryFilePermissions( Path path )
    {
        try
        {
            Files.setPosixFilePermissions( path, PosixFilePermissions.fromString( "rw-r--r--" ) );
        }
        catch( IOException | UnsupportedOperationException e )
        {
            AppLogService.debug( "SEO : unable to set the permissions of {}, keeping the ones it was created with", path, e );
        }
    }

    /**
     * Logs a message for every sitemap left at the webapp root by a former version of this plugin. Nothing writes there
     * any more, so those files are frozen, and the web server keeps serving them to the search engines that discovered
     * them. The message disappears once they have been removed. Nothing is said about sitemap.xml while the sitemap
     * plugin is enabled, since that plugin legitimately maintains it there.
     */
    private static void warnObsoleteSitemap( )
    {
        boolean bSitemapPluginEnabled = PluginService.isPluginEnable( PLUGIN_SITEMAP );

        for ( String strObsolete : OBSOLETE_WEBAPP_SITEMAPS )
        {
            if ( bSitemapPluginEnabled && LEGACY_SITEMAP_FILE_PATH.equals( strObsolete ) )
            {
                continue;
            }

            if ( new File( AppPathService.getWebAppPath( ) + strObsolete ).exists( ) )
            {
                AppLogService.error(
                        "SEO : {} is left over at the webapp root and no longer written. The sitemap is now generated outside the webapp and served by the plugin itself : remove that file, and point the Sitemap directive of robots.txt at /sitemap-seo.xml.",
                        strObsolete );
            }
        }
    }

    /**
     * Get sitemap URLs
     * 
     * @return The list of URL to add to sitemap
     */
    private static List<FriendlyUrl> getSitemapUrls( )
    {
        List<FriendlyUrl> list = new ArrayList<FriendlyUrl>( );

        for ( FriendlyUrl url : FriendlyUrlHome.findAll( ) )
        {
            if ( url.isSitemap( ) )
            {
                url.setFriendlyUrl( FriendlyUrlUtils.cleanUrl( url.getFriendlyUrl( ) ) );
                list.add( url );
            }
        }

        return list;
    }
}
