

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="seoUrlRewriter" scope="session" class="fr.paris.lutece.plugins.seo.web.UrlRewriterAdminJspBean" />

<%
    seoUrlRewriter.init( request, seoUrlRewriter.RIGHT_MANAGE_URLREWRITERADMIN );
    response.sendRedirect( seoUrlRewriter.doModifyRule( request ) );
%>

<%@ include file="../../AdminFooter.jsp" %>

