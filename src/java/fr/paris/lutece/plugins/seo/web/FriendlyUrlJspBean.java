/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.plugins.seo.service.CanonicalUrlService;
import fr.paris.lutece.plugins.seo.service.FriendlyUrlService;
import fr.paris.lutece.plugins.seo.service.RuleFileService;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.plugins.seo.service.generator.FriendlyUrlGeneratorService;
import fr.paris.lutece.plugins.seo.service.generator.GeneratorOptions;
import fr.paris.lutece.plugins.seo.service.sitemap.SitemapUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage FriendlyUrl features ( manage, create, modify, remove )
 */
public class FriendlyUrlJspBean extends SEOPanelJspBean
{
    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_SEO = SEOJspBean.RIGHT_MANAGE_SEO;

    
    public static final String PARAMETER_URL_ID = "id_url";
    public static final String PARAMETER_TOGGLE = "toggle";
    public static final String PARAMETER_FROM = "rule_from";
    public static final String PARAMETER_TO = "rule_to";
    public static final String PARAMETER_CANONICAL = "canonical";
    public static final String PARAMETER_SITEMAP = "sitemap";
    public static final String PARAMETER_CHANGE_FREQ = "change_freq";
    public static final String PARAMETER_PRIORITY = "priority";
    
    public static final String TOGGLE_CANONICAL_URLS = "add_canonical_url";
    public static final String TOGGLE_REPLACE_URL = "replace_url_in_content";
    public static final String TOGGLE_FRIENDLY_URL_DAEMON = "friendly_url_daemon_enabled";
    
    // parameters
    private static final String PARAMETER_GENERATOR_KEY = "generator_key";
    private static final String PARAMETER_URLREWRITERRULE_PAGE_INDEX = "page_index";
    private static final String PARAMETER_OPTION_FORCE_UPDATE = "option_force_update";
    private static final String PARAMETER_OPTION_ADD_PATH = "option_add_path";
    private static final String PARAMETER_OPTION_HTML_SUFFIX = "option_html_suffix";
    private static final String VALUE_ON = "on";

    // templates
    private static final String TEMPLATE_MANAGE_FRIENDLY_URL = "/admin/plugins/seo/manage_friendly_urls.html";
    private static final String TEMPLATE_CREATE_RULE = "/admin/plugins/seo/create_friendly_url.html";
    private static final String TEMPLATE_MODIFY_RULE = "/admin/plugins/seo/modify_friendly_url.html";
    private static final String TEMPLATE_GENERATE_ALIAS = "/admin/plugins/seo/generate_friendly_urls.html";
    private static final String TEMPLATE_CONTENT = "/admin/plugins/seo/panel/friendly_urls_panel.html";
    private static final String MARK_REWRITE_CONFIG_UPDATE = "rewrite_config_last_update";
    private static final String MARK_CONFIG_UPTODATE = "config_uptodate";
    private static final String MARK_URL_REPLACE = "url_replace";
    private static final String MARK_CANONICAL_URLS = "canonical_urls";
    private static final String MARK_FRIENDLY_URL_DAEMON = "friendly_url_daemon";
    private static final String PROPERTY_TITLE = "seo.panel.friendly_urls.title";
    private static final int PANEL_ORDER = 1;
    private static final String PANEL_KEY = "FRIENDLY_URL";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_FRIENDLY_URLS = "seo.manage_friendly_urls.pageTitle";

    // Markers
    private static final String MARK_RULE = "url";
    private static final String MARK_FRIENDLY_URLS_LIST = "friendly_url_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_GENERATORS_LIST = "generators_list";
    private static final String MARK_CHANGE_FREQ_LIST = "change_freq_list";
    private static final String MARK_SELECTED_CHANGE_FREQ = "selected_change_freq";
    private static final String MARK_PRIORITY_LIST = "priority_list";
    private static final String MARK_SELECTED_PRIORITY = "selected_priority";

    // Jsp Definition
    private static final String JSP_DO_DELETE_URL = "jsp/admin/plugins/seo/DoRemoveFriendlyUrl.jsp";
    private static final String JSP_URL_MANAGE_FRIENDLY_URLS = "jsp/admin/plugins/seo/ManageFriendlyUrls.jsp";
    private static final String JSP_MANAGE_FRIENDLY_URLS = "ManageFriendlyUrls.jsp";
    private static final String JSP_GENERATE_FRIENDLY_URLS = "GenerateAliasRules.jsp";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE = "seo.listFriendlyUrls.itemsPerPage";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_URL = "seo.message.confirmRemoveUrl";
    private static final String MESSAGE_GENERATION_FAILED = "seo.message.generationSuccessful";

    // Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Returns the list of friendly_url
     *
     * @param request
     *            The Http request
     * @return the friendly_urls list
     */
    public String getManageFriendlyUrls( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_FRIENDLY_URLS );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_URL_MANAGE_FRIENDLY_URLS );
        String strUrl = url.getUrl( );
        List<FriendlyUrl> listFriendlyUrls = FriendlyUrlHome.findAll( );
        LocalizedPaginator paginator = new LocalizedPaginator( (List<FriendlyUrl>) listFriendlyUrls, _nItemsPerPage, strUrl,
                PARAMETER_URLREWRITERRULE_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_FRIENDLY_URLS_LIST, paginator.getPageItems( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FRIENDLY_URL, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Provides the create url page
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    public String getCreateUrl( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_CHANGE_FREQ_LIST, SitemapUtils.getChangeFrequencyValues( ) );
        model.put( MARK_SELECTED_CHANGE_FREQ, SitemapUtils.CHANGE_FREQ_VALUES [3] );
        model.put( MARK_PRIORITY_LIST, SitemapUtils.getPriorityValues( ) );
        model.put( MARK_SELECTED_PRIORITY, SitemapUtils.PRIORITY_VALUES [0] );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Create a new url
     *
     * @param request
     *            The HTTP request
     * @return the forward url
     */
    public String doCreateUrl( HttpServletRequest request )
    {
        FriendlyUrl url = new FriendlyUrl( );
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
     * @param request
     *            The HTTP request
     * @return The page
     */
    public String getModifyUrl( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_URL_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        FriendlyUrl url = FriendlyUrlHome.findByPrimaryKey( nRuleId );

        HashMap model = new HashMap( );
        model.put( MARK_RULE, url );
        model.put( MARK_CHANGE_FREQ_LIST, SitemapUtils.getChangeFrequencyValues( ) );
        model.put( MARK_PRIORITY_LIST, SitemapUtils.getPriorityValues( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RULE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Modify url's attributes
     *
     * @param request
     *            The HTTP request
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
     * @param request
     *            The HTTP request
     * @param url
     *            The url object to fill
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
        url.setSitemapChangeFreq( strChangeFreq );
        url.setSitemapPriority( strPriority );

        return null;
    }

    /**
     * Confirm the url deletion
     *
     * @param request
     *            The HTTP request
     * @return The forward url
     */
    public String deleteUrl( HttpServletRequest request )
    {
        String strUrlId = request.getParameter( PARAMETER_URL_ID );
        UrlItem url = new UrlItem( JSP_DO_DELETE_URL );

        url.addParameter( PARAMETER_URL_ID, strUrlId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_URL, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete the url
     *
     * @param request
     *            The HTTP request
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
     * Remove all URL
     * @param request The HTTP request
     * @return The forward url
     */
    public String doDeleteAllUrls( HttpServletRequest request )
    {
        FriendlyUrlHome.removeAll(  );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Generate the url file
     *
     * @param request
     *            The HTTP request
     * @return The forward url
     */
    public String doGenerate( HttpServletRequest request )
    {
        String strMessage;
        int nMessageType;

        try
        {
            RuleFileService.generateFile( );
        }
        catch( IOException e )
        {
            AppLogService.error( "Error generating url file : " + e.getMessage( ), e );
            strMessage = MESSAGE_GENERATION_FAILED;
            nMessageType = AdminMessage.TYPE_STOP;

            return AdminMessageService.getMessageUrl( request, strMessage, getHomeUrl( request ), nMessageType );
        }

        return getHomeUrl( request );
    }

    /**
     * Returns the Generate Alias Rules page
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    public String getGenerateAliasRules( HttpServletRequest request )
    {
        HashMap model = new HashMap( );
        model.put( MARK_CHANGE_FREQ_LIST, SitemapUtils.getChangeFrequencyValues( ) );
        model.put( MARK_PRIORITY_LIST, SitemapUtils.getPriorityValues( ) );
        model.put( MARK_GENERATORS_LIST, FriendlyUrlGeneratorService.instance( ).getGenerators( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_GENERATE_ALIAS, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Returns the Generate Alias Rules page
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    public String doGenerateAliasRules( HttpServletRequest request )
    {
        GeneratorOptions options = new GeneratorOptions( );

        options.setForceUpdate( getOption( request, PARAMETER_OPTION_FORCE_UPDATE ) );
        options.setAddPath( getOption( request, PARAMETER_OPTION_ADD_PATH ) );
        options.setHtmlSuffix( getOption( request, PARAMETER_OPTION_HTML_SUFFIX ) );

        DatastoreService.setDataValue( SEODataKeys.KEY_GENERATOR_ADD_PATH, options.isAddPath( ) ? DatastoreService.VALUE_TRUE : DatastoreService.VALUE_FALSE );
        DatastoreService.setDataValue( SEODataKeys.KEY_GENERATOR_ADD_HTML_SUFFIX, options.isHtmlSuffix( ) ? DatastoreService.VALUE_TRUE
                : DatastoreService.VALUE_FALSE );

        FriendlyUrlGeneratorService.instance( ).generate( options );

        return JSP_MANAGE_FRIENDLY_URLS;
    }

    /**
     * Retrieve an option value from the request
     * 
     * @param request
     *            The HTTP request
     * @param strParameter
     *            The parameter
     * @return The value
     */
    private boolean getOption( HttpServletRequest request, String strParameter )
    {
        String strValue = request.getParameter( strParameter );

        return ( strValue != null ) && ( strValue.equals( VALUE_ON ) );
    }

    /**
     * Enable or disable the replace post processor service
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    public String doToggle( HttpServletRequest request )
    {
        String strToggle = request.getParameter( PARAMETER_TOGGLE );

        switch( strToggle )
        {
            case TOGGLE_REPLACE_URL:
                toggleReplaceUrl( );
                break;
            case TOGGLE_CANONICAL_URLS:
                toggleCanonicalUrls( );
                break;
            case TOGGLE_FRIENDLY_URL_DAEMON:
                toggleFriendlyUrlDaemon( );
                break;
            default:
                break;
        }

        return getHomeUrl( request );
    }

    /**
     * Toggle replace URL in content (enabled/disabled)
     */
    private void toggleReplaceUrl( )
    {
        String strStatus = DatastoreService.getDataValue( SEODataKeys.KEY_URL_REPLACE_ENABLED, DatastoreService.VALUE_FALSE );

        if ( strStatus.equals( DatastoreService.VALUE_TRUE ) )
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_URL_REPLACE_ENABLED, DatastoreService.VALUE_FALSE );
            FriendlyUrlService.instance( ).setUrlReplaceEnabled( false );
            AppLogService.info( "SEO : URL replace service disabled" );
        }
        else
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_URL_REPLACE_ENABLED, DatastoreService.VALUE_TRUE );
            FriendlyUrlService.instance( ).setUrlReplaceEnabled( true );
            AppLogService.info( "SEO : URL replace service enabled" );
        }
    }

    /**
     * Toggle add Canonical URL in content (enabled/disabled)
     */
    private void toggleCanonicalUrls( )
    {
        String strStatus = DatastoreService.getDataValue( SEODataKeys.KEY_CANONICAL_URLS_ENABLED, DatastoreService.VALUE_FALSE );

        if ( strStatus.equals( DatastoreService.VALUE_TRUE ) )
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_CANONICAL_URLS_ENABLED, DatastoreService.VALUE_FALSE );
            CanonicalUrlService.instance( ).setCanonicalUrlsEnabled( false );
            AppLogService.info( "SEO : Canonical URLs disabled" );
        }
        else
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_CANONICAL_URLS_ENABLED, DatastoreService.VALUE_TRUE );
            CanonicalUrlService.instance( ).setCanonicalUrlsEnabled( true );
            AppLogService.info( "SEO : Canonical URLs enabled" );
        }
    }

    /**
     * Toggle start Friendly URL daemon (enabled/disabled)
     */
    private void toggleFriendlyUrlDaemon( )
    {
        String strStatus = DatastoreService.getDataValue( SEODataKeys.KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED, DatastoreService.VALUE_FALSE );

        if ( strStatus.equals( DatastoreService.VALUE_TRUE ) )
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED, DatastoreService.VALUE_FALSE );
            AppLogService.info( "SEO : Friendly URL Daemon disabled" );
        }
        else
        {
            DatastoreService.setDataValue( SEODataKeys.KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED, DatastoreService.VALUE_TRUE );
            AppLogService.info( "SEO : Friendly URL Daemon enabled" );
        }
    }

    /**
     * Save update to generator settings
     * 
     * @param request
     *            The HTTP request
     * @return The forward URL
     */
    public String doUpdateGeneratorSettings( HttpServletRequest request )
    {
        String strKey = request.getParameter( PARAMETER_GENERATOR_KEY );
        String strChangeFreq = request.getParameter( PARAMETER_CHANGE_FREQ );
        String strPriority = request.getParameter( PARAMETER_PRIORITY );

        String strPrefix = SEODataKeys.PREFIX_GENERATOR + strKey;
        DatastoreService.setDataValue( strPrefix + SEODataKeys.SUFFIX_CHANGE_FREQ, strChangeFreq );
        DatastoreService.setDataValue( strPrefix + SEODataKeys.SUFFIX_PRIORITY, strPriority );

        return JSP_GENERATE_FRIENDLY_URLS;
    }

    // //////////////////////////////////////////////////////////////////////////
    // Panel

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPanelTitle( )
    {
        return I18nService.getLocalizedString( PROPERTY_TITLE, getPanelLocale( ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPanelContent( )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_REWRITE_CONFIG_UPDATE, DatastoreService.getDataValue( SEODataKeys.KEY_REWRITE_CONFIG_UPDATE, "" ) );
        model.put( MARK_CONFIG_UPTODATE, DatastoreService.getDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, "" ).equals( DatastoreService.VALUE_TRUE ) );
        model.put( MARK_URL_REPLACE, DatastoreService.getDataValue( SEODataKeys.KEY_URL_REPLACE_ENABLED, "" ).equals( DatastoreService.VALUE_TRUE ) );
        model.put( MARK_CANONICAL_URLS, DatastoreService.getDataValue( SEODataKeys.KEY_CANONICAL_URLS_ENABLED, "" ).equals( DatastoreService.VALUE_TRUE ) );
        model.put( MARK_FRIENDLY_URL_DAEMON,
                DatastoreService.getDataValue( SEODataKeys.KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED, "" ).equals( DatastoreService.VALUE_TRUE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTENT, getPanelLocale( ), model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getPanelOrder( )
    {
        return PANEL_ORDER;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPanelKey( )
    {
        return PANEL_KEY;
    }
}
