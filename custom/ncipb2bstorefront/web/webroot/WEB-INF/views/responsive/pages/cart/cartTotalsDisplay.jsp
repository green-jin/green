<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<%-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue  --%>


<!-- <div class="col-lg-10 container-fluid"> -->
	<div class="row">
		
	      <div class="col-lg-3 mb-2">
				<cart:cartVoucher cartData="${cartData}"/>
		  </div>
			
		  <div class="col-lg-5 mb-2"></div>	
			
	      <div class="col-lg-4 mb-2">
			  <cart:cartTotals cartData="${cartData}" showTax="false"/>
	          <cart:ajaxCartTotals/>
			  
	      </div>
	</div>      
<!-- <div>   -->
<!-- <div class="row"> -->
<!--     <div class="col-xs-12 col-md-5 col-lg-6"> -->
<!--         <div class="cart-voucher"> -->
<%--             <cart:cartVoucher cartData="${cartData}"/> --%>
<!--         </div> -->
<!--     </div> -->
<!--     <div class="col-xs-12 col-md-7 col-lg-6"> -->
<!--         <div class="cart-totals"> -->
<%--             <cart:cartTotals cartData="${cartData}" showTax="false"/> --%>
<%--             <cart:ajaxCartTotals/> --%>
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->
   