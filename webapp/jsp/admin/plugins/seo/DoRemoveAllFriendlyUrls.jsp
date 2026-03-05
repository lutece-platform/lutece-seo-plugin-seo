<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.seo.web.SEOJspBean"%>

${ seoFriendlyUrl.init( pageContext.request, SEOJspBean.RIGHT_MANAGE_SEO ) }
${ pageContext.response.sendRedirect( seoFriendlyUrl.doDeleteAllUrls( pageContext.request ) ) }
