<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%--<c:set value="${fn:escapeXml(component.styleClass)}" var="navigationClassHtml"/>--%>
<%--My Account--%>
<c:if test="${component.visible}">
    <%--    <div class="${navigationClassHtml} js-${navigationClassHtml} display-none NAVcompONENT" data-title="${fn:escapeXml(component.navigationNode.title)}">--%>
    <li class="nav-item dropdown megamenu" style="position:static;">
        <a class="nav-link dropdown-toggle text-white " href="#" data-toggle="dropdown" role="button"
           aria-haspopup="true" aria-expanded="false"><i
                class="fa fa-id-card px-2"></i><spring:theme code="header.link.myAccount"/></a>
        <div class="dropdown-menu">
            <div class="px-0 container">
                <div class="row">
                    <c:forEach items="${component.navigationNode.children}" var="topLevelChild">
                        <c:forEach items="${topLevelChild.entries}" var="entry">

                            <div class="col-md-4">
                                <cms:component component="${entry.item}"
                                               evaluateRestriction="true"/>
                            </div>

                        </c:forEach>
                    </c:forEach>
                </div>
            </div>
        </div>
    </li>
</c:if>
