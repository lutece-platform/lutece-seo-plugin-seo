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
package fr.paris.lutece.plugins.seo.web;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.service.FriendlyUrlGeneratorService;
import fr.paris.lutece.plugins.seo.service.GeneratorOptions;
import fr.paris.lutece.plugins.seo.service.RuleFileService;
import fr.paris.lutece.plugins.seo.service.sitemap.SitemapUtils;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage FriendlyUrl features (
 * manage, create, modify, remove )
 */
public class FriendlyUrlJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_SEO = SEOJspBean.RIGHT_MANAGE_SEO;

    // parameters
    private static final String PARAMETER_URL_ID = "id_url";
    private static final String PARAMETER_FROM = "rule_from";
    private static final String PARAMETER_TO = "rule_to";
    private static final String PARAMETER_CANONICAL = "canonical";
    private static final String PARAMETER_SITEMAP = "sitemap";
    private static final String PARAMETER_CHANGE_FREQ = "change_freq";
    private static final String PARAMETER_PRIORITY = "priority";
    private static final String PARAMETER_URLREWRITERRULE_PAGE_INDEX = "friendly_url_page_index";
    private static final String PARAMETER_OPTION_FORCE_UPDATE = "option_force_update";
    private static final String PARAMETER_OPTION_ADD_PATH = "option_add_path";
    private static final String PARAMETER_OPTION_HTML_SUFFIX = "option_html_suffix";
    private static final String VALUE_ON = "on";

    // templates
    private static final String TEMPLATE_MANAGE_FRIENDLY_URL = "/admin/plugins/seo/manage_friendly_urls.html";
    private static final String TEMPLATE_CREATE_RULE = "/admin/plugins/seo/create_friendly_url.html";
    private static final String TEMPLATE_MODIFY_RULE = "/admin/plugins/seo/modify_friendly_url.html";
    private static final String TEMPLATE_GENERATE_ALIAS = "/admin/plugins/seo/generate_alias_rules.html";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_FRIENDLY_URLS = "seo.manage_friendly_urls.pageTitle";

    // Markers
    private static final String MARK_RULE = "url";
    private static final String MARK_FRIENDLY_URLS_LIST = "friendly_url_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_GENERATORS_LIST = "generators_list";
    private static final String MARK_CHECKED_CANONICAL = "checked_canonical";
    private static final String MARK_CHECKED_SITEMAP = "checked_sitemap";
    private static final String MARK_CHANGE_FREQ_LIST = "change_freq_list";
    private static final String MARK_SELECTED_CHANGE_FREQ = "selected_change_freq";
    private static final String MARK_PRIORITY_LIST = "priority_list";
    private static final String MARK_SELECTED_PRIORITY = "selected_priority";

    // Jsp Definition
    private static final String JSP_DO_DELETE_URL = "jsp/admin/plugins/seo/DoRemoveFriendlyUrl.jsp";
    private static final String JSP_URL_MANAGE_FRIENDLY_URLS = "jsp/admin/plugins/seo/ManageFriendlyUrls.jsp";
    private static final String JSP_MANAGE_FRIENDLY_URLS = "ManageFriendlyUrls.jsp";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE = "seo.listFriendlyUrls.itemsPerPage";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_URL = "seo.message.confirmRemoveUrl";
    private static final String MESSAGE_GENERATION_SUCCESSFUL = "seo.message.generationSuccessful";
    private static final String MESSAGE_GENERATION_FAILED = "seo.message.generationSuccessful";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Returns the list of friendly_url
     *
     * @param request The Http request
     * @return the friendly_urls list
     */
    public String getManageFriendlyUrls( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_FRIENDLY_URLS );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_URL_MANAGE_FRIENDLY_URLS );
        String strUrl = url.getUrl(  );
        Collection<FriendlyUrl> listURLREWRITERRULEs = FriendlyUrlHome.findAll(  );
        Paginator paginator = new Paginator( (List<FriendlyUrl>) listURLREWRITERRULEs, _nItemsPerPage, strUrl,
                PARAMETER_URLREWRITERRULE_PAGE_INDEX, _strCurrentPageIndex );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_FRIENDLY_URLS_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FRIENDLY_URL, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Provides the create url page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String getCreateUrl( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        
        model.put( MARK_CHECKED_CANONICAL , 1 );
        model.put( MARK_CHECKED_SITEMAP , 1 );
        model.put( MARK_CHANGE_FREQ_LIST , SitemapUtils.getChangeFrequencyValues() );
        model.put( MARK_SELECTED_CHANGE_FREQ , SitemapUtils.CHANGE_FREQ_VALUES[3] );
        model.put( MARK_PRIORITY_LIST , SitemapUtils.getPriorityValues() );
        model.put( MARK_SELECTED_PRIORITY , SitemapUtils.PRIORITY_VALUES[0] );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create a new url
     *
     * @param request The HTTP request
     * @return the forward url
     */
    public String doCreateUrl( HttpServletRequest request )
    {
        FriendlyUrl url = new FriendlyUrl(  );
        String strErrorUrl = getData( request, url );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        FriendlyUrlHome.create( url );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Provides the modify url page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String getModifyUrl( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_URL_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        FriendlyUrl url = FriendlyUrlHome.findByPrimaryKey( nRuleId );

        HashMap model = new HashMap(  );
        model.put( MARK_RULE, url );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify url's attributes
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String doModifyUrl( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_URL_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        FriendlyUrl url = FriendlyUrlHome.findByPrimaryKey( nRuleId );
        String strErrorUrl = getData( request, url );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        FriendlyUrlHome.update( url );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Fills url infos from the request
     *
     * @param request The HTTP request
     * @param url The url object to fill
     * @return An ErrorUrl or null if no error
     */
    private String getData( HttpServletRequest request, FriendlyUrl url )
    {
        String strFrom = request.getParameter( PARAMETER_FROM );
        String strTo = request.getParameter( PARAMETER_TO );
        String strCanonical = request.getParameter( PARAMETER_CANONICAL );
        String strSitemap = request.getParameter( PARAMETER_SITEMAP );
        String strChangeFreq = request.getParameter( PARAMETER_CHANGE_FREQ );
        String strPriority = request.getParameter( PARAMETER_PRIORITY );

        if ( ( strFrom == null ) || ( strFrom.equals( "" ) ) || ( strTo == null ) || ( strTo.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        url.setFriendlyUrl( strFrom );
        url.setTechnicalUrl( strTo.replaceAll( "&", "&amp;" ) );
        url.setCanonical( strCanonical != null );
        url.setSitemap( strSitemap != null );
        url.setSitemapChangeFreq(strChangeFreq);
        url.setSitemapPriority(strPriority);

        return null;
    }

    /**
     * Confirm the url deletion
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String deleteUrl( HttpServletRequest request )
    {
        String strUrlId = request.getParameter( PARAMETER_URL_ID );
        UrlItem url = new UrlItem( JSP_DO_DELETE_URL );
        
        url.addParameter( PARAMETER_URL_ID, strUrlId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_URL, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete the url
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String doDeleteUrl( HttpServletRequest request )
    {
        String strUrlId = request.getParameter( PARAMETER_URL_ID );
        int nUrlId = Integer.parseInt( strUrlId );

        FriendlyUrlHome.remove( nUrlId );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Generate the url file
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String doGenerate( HttpServletRequest request )
    {
        String strMessage = MESSAGE_GENERATION_SUCCESSFUL;
        int nMessageType = AdminMessage.TYPE_CONFIRMATION;

        try
        {
            RuleFileService.generateFile(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error generating url file : " + e.getMessage(  ), e );
            strMessage = MESSAGE_GENERATION_FAILED;
            nMessageType = AdminMessage.TYPE_STOP;
        }

        return AdminMessageService.getMessageUrl( request, strMessage, getHomeUrl( request ), nMessageType );
    }

    /**
     * Returns the Generate Alias Rules page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String getGenerateAliasRules( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_GENERATORS_LIST, FriendlyUrlGeneratorService.instance(  ).getGenerators(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_GENERATE_ALIAS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the Generate Alias Rules page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String doGenerateAliasRules( HttpServletRequest request )
    {
        GeneratorOptions options = new GeneratorOptions(  );

        options.setForceUpdate( getOption( request, PARAMETER_OPTION_FORCE_UPDATE ) );
        options.setAddPath( getOption( request, PARAMETER_OPTION_ADD_PATH ) );
        options.setHtmlSuffix( getOption( request, PARAMETER_OPTION_HTML_SUFFIX ) );

        FriendlyUrlGeneratorService.instance(  ).generate( options );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Retrieve an option value from the request
     * @param request The HTTP request
     * @param strParameter The parameter
     * @return The value
     */
    private boolean getOption( HttpServletRequest request, String strParameter )
    {
        String strValue = request.getParameter( strParameter );

        if ( ( strValue != null ) && ( strValue.equals( VALUE_ON ) ) )
        {
            return true;
        }

        return false;
    }
}