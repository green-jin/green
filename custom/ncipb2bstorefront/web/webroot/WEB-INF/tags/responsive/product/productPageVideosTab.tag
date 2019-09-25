<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="formElement"
           tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:url value="${product.url}/downloadFile" var="downloadFileUrl"/>

<spring:htmlEscape defaultHtmlEscape="true"/>

<div class="d-flex justify-content-center mb-3">
    <c:forEach items="${galleryVideos}" var="videoData" varStatus="dataStatus">
        <video width="850" height="480" controls="autoplay" preload="auto" autoplay="autoplay">
                <%--    <source src="https://127.0.0.1:9002/ncipb2bstorefront/medias/?context=bWFzdGVyfHZpZGVvc3w4MzQzNzd8dmlkZW8vbXA0fHZpZGVvcy9oZTgvaDA3Lzg3OTY4NDc1MzgyMDYuYmlufDI0NTk1YTg5ZjY5NWZhNDI5MWM1YTQxNDVkZWMwYmQzMmYyNzcyOWZhY2QwMzI1MGZjMDYxMWJjOTZkMzc2OGM" type="video/mp4">&ndash;%&gt;--%>
            <source src=${videoData.url} type="video/mp4">
        </video>
    </c:forEach>
</div>


<%--<div class="d-flex justify-content-center mb-3">--%>
<%--&lt;%&ndash;<c:forEach items="${galleryVideos}" var="videoData" varStatus="dataStatus">&ndash;%&gt;--%>
<%--&lt;%&ndash;    ${videoData.url}&ndash;%&gt;--%>
<%--    <video width="850" height="480" controls="autoplay" preload="auto" autoplay="autoplay">--%>
<%--            		<source src="https://127.0.0.1:9002/ncipb2bstorefront/medias/?context=bWFzdGVyfHZpZGVvc3w4MzQzNzd8dmlkZW8vbXA0fHZpZGVvcy9oZTgvaDA3Lzg3OTY4NDc1MzgyMDYuYmlufDI0NTk1YTg5ZjY5NWZhNDI5MWM1YTQxNDVkZWMwYmQzMmYyNzcyOWZhY2QwMzI1MGZjMDYxMWJjOTZkMzc2OGM" type="video/mp4">--%>
<%--&lt;%&ndash;        <source src=${videoData.url} type="video/mp4">&ndash;%&gt;--%>
<%--    </video>--%>
<%--&lt;%&ndash;</c:forEach>&ndash;%&gt;--%>
<%--</div>--%>

