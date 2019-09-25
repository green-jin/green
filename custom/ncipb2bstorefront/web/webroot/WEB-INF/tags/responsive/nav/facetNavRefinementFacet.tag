<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true"
              type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>


<c:if test="${not empty facetData.values}">
    <ycommerce:testId code="facetNav_title_${facetData.name}">

                <%--					按照XX購物--%>
                <h5 class="pt-5"><spring:theme code="search.nav.facetTitle"
                                               arguments="${facetData.name}"/></h5>


            <div class="js-facet-values js-facet-form facet js-facet">

                    <%--								展開分類--%>
                <c:if test="${not empty facetData.topValues}">
                    <ul class="facet__list js-facet-list js-facet-top-values">
                        <c:forEach items="${facetData.topValues}" var="facetValue">
                            <%--                            <li class="d-flex align-items-center mb-2">--%>

                            <%--								價格分類--%>
                            <c:if test="${facetData.multiSelect}">
                                <div class="custom-control custom-checkbox mb-2">
                                    <form action="#" method="get">
                                        <!-- facetValue.query.query.value and searchPageData.freeTextSearch are html output encoded in the backend -->
                                        <input type="hidden" name="q"
                                               value="${facetValue.query.query.value}"/>
                                        <input type="hidden" name="text"
                                               value="${searchPageData.freeTextSearch}"/>

                                        <label class="custom-control-label" >
											<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}
												   class="custom-control-input js-facet-checkbox"/>
                                                ${fn:escapeXml(facetValue.name)}
                                                <%--												<ycommerce:testId code="facetNav_count">--%>
                                                <%--                                                    <span class="facet__value__count"><spring:theme--%>
                                                <%--                                                            code="search.nav.facetValueCount"--%>
                                                <%--                                                            arguments="${facetValue.count}"/></span>--%>
                                                <%--                                                </ycommerce:testId>--%>

                                        </label>
                                    </form>
                                </div>
                            </c:if>

                            <%--							一般商品分類--%>
                            <c:if test="${not facetData.multiSelect}">
							<li class="d-flex align-items-center mb-2">
                                <c:url value="${facetValue.query.url}"
                                       var="facetValueQueryUrl"/>
                                <a href="${fn:escapeXml(facetValueQueryUrl)}&amp;text=${searchPageData.freeTextSearch}"
                                   class="text-secondary pr-3">${fn:escapeXml(facetValue.name)}</a>&nbsp;
                                <ycommerce:testId code="facetNav_count">
                                        <span class="badge badge-primary badge-pill"><spring:theme
                                                code="search.nav.facetValueCount"
                                                arguments="${facetValue.count}"/></span>
                                </ycommerce:testId>
							</li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </c:if>


                    <%--							收合分類--%>
                <ul class="facet__list js-facet-list <c:if test="${not empty facetData.topValues}">facet__list--hidden js-facet-list-hidden</c:if>">
                    <c:forEach items="${facetData.values}" var="facetValue">

                        <%--						價格分類--%>
                        <c:if test="${facetData.multiSelect}">
                            <div class="custom-control custom-checkbox mb-2">
                                <ycommerce:testId code="facetNav_selectForm">
                                    <form action="#" method="get">
                                        <!-- facetValue.query.query.value and searchPageData.freeTextSearch are html output encoded in the backend -->
                                        <input type="hidden" name="q"
                                               value="${facetValue.query.query.value}"/>
                                        <input type="hidden" name="text"
                                               value="${searchPageData.freeTextSearch}"/>


                                        <label class="custom-control-label" >
											<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''}
												   class="custom-control-input js-facet-checkbox"/>

                                                ${fn:escapeXml(facetValue.name)}&nbsp;

                                                <%--符合篩選條件的產品數量--%>
                                                <%--											<ycommerce:testId code="facetNav_count">--%>
                                                <%--                                                <span class="facet__value__count"><spring:theme--%>
                                                <%--                                                        code="search.nav.facetValueCount"--%>
                                                <%--                                                        arguments="${facetValue.count}"/></span>--%>
                                                <%--                                            </ycommerce:testId>--%>
                                                <%--										</span>--%>
                                        </label>
                                    </form>
                                </ycommerce:testId>
                            </div>
                        </c:if>

                        <%--一般分類--%>
                        <c:if test="${not facetData.multiSelect}">
                            <li class="d-flex align-items-center mb-2">
                                <c:url value="${facetValue.query.url}" var="facetValueQueryUrl"/>

                                <a href="${fn:escapeXml(facetValueQueryUrl)}"
                                   class="text-secondary pr-3">${fn:escapeXml(facetValue.name)}</a>
                                <ycommerce:testId code="facetNav_count">
                                    <span class="badge badge-primary badge-pill"><spring:theme
                                            code="search.nav.facetValueCount"
                                            arguments="${facetValue.count}"/></span>
                                </ycommerce:testId>
                            </li>
                        </c:if>

                    </c:forEach>
                </ul>


                    <%--				展開收合字樣--%>
                <c:if test="${not empty facetData.topValues}">
				<span class="facet__values__more js-more-facet-values">
					<a href="#" class="js-more-facet-values-link"><spring:theme
                            code="search.nav.facetShowMore_${facetData.code}"/></a>
				</span>
                    <span class="facet__values__less js-less-facet-values">
					<a href="#" class="js-less-facet-values-link"><spring:theme
                            code="search.nav.facetShowLess_${facetData.code}"/></a>
				</span>
                </c:if>
            </div>
    </ycommerce:testId>
</c:if>
