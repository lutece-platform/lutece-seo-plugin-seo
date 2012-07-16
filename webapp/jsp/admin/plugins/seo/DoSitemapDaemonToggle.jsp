<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="seoSitemap" scope="session" class="fr.paris.lutece.plugins.seo.web.SitemapJspBean" />

<%
    seoSitemap.init( request, seoSitemap.RIGHT_MANAGE_SEO );
    response.sendRedirect( seoSitemap.doSitemapDaemonToggle( request ) );
%>

<%@ include file="../../AdminFooter.jsp" %>


