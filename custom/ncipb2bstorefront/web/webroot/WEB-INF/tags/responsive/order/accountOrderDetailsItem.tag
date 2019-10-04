<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="consignment" required="true" type="de.hybris.platform.commercefacades.order.data.ConsignmentData" %>
<%@ attribute name="inProgress" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />
 
<!--  accountOrderDetailsItem.tag start  -->
<div class="row p-4 border border-secondary no-gutters mb-4">
	<div class="row col-12 no-gutters mb-4">
		<c:set var="consignment" scope="request" value="${consignment}" />  
		<div class="col-md-4 align-content-center">
			<h4 class="text-danger">
				<ycommerce:testId code="orderDetail_consignmentStatus_label"> 
	            	<spring:theme code="text.account.order.consignment.status.${consignment.statusDisplay}" />
	            </ycommerce:testId>
			</h4>
		</div>
		<div class="col-md-4 row align-content-center no-gutters border border-white">
			<div class="col-4 l-gray text-center p-2 border border-secondary bg-secondary text-white">
				<spring:theme code="text.account.order.lfdat.date" />
			</div>
<!-- 			aaaa:<br/> -->
<%-- 			dely_type:${consignment.dely_type}<br/> --%>
<%-- 			ncipdelivery:${consignment.ncipdelivery}<br/> --%>
<%-- 			lfdat:${consignment.lfdat}<br/> --%>
<%-- 			shippingDate:${consignment.shippingDate}<br/>  --%>
<%-- 			remark:${consignment.remark}<br/> --%>
<%-- 			shipping_number:${consignment.shipping_number}<br/> --%>
			<div class="col-8 col-md-7 bg-light p-2 border border-secondary">
				<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
					<span class="well-headline-sub">
	                  <fmt:formatDate  value="${consignment.lfdat}" dateStyle="medium" timeStyle="short" type="both"/>
	                </span>
				</ycommerce:testId>
			</div>
		</div>
		<div class="col-md-4 row align-content-center no-gutters border border-white">
			<div class="col-4 l-gray text-center p-2 border border-secondary bg-secondary text-white"> 
				 <spring:theme code="text.account.order.shipping.date" />
			</div>
			<div class="col-8 bg-light p-2 border border-secondary">
				<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
					<span class="well-headline-sub">
	                   <fmt:formatDate value="${consignment.shippingDate}" dateStyle="medium" timeStyle="short" type="both"/>
	                </span>
				</ycommerce:testId>
			</div>
		</div>
	</div>
	
	<ul class="item__list">
	    <div class="d-none d-md-block col-12 bg-dark p-2 no-gutters">
		  <div class="row no-gutters">	
				<div class="col-3"></div>
				<div class="col-2 text-white"><spring:theme code="basket.page.item"/></div>
				<div class="col-2 text-white"><spring:theme code="basket.page.price"/></div>
				<div class="col-2 text-white"><spring:theme code="basket.page.qty"/></div>
				<div class="col-3 text-white"><spring:theme code="basket.page.total"/></div>
			</div> 
		</div>
		<ycommerce:testId code="orderDetail_itemBody_section">
			<c:forEach items="${consignment.entries}" var="entry" varStatus="loop">
				<order:ncipb2bOrderEntryDetails orderEntry="${entry.orderEntry}" consignmentEntry="${entry}" order="${order}" itemIndex="${loop.index}"/>
			</c:forEach>
		</ycommerce:testId>
	</ul>
	 
	<!-- <div class="well well-quinary well-xs"> -->
	<%-- 	<ycommerce:testId code="orderDetail_itemHeader_section">			 --%>
	<!-- 		<div class="well-headline"> -->
	<%--             <ycommerce:testId code="orderDetail_consignmentStatus_label"> --%>
	<%--                 <spring:theme code="text.account.order.consignment.status.${consignment.statusDisplay}" /> --%>
	<%--             </ycommerce:testId> --%>
	
	<%-- 			<ycommerce:testId code="orderDetail_consignmentStatusDate_label"> --%>
	<!-- 				<span class="well-headline-sub"> -->
	<%--                     <fmt:formatDate value="${consignment.statusDate}" dateStyle="medium" timeStyle="short" type="both"/> --%>
	<!--                 </span> -->
	<%-- 			</ycommerce:testId> --%>
	<!-- 		</div> -->
	
	<!--         <div class="well-content col-sm-12 col-md-9"> -->
	<!--             <div class="row"> -->
	<!--                 <div class="col-sm-12 col-md-9"> -->
	<%--                     <c:choose> --%>
	<%--                         <c:when test="${consignment.deliveryPointOfService ne null}"> --%>
	<%--                             <ycommerce:testId code="orderDetail_storeDetails_section"> --%>
	<%--                                 <order:storeAddressItem deliveryPointOfService="${consignment.deliveryPointOfService}" inProgress="${inProgress}" statusDate="${consignment.statusDate}"/> --%>
	<%--                             </ycommerce:testId> --%>
	<%--                         </c:when> --%>
	<%--                         <c:otherwise> --%>
	<!--                             <div class="row"> -->
	<!--                                 <div class="col-sm-6 col-md-4"> -->
	<!--                                     <div class="order-ship-to"> -->
	<%--                                         <ycommerce:testId code="orderDetail_deliveryAddress_section"> --%>
	<%--                                             <div class="label-order"><spring:theme code="text.account.order.shipto"/></div> --%>
	<%--                                             <div class="value-order"><order:addressItem address="${orderData.deliveryAddress}"/></div> --%>
	<%--                                         </ycommerce:testId> --%>
	<!--                                     </div> -->
	<!--                                 </div> -->
	
	<!--                                 <div class="col-sm-6 col-md-4"> -->
	<!--                                     <div class="order-shipping-method"> -->
	<%--                                         <ycommerce:testId code="orderDetail_deliveryMethod_section"> --%>
	<%--                                             <order:deliveryMethodItem order="${orderData}"/> --%>
	<%--                                         </ycommerce:testId> --%>
	<!--                                     </div> -->
	<!--                                 </div> -->
	<!--                             </div> -->
	
	<%--                             <c:if test="${not inProgress}"> --%>
	<%--                                 <c:choose> --%>
	<%--                                     <c:when test="${consignment.status.code eq 'SHIPPED' and not empty consignment.trackingID}" > --%>
	<!--                                         <div class="col-sm-4"> -->
	<!--                                             <div class="order-tracking-no"> -->
	<%--                                                 <ycommerce:testId code="orderDetail_trackingId_label"> --%>
	<%--                                                     <span class="label-order"><spring:theme code="text.account.order.tracking" text="Tracking No." /></span> --%>
	<!--                                                     <br> -->
	<%--                                                     <span class="order-track-number">${fn:escapeXml(consignment.trackingID)}</span> --%>
	<%--                                                 </ycommerce:testId> --%>
	<!--                                             </div> -->
	<!--                                         </div> -->
	<%--                                     </c:when> --%>
	<%--                                 </c:choose> --%>
	<%--                             </c:if> --%>
	<%--                         </c:otherwise> --%>
	<%--                     </c:choose> --%>
	<!--                 </div> -->
	<!--             </div> -->
	<!--         </div> -->
	<%-- 	</ycommerce:testId> --%>
	<!-- </div> -->
		 
</div> 