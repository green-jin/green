<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!-- Page Content -->
<div class="container">
    <h3>${fn:escapeXml(product.name)}</h3>
    <h5><spring:theme code="product.detail.Id"/>${fn:escapeXml(product.code)}</h5>

    <div class="row mt-3">

        <div class="col-md-7 mb-3">
            <!--產品圖-->
            <product:productImagePanel galleryImages="${galleryImages}"/>
        </div>


        <div class="col-md-5 justify-content-start">

            <%--價格--%>
            <ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
                <product:productPricePanel product="${product}"/>
            </ycommerce:testId>

            <%--規格--%>
            <h5 class="my-3"><spring:theme code="product.detail.summary"/></h5>
            <span class="my-3">${ycommerce:sanitizeHTML(product.summary)}</span>

            <%--辨識商品樣式--%>
            <cms:pageSlot position="VariantSelector" var="component" element="div"
                          class="page-details-variants-select">
                <cms:component component="${component}" element="div"
                               class="yComponentWrapper page-details-variants-select-component"/>
            </cms:pageSlot>

            <%--    加入購物車--%>
            <cms:pageSlot position="AddToCart" var="component">
                <cms:component component="${component}"/>
            </cms:pageSlot>

<%--             promotion   --%>
                <product:productPromotionSection product="${product}"/>

        </div> <%--col-md-5 justify-content-start--%>



    </div> <%--        row mt-3--%>



    <%--	四個頁籤--%>
    <product:productPageTabs />

    <!--推薦/相關連產品-->
    <cms:pageSlot position="UpSelling" var="comp" element="div" class="productDetailsPageSectionUpSelling">
        <cms:component component="${comp}" element="div" class="productDetailsPageSectionUpSelling-component"/>
    </cms:pageSlot>

</div><%--        container--%>