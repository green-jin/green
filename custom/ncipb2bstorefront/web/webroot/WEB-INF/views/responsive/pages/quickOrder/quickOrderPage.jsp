<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="quickorder" tagdir="/WEB-INF/tags/responsive/quickorder" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:theme code="product.grid.confirmQtys.message" var="gridConfirmMessage"/>

<%-- <template:page pageTitle="${pageTitle}">
	<div id="quickOrder" class="account-section" data-grid-confirm-message="${gridConfirmMessage}">
        <div class="account-section-content">
            <div class="quick-order-section-header account-section-header">
                <spring:theme code="text.quickOrder.header" />
            </div>

            <div class="row">
                <div class="col-xs-12 col-md-7 col-lg-6">
                    <div class="quick-order__introduction">
                        <cms:pageSlot position="TopContent" var="feature">
                            <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
                        </cms:pageSlot>
                    </div>
                </div>

                <product:addToCartTitle/>
                <div class="col-xs-12 col-md-5 col-lg-6 pull-rightt">
                    <div class="row quick-order__actions">
                        <div class="pull-right col-sm-3 col-md-6 col-lg-5 quick-order__add-to-cart-btn">
                            <product:productFormAddToCartButton addToCartBtnId="js-add-to-cart-quick-order-btn-top" />
                        </div>
                        <div class="pull-right col-sm-4 col-md-6 col-lg-5 text-right">
                            <quickorder:quickorderResetButton resetBtnId="js-reset-quick-order-form-btn-top" resetBtnClass="quick-order__reset-link"/>
                        </div>
                    </div>
                </div>
            </div>
			
			<quickorder:quickorderListRows/>

            <div class="row">
                <div class="col-xs-12 col-md-5 col-lg-6 pull-right">
                    <div class="row quick-order__actions">
                        <div class="pull-right col-sm-3 col-md-6 col-lg-5 quick-order__add-to-cart-btn">
                            <product:productFormAddToCartButton addToCartBtnId="js-add-to-cart-quick-order-btn-bottom" />
                        </div>
                        <div class="pull-right col-sm-4 col-md-6 col-lg-5 text-right">
                            <quickorder:quickorderResetButton resetBtnId="js-reset-quick-order-form-btn-bottom" resetBtnClass="quick-order__reset-link"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template:page> --%>

<template:page pageTitle="${pageTitle}">
	<div class="col-lg-10 container-fluid">
        	<h4 class="mt-5">
                <spring:theme code="text.quickOrder.header" />
			</h4>
			<hr>
            <div class="row">
                <div class="col-md-8 mb-3">
                	<span class="text-left">
	                    <cms:pageSlot position="TopContent" var="feature">
	                        <cms:component component="${feature}"/>
	                    </cms:pageSlot>
	                </span>
                </div>
                <product:addToCartTitle/>
                <div class="col-md-2 d-flex justify-content-center mb-3">
                    <quickorder:quickorderResetButton resetBtnId="js-reset-quick-order-form-btn-top" resetBtnClass="quick-order__reset-link"/>
                </div>
                <div class="text-right col-md-2 mb-3">
                    <product:productFormAddToCartButton addToCartBtnId="js-add-to-cart-quick-order-btn-top" />
                </div>
            </div>
			
			<quickorder:quickorderListRows/>

            <div class="row mb-3">
            	<div class="col-md-8 mb-3"></div>
                <div class="text-right col-md-2 d-flex justify-content-center mb-3">
                    <quickorder:quickorderResetButton resetBtnId="js-reset-quick-order-form-btn-bottom" resetBtnClass="quick-order__reset-link"/>
                </div>
                <div class="quick-order__add-to-cart-btn col-md-2 mb-3">
                    <product:productFormAddToCartButton addToCartBtnId="js-add-to-cart-quick-order-btn-bottom" />
                </div>
                        
           </div>
       <div id="quickOrder" class="account-section" data-grid-confirm-message="${gridConfirmMessage}"></div>
    </div>
</template:page>