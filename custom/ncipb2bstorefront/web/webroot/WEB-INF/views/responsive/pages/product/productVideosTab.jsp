<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div id="video" class="tab-pane fade p-4" role="tabpanel" aria-labelledby="nav-video-tab">
    <h4 class="mb-4"><spring:theme code="product.detail.tab.videos"/></h4>

    <!-- 外嵌影片
      <div class="embed-responsive embed-responsive-16by9">
              <iframe class="embed-responsive-item" src="https://www.youtube.com/embed/nMHBcqxDq08" allowfullscreen></iframe>
        </div>-->

    <!-- 播放影片 -->
    <product:productPageVideosTab product="${product}"/>

</div>


<%--<div id="tabreview" class="tabhead">--%>
<%--    <a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>--%>
<%--</div>--%>
<%--<div class="tabbody">--%>
<%--    <div class="container-lg">--%>
<%--        <div class="row">--%>
<%--            <div class="col-md-6 col-lg-4">--%>
<%--                <div class="tab-container">--%>
<%--                    <product:productPageVideosTab product="${product}" />--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

