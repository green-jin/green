<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<!-- homePage -->
 <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
   <ol class="carousel-indicators">
     <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
     <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
   </ol>

   <div class="carousel-inner" role="listbox">  
     <c:forEach items="${banners}" var="banner" varStatus="status">
     	  
      <c:choose>
    <c:when test="${status.index =='0'}">
		<c:url value="${banner.urlLink}" var="encodedUrl" /> 
	  	<div class="carousel-item active"> 
	  		<img src="${fn:escapeXml(banner.media.url)}" 
				 alt="${not empty banner.headline ? fn:escapeXml(banner.headline) : fn:escapeXml(banner.media.altText)}" 
				 title="${not empty banner.headline ? fn:escapeXml(banner.headline) : fn:escapeXml(banner.media.altText)}"/>
	  	</div> 
    </c:when>    
    <c:otherwise>
		<c:url value="${banner.urlLink}" var="encodedUrl" /> 
	  	<div class="carousel-item"> 
	  		<img src="${fn:escapeXml(banner.media.url)}" 
				 alt="${not empty banner.headline ? fn:escapeXml(banner.headline) : fn:escapeXml(banner.media.altText)}" 
				 title="${not empty banner.headline ? fn:escapeXml(banner.headline) : fn:escapeXml(banner.media.altText)}"/>
	  	</div> 
    </c:otherwise>
</c:choose> 
</c:forEach> 
   </div>
   
   <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
     <span class="carousel-control-prev-icon" aria-hidden="true"></span>
     <span class="sr-only">Previous</span>
   </a>
   
   <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
     <span class="carousel-control-next-icon" aria-hidden="true"></span>
     <span class="sr-only">Next</span>
   </a>
 </div>
