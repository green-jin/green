<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />
accountOrderHistoryPage.jsp start
<c:set var="searchUrl" value="/my-account/orders?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}"/>

<div class="col-lg-10 container-fluid"> 
	<h4 class="text-left ml-3"><spring:theme code="text.account.orderHistory" /></h4>
	<hr>
	<c:if test="${empty searchPageData.results}">
		<div class="account-section-content content-empty">
			<ycommerce:testId code="orderHistory_noOrders_label">
				<spring:theme code="text.account.orderHistory.noOrders" />
			</ycommerce:testId>
		</div>
	</c:if>
	<c:if test="${not empty searchPageData.results}">
		<div class="account-section-content	">
			<div class="account-orderhistory">
				<div class="account-orderhistory-pagination">
					<nav:pagination top="true" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
				</div>
	            <div class="account-overview-table">
					<table class="table mb-4">
						<thead class="thead-dark text-center align-middle">
						<tr class="account-orderhistory-table-head responsive-table-head hidden-xs">
							<th><spring:theme code="text.account.orderHistory.orderNumber" /></th>
							<th><spring:theme code="text.account.orderHistory.purchaseOrderNo"/></th> 
							<th><spring:theme code="text.account.orderHistory.orderStatus"/></th>
							<th><spring:theme code="text.account.orderHistory.datePlaced"/></th>
							<th><spring:theme code="text.account.orderHistory.total"/></th>
						</tr> 
						</thead>
						<c:forEach items="${searchPageData.results}" var="order">
							<tr class="responsive-table-item">
								<ycommerce:testId code="orderHistoryItem_orderDetails_link">
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.orderHistory.orderNumber" /></td>
									<td class="responsive-table-cell text-center align-middle"">
										<spring:url value="/my-account/order/{/orderCode}" var="orderDetailsUrl" htmlEscape="false">
											<spring:param name="orderCode" value="${order.code}"/>
										</spring:url>
										<a href="${fn:escapeXml(orderDetailsUrl)}" class="responsive-table-link">
											${fn:escapeXml(order.code)}
										</a>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.orderHistory.orderStatus"/></td>
									<td class="text-center align-middle">${order.purchaseOrderNumber}</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.orderHistory.orderStatus"/></td>
									<td class="status text-center align-middle">
										<c:choose>
										   <c:when test="${order.statusDisplay eq 'approved' || order.statusDisplay eq 'merchant.rejected' || order.statusDisplay eq 'merchant.approved=' || order.statusDisplay eq 'rejected' || order.statusDisplay eq 'pending.merchant.approval' || order.statusDisplay eq 'pending.approval' || order.statusDisplay eq 'assigned.admin'}">   
										   	<spring:theme code="addon.text.account.order.status.display.${order.statusDisplay}"/>
										   </c:when>
										   <c:otherwise> 
										   	<spring:theme code="text.account.order.status.display.${order.statusDisplay}"/>
										   </c:otherwise> 
										</c:choose> 
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.orderHistory.datePlaced"/></td>
									<td class="responsive-table-cell text-center align-middle"><fmt:formatDate value="${order.placed}" dateStyle="medium" timeStyle="short" type="both"/></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.orderHistory.total"/></td>
									<td class="responsive-table-cell responsive-table-cell-bold text-center align-middle">${fn:escapeXml(order.total.formattedValue)}</td>
								</ycommerce:testId>
							</tr>
						</c:forEach>
					</table>
	            </div>
			</div>
			<br/>
			<div class="account-orderhistory-pagination">
				<nav:pagination top="false" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>
		</div>
	</c:if> 
</div>