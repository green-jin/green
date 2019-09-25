<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/responsive/template/cms" %>

<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic" />
<c:choose>
	<c:when test="${wro4jEnabled}">
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(contextPath)}/wro/all_responsive.css" />
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(contextPath)}/wro/${fn:escapeXml(themeName)}_responsive.css" />
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(contextPath)}/wro/addons_responsive.css" />
	</c:when>
	<c:otherwise>

		<%--  AddOn Common CSS files --%>
		<c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
			<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(addOnCommonCss)}"/>
		</c:forEach>
        <%-- Theme CSS files --%>

        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/style.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/style_ncip2.css"/>
<%--        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/ncip/bootstrap.css"/>--%>
<%--        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/ncip/bootstrap.min.css"/>--%>
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/bootstrap.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/font-awesome.min.css"/>
        <!--<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/ncip/bootstrap.css.map"/>
        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/ncip/bootstrap.min.css.map"/>-->

        <link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/style_ncip.css"/>

		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/hb_ncip.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(themeResourcePath)}/css/hb_ncip0923.css"/>




	</c:otherwise>
</c:choose>

<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${fn:escapeXml(addOnThemeCss)}"/>
</c:forEach>

<cms:previewCSS cmsPageRequestContextData="${cmsPageRequestContextData}" />
