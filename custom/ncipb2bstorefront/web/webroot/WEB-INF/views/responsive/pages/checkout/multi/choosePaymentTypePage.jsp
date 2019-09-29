<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="b2b-multi-checkout" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/checkout/multi" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

<div class="col-lg-10 container-fluid">
	<!-- Step1: checkout/multi/payment-type/choose -->
<!-- 	Step1: checkout/multi/payment-type/choose choosePaymentTypePage.jsp -->
	
	<div class="row pb-3">
	    <div class="col-md-6">
	        <div class="checkout-headline">
	        	 <h3 class="p-3">
	        	 	<i class="fa fa-lock pr-2"></i>
	        	 	<spring:theme code="checkout.multi.secure.checkout"></spring:theme>
	        	 </h3>
	        </div>
			
			<div id="accordion">
<!-- 				<div class="card"> -->
					<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			            <jsp:body>
			                <b2b-multi-checkout:paymentTypeForm/>
			            </jsp:body>
		        	</multi-checkout:checkoutSteps>
<!-- 				</div>	 -->
			</div> 
	    </div>
	    
	    <div class="col-md-6"> 
	        <multi-checkout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="false" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
	    </div>
	
	<!--     <div class="col-sm-12 col-lg-12">  -->
	<!--         cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help" -->
	<%--             cms:component component="${feature}"/ --%>
	<!--         /cms:pageSlot -->
	<!--     </div> -->
	    
	</div>
</div>
</template:page>
