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
            <!--大圖-->
            <product:productImagePanel galleryImages="${galleryImages}"/>
            <%--			<img class="img-fluid mb-3" src="img/800x800.jpg" alt="">--%>
            <br>

            <!--小圖-->
            <img class="img-fluid prod_s mr-2" src="img/800x800.jpg" alt="">
            <img class="img-fluid prod_s mr-2" src="img/800x800.jpg" alt="">
            <img class="img-fluid prod_s mr-2" src="img/800x800.jpg" alt="">
            <img class="img-fluid prod_s mr-2" src="img/800x800.jpg" alt="">
            <img class="img-fluid prod_s mr-2" src="img/800x800.jpg" alt="">
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


            <%--Lanct Test--%>

            <%--<div class="product-details page-title">--%>
            <%--	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">--%>
            <%--		<div class="name">${fn:escapeXml(product.name)}<span class="sku">ID</span><span class="code">${fn:escapeXml(product.code)}</span></div>--%>
            <%--	</ycommerce:testId>--%>
            <%--	<product:productReviewSummary product="${product}" showLinks="true"/>--%>
            <%--</div>--%>
            <%--<div class="row">--%>
            <%--	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">--%>
            <%--&lt;%&ndash;		<product:productImagePanel galleryImages="${galleryImages}" />&ndash;%&gt;--%>
            <%--	</div>--%>
            <%--	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>--%>
            <%--	<div class="col-sm-6 col-lg-8">--%>
            <%--		<div class="product-main-info">--%>
            <%--			<div class="row">--%>
            <%--				<div class="col-lg-6">--%>
            <%--					<div class="product-details">--%>
            <%--						<product:productPromotionSection product="${product}"/>--%>
            <%--						<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">--%>
            <%--							<product:productPricePanel product="${product}" />--%>
            <%--							${product.listpr}--%>
            <%--						</ycommerce:testId>--%>
            <%--						<div class="description">${ycommerce:sanitizeHTML(product.summary)}</div>--%>
            <%--					</div>--%>
            <%--				</div>--%>

            <%--				<div class="col-sm-12 col-md-9 col-lg-6">--%>
            <%--					<cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">--%>
            <%--						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>--%>
            <%--					</cms:pageSlot>--%>
            <%--					<cms:pageSlot position="AddToCart" var="component" element="div" class="page-details-variants-select">--%>
            <%--						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-add-to-cart-component"/>--%>
            <%--					</cms:pageSlot>--%>
            <%--				</div>--%>
            <%--			</div>--%>
        </div> <%--col-md-5 justify-content-start--%>


    </div> <%--        row mt-3--%>

    <%--	四個頁籤--%>
    <product:productPageTabs />
</div><%--        container--%>