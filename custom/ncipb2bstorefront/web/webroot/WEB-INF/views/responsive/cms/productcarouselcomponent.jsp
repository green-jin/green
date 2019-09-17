<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<!-- Section3Slot-Homepage : ncipb2bHomepageNewProductCarouselComponent -->
<!-- homePage -->
<div class="col-lg-10 container-fluid">
	<c:choose>
		<c:when test="${not empty productData}"> 
			<h1 class="my-5">${fn:escapeXml(title)}</h1>
			
			<c:choose>
				<c:when test="${component.popup}">
					<div class="row">
						<c:forEach items="${productData}" var="product"> 
							<c:url value="${product.url}/quickView" var="productQuickViewUrl"/>
							<div class="col-lg-2 col-md-6 mb-2">
								<a href="${productQuickViewUrl}">
									<product:productPrimaryReferenceImage product="${product}" format="product"/>
								</a>
								${fn:escapeXml(product.name)}<br>
								<format:fromPrice priceData="${product.price}"/>
							</div>
						</c:forEach> 
					</div>	
				</c:when>
				<c:otherwise> 
					<div class="row">
						<c:forEach items="${productData}" var="product"> 
							<div class="col-lg-2 col-md-6 mb-2">
								<c:url value="${product.url}" var="productUrl"/> 
								<a href="${productUrl}"> 
									<product:productPrimaryImage product="${product}" format="product"/>  
								</a>
								<p class="text-center h5">
									${fn:escapeXml(product.name)} <br>
									<format:fromPrice priceData="${product.price}"/> 
								</p> 
							</div> 
						</c:forEach>
					</div> 
				</c:otherwise>
			</c:choose> 
		</c:when>
	
		<c:otherwise>
			<component:emptyComponent/>
		</c:otherwise>
	</c:choose>
</div>
