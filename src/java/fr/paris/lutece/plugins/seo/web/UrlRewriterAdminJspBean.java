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

import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.plugins.seo.service.RuleFileService;
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
 * This class provides the user interface to manage UrlRewriterRule features (
 * manage, create, modify, remove )
 */
public class UrlRewriterAdminJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_URLREWRITERADMIN = SEOJspBean.RIGHT_MANAGE_SEO;

    // parameters
    private static final String PARAMETER_RULE_ID = "id_rule";
    private static final String PARAMETER_FROM = "rule_from";
    private static final String PARAMETER_TO = "rule_to";
    private static final String PARAMETER_URLREWRITERRULE_PAGE_INDEX = "urlrewriterrule_page_index";
    private static final String PARAMETER_OPTION_FORCE_UPDATE = "option_force_update";
    private static final String PARAMETER_OPTION_ADD_PATH = "option_add_path";
    private static final String PARAMETER_OPTION_HTML_SUFFIX = "option_html_suffix";
    private static final String VALUE_ON = "on";

    // templates
    private static final String TEMPLATE_MANAGE_URLREWRITERRULES = "/admin/plugins/seo/manage_urlrewriterrules.html";
    private static final String TEMPLATE_CREATE_RULE = "/admin/plugins/seo/create_urlrewriterrule.html";
    private static final String TEMPLATE_MODIFY_RULE = "/admin/plugins/seo/modify_urlrewriterrule.html";
    private static final String TEMPLATE_GENERATE_ALIAS = "/admin/plugins/seo/generate_alias_rules.html";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_URLREWRITERRULES = "seo.manage_urlrewriterrules.pageTitle";

    // Markers
    private static final String MARK_RULE = "rule";
    private static final String MARK_URLREWRITERRULE_LIST = "urlrewriterrule_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_GENERATORS_LIST = "generators_list";

    // Jsp Definition
    private static final String JSP_DO_DELETE_RULE = "jsp/admin/plugins/seo/DoRemoveUrlRewriterRule.jsp";
    private static final String JSP_MANAGE_URLREWRITERRULES = "jsp/admin/plugins/seo/ManageUrlRewriterRules.jsp";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE = "seo.listUrlRewriterRules.itemsPerPage";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_RULE = "seo.message.confirmRemoveRule";
    private static final String MESSAGE_GENERATION_SUCCESSFUL = "seo.message.generationSuccessful";
    private static final String MESSAGE_GENERATION_FAILED = "seo.message.generationSuccessful";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Returns the list of urlrewriterrule
     *
     * @param request The Http request
     * @return the urlrewriterrules list
     */
    public String getManageUrlRewriterRules( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_URLREWRITERRULES );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_URLREWRITERRULE_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_MANAGE_URLREWRITERRULES );
        String strUrl = url.getUrl(  );
        Collection<UrlRewriterRule> listURLREWRITERRULEs = UrlRewriterRuleHome.findAll(  );
        Paginator paginator = new Paginator( (List<UrlRewriterRule>) listURLREWRITERRULEs, _nItemsPerPage, strUrl,
                PARAMETER_URLREWRITERRULE_PAGE_INDEX, _strCurrentPageIndex );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_URLREWRITERRULE_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_URLREWRITERRULES, getLocale(  ),
                model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Provides the create rule page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String getCreateRule( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create a new rule
     *
     * @param request The HTTP request
     * @return the forward url
     */
    public String doCreateRule( HttpServletRequest request )
    {
        UrlRewriterRule rule = new UrlRewriterRule(  );
        String strErrorUrl = getData( request, rule );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        UrlRewriterRuleHome.create( rule );

        return getHomeUrl( request );
    }

    /**
     * Provides the modify rule page
     *
     * @param request The HTTP request
     * @return The page
     */
    public String getModifyRule( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_RULE_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        UrlRewriterRule rule = UrlRewriterRuleHome.findByPrimaryKey( nRuleId );

        HashMap model = new HashMap(  );
        model.put( MARK_RULE, rule );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify rule's attributes
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String doModifyRule( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_RULE_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        UrlRewriterRule rule = UrlRewriterRuleHome.findByPrimaryKey( nRuleId );
        String strErrorUrl = getData( request, rule );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        UrlRewriterRuleHome.update( rule );

        return getHomeUrl( request );
    }

    /**
     * Fills rule infos from the request
     *
     * @param request The HTTP request
     * @param rule The rule object to fill
     * @return An ErrorUrl or null if no error
     */
    private String getData( HttpServletRequest request, UrlRewriterRule rule )
    {
        String strFrom = request.getParameter( PARAMETER_FROM );
        String strTo = request.getParameter( PARAMETER_TO );

        if ( ( strFrom == null ) || ( strFrom.equals( "" ) ) || ( strTo == null ) || ( strTo.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        rule.setRuleFrom( strFrom );
        rule.setRuleTo( strTo.replaceAll( "&", "&amp;" ) );

        return null;
    }

    /**
     * Confirm the rule deletion
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String deleteRule( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_RULE_ID );
        UrlItem url = new UrlItem( JSP_DO_DELETE_RULE );
        url.addParameter( PARAMETER_RULE_ID, strRuleId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_RULE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete the rule
     *
     * @param request The HTTP request
     * @return The forward url
     */
    public String doDeleteRule( HttpServletRequest request )
    {
        String strRuleId = request.getParameter( PARAMETER_RULE_ID );
        int nRuleId = Integer.parseInt( strRuleId );

        UrlRewriterRuleHome.remove( nRuleId );

        return getHomeUrl( request );
    }

    /**
     * Generate the rule file
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
            AppLogService.error( "Error generating rule file : " + e.getMessage(  ), e );
            strMessage = MESSAGE_GENERATION_FAILED;
            nMessageType = AdminMessage.TYPE_STOP;
        }

        return AdminMessageService.getMessageUrl( request, strMessage, getHomeUrl( request ), nMessageType );
    }

}
