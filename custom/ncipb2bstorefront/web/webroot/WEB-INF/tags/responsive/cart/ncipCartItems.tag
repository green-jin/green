<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="errorStatus" value="<%= de.hybris.platform.catalog.enums.ProductInfoStatus.valueOf(\"ERROR\") %>" />

<table class="table">
  <thead class="thead-dark text-center">
    <tr>
	  <th></th>
	  <th><spring:theme code="basket.page.product"/></th>
      <th><spring:theme code="basket.page.productID"/></th>
      <th><spring:theme code="basket.page.unitprice"/></th>
      <th><spring:theme code="basket.page.qty"/></th>
      <th><spring:theme code="basket.page.status"/></th>
      <th><spring:theme code="basket.page.total"/></th>
      <th><spring:theme code="basket.page.delete"/></th>
	  </tr>
  </thead>
	
  <tbody>
	
	<c:forEach items="${cartData.rootGroups}" var="group" varStatus="loop">
    	<cart:ncipRootEntryGroup cartData="${cartData}" entryGroup="${group}"/>
        <p></p>
    </c:forEach>
  </tbody>
</table>

<product:productOrderFormJQueryTemplates />
<storepickup:pickupStorePopup />
