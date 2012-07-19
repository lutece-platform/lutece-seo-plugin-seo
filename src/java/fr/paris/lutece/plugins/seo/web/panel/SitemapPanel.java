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
package fr.paris.lutece.plugins.seo.web.panel;

import fr.paris.lutece.plugins.seo.service.SEOProperties;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.Map;


/**
 * Sitemap Panel
 */
public class SitemapPanel extends SEOAbstractPanel implements SEOPanel
{
    private static final String TEMPLATE_CONTENT = "/admin/plugins/seo/panel/sitemap_panel.html";
    private static final String PROPERTY_TITlE = "seo.panel.sitemap.title";
    private static final int ORDER = 1;
    private static final String MARK_LAST_GENERATION = "sitemapLastGeneration";
    private static final String MARK_DAEMON_ENABLED = "daemon_enabled";

    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle(  )
    {
        return I18nService.getLocalizedString( PROPERTY_TITlE, getLocale(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getContent(  )
    {
        String strDeamon = DatastoreService.getDataValue(SEOProperties.SITEMAP_DEAMON_ENABLED, SEOProperties.VALUE_FALSE);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put( MARK_LAST_GENERATION, DatastoreService.getDataValue(SEOProperties.SITEMAP_UPDATE_LOG , "" ));
        model.put( MARK_DAEMON_ENABLED, strDeamon.equals( SEOProperties.VALUE_TRUE ));
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTENT, getLocale(  ) , model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getOrder(  )
    {
        return ORDER;
    }
}
