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

    <c:forEach items="${galleryVideos}" var="videoData" varStatus="dataStatus">
        <video width="850" height="480" autoplay="autoplay" controls="autoplay">
                <%--		<source src="https://powertools.local:9002/medias/W3580601-TW.mp4?context=bWFzdGVyfHZpZGVvfDgzNDM3N3x2aWRlby9tcDR8dmlkZW8vaDNmL2g2YS84Nzk2NTk2MzcxNDg2LmJpbnw5YjIzMGFkYTQ1MjYxYjI2ZWUxNjUwZjBlMTMxZGRhYTlhNzcwZjAzZThlODM0ZDM0NDk2MDU5OTU4MTRiZDky" type="video/mp4">--%>
            <source src= ${videoData.url}
                            type="video/mp4">
        </video>
    </c:forEach>

</div>
