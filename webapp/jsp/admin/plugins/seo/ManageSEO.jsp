<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="seo" scope="session" class="fr.paris.lutece.plugins.seo.web.SEOJspBean" />

<% seo.init( request, seo.RIGHT_MANAGE_SEO ); %>
<%= seo.getManageSEO ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
