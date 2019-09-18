<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="searchUrl"
	value="/my-account/orders?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}" />

<h4 class="text-left ml-3">
	<spring:theme code="text.account.orderHistory" />
</h4>
<hr>
<c:if test="${empty searchPageData.results}">
	<div class="account-section-content content-empty">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.orderHistory.noOrders" />
		</ycommerce:testId>
	</div>
</c:if>
<c:if test="${not empty searchPageData.results}">
	<nav:pagination top="true" msgKey="text.account.orderHistory.page"
		showCurrentPageInfo="true" hideRefineButton="true"
		supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}" searchUrl="${searchUrl}"
		numberPagesShown="${numberPagesShown}" isTop="true"/>
	<hr>
	<table class="table mb-4">
		<thead class="thead-dark text-center">
			<tr>
				<th><spring:theme code="text.account.orderHistory.orderNumber" /></th>
				<th><spring:theme
						code="text.account.orderHistory.purchaseOrderNumber" /></th>
				<th><spring:theme code="text.account.orderHistory.orderStatus" /></th>
				<th><spring:theme code="text.account.orderHistory.datePlaced" /></th>
				<th><spring:theme code="text.account.orderHistory.total" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${searchPageData.results}" var="order">
				<tr>
					<ycommerce:testId code="orderHistoryItem_orderDetails_link">
						<td scope="row" class="text-center align-middle"><spring:url
								value="/my-account/order/{/orderCode}" var="orderDetailsUrl"
								htmlEscape="false">
								<spring:param name="orderCode" value="${order.code}" />
							</spring:url> <a href="${fn:escapeXml(orderDetailsUrl)}">
								${fn:escapeXml(order.code)} </a></td>
						<td class="text-left align-middle">
							<%-- <spring:theme code="text.account.order.status.display.${order.purchaseOrderNumber}"/> --%>
							${fn:escapeXml(order.purchaseOrderNumber)}
						</td>
						<td class="text-center align-middle"><spring:theme
								code="text.account.order.status.display.${order.statusDisplay}" />
						</td>
						<td class="text-center align-middle"><fmt:formatDate
								value="${order.placed}" dateStyle="medium" timeStyle="short"
								type="both" /></td>
						<td class="text-center align-middle">
							${fn:escapeXml(order.total.formattedValue)}</td>
					</ycommerce:testId>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<nav:pagination top="false" msgKey="text.account.orderHistory.page"
		showCurrentPageInfo="true" hideRefineButton="true"
		supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}" searchUrl="${searchUrl}"
		numberPagesShown="${numberPagesShown}" isTop="false"/>

</c:if>