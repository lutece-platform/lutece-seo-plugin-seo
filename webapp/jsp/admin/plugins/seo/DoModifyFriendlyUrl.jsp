<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="seoFriendlyUrl" scope="session" class="fr.paris.lutece.plugins.seo.web.FriendlyJspBean" />

<%
    seoFriendlyUrl.init( request, seoFriendlyUrl.RIGHT_MANAGE_SEO );
    response.sendRedirect( seoFriendlyUrl.doModifyUrl( request ) );
%>

<%@ include file="../../AdminFooter.jsp" %>

