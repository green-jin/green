<%@ attribute name="supportedCountries" required="true"
	type="java.util.List"%>
<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="cancelUrl" required="false" type="java.lang.String"%>
<%@ attribute name="addressBook" required="false"
	type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />



<!-- Page Content -->


<!-- TW 先隱藏國家選項
						<div class="p-2">
							<select class="custom-select text-secondary">
							   <option selected>國家/地區</option>
							   <option value="1">A國名</option>
							   <option value="2">B國名</option>
							   <option value="3">C國名</option>
							</select>
						</div>
					-->

<c:if test="${not empty deliveryAddresses}">
	<button type="button" class="btn btn-secondary btn-block">
		<spring:theme
			code="checkout.checkout.multi.deliveryAddress.viewAddressBook" />
	</button>
	<br>
</c:if>
<c:if test="${empty addressFormEnabled or addressFormEnabled}">
	<form:form method="post" commandName="addressForm">
		<form:hidden path="addressId" class="add_edit_delivery_address_id"
			status="${not empty suggestedAddresses ? 'hasSuggestedAddresses' : ''}" />
		<input type="hidden" name="bill_state" id="address.billstate" />
		<div class="p-2 ">
			<!-- 					<input class="form-control p-3" type="text" placeholder="郵遞區號"> -->
			<formElement:formInputBox idKey="address.postcode"
				labelKey="" path="postcode" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.postcode" />
		</div>

		<!-- TW 先隱藏省市/自治區選項
						<div class="p-2">
							<select class="custom-select text-secondary">
							   <option selected>省市/自治區</option>
							   <option value="1">A市</option>
							   <option value="2">B市</option>
							   <option value="3">C市</option>
							</select>
						</div>
					-->

	    <div class="p-2 ">
	    	<div style="display:none">
				<div id="countrySelector"
					data-address-code="${fn:escapeXml(addressData.id)}"
					data-country-iso-code="${fn:escapeXml(addressData.country.isocode)}"
					class="form-group">
	
					<formElement:formSelectBox idKey="address.country"
						labelKey="" path="countryIso" mandatory="true"
						skipBlank="false" skipBlankMessageKey="address.country"
						items="${supportedCountries}" itemValue="isocode"
						selectedValue="${addressForm.countryIso = 'CN'}" selectCSSClass="form-control" />
				</div>
				<formElement:formSelectBox idKey="address.region" labelKey="" path="regionIso" mandatory="true" skipBlank="false" skipBlankMessageKey="address.selectProvince" items="${regions}" itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}" selectedValue="${addressForm.regionIso = 'CN-71'}" selectCSSClass="form-control"/>
			</div>

			<formElement:formInputBox idKey="address.townCity"
				labelKey="" path="townCity" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.townCity" />
		</div>

		<div class="p-2 ">
			<formElement:formInputBox idKey="address.line1"
				labelKey="" path="line1" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.street" />
		</div>		
		
		<div class="p-2 ">			
			<formElement:formInputBox idKey="address.line2"
				labelKey="" path="line2" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.building" />
		</div>

		<div class="p-2 ">
			<formElement:formInputBox idKey="address.firstName"
				labelKey="" path="firstName"
				inputCSS="form-control p-3" mandatory="true"
				placeholder="address.firstName" />
		</div>

		<div class="p-2 ">
			<formElement:formInputBox idKey="address.surname"
				labelKey="" path="lastName" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.surname" />
		</div>

		<div class="p-2 ">
			<formElement:formSelectBox idKey="address.title"
				labelKey="" path="titleCode" mandatory="true"
				skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect"
				items="${titles}" selectedValue="${addressForm.titleCode}"
				selectCSSClass="custom-select  text-secondary" />
		</div>
		
		<div class="p-2 ">
			<!-- 							<input class="form-control p-3" type="text" placeholder="街道名稱/段/巷/弄"> -->
			<formElement:formInputBox idKey="address.phone"
				labelKey="" path="phone" inputCSS="form-control p-3"
				mandatory="true" placeholder="address.phone" />
		</div>

		<div class="p-2">
		
			<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
				<div class="checkbox">
					<c:choose>
						<c:when test="${showSaveToAddressBook}">
<%-- 						<c:when test="true"> --%>
							<formElement:formCheckbox idKey="saveAddressInMyAddressBook"
								labelKey="checkout.summary.deliveryAddress.saveAddressInMyAddressBook"
								path="saveInAddressBook" inputCSS="form-check-input"
								labelCSS="form-check-label" mandatory="true" />
						</c:when>
						<c:when test="${not addressBookEmpty && not isDefaultAddress}">
							<ycommerce:testId code="editAddress_defaultAddress_box">
								<formElement:formCheckbox idKey="defaultAddress"
									labelKey="address.default" path="defaultAddress"
									inputCSS="form-check-input"
									labelCSS="form-check-label" mandatory="true" />
							</ycommerce:testId>
						</c:when>
					</c:choose>
				</div>
			</sec:authorize>
		</div>

		<div class="row mb-4 p-2">
				<c:choose>
					<c:when test="${edit eq true && not addressBook}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button
								class="positive right change_address_button show_processing_message"
								type="submit">
								<spring:theme code="checkout.multi.saveAddress" />
							</button>
						</ycommerce:testId>
					</c:when>
					<c:when test="${addressBook eq true}">
<!-- 						<div class="accountActions"> -->
								<div class="col-6 accountButtons">
									<ycommerce:testId code="editAddress_cancelAddress_button">
										<c:url value="${cancelUrl}" var="cancel" />
										<a class="btn btn-secondary btn-block"
											href="${fn:escapeXml(cancel)}"> <spring:theme
												code="text.button.cancel" />
										</a>
									</ycommerce:testId>
								</div>
								
								<div class="col-6 accountButtons">
									<ycommerce:testId code="editAddress_saveAddress_button">
										<button
											class="btn btn-primary btn-block change_address_button show_processing_message"
											type="submit">
											<spring:theme code="text.button.save" />
										</button>
									</ycommerce:testId>
								</div>
					</c:when>
				</c:choose>

		</div>
	</form:form>
</c:if>

<style>
/* .form-inline .form-control { */
/*     margin-bottom: 1rem; */
/*     width: 500px; */
/* } */
/* .text-secondary { */
/*     width: 500px !important; */
/* } */

</style>
<!-- /.container -->



<%-- <c:if test="${not empty deliveryAddresses}"> --%>
<!-- 	<button type="button" class="btn btn-default btn-block js-address-book"> -->
<%-- 		<spring:theme code="checkout.checkout.multi.deliveryAddress.viewAddressBook" /> --%>
<!-- 	</button> -->
<!-- 	<br> -->
<%-- </c:if> --%>

<%-- <c:if test="${empty addressFormEnabled or addressFormEnabled}"> --%>
<%-- 	<form:form method="post" commandName="addressForm"> --%>
<%-- 		<form:hidden path="addressId" class="add_edit_delivery_address_id" --%>
<%-- 			status="${not empty suggestedAddresses ? 'hasSuggestedAddresses' : ''}" /> --%>
<!-- 		<input type="hidden" name="bill_state" id="address.billstate" /> -->
	
<%-- 		<div id="countrySelector" data-address-code="${fn:escapeXml(addressData.id)}" --%>
<%-- 			data-country-iso-code="${fn:escapeXml(addressData.country.isocode)}" --%>
<!-- 			class="form-group"> -->
<%-- 			<formElement:formSelectBox idKey="address.country" --%>
<%-- 				labelKey="address.country" path="countryIso" mandatory="true" --%>
<%-- 				skipBlank="false" skipBlankMessageKey="address.country" --%>
<%-- 				items="${supportedCountries}" itemValue="isocode" --%>
<%-- 				selectedValue="${addressForm.countryIso}" --%>
<%-- 				selectCSSClass="form-control" /> --%>
<!-- 		</div> -->
<!-- 		<div id="i18nAddressForm" class="i18nAddressForm"> -->
<%-- 			<c:if test="${not empty country}"> --%>
<%-- 				<address:addressFormElements regions="${regions}" --%>
<%-- 					country="${country}" /> --%>
<%-- 			</c:if> --%>
<!-- 		</div> -->
<%-- 		<sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')"> --%>
<!-- 			<div class="checkbox"> -->
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${showSaveToAddressBook}"> --%>
<%-- 						<formElement:formCheckbox idKey="saveAddressInMyAddressBook" --%>
<%-- 							labelKey="checkout.summary.deliveryAddress.saveAddressInMyAddressBook" --%>
<%-- 							path="saveInAddressBook" inputCSS="add-address-left-input" --%>
<%-- 							labelCSS="add-address-left-label" mandatory="true" /> --%>
<%-- 					</c:when> --%>
<%-- 					<c:when test="${not addressBookEmpty && not isDefaultAddress}"> --%>
<%-- 						<ycommerce:testId code="editAddress_defaultAddress_box"> --%>
<%-- 							<formElement:formCheckbox idKey="defaultAddress" --%>
<%-- 								labelKey="address.default" path="defaultAddress" --%>
<%-- 								inputCSS="add-address-left-input" --%>
<%-- 								labelCSS="add-address-left-label" mandatory="true" /> --%>
<%-- 						</ycommerce:testId> --%>
<%-- 					</c:when> --%>
<%-- 				</c:choose> --%>
<!-- 			</div> -->
<%-- 		</sec:authorize> --%>
<!-- 		<div id="addressform_button_panel" class="form-actions"> -->
<%-- 			<c:choose> --%>
<%-- 				<c:when test="${edit eq true && not addressBook}"> --%>
<%-- 					<ycommerce:testId code="multicheckout_saveAddress_button"> --%>
<!-- 						<button -->
<!-- 							class="positive right change_address_button show_processing_message" -->
<!-- 							type="submit"> -->
<%-- 							<spring:theme code="checkout.multi.saveAddress" /> --%>
<!-- 						</button> -->
<%-- 					</ycommerce:testId> --%>
<%-- 				</c:when> --%>
<%-- 				<c:when test="${addressBook eq true}"> --%>
<!-- 					<div class="accountActions"> -->
<!-- 						<div class="row"> -->
<!-- 							<div class="col-sm-6 col-sm-push-6 accountButtons"> -->
<%-- 								<ycommerce:testId code="editAddress_saveAddress_button"> --%>
<!-- 									<button class="btn btn-primary btn-block change_address_button show_processing_message" -->
<!-- 											type="submit"> -->
<%-- 										<spring:theme code="text.button.save" /> --%>
<!-- 									</button> -->
<%-- 								</ycommerce:testId> --%>
<!-- 							</div> -->
<!-- 							<div class="col-sm-6 col-sm-pull-6 accountButtons"> -->
<%-- 								<ycommerce:testId code="editAddress_cancelAddress_button"> --%>
<%-- 									<c:url value="${cancelUrl}" var="cancel"/> --%>
<%-- 									<a class="btn btn-block btn-default" href="${fn:escapeXml(cancel)}"> --%>
<%-- 										<spring:theme code="text.button.cancel" /> --%>
<!-- 									</a> -->
<%-- 								</ycommerce:testId> --%>
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<%-- 				</c:when> --%>
<%-- 			</c:choose> --%>
<!-- 		</div> -->
<%-- 	</form:form> --%>
<%-- </c:if> --%>