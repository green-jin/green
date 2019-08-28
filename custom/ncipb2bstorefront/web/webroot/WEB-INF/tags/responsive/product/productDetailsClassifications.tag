<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<spring:url value="${product.url}/downloadFile" var="downloadFileUrl"/>--%>
<c:url value="${product.url}/downloadFile" var="downloadFileUrl"/>
<script>
	function PostData() {
		$.ajax({
			type: "POST",
			url: "post.go",
			data : "",
			success: function(msg) {
			}
		});
		return false;
	}
</script>



<div class="product-classifications">
	<form:form method="post" action="${downloadFileUrl}">
		<c:forEach items="${galleryFiles}" var="fileData" varStatus="dataStatus">
			<br/>
<%--			<button type="submit">Download</button>--%>
			<input class="btn-normal" type="submit"
				   value="download"/>
			<a href=${fileData.url}>${fileData.altText}</a>
		</c:forEach>
	</form:form>

	<c:if test="${not empty product.classifications}">
		<c:forEach items="${product.classifications}" var="classification">
			<div class="headline">${fn:escapeXml(classification.name)}</div>
				<table class="table">
					<tbody>
						<c:forEach items="${classification.features}" var="feature">
							<tr>
								<td class="attrib">${fn:escapeXml(feature.name)}</td>
								<td>
									<c:forEach items="${feature.featureValues}" var="value" varStatus="status">
										${fn:escapeXml(value.value)}
										<c:choose>
											<c:when test="${feature.range}">
												${not status.last ? '-' : fn:escapeXml(feature.featureUnit.symbol)}
											</c:when>
											<c:otherwise>
												${fn:escapeXml(feature.featureUnit.symbol)}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</c:forEach>
	</c:if>
</div>