<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ attribute name="orderEntry" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>
<%@ attribute name="consignmentEntry" required="false"
              type="de.hybris.platform.commercefacades.order.data.ConsignmentEntryData" %>
<%@ attribute name="itemIndex" required="true" type="java.lang.Integer" %>
<%@ attribute name="targetUrl" required="false" type="java.lang.String" %>
<%@ attribute name="showStock" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showViewConfigurationInfos" required="false" type="java.lang.Boolean" %>
<%@ attribute name="viewConfigurationInfosBaseUrl" required="false" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="varShowStock" value="${(empty showStock) ? true : showStock}" />
<c:set var="defaultViewConfigurationInfosBaseUrl" value="/my-account/order" />

<c:url value="${orderEntry.product.url}" var="productUrl"/>
<c:set var="entryStock" value="${fn:escapeXml(orderEntry.product.stock.stockLevelStatus.code)}"/>
<li class="item__list--item">
<div class="row col-12 d-flex align-items-center my-4 p-2 no-gutters">

	<div class="col-3">
		<div class="d-flex justify-content-center">
			<%-- chevron for multi-d products --%>
<!-- 		    <div class="hidden-xs hidden-sm item__toggle"> -->
<%-- 		        <c:if test="${orderEntry.product.multidimensional}"> --%>
<%-- 		            <div class="js-show-multiD-grid-in-order" data-index="${itemIndex}"> --%>
<%-- 		                <ycommerce:testId code="cart_product_updateQuantity"> --%>
<!-- 		                    <span class="glyphicon glyphicon-chevron-down"></span> -->
<%-- 		                </ycommerce:testId> --%>
<!-- 		            </div> -->
<%-- 		        </c:if> --%>
<!-- 		    </div> -->
		
		    <%-- product image --%>
	        <ycommerce:testId code="orderDetail_productThumbnail_link">
	            <a href="${fn:escapeXml(productUrl)}">
	               <product:productPrimaryImage product="${orderEntry.product}" format="thumbnail"/>
	            </a>
	        </ycommerce:testId>
		</div>
  	</div>
  	
  	<div class="col-6 d-flex flex-md-row flex-column align-items-center no-gutters bg-white">
  		<%-- product name, code, promotions --%>
		<div class="col-md-4">
			<h6 class="text-primary">
				<ycommerce:testId code="orderDetails_productName_link">
	            	<a href="${orderEntry.product.purchasable ? fn:escapeXml(productUrl) : ''}"><span class="item__name">${fn:escapeXml(orderEntry.product.name)}</span></a>
	        	</ycommerce:testId>
			</h6> 
			<ycommerce:testId code="orderDetails_productCode">
	        	${fn:escapeXml(orderEntry.product.code)}
	         </ycommerce:testId> 
<%-- 	         <c:if test="${not empty order.appliedProductPromotions}"> --%>
<!-- 	           	<div class="promo"> -->
<!-- 	               	<ul> -->
<%-- 	                   	<c:forEach items="${order.appliedProductPromotions}" var="promotion"> --%>
<%-- 	                       	<c:set var="displayed" value="false"/> --%>
<%-- 	                       	<c:forEach items="${promotion.consumedEntries}" var="consumedEntry"> --%>
<%-- 	                           	<c:if test="${not displayed and consumedEntry.orderEntryNumber == orderEntry.entryNumber}"> --%>
<%-- 	                               	<c:set var="displayed" value="true"/> --%>
<!-- 	 								<li> -->
<%-- 										<ycommerce:testId code="orderDetail_productPromotion_label"> --%>
<%-- 	                                       ${ycommerce:sanitizeHTML(promotion.description)} --%>
<%-- 	                                    </ycommerce:testId> --%>
<!-- 	                                </li> -->
<%-- 	                            </c:if> --%>
<%-- 	                        </c:forEach> --%>
<%-- 	                    </c:forEach> --%>
<!-- 	                </ul> -->
<!-- 	            </div> -->
<%-- 	        </c:if> --%>
	        
<%-- 	        <common:configurationInfos entry="${orderEntry}"/> --%>
<%-- 	        <c:if test="${empty showViewConfigurationInfos || showViewConfigurationInfos eq true}">        	 --%>
<%-- 	        	<common:viewConfigurationInfos baseUrl="${empty viewConfigurationInfosBaseUrl ? defaultViewConfigurationInfosBaseUrl : viewConfigurationInfosBaseUrl}"  --%>
<%-- 	        		orderEntry="${orderEntry}" itemCode="${order.code}"/>	 --%>
<%-- 	        </c:if> --%>
		</div>

		<div class="col-md-4 row no-gutters">
			<div class="col-auto">
				<%-- price --%>  
				<ycommerce:testId code="orderDetails_productItemPrice_label">
				    <order:orderEntryPrice orderEntry="${orderEntry}"/>
				</ycommerce:testId>
			</div>
		</div>
		
		<div class="col-md-4 row no-gutters">
			<div class="col-auto">
				<%-- quantity --%>
<%-- 				<c:forEach items="${orderEntry.product.baseOptions}" var="option"> --%>
<%-- 					<c:if test="${not empty option.selected and option.selected.url eq orderEntry.product.url}"> --%>
<%-- 						<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption"> --%>
<!-- 							<div> -->
<%-- 								<ycommerce:testId code="orderDetail_variantOption_label"> --%>
<%-- 								   <span>${fn:escapeXml(selectedOption.name)}:</span> --%>
<%-- 									<span>${fn:escapeXml(selectedOption.value)}</span> --%>
<%-- 								</ycommerce:testId> --%>
<!-- 							</div> -->
<%-- 							<c:set var="entryStock" value="${fn:escapeXml(option.selected.stock.stockLevelStatus.code)}"/> --%>
<%-- 						</c:forEach> --%>
<%-- 					</c:if> --%>
<%-- 				</c:forEach> --%>
				<ycommerce:testId code="orderDetails_productQuantity_label"> 
					<c:choose>
						<c:when test="${consignmentEntry ne null }">
						   ${fn:escapeXml(consignmentEntry.quantity)}
						</c:when>
						<c:otherwise>
						   ${fn:escapeXml(orderEntry.quantity)}
						</c:otherwise>
					</c:choose>  
				</ycommerce:testId>
			</div>
		</div>
	</div>
			    
	
	<div class="col-3 align-self-md-center align-self-end">
		<div class="text-left">
			<h6>
				<%-- total --%>
		        <ycommerce:testId code="orderDetails_productTotalPrice_label">
		          <format:price priceData="${orderEntry.totalPrice}" displayFreeForZero="true"/>
		        </ycommerce:testId>
			</h6>
		</div>
	</div>
	  
	</div>
<!--     <div class="item__quantity__total visible-xs visible-sm"> -->
<%--         <c:set var="showEditableGridClass" value=""/> --%>
<%--         <c:if test="${orderEntry.product.multidimensional}"> --%>
<%--             <c:set var="showEditableGridClass" --%>
<%--                    value="js-show-multiD-grid-in-order"/> --%>
<%--         </c:if> --%>

<%--         <div class="details ${showEditableGridClass}" data-index="${itemIndex}"> --%>
<!--             <div class="qty"> -->
<%--                 <ycommerce:testId code="orderDetails_productQuantity_label"> --%>
<%--                     <label><spring:theme code="text.account.order.qty"/>:</label> --%>
<!--                         <span class="qtyValue"> -->
<%--                             <c:choose> --%>
<%--                                 <c:when test="${consignmentEntry ne null }"> --%>
<%--                                    66 ${fn:escapeXml(consignmentEntry.quantity)} --%>
<%--                                 </c:when> --%>
<%--                                 <c:otherwise> --%>
<%--                                   66  ${fn:escapeXml(orderEntry.quantity)} --%>
<%--                                 </c:otherwise> --%>
<%--                             </c:choose> --%>
<!--                         </span> -->
<%--                 </ycommerce:testId> --%>
<%--                 <c:if test="${orderEntry.product.multidimensional}"> --%>
<%--                     <ycommerce:testId code="cart_product_updateQuantity"> --%>
<!--                         77<span class="glyphicon glyphicon-chevron-right"></span> -->
<%--                     </ycommerce:testId> --%>
<%--                 </c:if> --%>
<!--                 <div class="item__total"> -->
<%--                     <ycommerce:testId code="orderDetails_productTotalPrice_label"> --%>
<%--                        88 <format:price priceData="${orderEntry.totalPrice}" displayFreeForZero="true"/> --%>
<%--                     </ycommerce:testId> --%>
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
</li>
