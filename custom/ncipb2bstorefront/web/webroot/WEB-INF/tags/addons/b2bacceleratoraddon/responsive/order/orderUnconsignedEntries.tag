<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<!-- add on  b2bacceleratoraddon orderUnconsignedEntries.tag start  -->
<div class="row p-4 border border-secondary no-gutters mb-4">
	<div class="row col-12 no-gutters mb-4">
		<div class="col-md-4 align-content-center">
			<h4 class="text-danger">
				<spring:theme
					code="text.account.order.unconsignedEntry.status.pending" />
			</h4>
<%-- 						<c:choose> --%>
<%-- 				            <c:when test="${entry.deliveryPointOfService ne null}"> --%>
<!-- 				                <div class="well-content"> -->
<!-- 				                    <div class="row"> -->
<!-- 				                        <div class="col-sm-12 col-md-9"> -->
<%-- 				                            <order:storeAddressItem deliveryPointOfService="${entry.deliveryPointOfService}" /> --%>
<!-- 				                        </div> -->
<!-- 				                    </div> -->
<!-- 				                </div> -->
<%-- 				            </c:when> --%>
<%-- 				            <c:otherwise> --%>
<!-- 				                <div class="well-content"> -->
<!-- 				                    <div class="row"> -->
<!-- 				                        <div class="col-md-5"> -->
<!-- 				                            <div class="order-ship-to"> -->
<!-- 				                                <div class="label-order"> -->
<%-- 				                                    <spring:theme code="text.account.order.shipto" text="Ship To" /> --%>
<!-- 				                                </div> -->
<!-- 				                                <div class="value-order"> -->
<%-- 				                                    <order:addressItem address="${orderData.deliveryAddress}"/> --%>
<!-- 				                                </div> -->
<!-- 				                            </div> -->
<!-- 				                        </div> -->
<!-- 				                    </div> -->
<!-- 				                </div> -->
<%-- 				            </c:otherwise> --%>
<%-- 				        </c:choose> --%>
		</div>
		<div
			class="col-md-4 row align-content-center no-gutters border border-white">
			<div
				class="col-4 l-gray text-center p-2 border border-secondary bg-secondary text-white">
				<spring:theme code="text.account.order.lfdat.date" />
			</div>
			<div class="col-8 col-md-7 bg-light p-2 border border-secondary">
				<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
					<span class="well-headline-sub"> 
						<fmt:formatDate  value="${consignment.lfdat}" dateStyle="medium" timeStyle="short" type="both" />
					</span>
				</ycommerce:testId>
			</div>
		</div>
		<div
			class="col-md-4 row align-content-center no-gutters border border-white">
			<div
				class="col-4 l-gray text-center p-2 border border-secondary bg-secondary text-white">
				<spring:theme code="text.account.order.shipping.date" />
			</div>
			<div class="col-8 bg-light p-2 border border-secondary">
				<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
					<span class="well-headline-sub"> 
						<fmt:formatDate value="${consignment.shippingDate}" dateStyle="medium" timeStyle="short" type="both" />
					</span>
				</ycommerce:testId>
			</div>
		</div>
	</div>
	<ul class="item__list">
		<div class="d-none d-md-block col-12 bg-dark p-2 no-gutters">
			<div class="row no-gutters">
				<div class="col-3"></div>
				<div class="col-2 text-white">
					<spring:theme code="basket.page.item" />
				</div>
				<div class="col-2 text-white">
					<spring:theme code="basket.page.price" />
				</div>
				<div class="col-2 text-white">
					<spring:theme code="basket.page.qty" />
				</div>
				<div class="col-3 text-white">
					<spring:theme code="basket.page.total" />
				</div>
			</div>
		</div>
		<c:forEach items="${order.unconsignedEntries}" var="entry"
			varStatus="loop">
			<%-- 	        <order:orderEntryDetails orderEntry="${entry}" order="${order}" itemIndex="${loop.index}"/> --%>
			<order:ncipb2bOrderEntryDetails orderEntry="${entry}"
				order="${order}" itemIndex="${loop.index}" />
		</c:forEach>
	</ul>
</div>

