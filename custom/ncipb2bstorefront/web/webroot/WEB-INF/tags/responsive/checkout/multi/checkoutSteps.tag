<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="checkoutSteps" required="true" type="java.util.List" %>
<%@ attribute name="progressBarId" required="true" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<ycommerce:testId code="checkoutSteps">
${fn:escapeXml(cssClass)} 
     <div class="checkout-steps ${fn:escapeXml(cssClass)}">
        <c:forEach items="${checkoutSteps}" var="checkoutStep" varStatus="status">
            <c:url value="${checkoutStep.url}" var="stepUrl"/>
            <c:choose>
                <c:when test="${progressBarId eq checkoutStep.progressBarId}">
                    <c:set scope="page"  var="activeCheckoutStepNumber"  value="${checkoutStep.stepNumber}"/>
                    <a href="${fn:escapeXml(stepUrl)}" class="step-head js-checkout-step active">
                        <h5>
                        	<c:choose>
								<c:when test="${checkoutStep.progressBarId eq 'deliveryAddress.noPickup'}">   
									<div class="title"><spring:theme code="checkout.multi.deliveryAddress.shippingAddress"/></div>
								</c:when> 
								<c:otherwise>   
									<div class="title">
										<c:choose> 
										  <c:when test="${checkoutStep.progressBarId eq 'paymentType'}">
										     <spring:theme code="checkout.multi.paymentType.title"/>
										  </c:when>
										  <c:otherwise> 
										  	<spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>
										  </c:otherwise>
										</c:choose> 
									</div>  
								</c:otherwise>
							</c:choose> 
                        	
                        </h5>
                    </a>
                    <div class="step-body"><jsp:doBody/></div>
                </c:when>
                <c:when test="${checkoutStep.stepNumber > activeCheckoutStepNumber}">
                    <a href="${fn:escapeXml(stepUrl)}" class="step-head js-checkout-step ">
                    	<h5>
                        	<c:choose>
								<c:when test="${checkoutStep.progressBarId eq 'deliveryAddress.noPickup'}">   
									<div class="title"><spring:theme code="checkout.multi.deliveryAddress.shippingAddress"/></div>
								</c:when> 
								<c:otherwise>  
									<div class="title"><spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/></div>  
								</c:otherwise>
							</c:choose> 
                        	
                        </h5> 
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${fn:escapeXml(stepUrl)}" class="step-head js-checkout-step "> 
						<c:choose>
							<c:when test="${checkoutStep.progressBarId eq 'paymentType'}">   
								 <div class="title">
		                        	<spring:theme code="checkout.multi.paymentType.title"/>
		                        </div>
							</c:when>  
							<c:when test="${checkoutStep.progressBarId eq 'deliveryAddress.noPickup'}">   
								 <div class="title">
		                        	<spring:theme code="checkout.multi.deliveryAddress.noPickup.title"/>
		                        </div>
							</c:when>  
							<c:otherwise> 
							 	 <div class="title">
		                        	<spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>
		                        </div>
							</c:otherwise>  
						</c:choose>  
                        <div class="edit">
                            <span class="text-primary fa fa-pencil fa-2x"></span>
                        </div>
                    </a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</ycommerce:testId>