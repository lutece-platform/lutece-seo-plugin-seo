/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.web;

import fr.paris.lutece.plugins.seo.service.SEOProperties;
import fr.paris.lutece.plugins.seo.service.sitemap.SitemapService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author pierre
 */
public class SitemapJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_SEO = SEOJspBean.RIGHT_MANAGE_SEO;

    /**
     * Generate Sitemap.xml
     * @param request The HTTP request
     * @return The log
     */
    public String doGenerateSitemap( HttpServletRequest request )
    {
        SitemapService.generateSitemap(  );

        return getHomeUrl( request );
    }
    
    /**
     * Toggle Daemon activation
     * @param request The HTTP request
     * @return The Home URL
     */
    public String doSitemapDaemonToggle( HttpServletRequest request )
    {
        String strDeamon = DatastoreService.getDataValue(SEOProperties.SITEMAP_DEAMON_ENABLED, SEOProperties.VALUE_FALSE);
        if( strDeamon.equals(SEOProperties.VALUE_TRUE))
        {
            DatastoreService.setDataValue(SEOProperties.SITEMAP_DEAMON_ENABLED, SEOProperties.VALUE_FALSE);
        }
        else
        {
            DatastoreService.setDataValue(SEOProperties.SITEMAP_DEAMON_ENABLED, SEOProperties.VALUE_TRUE);
        }
        return getHomeUrl( request );

    }
}
