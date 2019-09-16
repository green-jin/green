<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="hideHeaderLinks" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<!-- nav -->
<%--<link href="ncipb2bstorefront/_ui/responsive/theme-lambda/css/style.css" rel="stylesheet">--%>
<%--<link href="ncipb2bstorefront/_ui/responsive/theme-lambda/css/bootstrap.min.css" rel="stylesheet">--%>
<%--<link href="ncipb2bstorefront/_ui/responsive/theme-lambda/css/hb.css" rel="stylesheet">--%>
<nav class="navbar navbar-expand-lg navbar-dark sticky-top">
    <div class="container">

        <%--        LOGO--%>
        <cms:pageSlot position="SiteLogo" var="logo" limit="1">
            <cms:component component="${logo}" element="" class="navbar-brand px-3"/>
        </cms:pageSlot>


        <div class="navbar-toggler-right">
            <!--頁面縮小的menu button-->
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbar" aria-controls="navbarResponsive" aria-expanded="false"
                    aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <!--頁面縮小的menu button-->
        </div>
        <div class="collapse navbar-collapse flex-column" id="navbar">

            <!--第一條 Navbar 	-->
            <ul class="navbar-nav w-100 justify-content-end px-3">

                <c:if test="${empty hideHeaderLinks}">

                    <sec:authorize access="hasAnyRole('ROLE_ANONYMOUS')">
                        <ycommerce:testId code="header_Login_link">

                            <%--About Us--%>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="#"><i
                                        class="fa fa-users px-2"></i><spring:theme
                                        code="header.link.aboutus"/></a>
                            </li>

                            <%--                            登入--%>
                            <c:url value="/login" var="loginUrl"/>
                            <a class="nav-link text-white" href="${fn:escapeXml(loginUrl)}">
                                <i class="fa fa-sign-in px-2"></i><spring:theme
                                    code="header.link.login"/></a>
                        </ycommerce:testId>
                    </sec:authorize>
                    <%--                    登入 End--%>

                    <%--   你好xxx經銷商--%>
                    <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
                        <c:set var="maxNumberChars" value="25"/>
                        <c:if test="${fn:length(user.firstName) gt maxNumberChars}">
                            <c:set target="${user}" property="firstName"
                                   value="${fn:substring(user.firstName, 0, maxNumberChars)}..."/>
                        </c:if>

                        <li class="nav-item">
                            <ycommerce:testId code="header_LoggedUser">
                                <a class="nav-link text-white" href="#"><i
                                        class="fa fa-user px-2"></i><spring:theme
                                        code="header.link.welcome.front"
                                        arguments="${user.firstName},${user.lastName}"/>
                                    <spring:theme
                                            code="header.link.welcome.back"/></a>

                            </ycommerce:testId>
                        </li>
                    </sec:authorize>
                    <%--   你好xxx經銷商 End--%>

                    <%--                    我的帳戶--%>

                    <cms:pageSlot position="HeaderLinks" var="link">
                        <cms:component component="${link}"/>
                    </cms:pageSlot>
                    <%--                    我的帳戶--%>

                    <%--                    登出--%>
                    <li class="nav-item">
                        <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
                            <ycommerce:testId code="header_signOut">
                                <c:url value="/logout" var="logoutUrl"/>
                                <a class="nav-link text-white" href="${fn:escapeXml(logoutUrl)}">
                                    <i class="fa fa-sign-out px-2"></i>
                                    <spring:theme code="header.link.logout"/>
                                </a>
                            </ycommerce:testId>
                        </sec:authorize>
                    </li>
                    <%--                    登出--%>
                </c:if>

                <%--Quick Order & Mini Cart--%>
                <cms:pageSlot position="MiniCart" var="cart">
                    <cms:component component="${cart}"/>
                </cms:pageSlot>
                <%--Quick Order & Mini Cart End--%>

            </ul>

            <!--第二條 Navbar -->
            <nav:topNavigation/>
        </div>


    </div>
</nav>



<!------------end nav -->
<%--<script src="/ncipb2bstorefront/_ui/responsive/common/js/ncip/jquery.min.js"></script>--%>
<%--<script src="/ncipb2bstorefront/_ui/responsive/common/js/ncip/bootstrap.min.js"></script>--%>