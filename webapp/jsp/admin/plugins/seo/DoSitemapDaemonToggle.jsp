<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.seo.web.SEOJspBean"%>

${ seoSitemap.init( pageContext.request, SEOJspBean.RIGHT_MANAGE_SEO ) }
${ pageContext.response.sendRedirect( seoSitemap.doSitemapDaemonToggle( pageContext.request ) ) }
