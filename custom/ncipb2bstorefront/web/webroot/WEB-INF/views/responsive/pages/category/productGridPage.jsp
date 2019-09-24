<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature" element="div" class="product-grid-section1-slot">
		<cms:component component="${feature}" element="div" class="yComponentWrapper map product-grid-section1-component"/>
	</cms:pageSlot>
<div class="col-lg-10 container-fluid">

	<div class="row">

		<!--左側篩選列  篩選列在行動裝置收合成按鈕請套用Hybris原本的JS-->
		<div class="col-lg-3 mb-4">
				<cms:pageSlot position="ProductLeftRefinements" var="feature" element="div" class="product-grid-left-refinements-slot">
					<cms:component component="${feature}" element="div" class="yComponentWrapper product-grid-left-refinements-component"/>
				</cms:pageSlot>
		</div>

<%--		搜尋bar--%>
		<div class="col-lg-9 mb-4">
			<div class="row mb-4 justify-content-end">
				<div class="col-lg-4 input-group mb-4 col-sm-12 col-md-6">
					<cms:pageSlot position="SearchBox" var="component">
						<cms:component component="${component}" element="div"/>
					</cms:pageSlot>
				</div>
			</div>

<%--			產品--%>
			<cms:pageSlot position="ProductGridSlot" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>

		</div>
	</div>
</div>
<!-- /.container -->
	<storepickup:pickupStorePopup />
</template:page>