<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.seo.web.SEOJspBean"%>

${ seoFriendlyUrl.init( pageContext.request, SEOJspBean.RIGHT_MANAGE_SEO ) }
${ seoFriendlyUrl.getManageFriendlyUrls( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
