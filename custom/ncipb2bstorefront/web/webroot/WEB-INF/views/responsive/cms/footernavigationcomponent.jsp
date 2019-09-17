<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 
<!-- homePage Footer start -->
<footer class="py-5 bg-dark">
	<div class="col-lg-10 container-fluid">
		<div class="row">
			<c:forEach items="${component.navigationNode.children}"
				var="childLevel1">
				<c:forEach items="${childLevel1.children}"
					step="${component.wrapAfter}" varStatus="i">
					<div class="col-md-3 mb-4">
						<c:if test="${component.wrapAfter > i.index}">
							<h5 class="text-left text-white border-bottom">${fn:escapeXml(childLevel1.title)}</h5>
						</c:if>
						<c:forEach items="${childLevel1.children}" var="childLevel2"
							begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
							<c:forEach items="${childLevel2.entries}" var="childlink">
								<c:choose>
									<c:when test="${fn:contains(childlink.item.name, 'Foot')}">
										<cms:component component="${childlink.item}"
											evaluateRestriction="true" class="text-white px-3" /> &nbsp;&nbsp;
									   </c:when>
									<c:otherwise>
										<small> <cms:component component="${childlink.item}"
												evaluateRestriction="true" class="text-white" />
												<br>
										</small>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>
					</div>
				</c:forEach>
			</c:forEach>
		</div>
		<div class="row">
			<div class="col-md-3 mb-4 d-flex justify-content-center justify-content-md-start text-white">
				<small>${fn:escapeXml(notice)}</small>
			</div>
			<div class="col-md-3"></div>
			<div class="col-md-3 mb-4"></div>

			<div class="col-md-3 d-flex justify-content-center justify-content-md-end">
				<small>  
					<a class="text-white" href="${component.serviceStatementUrlLike}">${component.serviceStatement}</a>
				</small>
			</div>
		</div>
	</div>
</footer>
<!-- Footer end -->