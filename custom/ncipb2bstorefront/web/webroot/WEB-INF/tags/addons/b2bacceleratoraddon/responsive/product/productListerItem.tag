<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="isOrderForm" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<c:set var="showEditableGridClass" value=""/>

<%--<c:choose>--%>
<%--    <c:when test="${isForceInStock}">--%>
<%--        <c:set var="maxQty" value="FORCE_IN_STOCK"/>--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--        <c:set var="maxQty" value="100"/>--%>
<%--    </c:otherwise>--%>
<%--</c:choose>--%>
<c:set var="qtyMinus" value="0"/>


<c:if test="${not empty isOrderForm && isOrderForm}">
    <%--	<li class="item__list--item ${hasPromotion ? ' productListItemPromotion' : ''}">--%>
    <ycommerce:testId code="test_searchPage_wholeProduct">

        <%-- product image --%>
        <div class="col-4 col-md-2 d-flex justify-content-center align-self-center">
            <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">
                <product:productPrimaryImage product="${product}" format="thumbnail"/>
            </a>
        </div>

        <div class="col-8 col-md-10 no-gutters">
        <div class="row no-gutters">
        <%-- product name, code, promotions --%>
        <%--            <div class="item__info">--%>
        <div class="col-md-4 align-self-center no-gutters">
            <ycommerce:testId code="searchPage_productName_link_${product.code}">
                <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">
                    <h6 class="text-primary">${ycommerce:sanitizeHTML(product.name)}</h6>
                </a>
            </ycommerce:testId>
        </div>

        <%--                <div class="item__code">--%>
        <%--                        ${fn:escapeXml(product.code)}--%>
        <%--                </div>--%>

        <%-- Future Availability 未來可用性 --%>
        <%--                <div class="productFutureAvailability">--%>
        <%--                    <product:productFutureAvailability product="${product}"--%>
        <%--                                                       futureStockEnabled="${futureStockEnabled}"/>--%>
        <%--                </div>--%>
        <%--            </div>--%>

        <%-- price --%>
        <div class="col-md-2 align-self-center no-gutters">
            <ycommerce:testId code="searchPage_price_label_${product.code}">
                        <span class="visible-xs visible-sm"><spring:theme
                                code="basket.page.itemPrice"/>: </span>
                <product:productListerItemPrice product="${product}"/>
            </ycommerce:testId>
        </div>


        <%-- description --%>
        <%--			<div class="item__description">--%>
        <%--				<c:if test="${not empty product.summary}">--%>
        <%--					${ycommerce:sanitizeHTML(product.summary)}--%>
        <%--				</c:if>--%>
        <%--				<product:productListerClassifications product="${product}"/>--%>
        <%--			</div>--%>

        <%-- quantity --%>
        <div class="col-md-2 align-self-center no-gutters">
            <c:choose>
                <c:when test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">
                    <div class="py-3">
                        <spring:theme code="product.grid.outOfStock"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <label class="visible-xs visible-sm"><spring:theme
                            code="basket.page.qty"/>:</label>
                    <span data-variant-price="${fn:escapeXml(product.price.value)}"
                          id="productPrice[${fn:escapeXml(sessionScope.skuIndex)}]"
                          class="price hidden">${fn:escapeXml(product.price.value)}</span>
                    <input type="hidden"
                           id="productPrice[${fn:escapeXml(sessionScope.skuIndex)}]"
                           value="${fn:escapeXml(product.price.value)}"/>
                    <input type="hidden" class="${fn:escapeXml(product.code)} sku"
                           name="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].sku"
                           id="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].sku"
                           value="${fn:escapeXml(product.code)}"/>

                    <div class="btn-group py-3 js-qty-selector" role="group"
                         aria-label="First group">
                        <c:set value='{"product":"${ycommerce:encodeJSON(product.code)}"}'
                               var="productSelectionJson"/>

                            <%--						<button type="button" class="btn btn-secondary"--%>
                            <%--								onclick="minusFunction(${fn:escapeXml(sessionScope.skuIndex)})">---%>
                            <%--						</button>--%>

                        <c:set value='{"product":"${ycommerce:encodeJSON(product.code)}"}'
                               var="productSelectionJson"/>
                        <input type="text" maxlength="3" size="3"
                               id="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"
                               name="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"
                               data-product-selection="${fn:escapeXml(productSelectionJson)}"
                               class="sku-quantity form-control"
                               value="${fn:escapeXml(qtyMinus)}"
                               data-max="${fn:escapeXml(maxQty)}"
                               data-min="0"
                               style="text-align: center"
					onkeyup="sumAll(${fn:escapeXml(sessionScope.skuIndex)})">


<%--                        <input type="text" maxlength="3" size="1"--%>
<%--                               id="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"--%>
<%--                               name="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"--%>
<%--                               data-product-selection="${fn:escapeXml(productSelectionJson)}"--%>
<%--                               class="sku-quantity form-control" value="0"--%>
<%--							   style="text-align: center"--%>
<%--							   onkeyup="sumAll(${fn:escapeXml(sessionScope.skuIndex)})">--%>

                            <%--						<button type="button" class="btn btn-secondary"--%>
                            <%--								onclick="plusFunction(${fn:escapeXml(sessionScope.skuIndex)})">+--%>
                            <%--						</button>--%>

                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- total --%>
        <div id="price${fn:escapeXml(sessionScope.skuIndex)}"
             data-product-price="${product.price.value}"
             class="col-md-3 align-self-center no-gutters d-flex justify-content-md-end justify-content-start">

            <c:if test="${product.stock.stockLevelStatus.code != 'outOfStock'}">
                <h6>
                    <div id="total${fn:escapeXml(sessionScope.skuIndex)}"></div>
                </h6>
            </c:if>

            <c:set var="skuIndex" scope="session"
                   value="${fn:escapeXml(sessionScope.skuIndex + 1)}"/>
        </div>
    </ycommerce:testId>
    </div>
    </div>
    <%--	</li>--%>
</c:if>

<%--<script>--%>
<%--  function minusFunction(index) {--%>

<%--    var initialNumber = document.getElementById("cartEntries[" + index + "].quantity").value;--%>
<%--    var price = Number(document.getElementById("price" + index).getAttribute("data-product-price"));--%>
<%--    var qty = Number(document.getElementById("cartEntries[" + index + "].quantity").value);--%>
<%--    if (initialNumber >= 1) {--%>
<%--      document.getElementById("cartEntries[" + index + "].quantity").value = Number(initialNumber)--%>
<%--          - 1;--%>
<%--      document.getElementById("total" + index).innerText = "$" + price * qty;--%>
<%--      ACC.orderform.addToCartByButton(document.getElementById("total" + index).innerText);--%>
<%--    }--%>
<%--  }--%>
<%--</script>--%>
<%--<script>--%>
<%--  function plusFunction(index) {--%>
<%--    ACC.orderform.addToCartByButton(+1);--%>
<%--    var initialNumber = document.getElementById("cartEntries[" + index + "].quantity").value;--%>
<%--    document.getElementById("cartEntries[" + index + "].quantity").value = Number(initialNumber)--%>
<%--        + 1;--%>
<%--    document.getElementById("total" + index).innerText = "$" + Number(--%>
<%--        document.getElementById("price" + index).getAttribute("data-product-price")) * Number(--%>
<%--        document.getElementById("cartEntries[" + index + "].quantity").value);--%>
<%--  }--%>
<%--</script>--%>

<%--<script>--%>
<%--	function total(index) {--%>
<%--		// var initialNumber = document.getElementById("productNumber" + code).value;--%>
<%--		document.getElementById("total" + index).innerText = "$" + Number(--%>
<%--				document.getElementById("price" + index).getAttribute("data-product-price")) * Number(--%>
<%--				document.getElementById("cartEntries["+index+"].quantity").value) + ".00";--%>
<%--	}--%>
<%--</script>--%>

<script>
	function sumAll(index){

		var initialNumber = document.getElementById("cartEntries[" + index + "].quantity").value;
		var unitprice = Number(document.getElementById("price" + index).getAttribute("data-product-price"));
		document.getElementById("total" + index).innerText ="$" + unitprice * initialNumber;

	}
</script>

<%--Hybris原生--%>
<%--<c:if test="${not empty isOrderForm && isOrderForm}">--%>
<%--	<li class="item__list--item${hasPromotion ? ' productListItemPromotion' : ''}">--%>
<%--	    <ycommerce:testId code="test_searchPage_wholeProduct">--%>

<%--	        &lt;%&ndash; product image &ndash;%&gt;--%>
<%--	        <div class="item__image">--%>
<%--	            <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">--%>
<%--	                <product:productPrimaryImage product="${product}" format="thumbnail"/>--%>
<%--	            </a>--%>
<%--	        </div>--%>

<%--	        &lt;%&ndash; product name, code, promotions &ndash;%&gt;--%>
<%--	        <div class="item__info">--%>
<%--	            <ycommerce:testId code="searchPage_productName_link_${product.code}">--%>
<%--	                <a href="${fn:escapeXml(productUrl)}" title="${fn:escapeXml(product.name)}">--%>
<%--	                    <div class="item-name">${ycommerce:sanitizeHTML(product.name)}</div>--%>
<%--	                </a>--%>
<%--	            </ycommerce:testId>--%>

<%--	            <div class="item__code">--%>
<%--	            	${fn:escapeXml(product.code)}--%>
<%--	            </div>--%>

<%--	            &lt;%&ndash; Future Availability &ndash;%&gt;--%>
<%--	            <div class="productFutureAvailability">--%>
<%--	                <product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}"/>--%>
<%--	            </div>--%>
<%--	        </div>--%>

<%--	        &lt;%&ndash; price &ndash;%&gt;--%>
<%--	        <div class="item__price">--%>
<%--	            <ycommerce:testId code="searchPage_price_label_${product.code}">--%>
<%--	                <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>--%>
<%--	                <product:productListerItemPrice product="${product}"/>--%>
<%--	            </ycommerce:testId>--%>
<%--	        </div>--%>

<%--	        &lt;%&ndash; description &ndash;%&gt;--%>
<%--	        <div class="item__description">--%>
<%--	            <c:if test="${not empty product.summary}">--%>
<%--					${ycommerce:sanitizeHTML(product.summary)}--%>
<%--	            </c:if>--%>
<%--	            <product:productListerClassifications product="${product}"/>--%>
<%--	        </div>--%>

<%--	        &lt;%&ndash; quantity &ndash;%&gt;--%>
<%--	        <div class="item__quantity">--%>
<%--	        	<c:choose>--%>
<%--	        		<c:when test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">--%>
<%--		            	<spring:theme code="product.grid.outOfStock" />--%>
<%--		        	</c:when>--%>
<%--		        	<c:otherwise>--%>
<%--			            <label class="visible-xs visible-sm"><spring:theme code="basket.page.qty"/>:</label>--%>
<%--			            <span data-variant-price="${fn:escapeXml(product.price.value)}" id="productPrice[${fn:escapeXml(sessionScope.skuIndex)}]"--%>
<%--			                    class="price hidden">${fn:escapeXml(product.price.value)}</span>--%>
<%--			            <input type=hidden id="productPrice[${fn:escapeXml(sessionScope.skuIndex)}]"--%>
<%--			                    value="${fn:escapeXml(product.price.value)}"/>--%>
<%--			            <input type="hidden" class="${fn:escapeXml(product.code)} sku"--%>
<%--			                    name="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].sku"--%>
<%--			                    id="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].sku" value="${fn:escapeXml(product.code)}"/>--%>

<%--			            <c:set value='{"product":"${ycommerce:encodeJSON(product.code)}"}' var="productSelectionJson"/>--%>
<%--			            <input type="text" maxlength="3" size="1"--%>
<%--			                    id="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"--%>
<%--			                    name="cartEntries[${fn:escapeXml(sessionScope.skuIndex)}].quantity"--%>
<%--			                    data-product-selection="${fn:escapeXml(productSelectionJson)}"--%>
<%--			                    class="sku-quantity form-control" value="0">--%>
<%--			            <c:set var="skuIndex" scope="session" value="${fn:escapeXml(sessionScope.skuIndex + 1)}"/>--%>
<%--					</c:otherwise>--%>
<%--	           	</c:choose>--%>
<%--	        </div>--%>

<%--	        &lt;%&ndash; total &ndash;%&gt;--%>
<%--	        <div class="item__total">--%>
<%--				<span class="left js-total-price" id="total-price"><spring:theme code="order.form.currency"/>0.00</span>--%>
<%--				<input type="hidden" id="total-price-value" class="js-total-price-value" value="0">--%>
<%--	        </div>--%>
<%--	    </ycommerce:testId>--%>
<%--	</li>--%>
<%--</c:if>--%>
