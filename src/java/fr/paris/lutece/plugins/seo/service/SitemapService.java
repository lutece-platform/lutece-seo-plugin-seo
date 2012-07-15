/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
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

import java.util.*;


/**
 *
 * @author pierre
 */
public class SitemapService
{
    private static final String TEMPLATE_SITEMAP_XML = "/admin/plugins/seo/sitemap.xml";
    private static final String MARK_URLS_LIST = "urls_list";
    private static final String FILE_SITEMAP = "/sitemap.xml";
    private static final String PROPERTY_SITEMAP_LOG = "seo.sitemap.log";

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
        SEOPropertiesService.setProperty( SEOProperties.SITEMAP_UPDATE_LOG , strLog );

        return strLog;
    }

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
