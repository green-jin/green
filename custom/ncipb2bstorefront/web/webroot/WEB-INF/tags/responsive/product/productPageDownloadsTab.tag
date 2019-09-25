<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:url value="${product.url}/downloadFile" var="downloadFileUrl"/>

<spring:htmlEscape defaultHtmlEscape="true"/>

<%--<c:url value="${product.url}/downloads" var="productDownloadsActionUrl"/>--%>

    <form:form method="post" action="${downloadFileUrl}">
        <table class="table">
            <thead class="thead-dark text-left">
            <tr>
                <th>#</th>
                <th><spring:theme code="downloads.item"/></th>
                <th><spring:theme code="downloads.downloads"/></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${galleryFiles}" var="fileData" varStatus="dataStatus">
                <tr>
                    <th scope="row" class="bg-light">${dataStatus.index}</th>
                    <td>
                        <c:choose>
                            <c:when test="${fileData.fileItems == 'I'}">
                                <spring:theme code="downloads.InstallSpec"/>
                            </c:when>
                            <c:when test="${fileData.fileItems == 'S'}">
                                <spring:theme code="downloads.DetailSpec"/>
                            </c:when>
                            <c:when test="${fileData.fileItems == 'D'}">
                                <spring:theme code="downloads.dwg"/>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fileData.format == 'pdf'}">
                                <i class="fa fa-file-pdf-o mr-3">&nbsp;</i>
                            </c:when>
                            <c:when test="${fileData.format == 'dwg'}">
                                <i class="fa fa-download mr-3">&nbsp;</i>
                            </c:when>
                        </c:choose>
                        <a href="${fileData.url}" download="${fileData.altText}"><spring:theme code="downloads.downloadfile"/></a>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </form:form>