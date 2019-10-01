<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/responsive/quote" %>

<spring:htmlEscape defaultHtmlEscape="true" />

    
<div class="js-cart-totals">
    <div class="row">
<%--     <div class="col-xs-6 cart-totals-left"><spring:theme code="basket.page.totals.subtotal"/></div> --%>
    <div class="col-lg-6 col-sm-6 text-left"><spring:theme code="basket.page.totals.subtotal"/></div>
<%--     <div class="col-xs-6 cart-totals-right text-right"><ycommerce:testId code="Order_Totals_Subtotal"><format:price priceData="${cartData.subTotal}"/></ycommerce:testId></div> --%>
	<div class=" col-lg-6 col-sm-6 text-right"><ycommerce:testId code="Order_Totals_Subtotal"><format:price priceData="${cartData.subTotal}"/></ycommerce:testId></div>

    <c:if test="${not empty cartData.deliveryCost}">
        <div class=" col-lg-6 col-sm-6 text-left"><spring:theme code="basket.page.totals.delivery"/></div>
        <div class=" col-lg-6 col-sm-6 text-right"><format:price priceData="${cartData.deliveryCost}" displayFreeForZero="TRUE"/></div>
     </c:if>


    <c:if test="${cartData.net && cartData.totalTax.value > 0 && showTax}">
        <div class=" col-lg-6 col-sm-6 text-left"><spring:theme code="basket.page.totals.netTax"/></div>
        <div class=" col-lg-6 col-sm-6 text-right"><format:price priceData="${cartData.totalTax}"/></div>
    </c:if>
    
	<c:if test="${not empty cartData.quoteData}">
		<quote:quoteDiscounts cartData="${cartData}"/>
	</c:if>
	
	<c:if test="${cartData.quoteDiscounts.value > 0}">
		<div class="col-xs-6 cart-totals-left discount">
			<spring:theme code="basket.page.quote.discounts" />
		</div>
		<div class="col-xs-6 cart-totals-right text-right discount">
			<ycommerce:testId code="Quote_Totals_Savings">
				<format:price priceData="${cartData.quoteDiscounts}" displayNegationForDiscount="true" />
			</ycommerce:testId>
		</div>
	</c:if>
	</div>
	
	<div class="row pb-3">
	<c:if test="${cartData.totalDiscounts.value > 0}">
<!-- 		<div class="col-xs-6 cart-totals-left discount"> -->
		<div class="col-lg-6 col-sm-6 text-left text-success"><spring:theme code="basket.page.totals.discounts"/></div>
		<div class="col-lg-6 col-sm-6 text-right text-success">
		    <ycommerce:testId code="Order_Totals_Savings">
				<format:price priceData="${cartData.totalDiscounts}" displayNegationForDiscount="true" />
			</ycommerce:testId>
		</div>	
<!-- 		</div> -->
		<div class="col-xs-6 cart-totals-right text-right discount">
			
		</div>
	</c:if>
	</div>
	<hr>
	<div class="row">
    <div class="col-lg-6 col-sm-6 text-left grand-total"><h4><spring:theme code="basket.page.totals.total"/></h4></div>
    <div class="col-lg-6 col-sm-6 text-right grand-total">
        <ycommerce:testId code="cart_totalPrice_label">
            <c:choose>
                <c:when test="${showTax}">
                    <h4><format:price priceData="${cartData.totalPriceWithTax}"/></h4>
                </c:when>
                <c:otherwise>
                    <h4><format:price priceData="${cartData.totalPrice}"/></h4>
                </c:otherwise>
            </c:choose>
        </ycommerce:testId>
    </div>
	</div>

    <c:if test="${not cartData.net}">
        <div class="cart-totals-taxes text-right">
            <ycommerce:testId code="cart_taxes_label"><spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!"/></ycommerce:testId>
         </div>
    </c:if>


    <c:if test="${cartData.net && not showTax }">
        <div class="cart-totals-taxes text-right">
            <ycommerce:testId code="cart_taxes_label"><spring:theme code="basket.page.totals.noNetTax"/></ycommerce:testId>
        </div>
    </c:if>
</div>



		  

		  
      