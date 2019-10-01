<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/cart/export" var="exportUrl" htmlEscape="false"/>
<div class=" col-lg-6 mb-2 pull-left">
   <div class="text-left">
<%-- 	<a href="${exportUrl}" class="export__cart--link"> --%>
	<a href="${exportUrl}">
		<spring:theme code="basket.export.csv.file" />
	</a>
	</div>
</div>