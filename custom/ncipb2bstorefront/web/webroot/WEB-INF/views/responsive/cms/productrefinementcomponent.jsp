<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<div id="product-facet" class="product__facet js-product-facet">
<%--    已勾選的條件--%>
    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
<%--    可供勾選的條件--%>
    <nav:facetNavRefinements pageData="${searchPageData}"/>
</div>


<%--Hybris 原生邏輯--%>
<%--<div id="product-facet" class="hidden-sm hidden-xs product__facet js-product-facet">--%>
<%--    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>--%>
<%--    <nav:facetNavRefinements pageData="${searchPageData}"/>--%>
<%--</div>--%>