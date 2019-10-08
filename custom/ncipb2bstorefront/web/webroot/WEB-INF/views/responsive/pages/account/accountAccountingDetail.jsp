<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmtt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<spring:url value="${redirectUrl}" var="redirect"/>
<c:set var="allFiles" value="${allFiles}"/>
<c:set var="filternumber" value="${filternum}"/>

<!-- Page Content -->
<div class="col-lg-10 container-fluid">

    <!-- 對帳單外框 -->
    <div class="row py-4">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h4><spring:theme code="myaccount.accountingDetail.tittle"/></h4>
            <%--            客戶名稱--%>
            <h5 class="text-left text-dark"><spring:theme
                    code="myaccount.accountingDetail.customerName"/>${fn:escapeXml(customerData.unitname)}</h5>
            <%--            付款條件--%>
            <h5 class="text-left text-dark pb-1"><spring:theme
                    code="myaccount.accountingDetail.paymentType"/>
                <spring:theme
                        code="myaccount.accountingDetail.paymentType.${fn:escapeXml(customerData.zterm)}"/></h5>
           <form:form action="../my-account/accounting-detail" id="acct">
            <div class="row no-gutters">
                <div class="col-4 col-md-3 p-1">
                    <button onclick="filterFunction(3)" type="button"
                            class="btn btn-outline-primary btn-block"><spring:theme
                            code="myaccount.accountingDetail.3months"/></button>
                </div>
                <div class="col-4 col-md-3 p-1">
                    <button onclick="filterFunction(6)" type="button"
                            class="btn btn-outline-primary btn-block"><spring:theme
                            code="myaccount.accountingDetail.halfyear"/></button>
                </div>
                <div class="col-4 col-md-3 p-1">
                    <button onclick="filterFunction(12)" type="button"
                            class="btn btn-outline-primary btn-block"><spring:theme
                            code="myaccount.accountingDetail.1year"/></button>
                </div>
                <input id="inputvValue" type="text" name="num" value="3" hidden/>
                </form:form>
                <div class="col-4 col-md-3"></div>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>


    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">

            <table class="table table-hover">
                <thead class="thead-dark text-left" id="theader">
                <tr>
                    <th>#</th>
                    <th><spring:theme code="myaccount.accountingDetail.year"/></th>
                    <th><spring:theme code="myaccount.accountingDetail.month"/></th>
                    <th><spring:theme code="myaccount.accountingDetail.downloadPDF"/></th>
                </tr>
                </thead>

                <tbody id="body">
                <c:forEach items="${allFiles}" var="file" varStatus="dataStatus">
                    <c:if test="${file.filterdate <= filternumber}">
                        <form:form action="/ncipb2bstorefront/ncipb2b/zh/USD${redirectUrl}"
                                   method="get" id="myform${dataStatus.index}${file.date_year}${file.date_month}">
                            <th scope="row">${dataStatus.index}</th>
                            <td>${file.date_year}</td>
                            <td><spring:theme code="myaccount.accountingDetail.month${file.date_month}"/></td>
                            <td><i class="fa fa-file-pdf-o mr-3"></i><a href="" onclick="downloadFunction(${dataStatus.index}${file.date_year}${file.date_month})"><spring:theme
                                    code="myaccount.accountingDetail.download"/></a></td>
                            <input type="text" name="accounting_index" value="${dataStatus.index}" hidden/>
                            <input type="text" name="stu_n" value="${file.url}" hidden/>
                            <input type="text" name="accounting_fileName" value="${file.filename}" hidden/>
                        </form:form>
                        </tr>
                    </c:if>
                </c:forEach>
                </tbody>

            </table>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
<!-- /.container -->


<%--<script>--%>
<%--  function filterFunction3() {--%>
<%--    if (document.getElementById("body") != undefined) {--%>
<%--      document.getElementById("body").remove();--%>
<%--    }--%>
<%--      $('#accountingFilrter3').tmpl().insertAfter(document.getElementById("theader"));--%>
<%--  }--%>

<%--  function filterFunction6() {--%>
<%--    if (document.getElementById("body") != undefined) {--%>
<%--      document.getElementById("body").remove();--%>
<%--    }--%>
<%--    $('#accountingFilrter6').tmpl().insertAfter(document.getElementById("theader"));--%>
<%--  }--%>

<%--  function filterFunction12() {--%>
<%--    if (document.getElementById("body") != undefined) {--%>
<%--      document.getElementById("body").remove();--%>
<%--    }--%>
<%--    $('#accountingFilrter12').tmpl().insertAfter(document.getElementById("theader"));--%>
<%--  }--%>
<%--</script>--%>



<%--<script id="accountingFilrter12" type="text/x-jquery-tmpl">--%>
<%--    <c:set var="number" value="{{= value}}"/>--%>
<%--    <tbody id="body">--%>
<%--    <c:forEach items="${allFiles}" var="file" varStatus="dataStatus">--%>
<%--    <c:if test="${file.filterdate <= 12}">--%>
<%--        <form:form action="/ncipb2bstorefront/ncipb2b/zh/USD/my-account/accounting-detail/download"--%>
<%--                   method="get" id="myform">--%>
<%--            <th scope="row">${dataStatus.index}</th>--%>
<%--            <td>${file.date_year}</td>--%>
<%--            <td><spring:theme code="myaccount.accountingDetail.month${file.date_month}"/></td>--%>
<%--            <td><i class="fa fa-file-pdf-o mr-3"></i><a href="" onclick="downloadFunction(${file.filename})"><spring:theme--%>
<%--                code="myaccount.accountingDetail.download"/></a></td>--%>
<%--            <input type="text" name="accounting_index" value="${dataStatus.index}" hidden/>--%>
<%--            <input type="text" name="stu_n" value="${file.url}" hidden/>--%>
<%--            <input type="text" name="accounting_fileName" value="${file.filename}" hidden/>--%>
<%--        </form:form>--%>
<%--        </tr>--%>
<%--    </c:if>--%>
<%--</c:forEach>--%>
<%--</tbody>--%>
<%--</script>--%>

<%--<script id="accountingFilrter6" type="text/x-jquery-tmpl">--%>
<%--    <c:set var="number" value="{{= value}}"/>--%>
<%--    <tbody id="body">--%>
<%--    <c:forEach items="${allFiles}" var="file" varStatus="dataStatus">--%>
<%--    <c:if test="${file.filterdate <= 6}">--%>
<%--        <form:form action="/ncipb2bstorefront/ncipb2b/zh/USD/my-account/accounting-detail/download"--%>
<%--                   method="get" id="myform">--%>
<%--            <th scope="row">${dataStatus.index}</th>--%>
<%--            <td>${file.date_year}</td>--%>
<%--            <td><spring:theme code="myaccount.accountingDetail.month${file.date_month}"/></td>--%>
<%--            <td><i class="fa fa-file-pdf-o mr-3"></i><a href="" onclick="downloadFunction(${file.filename})"><spring:theme--%>
<%--                code="myaccount.accountingDetail.download"/></a></td>--%>
<%--            <input type="text" name="accounting_index" value="${dataStatus.index}" hidden/>--%>
<%--            <input type="text" name="stu_n" value="${file.url}" hidden/>--%>
<%--            <input type="text" name="accounting_fileName" value="${file.filename}" hidden/>--%>
<%--        </form:form>--%>
<%--        </tr>--%>
<%--    </c:if>--%>
<%--</c:forEach>--%>
<%--</tbody>--%>
<%--</script>--%>

<%--<script id="accountingFilrter3" type="text/x-jquery-tmpl">--%>
<%--    <c:set var="number" value="{{= value}}"/>--%>
<%--    <tbody id="body">--%>
<%--    <c:forEach items="${allFiles}" var="file" varStatus="dataStatus">--%>
<%--    <c:if test="${file.filterdate <= 3}">--%>
<%--        <form:form action="/ncipb2bstorefront/ncipb2b/zh/USD/my-account/accounting-detail/download"--%>
<%--                   method="get" id="myform">--%>
<%--            <th scope="row">${dataStatus.index}</th>--%>
<%--            <td>${file.date_year}</td>--%>
<%--            <td><spring:theme code="myaccount.accountingDetail.month${file.date_month}"/></td>--%>
<%--            <td><i class="fa fa-file-pdf-o mr-3"></i><a href="" onclick="downloadFunction()"><spring:theme--%>
<%--                code="myaccount.accountingDetail.download"/></a></td>--%>
<%--            <input type="text" name="accounting_index" value="${dataStatus.index}" hidden/>--%>
<%--            <input type="text" name="stu_n" value="${file.url}" hidden/>--%>
<%--            <input type="text" name="accounting_fileName" value="${file.filename}" hidden/>--%>
<%--        </form:form>--%>

<%--        </tr>--%>
<%--    </c:if>--%>
<%--</c:forEach>--%>
<%--</tbody>--%>
<%--</script>--%>
<script>
  function downloadFunction(index) {
    document.getElementById("myform"+index).submit();
  }

  function filterFunction(num) {
    document.getElementById("inputvValue").value = num;
    document.getElementById('acct').submit()
  }

</script>
