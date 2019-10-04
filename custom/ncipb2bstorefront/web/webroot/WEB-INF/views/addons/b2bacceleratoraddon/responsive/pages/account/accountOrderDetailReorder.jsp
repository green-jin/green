<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<!-- add on b2bacceleratoraddon accountOrderDetailReorder.jsp start  -->
<div class="row p-3">
	<div class="col-md-3"></div>
	<div class="col-md-3"></div>
	<div class="col-md-3 orderBackBtn">
		<spring:url value="/my-account/orders" var="orderHistoryUrl" />
		<button type="button" class="btn btn-block btn-secondary"
			data-back-to-orders="${fn:escapeXml(orderHistoryUrl)}">
			<spring:theme code="text.account.orderDetails.backToOrderHistory" />
		</button>
	</div>
	<div class="col-md-3">
		<c:set var="orderCode" value="${orderData.code}" scope="request" />
		<action:actions element="div" parentComponent="${component}" />
	</div>
</div>