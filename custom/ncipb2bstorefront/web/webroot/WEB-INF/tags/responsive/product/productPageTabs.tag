<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%--<div class="tab-content bg-light">--%>
<%--	<cms:pageSlot position="Tabs" var="tabs">--%>
<%--		<cms:component component="${tabs}" />--%>
<%--	</cms:pageSlot>--%>
<%--</div>--%>
<ul class="nav nav-tabs justify-content-center" id="myTab" role="tablist">
	<li class="nav-item">
		<a class="nav-link active" id="nav-prod-tab" data-toggle="tab" href="#prod" role="tab" aria-controls="prod" aria-selected="true"><spring:theme code="product.detail.tab.detail"/></a>
	</li>

	<li class="nav-item">
		<a class="nav-link" id="nav-download-tab" data-toggle="tab" href="#download" role="tab" aria-controls="download" aria-selected="false"><spring:theme code="product.detail.tab.download"/></a>
	</li>

	<li class="nav-item">
		<a class="nav-link" id="nav-video-tab" data-toggle="tab" href="#video" role="tab" aria-controls="video" aria-selected="false"><spring:theme code="product.detail.tab.videos"/></a>
	</li>

	<li class="nav-item">
		<a class="nav-link" id="nav-work-tab" data-toggle="tab" href="#work" role="tab" aria-controls="work" aria-selected="false"><spring:theme code="product.detail.tab.delivery"/></a>
	</li>

</ul>

<div class="tab-content bg-light">
		<cms:pageSlot position="Tabs" var="tabs">
			<cms:component component="${tabs}" />
		</cms:pageSlot>
</div>

