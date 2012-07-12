<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="seoUrlRewriter" scope="session" class="fr.paris.lutece.plugins.seo.web.UrlRewriterAdminJspBean" />

<% seoUrlRewriter.init( request, seoUrlRewriter.RIGHT_MANAGE_URLREWRITERADMIN ); %>
<%= seoUrlRewriter.getManageUrlRewriterRules ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>