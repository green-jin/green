<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<!--  add on b2bacceleratoraddon  accountOrderDetailsOverview.tag start -->
<div class="row">
 <div class="col-sm-12 col-md-9 col-no-padding">
        <div class="row">
            <div class="col-sm-4 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderID_label"> 
                        <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.orderHistory.orderNumber"/></h6></span>
                        <span class="item-value text-dark text-left"><h6>${fn:escapeXml(orderData.code)}</h6></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <c:if test="${orderData.paymentType.code=='ACCOUNT' and not empty orderData.purchaseOrderNumber}">
                        <ycommerce:testId code="orderDetail_overviewPurchaseOrderNumber_label">
                            <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.order.orderDetails.purchaseOrderNumber"/></h6></span>
                            <span class="item-value text-dark text-left"><h6>${fn:escapeXml(orderData.purchaseOrderNumber)}</h6></span>
                        </ycommerce:testId>
                    </c:if>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderStatus_label">
                        <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.orderHistory.orderStatus"/></h6></span>
                        <c:if test="${not empty orderData.statusDisplay}">
                            <span class="item-value text-dark text-left"><h6><spring:theme code="text.account.order.status.display.${orderData.statusDisplay}"/></h6></span>
                        </c:if>
                    </ycommerce:testId>
                </div>
                 <div class="item-group">
                     <ycommerce:testId code="orderDetail_overviewStatusDate_label">
                        <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.orderHistory.datePlaced"/></h6></span>
                        <span class="item-value text-dark text-left"><h6><fmt:formatDate value="${order.created}" dateStyle="medium" timeStyle="short" type="both"/></h6></span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-4 item-wrapper">
            	<div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewPlacedBy_label">
                        <span class="item-label text-left text-secondary"><h6><spring:theme code="checkout.multi.summary.orderPlacedByUser"/></h6></span>
                        <span class="item-value text-dark text-left"><h6><spring:theme code="text.company.user.${order.b2bCustomerData.titleCode}.name" text=""/>&nbsp;${fn:escapeXml(order.b2bCustomerData.firstName)}&nbsp;${fn:escapeXml(order.b2bCustomerData.lastName)}</h6></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                     <ycommerce:testId code="orderDetail_deliveryAddress_section"> 
                         <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.orderHistory.shippingAddress"/></h6></span>
                         <div class="item-value text-dark text-left">
                         	<h6> 
	                         	${fn:escapeXml(order.deliveryAddress.country.name)}
	                         	${fn:escapeXml(order.deliveryAddress.town)}
	                         	${fn:escapeXml(order.deliveryAddress.line1)}
<%-- 								<c:if test="${not empty order.deliveryAddress.line2}"> --%>
<%-- 									line2:${fn:escapeXml(order.deliveryAddress.line2)} --%>
<%-- 								</c:if> --%>
							</h6> 
                         </div>
                     </ycommerce:testId>
                </div> 
                <div class="item-group">
                    <c:if test="${orderData.paymentType.code=='ACCOUNT'}">
                        <ycommerce:testId code="orderDetail_overviewParentBusinessUnit_label">
                            <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.orderHistory.phone"/></h6></span>
                            <span class="item-value text-dark text-left">
                            	<h6> 
                            		${fn:escapeXml(order.deliveryAddress.phone)}
                            	</h6>
                            </span>
                        </ycommerce:testId>
                    </c:if>
                </div>
            </div>
            <div class="col-sm-4 item-wrapper">
                <div class="item-group">
                	<ycommerce:testId code="orderDetail_overviewOrderTotal_label">
                        <span class="item-label text-left text-secondary"><h6><spring:theme code="text.account.order.total"/></h6></span>
                        <span class="item-value text-dark text-left"><h6><format:price priceData="${order.totalPriceWithTax}"/></h6></span>
                    </ycommerce:testId>
                </div>
<!--                 <div class="item-group"> -->
<%--                    <c:if test="${orderData.paymentType.code=='ACCOUNT'}"> --%>
<%--                         <ycommerce:testId code="orderDetail_overviewCostCenter_label"> --%>
<%--                             <span class="item-label"><spring:theme code="text.account.order.orderDetails.CostCenter"/></span> --%>
<%--                             <span class="item-value">${fn:escapeXml(orderData.costCenter.name)}</span> --%>
<%--                         </ycommerce:testId> --%>
<%--                     </c:if> --%>
<!--                 </div> -->
<!--                 <div class="item-group"> -->
<%--                 	<c:if test="${orderData.quoteCode ne null}"> --%>
<%-- 							<spring:url htmlEscape="false" value="/my-account/my-quotes/${orderData.quoteCode}" var="quoteDetailUrl"/> --%>
<%-- 	                    <ycommerce:testId code="orderDetail_overviewQuoteId_label"> --%>
<%-- 	                        <span class="item-label"><spring:theme code="text.account.quote.code"/></span> --%>
<!-- 	                        <span class="item-value"> -->
<%-- 										<a href="${fn:escapeXml(quoteDetailUrl)}" > --%>
<%-- 											${fn:escapeXml(orderData.quoteCode)} --%>
<!-- 										</a> -->
<!-- 	                        </span> -->
<%-- 	                    </ycommerce:testId> --%>
<%--                     </c:if> --%>
<!--                 </div> -->
            </div>
        </div>
    </div>
    <div class="col-sm-12 col-md-3 item-action">
        <c:set var="orderCode" value="${fn:escapeXml(order.code)}" scope="request"/>
        <c:set var="orderCancellable" value="${order.cancellable}" scope="request"/>
        <c:set var="orderReturnable" value="${order.returnable}" scope="request"/>
        <action:actions element="div" parentComponent="${component}"/>
        <script type="text/javascript" src="/yacceleratorstorefront/_ui/addons/orderselfserviceaddon/responsive/common/js/orderselfserviceaddon.js"></script>
    </div>
</div>

