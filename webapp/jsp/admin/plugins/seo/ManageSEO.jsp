<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.seo.web.SEOJspBean"%>

${ seoJspBean.init( pageContext.request, SEOJspBean.RIGHT_MANAGE_SEO ) }
${ seoJspBean.getManageSEO( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
