<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<!-- modify accountChangePasswordPage.jsp -->

<div class="col-lg-10 container-fluid">

  <!-- Page Content -->
	<div class="row pb-3">
	  <div class="col-md-3"></div>
	  <div class="col-md-6">
		  <h3 class="p-3"><spring:theme code="text.account.profile.update.password.form"/></h3>
		  
		  <hr>
  			<form:form action="${action}" method="post" commandName="updatePasswordForm">
			<div class="form-group">
				<formElement:formPasswordBox idKey="currentPassword"
								 labelKey="profile.currentPassword" path="currentPassword" inputCSS="form-control"
								 mandatory="true" />
			</div>

			<div class="form-group">
				<formElement:formPasswordBox idKey="newPassword"
								 labelKey="addon.profile.newPassword" path="newPassword" inputCSS="form-control"
								 mandatory="true" />
			</div>

			<div class="form-group">
				<formElement:formPasswordBox idKey="checkNewPassword"
								 labelKey="addon.profile.checkNewPassword" path="checkNewPassword" inputCSS="form-control"
								 mandatory="true" />
			</div>


			<div class="row">
			<div class="col-lg-6 col-sm-12 mb-3">
			  <button type="button" class="btn btn-secondary btn-block backToHome">
				<spring:theme code="text.button.cancel" text="Cancel" />
			  </button>
			</div>
			<div class="col-lg-6 col-sm-12 mb-3">
			  <button type="submit" class="btn btn-primary btn-block">
			    <spring:theme code="updatePwd.submit" text="Update Password" />
			  </button>
			</div>
			</div>
			</form:form>
  	</div>
</div>
		
	<div class="col-md-3"></div>
	
	<!-- /.container -->
</div>  



<!-- <div class="account-section-header"> -->
<!-- 	<div class="row"> -->
<!-- 		<div class="container-lg col-md-6"> -->
<%-- 			<spring:theme code="text.account.profile.updatePasswordForm"/> --%>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->

<!-- <div class="row"> -->
<!-- 	<div class="container-lg col-md-6"> -->
<!-- 		<div class="account-section-content"> -->
<!-- 			<div class="account-section-form"> -->
<%-- 				<form:form action="${action}" method="post" commandName="updatePasswordForm"> --%>

<%-- 					<formElement:formPasswordBox idKey="currentPassword" --%>
<%-- 												 labelKey="profile.currentPassword" path="currentPassword" inputCSS="form-control" --%>
<%-- 												 mandatory="true" /> --%>
<%-- 					<formElement:formPasswordBox idKey="newPassword" --%>
<%-- 												 labelKey="profile.newPassword" path="newPassword" inputCSS="form-control" --%>
<%-- 												 mandatory="true" /> --%>
<%-- 					<formElement:formPasswordBox idKey="checkNewPassword" --%>
<%-- 												 labelKey="profile.checkNewPassword" path="checkNewPassword" inputCSS="form-control" --%>
<%-- 												 mandatory="true" /> --%>


<!-- 					<div class="row"> -->
<!-- 						<div class="col-sm-6 col-sm-push-6"> -->
<!-- 							<div class="accountActions"> -->
<!-- 								<button type="submit" class="btn btn-primary btn-block"> -->
<%-- 									<spring:theme code="updatePwd.submit" text="Update Password" /> --%>
<!-- 								</button> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-6 col-sm-pull-6"> -->
<!-- 							<div class="accountActions"> -->
<!-- 								<button type="button" class="btn btn-default btn-block backToHome"> -->
<%-- 									<spring:theme code="text.button.cancel" text="Cancel" /> --%>
<!-- 								</button> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<%-- 				</form:form> --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->