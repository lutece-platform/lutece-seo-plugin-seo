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

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.*;


/**
 * RuleFileService
 */
public final class RuleFileService
{
    private static final String TEMPLATE_FILE = "/admin/plugins/seo/urlrewrite.xml";
    private static final String MARK_RULES_LIST = "rules_list";
    private static final String MARK_URL_LIST = "url_list";
    private static final String PROPERTY_FILE = "seo.configFilePath";
    private static final String PROPERTY_REWRITE_CONFIG_LOG = "seo.config.log";

    /**
     * Private constructor
     */
    private RuleFileService(  )
    {
    }

    /**
     * Generate the rule file
     * @throws java.io.IOException If an error occurs
     */
    public static void generateFile(  ) throws IOException
    {
        String strFilePath = AppPathService.getWebAppPath(  ) + AppPropertiesService.getProperty( PROPERTY_FILE );
        File file = new File( strFilePath );
        FileUtils.writeStringToFile( file, generateFileContent(  ) );
    }

    /**
     * Generate the rule file content
     * @return The file content
     */
    private static String generateFileContent(  )
    {
        HashMap model = new HashMap(  );
        Collection<UrlRewriterRule> listRules = UrlRewriterRuleHome.findAll(  );
        List<FriendlyUrl> listUrl = FriendlyUrlHome.findAll(  );

        model.put( MARK_RULES_LIST, listRules );
        model.put( MARK_URL_LIST, listUrl );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_FILE, Locale.getDefault(  ), model );

        String strResult = "OK";
        String strDate = DateFormat.getDateTimeInstance(  ).format( new Date(  ) );
        Object[] args = { strDate, listRules.size(  ) + listUrl.size() , strResult };
        String strLogFormat = I18nService.getLocalizedString( PROPERTY_REWRITE_CONFIG_LOG, Locale.getDefault(  ) );
        String strLog = MessageFormat.format( strLogFormat, args );
        SEOPropertiesService.setProperty( SEOProperties.REWRITE_CONFIG_UPDATE , strLog );
        SEOPropertiesService.setProperty( SEOProperties.CONFIG_UPTODATE , SEOProperties.VALUE_TRUE );
        
        return t.getHtml(  );
    }
}
