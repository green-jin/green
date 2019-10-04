<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="b2b-multi-checkout" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/checkout/multi/summary/placeOrder" var="placeOrderUrl" htmlEscape="false"/>
<spring:url value="/checkout/multi/termsAndConditions" var="getTermsAndConditionsUrl" htmlEscape="false"/>


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

<div class="col-lg-10 container-fluid">
	<!-- Step5: addon checkout/multi/summary/view -->
<!-- 	Step5: checkout/multi/summary/view addon checkoutSummaryPage.jsp -->
	
	<div class="row pb-3">
	    <div class="col-sm-6">
	        <div class="checkout-headline">
	            <i class="fa fa-lock pr-2"></i>
	            <spring:theme code="checkout.multi.secure.checkout"></spring:theme>
	        </div>
			
			<div id="accordion">
<!-- 				<div class="card"> -->
					<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			            <ycommerce:testId code="checkoutStepFour">
			                <div class="checkout-review hidden-xs">
			                    <div class="checkout-order-summary">
			                        <multi-checkout:orderTotals cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" subtotalsCssClasses="dark"/>
			                    </div>
			                </div>
			                <div class="place-order-form hidden-xs">
			                    <form:form action="${placeOrderUrl}" id="placeOrderForm1" commandName="placeOrderForm">
			                        <div class="checkbox">
			                            <label> <form:checkbox id="Terms1" path="termsCheck" />
			                                <spring:theme var="termsAndConditionsHtml" code="checkout.summary.placeOrder.readTermsAndConditions" arguments="${fn:escapeXml(getTermsAndConditionsUrl)}" htmlEscape="false"/>
			                                ${ycommerce:sanitizeHTML(termsAndConditionsHtml)}
			                            </label>
			                        </div>
			
			                        <button id="placeOrder" type="submit" class="btn btn-primary btn-block btn-place-order btn-block btn-lg checkoutSummaryButton" disabled="disabled">
			                            <spring:theme code="checkout.summary.placeOrder"/>
			                        </button>
			                        
			                        <c:if test="${cartData.quoteData eq null}">
				                        <button id="scheduleReplenishment" type="button" class="btn btn-secondary btn-default btn-block disabled scheduleReplenishmentButton checkoutSummaryButton" disabled="disabled">
				                            <spring:theme code="checkout.summary.schedule.replenishment"/>
				                        </button>
				
				                        <b2b-multi-checkout:replenishmentScheduleForm/>
			                        </c:if>
			                    </form:form>
			                </div>  
			            </ycommerce:testId>
			        </multi-checkout:checkoutSteps>
<!-- 				 </div> -->
			 </div>	 
	    </div>
	
	    <div class="col-sm-6">
	        <b2b-multi-checkout:checkoutOrderSummary cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="true" showTaxEstimate="true" showTax="true" />
	    </div>
	
		<!--     <div class="col-sm-12 col-lg-12">  -->
		<!--         cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help" -->
		<%--             cms:component component="${feature}"/ --%>
		<!--         /cms:pageSlot -->
		<!--     </div> -->
	</div>
</div> 
</template:page>