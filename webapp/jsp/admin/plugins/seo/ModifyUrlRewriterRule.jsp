<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.seo.web.UrlRewriterAdminJspBean"%>

${ seoUrlRewriter.init( pageContext.request, UrlRewriterAdminJspBean.RIGHT_MANAGE_URLREWRITERADMIN ) }
${ seoUrlRewriter.getModifyRule( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
