<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--<c:url value="${product.url}/downloadFile" var="downloadFileUrl"/>--%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="${product.url}/downloads" var="productDownloadsActionUrl" />


<div style="text-align: center">

	<!-- 	<div class="glyphicon glyphicon-download-alt"></div> -->
	<!-- 	<div class="fa fa-file-pdf"></div> -->


	<form:form method="post" action="${productDownloadsActionUrl}"
		commandName="reviewForm">

		<table class="table">
			<thead class="thead-dark">
				<tr>
					<th>#</th>
					<th><spring:theme code="downloads.item" /></th>
					<th><spring:theme code="downloads.downloads" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<c:forEach items="${galleryFiles}" var= "fileData" varStatus="dataStatus">
					<td>1</td>
					<td><spring:theme code="downloads.InstallSpec" /></td>
					<td><i class="fa fa-file-pdf-o">&nbsp;</i>
						<a href=${fileData.url} download="${fileData.altText}" ><spring:theme code="downloads.downloadfile"  /></a></td>
					</c:forEach>
				</tr>
				<tr>
					<td>2</td>
					<td><spring:theme code="downloads.DetailSpec" /></td>
					<td><i class="fa fa-file-pdf-o">&nbsp;</i><a href="#"><spring:theme
								code="downloads.downloadfile" /></a></td>
				</tr>
				<tr>
					<td>3</td>
					<td><spring:theme code="downloads.dwg"/></td>
					<td><i class="fa fa-download">&nbsp;</i><a href=${fileData.url}><spring:theme
								code="downloads.downloadfile" /></a></td>
					</div>
				</tr>
			</tbody>
		</table>
	</form:form>
</div>
