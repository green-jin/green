
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<b>titleNameActive:${component.titleNameActive}</b> <br/>

<c:if test="${component.titleNameActive == true}">
	<b>titleName:${component.titleName}</b>
</c:if>

<c:forEach items="${medias}" var="media">
	<c:choose>
		<c:when test="${empty imagerData}">
			<c:set var="imagerData">"${ycommerce:encodeJSON(media.width)}":"${ycommerce:encodeJSON(media.url)}"</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="imagerData">${imagerData},"${ycommerce:encodeJSON(media.width)}":"${ycommerce:encodeJSON(media.url)}"</c:set>
		</c:otherwise>
	</c:choose>
	<c:if test="${empty altText}">
		<c:set var="altTextHtml" value="${fn:escapeXml(media.altText)}"/>
	</c:if>
</c:forEach>

<c:url value="${urlLink}" var="simpleResponsiveBannerUrl" />

<div class="simple-banner banner__component--responsive">
	<c:set var="imagerDataJson" value="{${imagerData}}"/>
	<c:choose>
		<c:when test="${empty simpleResponsiveBannerUrl || simpleResponsiveBannerUrl eq '#'}">
			<img class="js-responsive-image" data-media='${fn:escapeXml(imagerDataJson)}' alt='${altTextHtml}' title='${altTextHtml}' style="">
		</c:when>
		<c:otherwise>
			<a href="${fn:escapeXml(simpleResponsiveBannerUrl)}">
				<img class="js-responsive-image" data-media='${fn:escapeXml(imagerDataJson)}' title='${altTextHtml}' alt='${altTextHtml}' style="">
			</a>
		</c:otherwise>
	</c:choose>
</div>