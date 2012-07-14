/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.web;

import fr.paris.lutece.plugins.seo.service.SitemapService;
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
}
