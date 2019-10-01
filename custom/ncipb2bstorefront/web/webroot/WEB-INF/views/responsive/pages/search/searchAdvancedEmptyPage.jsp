<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="addonProduct" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="addonFormElement" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/formElement" %>


<spring:htmlEscape defaultHtmlEscape="true"/>
<jsp:useBean id="additionalParams" class="java.util.HashMap"/>
<c:set target="${additionalParams}" property="searchResultType"
       value="${advancedSearchForm.searchResultType}"/>
<c:set target="${additionalParams}" property="isCreateOrderForm"
       value="${advancedSearchForm.createOrderForm}"/>
<c:set target="${additionalParams}" property="onlyProductIds"
       value="${advancedSearchForm.onlyProductIds}"/>


<template:page pageTitle="${pageTitle}">
    <jsp:body>

        <%--        HelpParagraphComponent--%>
        <div class="col-10 container-fluid" id="orderFormContainer">
                <%--            <cms:pageSlot position="SideContent" var="feature" element="div"--%>
                <%--                          class="span-4 side-content-slot cms_disp-img_slot">--%>
                <%--                <cms:component component="${feature}"/>--%>
                <%--            </cms:pageSlot>--%>

                <%--            訂貨單--%>
                <%--            <div class="advancedSearch">--%>
            <h4 class="mt-5"><spring:theme code="search.advanced"/></h4>
            <hr>

            <div class="row">
                <div class="col-md-6">
                    <spring:url value="/search/advanced" var="advancedSearchUrl"
                                htmlEscape="false"/>
                    <form:form action="${advancedSearchUrl}" method="get" name="advancedSearchForm"
                               commandName="advancedSearchForm">

                        <div class="form-group">
                                <%--                            關鍵字搜尋--%>
                            <div class="d-flex justify-content-between">
                                        <span class="searchInput">
                                        <label for="js-product-ids"><spring:theme
                                                code="search.advanced.keyword"/></label>
                                        </span>

                                    <%--                                只搜尋產品料號--%>
                                <div class="form-check">
                                    <input name="onlyProductIds" class="form-check-input"
                                           type="checkbox" id="js-enable-product-ids">
                                    <label class="form-check-label"
                                           for="js-enable-product-ids"><spring:theme
                                            code="search.advanced.onlyproductids"/></label>
                                </div>
                            </div>

                                <%--                            搜尋輸入框--%>
                            <input type="text" class="form-control" id="js-product-ids"
                                   placeholder="<spring:theme code="orderform.search.enterkeyword"/>"
                                   name="keywords">
                        </div>


                        <%--                        <div id="js-selected-product-ids"--%>
                        <%--                             class="selected_product_ids clearfix"></div>--%>

                        <div class="row mb-3">
                                <%--                            訂貨單--%>
                            <div class="col-6">
                                <div class="form-check">
                                    <input id="search-order-form" name="searchResultType"
                                           class="form-check-input" type="radio" value="order-form"
                                           checked>
                                    <label class="form-check-label"
                                           for="search-order-form"><spring:theme
                                            code="orderform.search.orderform"/></label>
                                </div>

                            </div>
                                <%--                            建立新的訂貨單--%>
                            <div class="col-6">
                                <div class="form-check">
                                    <input id="search-create-order-form" name="searchResultType"
                                           class="form-check-input" type="radio"
                                           value="create-order-form">
                                    <label class="form-check-label"
                                           for="search-create-order-form"><spring:theme
                                            code="orderform.search.createform"/></label>
                                </div>

                            </div>
                        </div>

                        <c:set var="isCreateOrderForm" value="${form.createOrderForm}"/>
                        <c:if test="${empty isCreateOrderForm }">
                            <c:set var="isCreateOrderForm" value="false"/>
                        </c:if>

                        <input type="hidden" name="isCreateOrderForm"
                               id="isCreateOrderForm"
                               value="${fn:escapeXml(isCreateOrderForm)}"/>
                        <button class="btn btn-primary btn-block mb-3 adv_search_button"
                                type="submit">
                            <spring:theme code="search.advanced.search"/>
                        </button>
                    </form:form>
                </div>
                <div class="col-md-6"></div>
            </div>
            <hr class="mb-4">

                <%--                    頁碼 & Sort - Top--%>
            <c:if test="${not empty advancedSearchForm.keywords}">
                <nav:orderFormPagination top="true" supportShowPaged="${isShowPageAllowed}"
                                         supportShowAll="false"
                                         searchPageData="${searchPageData}"
                                         searchUrl="${paginateUrl}"
                                         numberPagesShown="${numberPagesShown}"
                                         additionalParams="${additionalParams}"/>
            </c:if>

                <%--             選定的數量 & 加到購物車-Top       --%>
            <c:if test="${not empty advancedSearchForm.keywords && advancedSearchForm.orderFormSearchResultType}">
                <hr>
                <div class="row align-self-center">
                        <%--                    選定的數量 :  --%>
                    <div class="col-md-5 text-left d-none d-md-block">
                        <spring:theme code="product.grid.items.selected"/>&nbsp;
                        <span class="js-total-items-count">0</span>
                    </div>
                    <div class="col-6 text-left d-md-none">
                        <spring:theme code="product.grid.formDescription"/>
                    </div>

                        <%--                    小計--%>
                    <product:productFormAddToCartPanel product="${product}"
                                                       showViewDetails="false"
                                                       addToCartBtnId="js-add-to-cart-order-form-btn-top"/>
                </div>
                <hr>
            </c:if>



            <c:set var="skuIndex" scope="session" value="0"/>
            <spring:theme code="product.grid.confirmQtys.message" var="gridConfirmMessage"/>

                <%--            搜尋結果--%>
            <c:if test="${not empty advancedSearchForm.keywords && advancedSearchForm.orderFormSearchResultType}">
                <spring:url value="/cart/addGrid" var="addToCartGridUrl" htmlEscape="false"/>
                <form:form name="AddToCartOrderForm" id="AddToCartOrderForm"
                           action="${addToCartGridUrl}" method="post"
                           data-grid-confirm-message="${gridConfirmMessage}">
                    <addonProduct:productLister pageData="${searchPageData}" path="/search"
                                                itemType="ORDERFORM"
                                                isOnlyProductIds="${advancedSearchForm.onlyProductIds}"
                                                filterSkus="${advancedSearchForm.filterSkus}"/>
                </form:form>

                <div id="skuIndexSavedValue" name="skuIndexSavedValue"
                     data-sku-index="${sessionScope.skuIndex}">
                        <%--  don't remove this div. This is used by the order form search --%></div>
            </c:if>

                <%--                    建立新訂單--%>
            <c:if test="${ not empty advancedSearchForm.keywords && advancedSearchForm.createOrderFormSearchResultType && searchPageData.pagination.totalNumberOfResults != 0}">
                <form name="createOrderForm" id="createOrderForm" class="create-order-form"
                      data-grid-confirm-message="${gridConfirmMessage}">

                    <div class="create-order-form-button">
                        <button class="btn btn-primary js-create-order-form-button"
                                type="button" disabled="disabled"><spring:theme
                                code='search.advanced.createorderform'/></button>
                    </div>
                    <addonProduct:productLister pageData="${searchPageData}" path="/search"
                                                itemType="FILTER"
                                                isOnlyProductIds="false"
                                                filterSkus="${advancedSearchForm.filterSkus}"/>

                    <div class="create-order-form-button bottom">
                        <button class="btn btn-primary js-create-order-form-button"
                                type="button" disabled="disabled"><spring:theme
                                code='search.advanced.createorderform'/></button>
                    </div>
                </form>
            </c:if>

                <%--            選定的數量 & 加到購物車-Bottom --%>
            <c:if test="${not empty advancedSearchForm.keywords && advancedSearchForm.orderFormSearchResultType}">
                <div class="row align-self-center">
                        <%--                    選定的數量 :  --%>
                    <div class="col-md-5 text-left d-none d-md-block">
                        <spring:theme code="product.grid.items.selected"/>&nbsp;
                        <span class="js-total-items-count">0</span>
                    </div>
                    <div class="col-6 text-left d-md-none">
                        <spring:theme code="product.grid.formDescription"/>
                    </div>

                        <%--                    小計--%>
                    <product:productFormAddToCartPanel product="${product}"
                                                       showViewDetails="false"
                                                       addToCartBtnId="js-add-to-cart-order-form-btn-top"/>
                </div>

                <hr>
            </c:if>



                <%--                    頁碼 & Sort - Bottom--%>
            <c:if test="${not empty advancedSearchForm.keywords}">
                <nav:orderFormPagination top="true" supportShowPaged="${isShowPageAllowed}"
                                         supportShowAll="false"
                                         searchPageData="${searchPageData}"
                                         searchUrl="${paginateUrl}"
                                         numberPagesShown="${numberPagesShown}"
                                         additionalParams="${additionalParams}"/>
            </c:if>


            <script id="product-id-tag-box-template" type="text/x-jquery-tmpl">
                    <span class="product-id-tag-box js-remove-product-id" id="product-id-{{= productId }}">
                      <span class="product-id">{{= productId }}</span>
                      <span class="glyphicon glyphicon-remove">
                    </span>


            </script>

            <product:productOrderFormJQueryTemplates/>

            <cms:pageSlot position="BottomContent" var="comp" element="div"
                          class="span-20 cms_disp-img_slot right last">
                <cms:component component="${comp}"/>
            </cms:pageSlot>
        </div>

        <input id="searchByKeywordLabel" type="hidden"
               value='<spring:theme code="search.advanced.keyword"/>'/>
        <input id="searchByIdsLabel" type="hidden"
               value='<spring:theme code="search.advanced.productids"/>'/>
        <c:remove var="skuIndex" scope="session"/>
    </jsp:body>

</template:page>

