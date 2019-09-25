<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="isOrderForm" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<%--牌價--%>
<c:if test="${not empty product.listpr}">
 <h5><spring:theme code="product.detail.listpr"/>${product.listpr}</h5>
</c:if>

<%--您的價格--%>
<c:choose>
	<c:when test="${empty product.volumePrices}">
		<c:choose>
			<c:when test="${(not empty product.priceRange) and (product.priceRange.minPrice.value ne product.priceRange.maxPrice.value) and ((empty product.baseProduct) or (not empty isOrderForm and isOrderForm))}">
				<h5 class="my-3"><spring:theme code="product.detail.price"/><format:price priceData="${product.priceRange.minPrice}"/> - <format:price priceData="${product.priceRange.maxPrice}"/></h5>
			</c:when>
			<c:otherwise>
				<h5 class="my-3">
					<spring:theme code="product.detail.price"/>
					<format:fromPrice priceData="${product.price}"/>
				</h5>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>

<%--		<table class="volume__prices" cellpadding="0" cellspacing="0" border="0">--%>
<%--			<thead>--%>
<%--			<th class="volume__prices-quantity"><spring:theme code="product.volumePrices.column.qa"/></th>--%>
<%--			<th class="volume__price-amount"><spring:theme code="product.volumePrices.column.price"/></th>--%>
<%--			</thead>--%>
<%--			<tbody>--%>
<%--			<c:forEach var="volPrice" items="${product.volumePrices}">--%>
<%--				<tr>--%>
<%--					<td class="volume__price-quantity">--%>
<%--						<c:choose>--%>
<%--							<c:when test="${empty volPrice.maxQuantity}">--%>
<%--								${volPrice.minQuantity}+--%>
<%--							</c:when>--%>
<%--							<c:otherwise>--%>
<%--								${volPrice.minQuantity}-${volPrice.maxQuantity}--%>
<%--							</c:otherwise>--%>
<%--						</c:choose>--%>
<%--					</td>--%>
<%--					<td class="volume__price-amount text-right">${fn:escapeXml(volPrice.formattedValue)}</td>--%>
<%--				</tr>--%>
<%--			</c:forEach>--%>
<%--			</tbody>--%>
<%--		</table>--%>
	</c:otherwise>
</c:choose>