/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.web;

import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.plugins.seo.service.sitemap.SitemapService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author pierre
 */
public class SitemapJspBean extends SEOPanelJspBean implements SEOPanel
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_SEO = SEOJspBean.RIGHT_MANAGE_SEO;

    private static final String TEMPLATE_CONTENT = "/admin/plugins/seo/panel/sitemap_panel.html";
    private static final String PROPERTY_TITlE = "seo.panel.sitemap.title";
    private static final String MARK_LAST_GENERATION = "sitemapLastGeneration";
    private static final String MARK_DAEMON_ENABLED = "daemon_enabled";

    private static final int PANEL_ORDER = 2;
    private static final String PANEL_KEY = "SITE_MAP";

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
        String strDeamon = DatastoreService.getDataValue( SEODataKeys.KEY_SITEMAP_DEAMON_ENABLED,
                DatastoreService.VALUE_FALSE );

        if ( strDeamon.equals( DatastoreService.VALUE_TRUE ) )
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_SITEMAP_DEAMON_ENABLED, DatastoreService.VALUE_FALSE );
        }
        else
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_SITEMAP_DEAMON_ENABLED, DatastoreService.VALUE_TRUE );
        }

        return getHomeUrl( request );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Panel
    
    /**
     * {@inheritDoc }
     */
    @Override
    public String getPanelTitle(  )
    {
        return I18nService.getLocalizedString( PROPERTY_TITlE, getPanelLocale(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPanelContent(  )
    {
        String strDeamon = DatastoreService.getDataValue( SEODataKeys.KEY_SITEMAP_DEAMON_ENABLED,
                DatastoreService.VALUE_FALSE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LAST_GENERATION, DatastoreService.getDataValue( SEODataKeys.KEY_SITEMAP_UPDATE_LOG, "" ) );
        model.put( MARK_DAEMON_ENABLED, strDeamon.equals( DatastoreService.VALUE_TRUE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTENT, getPanelLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getPanelOrder(  )
    {
        return PANEL_ORDER;
    }

    @Override
    public String getPanelKey()
    {
        return PANEL_KEY;
    }

}
