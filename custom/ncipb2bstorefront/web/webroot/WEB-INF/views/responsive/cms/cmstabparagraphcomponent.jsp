<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<div id="work" class="tab-pane fade  p-4" role="tabpanel" aria-labelledby="nav-work-tab">
    <h4>${fn:escapeXml(component.title)}</h4>
    <p>${ycommerce:sanitizeHTML(component.content)}</p>
</div>


<%--<div class="tabhead">--%>
<%--	<a href="">${fn:escapeXml(component.title)}</a><span class="glyphicon"></span>--%>
<%--</div>--%>
<%--<div class="tabbody">${ycommerce:sanitizeHTML(component.content)}</div>--%>


