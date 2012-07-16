

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="seoFriendlyUrl" scope="session" class="fr.paris.lutece.plugins.seo.web.FriendlyUrlJspBean" />

<% seoFriendlyUrl.init( request, seoFriendlyUrl.RIGHT_MANAGE_SEO ); %>
<%= seoFriendlyUrl.getCreateUrl( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

