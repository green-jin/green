<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

	<div id="download" class="tab-pane fade  p-4" role="tabpanel" aria-labelledby="nav-download-tab">
		<h4><spring:theme code="product.detail.tab.download"/></h4>
		<product:productPageDownloadsTab product="${product}" />
	</div>


<%--<div id="tabreview" class="tabhead">--%>
<%--	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>--%>
<%--</div>--%>
<%--<div class="tabbody">--%>
<%--	<div class="container-lg">--%>
<%--		<div class="row">--%>
<%--			<div class="col-md-6 col-lg-4">--%>
<%--				<div class="tab-container">--%>
<%--					<product:productPageDownloadsTab product="${product}" />--%>
<%--				</div>--%>
<%--			</div>--%>
<%--		</div>--%>
<%--	</div>--%>
<%--</div>--%>

