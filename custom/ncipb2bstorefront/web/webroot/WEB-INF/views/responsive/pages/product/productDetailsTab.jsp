<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

	<div id="prod" class="tab-pane fade show active  p-4" role="tabpanel" aria-labelledby="nav-prod-tab">
		<h4><spring:theme code="product.detail.tab.detail"/></h4>
		<product:productDetailsTab product="${product}" />
    </div>

<%--<div class="tabhead">--%>
<%--	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>--%>
<%--</div>--%>
<%--<div class="tabbody">--%>
<%--	<div class="container-lg">--%>
<%--		<div class="row">--%>
<%--			<div class="col-md-6 col-lg-4">--%>
<%--				<div class="tab-container">--%>
<%--					<product:productDetailsTab product="${product}" />--%>
<%--				</div>--%>
<%--			</div>--%>
<%--		</div>--%>
<%--	</div>--%>
<%--</div>--%>

