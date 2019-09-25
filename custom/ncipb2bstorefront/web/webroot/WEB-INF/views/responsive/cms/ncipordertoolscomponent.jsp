<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<%--Quick Order--%>
<c:set value="${fn:escapeXml(component.styleClass)}" var="navigationClassHtml"/>

<c:if test="${component.visible}">
    <li class="nav-item dropdown">

        <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown3"
           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <i class="fa fa-file-text-o  px-2"></i><spring:theme code="header.link.quickOrder"/></a>

        <div class="dropdown-menu dropdown-menu-left text-white dw"
             aria-labelledby="navbarDropdownPortfolio">
            <c:forEach items="${component.navigationNode.children}" var="topLevelChild">
                <c:forEach items="${topLevelChild.entries}" var="entry">
                    <cms:component component="${entry.item}" evaluateRestriction="true"/><br/>
                </c:forEach>
            </c:forEach>
        </div>

    </li>
</c:if>