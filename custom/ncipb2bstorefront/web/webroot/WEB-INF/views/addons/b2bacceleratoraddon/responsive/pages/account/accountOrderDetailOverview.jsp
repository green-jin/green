<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<!-- addon b2bacceleratoraddon accountOrderDetailOverview.jsp start -->
<div class="m-0 mb-4 p-4 bg-light border border-secondary">
    <ycommerce:testId code="orderDetail_overview_section">
        <b2b-order:accountOrderDetailsOverview order="${orderData}"/>
    </ycommerce:testId>
</div>
<%-- <c:if test="${not empty orderData.placedBy}"> --%>
<!-- 	<div class="alert alert-info order-placedby"> -->
<%-- 	<c:choose> --%>
<%-- 		<c:when test="${not empty agent}"> --%>
<%-- 			<spring:theme code="text.account.order.placedBy" arguments="${orderData.placedBy}"/> --%>
<%-- 		</c:when> --%>
<%-- 		<c:otherwise> --%>
<%-- 			<spring:theme code="text.account.order.placedByText"/> --%>
<%-- 		</c:otherwise> --%>
<%-- 	</c:choose> --%>
<!-- 	</div> -->
<%-- </c:if> --%>