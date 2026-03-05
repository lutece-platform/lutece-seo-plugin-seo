<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.seo.web.UrlRewriterAdminJspBean"%>

${ seoUrlRewriter.init( pageContext.request, UrlRewriterAdminJspBean.RIGHT_MANAGE_URLREWRITERADMIN ) }
${ pageContext.response.sendRedirect( seoUrlRewriter.doModifyRule( pageContext.request ) ) }
