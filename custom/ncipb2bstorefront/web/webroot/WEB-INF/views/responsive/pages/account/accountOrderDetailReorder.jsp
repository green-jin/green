<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<!-- my-account/order -->
<!-- ==== accountOrderDetailReorder.jsp start ==== -->
<div class="row p-3">
   <div class="col-md-3"></div>
   <div class="col-md-3"></div>
   <div class="col-md-3 orderBackBtn">
     <spring:url value="/my-account/orders" var="orderHistoryUrl"/> 
     <button type="button" class="btn btn-block btn-secondary" data-back-to-orders="${fn:escapeXml(orderHistoryUrl)}">
         <spring:theme code="text.account.orderDetails.backToOrderHistory"/>
     </button> 
   </div>
   <div class="col-md-3">
       <c:set var="orderCode" value="${orderData.code}" scope="request"/>
       <action:actions element="div" parentComponent="${component}"/>
   </div>
</div>

<!-- <div class="cancel-panel"> -->
<!--     <div class="row"> -->
<!--         <div class="col-xs-12 col-sm-12 col-md-10 col-md-offset-2 col-lg-8 col-lg-offset-4"> -->
<!--             <div class="row"> -->
<!--                 <div class="col-md-3"> -->
<%-- 	                 <spring:url value="/my-account/orders" var="orderHistoryUrl"/> --%>
<!-- 	                 <div class="orderBackBtn"> -->
<%-- 	                     11<button type="button" class="btn btn-block btn-secondary" data-back-to-orders="${fn:escapeXml(orderHistoryUrl)}"> --%>
<%-- 	                         <spring:theme code="text.account.orderDetails.backToOrderHistory"/> --%>
<!-- 	                     </button> -->
<!-- 	                 </div> -->
<!--                 </div> -->
<!--                 <div class="col-md-3"> -->
<%--                    22 <c:set var="orderCode" value="${orderData.code}" scope="request"/> --%>
<%--                     <action:actions element="div" parentComponent="${component}"/> --%>
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->
<!-- ==== accountOrderDetailReorder.jsp end ====  -->