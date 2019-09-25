<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="work" class="tab-pane fade  p-4" role="tabpanel" aria-labelledby="nav-work-tab">
    <h4><spring:theme code="product.detail.tab.delivery"/></h4>
    <product:productPageDeliveryTab product="${product}"/>
</div>

