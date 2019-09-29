<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>

<c:if test="${not hasShippedItems}">
	<spring:theme code="checkout.pickup.no.delivery.required"/>
</c:if>

<ul class="checkout-order-summary-list">
<table class="table">
	<c:if test="${hasShippedItems}">
		<thead class="bg-dark text-white">
			<tr>
				<th class="text-center align-middle bg-dark text-white">
					<spring:theme code="checkout.pickup.items.to.be.delivered" />
				</th>
				<th class="bg-dark text-white"></th>
		 		<th class="bg-dark text-white"></th>   
			</tr>
		</thead>
	</c:if>  

	<tbody>    		
		<c:forEach items="${cartData.entries}" var="entry" varStatus="loop">
			<c:if test="${entry.deliveryPointOfService == null}">
				<c:url value="${entry.product.url}" var="productUrl"/>
				<tr>
					<!-- product images and url -->
					<th scope="row" class="text-center align-middle">
						<a href="${fn:escapeXml(productUrl)}">
							<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
						</a>
					</th> 
					<td class="text-left align-middle">
			        	<h6 class="text-primary">
			          		<a href="${fn:escapeXml(productUrl)}">${fn:escapeXml(entry.product.name)}</a>
			          	</h6> 
			          	<!--add  prodcut code -->
			          	${fn:escapeXml(entry.product.code)}<br>
			          	<span class="label-spacing"><spring:theme code="order.itemPrice" />:</span>
						<c:if test="${entry.product.multidimensional}">
							<%-- if product is multidimensional with different prices, show range, else, show unique price --%>
							<c:choose>
								<c:when test="${entry.product.priceRange.minPrice.value ne entry.product.priceRange.maxPrice.value}">
									<format:price priceData="${entry.product.priceRange.minPrice}" /> - <format:price priceData="${entry.product.priceRange.maxPrice}" />
								</c:when>
	                            <c:when test="${entry.product.priceRange.minPrice.value eq entry.product.priceRange.maxPrice.value}">
	                                <format:price priceData="${entry.product.priceRange.minPrice}" />
	                            </c:when>
								<c:otherwise>
									<format:price priceData="${entry.product.price}" />
								</c:otherwise>
							</c:choose>
							<br>
						</c:if> 
						<c:if test="${! entry.product.multidimensional}">
							<format:price priceData="${entry.basePrice}" displayFreeForZero="true" />
						</c:if> 
						<c:forEach items="${entry.product.baseOptions}" var="option">
							<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
								<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
									<div>${fn:escapeXml(selectedOption.name)}: ${fn:escapeXml(selectedOption.value)}</div>
								</c:forEach>
							</c:if>
						</c:forEach> 
						<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntryOrOrderEntryGroup(cartData, entry) && showPotentialPromotions}">
	                        <c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
	                            <c:set var="displayed" value="false"/>
	                            <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
	                                <c:if test="${not displayed && ycommerce:isConsumedByEntry(consumedEntry,entry)}">
	                                    <c:set var="displayed" value="true"/>
	                                    <span class="promotion">${ycommerce:sanitizeHTML(promotion.description)}</span>
	                                </c:if>
	                            </c:forEach>
	                        </c:forEach>
						</c:if> 
						<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntryOrOrderEntryGroup(cartData, entry)}">
	                        <c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
	                            <c:set var="displayed" value="false"/>
	                            <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
	                                <c:if test="${not displayed && ycommerce:isConsumedByEntry(consumedEntry,entry)}">
	                                    <c:set var="displayed" value="true"/>
	                                    <span class="promotion">${ycommerce:sanitizeHTML(promotion.description)}</span>
	                                </c:if>
	                            </c:forEach>
	                        </c:forEach>
						</c:if>
						<common:configurationInfos entry="${entry}"/>
						<c:if test="${entry.product.multidimensional}" >
							<a href="#" id="QuantityProductToggle" data-index="${loop.index}" class="showQuantityProductOverlay updateQuantityProduct-toggle">
								<span><spring:theme code="order.product.seeDetails"/></span>
							</a>
						</c:if>
<%-- 						<spring:url value="/checkout/multi/getReadOnlyProductVariantMatrix" var="targetUrl" htmlEscape="false"/> --%>
<%-- 						<grid:gridWrapper entry="${entry}" index="${loop.index}" styleClass="display-none"targetUrl="${targetUrl}"/> --%>
						<br>
					   	<span><spring:theme code="basket.page.buy.qty"/>:</span>${fn:escapeXml(entry.quantity)}<br>
					</td>  
					<!-- price -->
					<td class="text-right align-middle">
						<format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
					</td> 
				</tr>
			</c:if>
		</c:forEach>
	</tbody>	
</table>
</ul>
