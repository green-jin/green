<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${component.size == 4}">
	
		<%-- 		<component:brandcontainer_4/> --%>
		
		<c:if test="${component.mainTitleTextHidden == false}">
			<h1 class="my-5">${component.maintTitleText}</h1>
		</c:if> 
		
		<div class="row">
			<c:forEach items="${component.simpleBannerComponent}" var="objects">
				<div class="${component.name eq 'HomeDealerAreaBrandContainer'? 'col-lg-3 mb-2' : 'col-lg-3 col-sm-6 mb-5'}">
					<c:if test="${objects.subTitleTextHidden == false }">
						<h4>${objects.subTitleText}</h4>
					</c:if>
					<div class="pic"> 
						<c:forEach items="${objects.media.medias}" var="mediasImages" varStatus="number"> 
							<c:if test="${number.count == 1}">
								<a href="${objects.urlLink}"><img src="${mediasImages.URL}" alt="${mediasImages.code}" title="${mediasImages.code}"  width="${component.width}"  height="${component.height}"></a><br/>
							</c:if> 
						</c:forEach> 
					</div>
				</div>
			</c:forEach>
		</div>
		
		<c:if test="${component.name eq 'HomeDealerAreaBrandContainer'}">  
			<!-- HomeDealerAreaBrandContainer add hr tag  -->
			<hr>
		</c:if>
	</c:when>
	
	<c:when test="${component.size == 5}">
		<%-- 		<component:brandcontainer_5/> --%>
	</c:when>
</c:choose>
