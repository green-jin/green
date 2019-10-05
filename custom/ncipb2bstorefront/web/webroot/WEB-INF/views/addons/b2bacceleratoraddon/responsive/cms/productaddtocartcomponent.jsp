<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<!-- add on productaddtocartcomponent.jsp  -->

<c:set var="isForceInStock"
       value="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}"/>

<c:choose>
    <c:when test="${isForceInStock}">
        <c:set var="maxQty" value="FORCE_IN_STOCK"/>
    </c:when>
    <c:otherwise>
        <c:set var="maxQty" value="${product.stock.stockLevel}"/>
    </c:otherwise>
</c:choose>
<c:set var="qtyMinus" value="1"/>

<!-- 訂製品預購品 -->
<c:if test="${product.ma_type eq 'B' || product.ma_type eq 'D'}">

    <span class="text-danger"><spring:theme code="product.product.produceMsg"/></span>

</c:if>

<%--庫存不足，下單後30天(1.2階段)--%>
<input type="hidden" id="stockAmount" value="${product.stock.stockAmount}" ></input>

<%--<span class="text-danger"><spring:theme code="product.product.produceMsg1" arguments="${product.plifz}" var="stockMsg"/></span>--%>

<input type="hidden" id="stockMessage" value="${stockMsg}" ></input>


<div class="row align-item-center my-3 addtocart-component">
    <%--控制加到購物車的數量--%>
<%--    <div >--%>

        <c:if test="${empty showAddToCart ? true : showAddToCart}">

            <div class="col-4">
                <div class="btn-group js-qty-selector " role="group" aria-label="First group">
                    <button class="btn btn-secondary js-qty-selector-minus" onclick="ShowValue()"
                            type="button" <c:if test="${qtyMinus <= 1}"><c:out
                            value="disabled='disabled'"/></c:if> >-
                        </button>

<%--                 <button type="button" class="btn btn-outline-dark disabled">10</button>--%>

                    <input type="text" maxlength="3"
                           class="js-qty-selector-input" size="1"
                           value="${fn:escapeXml(qtyMinus)}"
                           data-max="${fn:escapeXml(maxQty)}" data-min="1" name="pdpAddtoCartInput"
                           id="pdpAddtoCartInput" onchange="ShowValue()"/>

                    <input type="hidden" id="stocklevel"
                           value="${product.stock.stockLevel}"></input>

                    <button class="btn btn-secondary js-qty-selector-plus" onclick="ShowValue()"
                            type="button">+
                    </button>
                </div>
            </div>
        </c:if>
        <c:if test="${product.stock.stockLevel gt 0}">
            <c:set var="productStockLevelHtml">${fn:escapeXml(product.stock.stockLevel)}&nbsp;
                <spring:theme code="product.variants.in.stock"/>
            </c:set>
        </c:if>
        <c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">
            <c:set var="productStockLevelHtml">
                <spring:theme code="product.variants.only.left"
                              arguments="${product.stock.stockLevel}"/>
            </c:set>
        </c:if>
        <c:if test="${isForceInStock}">
            <c:set var="productStockLevelHtml">
                <spring:theme code="product.variants.available"/>
            </c:set>
        </c:if>

        <%--	線上有貨--%>
<%--            <div class="stock-wrapper clearfix">--%>
<%--                ${productStockLevelHtml}--%>
<%--            </div>--%>

        <%--	  加到購物車--%><br/>
        <div class="col-8">
            			<c:if test="${multiDimensionalProduct}">
            				<c:url value="${product.url}/orderForm" var="productOrderFormUrl"/>
            				<a href="${productOrderFormUrl}"
            				   class=js-add-to-cart">
            					<spring:theme code="order.form"/>
            				</a>
            			</c:if>
			<action:actions element="div" parentComponent="${component}"/>
        </div>
</div>

<%--Lancy test--%>

<%--<c:set var="isForceInStock" value="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}"/>--%>

<%--<c:choose>--%>
<%--  <c:when test="${isForceInStock}">--%>
<%--    <c:set var="maxQty" value="FORCE_IN_STOCK"/>--%>
<%--  </c:when>--%>
<%--  <c:otherwise>--%>
<%--    <c:set var="maxQty" value="${product.stock.stockLevel}"/>--%>
<%--  </c:otherwise>--%>
<%--</c:choose>--%>
<%--<c:set var="qtyMinus" value="1" />--%>

<%--<!-- 訂製品預購品 -->--%>
<%--<c:if test="${product.ma_type eq 'B' || product.ma_type eq 'D'}">--%>

<%--	<spring:theme code="product.product.produceMsg"/>--%>

<%--</c:if>--%>

<%--<input type="hidden" id="stockAmount" value="${product.stock.stockAmount}" ></input>--%>

<%--<spring:theme code="product.product.produceMsg1" arguments="${product.plifz}" var="stockMsg"/>--%>

<%--<input type="hidden" id="stockMessage" value="${stockMsg}" ></input>--%>


<%--<div class="addtocart-component">--%>
<%--		<c:if test="${empty showAddToCart ? true : showAddToCart}">--%>
<%--		<div class="qty-selector input-group js-qty-selector">--%>
<%--			<span class="input-group-btn">--%>
<%--				<button class="btn btn-default js-qty-selector-minus" onclick="ShowValue()" type="button" <c:if test="${qtyMinus <= 1}"><c:out value="disabled='disabled'"/></c:if> ><span class="glyphicon glyphicon-minus" aria-hidden="true" ></span></button>--%>
<%--			</span>--%>
<%--				<input type="text" maxlength="3" class="form-control js-qty-selector-input" size="1" value="${fn:escapeXml(qtyMinus)}"--%>
<%--					   data-max="${fn:escapeXml(maxQty)}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput"  onchange="ShowValue()" />--%>

<%--			    <input type="hidden" id="stocklevel" value="${product.stock.stockLevel}" ></input>--%>

<%--			<span class="input-group-btn">--%>
<%--				<button class="btn btn-default js-qty-selector-plus" onclick="ShowValue()" type="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>--%>
<%--			</span>--%>
<%--		</div>--%>
<%--		</c:if>--%>
<%--		<c:if test="${product.stock.stockLevel gt 0}">--%>
<%--			<c:set var="productStockLevelHtml">${fn:escapeXml(product.stock.stockLevel)}&nbsp;--%>
<%--				<spring:theme code="product.variants.in.stock"/>--%>
<%--			</c:set>--%>
<%--		</c:if>--%>
<%--		<c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">--%>
<%--			<c:set var="productStockLevelHtml">--%>
<%--				<spring:theme code="product.variants.only.left" arguments="${product.stock.stockLevel}"/>--%>
<%--			</c:set>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isForceInStock}">--%>
<%--			<c:set var="productStockLevelHtml">--%>
<%--				<spring:theme code="product.variants.available"/>--%>
<%--			</c:set>--%>
<%--		</c:if>--%>
<%--		<div class="stock-wrapper clearfix">--%>
<%--			${productStockLevelHtml}--%>
<%--		</div>--%>
<%--		 <div class="actions">--%>
<%--        <c:if test="${multiDimensionalProduct}" >--%>
<%--                <c:url value="${product.url}/orderForm" var="productOrderFormUrl"/>--%>
<%--                <a href="${productOrderFormUrl}" class="btn btn-default btn-block btn-icon js-add-to-cart glyphicon-list-alt">--%>
<%--                    <spring:theme code="order.form" />--%>
<%--                </a>--%>
<%--        </c:if>--%>
<%--        <action:actions element="div"  parentComponent="${component}"/>--%>
<%--    </div>--%>
<%--</div>--%>


<%--<script type="text/javascript">--%>
<%--  function ShowValue() {--%>
<%--    alert( document.getElementById("pdpAddtoCartInput").value);--%>

<%--    var purchase = document.getElementById("pdpAddtoCartInput").value;--%>
<%--    // var stockAmount = document.getElementById("stockAmount").value;--%>
<%--    var stockMsg = document.getElementById("stockMessage").value;--%>

<%--    var purchaseAmt = parseInt(purchase, 10);--%>

<%--    if (purchaseAmt > stockAmount) {--%>
<%--      alert(stockMsg);--%>
<%--    }--%>
<%--  }--%>

<%--</script>--%>


