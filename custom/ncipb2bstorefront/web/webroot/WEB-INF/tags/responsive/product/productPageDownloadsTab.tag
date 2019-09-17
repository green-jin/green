<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core%22%25  >
<%@ taglib prefix="formElement"
           tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags%22%25  >
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form%22%25  >
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions%22%25  >
<c:url value="${product.url}/downloadFile" var="downloadFileUrl"/>

<spring:htmlEscape defaultHtmlEscape="true" />

<%-- <c:url value="${product.url}/downloads" var="productDownloadsActionUrl" /> --%>


<div style="text-align: center">

    <!--  <div class="glyphicon glyphicon-download-alt"></div> -->
    <!--  <div class="fa fa-file-pdf"></div> -->


    <%--  <form:form method="post" action="${downloadFileUrl}" --%>
    <%--   commandName="reviewForm"> --%>

    <form:form method="post" action="${downloadFileUrl}" >
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th>#</th>
                <th><spring:theme code="downloads.item" /></th>
                <th><spring:theme code="downloads.downloads" /></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${galleryFiles}" var= "fileData" varStatus="dataStatus">
            <tr>

                <td>1</td>
                <td>
                        <%--       <c:choose> --%>
                        <%--         <c:when test="${fileData.fileItems == 'I'}"> --%>
                        <%--          <spring:theme code="downloads.InstallSpec" /> --%>
                        <%--         </c:when> --%>
                        <%--         <c:when test="${fileData.fileItems == 'S'}"> --%>
                        <%--          <spring:theme code="downloads.DetailSpec" /> --%>
                        <%--         </c:when> --%>
                        <%--         <c:when test="${fileData.fileItems == 'D'}"> --%>
                        <%--          <spring:theme code="downloads.dwg"/> --%>
                        <%--         </c:when> --%>
                        <%--       </c:choose> --%>
                </td>
                <td>
                        <%--       <c:choose> --%>
                        <%--        <c:when test="${fileData.format == '1'}"> --%>
                    <!--             <i class="fa fa-file-pdf-o">&nbsp;</i> -->
                        <%--           </c:when> --%>
                        <%--           <c:when test="${fileData.format == '2'}"> --%>
                    <!--             <i class="fa fa-file-pdf-o">&nbsp;</i> -->
                        <%--           </c:when> --%>
                        <%--           <c:when test="${fileData.format == '3'}"> --%>
                    <!--             <i class="fa fa-file-pdf-o">&nbsp;</i> -->
                        <%--           </c:when> --%>
                        <%--       </c:choose> --%>
                    <a href=${fileData.url} download="${fileData.altText}" ><spring:theme code="downloads.downloadfile"  /></a>
                </td>
            </tr>
            </c:forEach>



            <!--     <tr> -->
            <!--      <td>2</td> -->
                <%--      <td><spring:theme code="downloads.DetailSpec" /></td> --%>
                <%--      <td><i class="fa fa-file-pdf-o">&nbsp;</i><a href="#"><spring:theme --%>
                <%--         code="downloads.downloadfile" /></a></td> --%>
            <!--     </tr> -->
            <!--     <tr> -->
            <!--      <td>3</td> -->
                <%--      <td><spring:theme code="downloads.dwg"/></td> --%>
                <%--      <td><i class="fa fa-download">&nbsp;</i><a href=${fileData.url}><spring:theme --%>
                <%--         code="downloads.downloadfile" /></a></td> --%>
            <!--      </div> -->
            <!--     </tr> -->
            <!--    </tbody> -->
        </table>
    </form:form>
</div>