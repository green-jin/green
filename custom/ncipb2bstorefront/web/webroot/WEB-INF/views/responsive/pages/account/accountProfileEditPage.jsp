<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<%-- <form:form action="update-profile" method="post" commandName="updateProfileForm"> --%>
<div class="col-lg-10 container-fluid">

  <!-- Page Content -->
	<div class="row pb-3">
	  <div class="col-md-3"></div>
	  <div class="col-md-6">
		  <h3 class="p-3"><spring:theme code="text.account.profile.updatePersonalDetails"/></h3>
			
			<div id="accordion">
						  	

  				<div class="card">
    				<div class="card-header" id="head1">
					  <div class="d-flex justify-content-between">
        				<button class="btn btn-link text-dark" data-toggle="collapse" data-target="#checkout1" aria-expanded="false" aria-controls="checkout1">
         				<h5><spring:theme code="profile.memeberInfo"/></h5>
        				</button>
						  
						  
					  </div>  
    				</div>

    				<div id="checkout1" class="collapse show" aria-labelledby="head1" data-parent="#accordion">
      				   <div class="card-body">
							<form:form action="update-profile" method="post" commandName="updateProfileForm">
							  
							 <div style="display:none">
			    				<formElement:formSelectBox  idKey="profile.title" labelKey="profile.title" path="titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="form.select.empty" items="${titleData}" selectCSSClass="form-control"/>
	            				<formElement:formInputBox idKey="profile.firstName" labelKey="profile.firstName" path="firstName" inputCSS="text" mandatory="true"/>
		        				<formElement:formInputBox idKey="profile.lastName" labelKey="profile.lastName" path="lastName" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.uid" labelKey="profile.uid" path="uid" inputCSS="text" mandatory="true"/>
							    <formElement:formInputBox idKey="profile.name_1" labelKey="profile.name_1" path="name1" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.cellnumber" labelKey="profile.cellnumber" path="cell_number" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.line" labelKey="profile.line" path="poc_line" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.wechat" labelKey="profile.wechat" path="poc_wechat" inputCSS="text" mandatory="true"/>
		  					 </div>
							  
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.name" labelKey="profile.name" path="name" inputCSS="text" mandatory="true"/>
							  </div>
								
							  <fieldset disabled>	
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.defaultb2bunit" labelKey="profile.defaultb2bunit" path="defaultb2bunit" inputCSS="text" mandatory="true"/>
							  </div>
							  </fieldset>
								
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.phone1" labelKey="profile.phone1" path="tel_number" inputCSS="text" mandatory="true"/>
							  </div>
								
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.fax" labelKey="profile.fax" path="fax_number" inputCSS="text" mandatory="true"/>
							  </div>
								
							<fieldset disabled>
								
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.conno" labelKey="profile.conno" path="conno" inputCSS="text" mandatory="true"/>
							  </div>

							  <div class="form-group">
								<formElement:formInputBox idKey="profile.edate" labelKey="profile.edate" path="edate" inputCSS="text" mandatory="true"/>
							  </div>
								
							</fieldset>
								
						   
						   
						<div class="row">

						   
							   <div class="col-lg-6 col-sm-12 mb-3 accountActions">
							   		<ycommerce:testId code="personalDetails_cancelPersonalDetails_button">
	                                    <button type="button" class="btn btn-secondary btn-block backToHome">
	                                        <spring:theme code="text.account.profile.cancel" text="Cancel"/>
	                                    </button>	
                                	</ycommerce:testId>

                                	
							   </div>
							   

							   <div class="col-lg-6 col-sm-12 mb-3 accountActions">
								  <ycommerce:testId code="personalDetails_savePersonalDetails_button">
								    
								   <button type="submit" class="btn btn-primary btn-block">
                                        <spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/>
                                    </button>
								    
                                  </ycommerce:testId>
							   </div>
						   </div>
						   </form:form>
      					</div>
    				</div>
  				</div>
				
			<br>

  				<div class="card">
    				<div class="card-header" id="head4">
					  <div class="d-flex justify-content-between">
        				<button class="btn btn-link text-dark" data-toggle="collapse" data-target="#checkout4" aria-expanded="false" aria-controls="checkout4">
         				<h5><spring:theme code="profile.managerinfo"/></h5>
        				</button>
					  </div>	  
    				</div>

    				<div id="checkout4" class="collapse" aria-labelledby="head4" data-parent="#accordion">
      					<div class="card-body">
							
						<form:form action="update-profile" method="post" commandName="updateProfileForm">
								
							<fieldset disabled>
							  <div class="form-group">
								<formElement:formInputBox idKey="profile.uid" labelKey="profile.uid" path="uid" inputCSS="text" mandatory="true"/>
							  </div>
							</fieldset>
								
								<div class="form-group">
									<formElement:formInputBox idKey="profile.name_1" labelKey="profile.name_1" path="name1" inputCSS="text" mandatory="true"/>
								</div>

								<div class="form-group">
									<formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" mandatory="true"/>
								</div>

								<div class="form-group">
									<formElement:formInputBox idKey="profile.cellnumber" labelKey="profile.cellnumber" path="cell_number" inputCSS="text" mandatory="true"/>
								</div>

								<div class="form-group">
									<formElement:formInputBox idKey="profile.line" labelKey="profile.line" path="poc_line" inputCSS="text" mandatory="true"/>
								</div>

								<div class="form-group">
									<formElement:formInputBox idKey="profile.wechat" labelKey="profile.wechat" path="poc_wechat" inputCSS="text" mandatory="true"/>
								</div>
							
						   <div class="row">
							   <div class="col-lg-6 col-sm-12 mb-3 accountActions">
							   		<ycommerce:testId code="personalDetails_cancelPersonalDetails_button">
	                                    <button type="button" class="btn btn-secondary btn-block backToHome">
	                                        <spring:theme code="text.account.profile.cancel" text="Cancel"/>
	                                    </button>	
                                	</ycommerce:testId>

                                	
							   </div>
							   

							   <div class="col-lg-6 col-sm-12 mb-3 accountActions">
								  <ycommerce:testId code="personalDetails_savePersonalDetails_button">

								    <button type="submit" class="btn btn-primary btn-block">
                                        <spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/>
                                    </button>
								    
                                  </ycommerce:testId>
							   </div>
						   </div>
						   <div style="display:none">
							    <formElement:formSelectBox  idKey="profile.title" labelKey="profile.title" path="titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="form.select.empty" items="${titleData}" selectCSSClass="form-control"/>
					            <formElement:formInputBox idKey="profile.firstName" labelKey="profile.firstName" path="firstName" inputCSS="text" mandatory="true"/>
						        <formElement:formInputBox idKey="profile.lastName" labelKey="profile.lastName" path="lastName" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.name" labelKey="profile.name" path="name" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.defaultb2bunit" labelKey="profile.defaultb2bunit" path="defaultb2bunit" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.phone1" labelKey="profile.phone1" path="tel_number" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.fax" labelKey="profile.fax" path="fax_number" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.conno" labelKey="profile.conno" path="conno" inputCSS="text" mandatory="true"/>
								<formElement:formInputBox idKey="profile.edate" labelKey="profile.edate" path="edate" inputCSS="text" mandatory="true"/>
		  	               </div>
						   
						  </form:form>
						</div>
					</div>
    			</div>
				
			</div>		
			
		</div>
		
	  <div class="col-md-3"></div>
	
	</div>	
  <!-- /.container -->
</div> 
<%-- </form:form> --%>


<!-- <div class="account-section-header"> -->
<!--     <div class="row"> -->
<!--         <div class="container-lg col-md-6"> -->
<%--             <spring:theme code="text.account.profile.updatePersonalDetails"/> --%>
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->
<!-- <div class="row"> -->
<!--     <div class="container-lg col-md-6"> -->
<!--         <div class="account-section-content"> -->
<!--             <div class="account-section-form"> -->
<%--                 <form:form action="update-profile" method="post" commandName="updateProfileForm"> --%>

<%--                     <formElement:formSelectBox idKey="profile.title" labelKey="profile.title" path="titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="form.select.empty" items="${titleData}" selectCSSClass="form-control"/> --%>
<%--                     <formElement:formInputBox idKey="profile.firstName" labelKey="profile.firstName" path="firstName" inputCSS="text" mandatory="true"/> --%>
<%--                     <formElement:formInputBox idKey="profile.lastName" labelKey="profile.lastName" path="lastName" inputCSS="text" mandatory="true"/> --%>

<!--                     <div class="row"> -->
<!--                         <div class="col-sm-6 col-sm-push-6"> -->
<!--                             <div class="accountActions"> -->
<%--                                 <ycommerce:testId code="personalDetails_savePersonalDetails_button"> --%>
<!--                                     <button type="submit" class="btn btn-primary btn-block"> -->
<%--                                         <spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/> --%>
<!--                                     </button> -->
<%--                                 </ycommerce:testId> --%>
<!--                             </div> -->
<!--                         </div> -->
<!--                         <div class="col-sm-6 col-sm-pull-6"> -->
<!--                             <div class="accountActions"> -->
<%--                                 <ycommerce:testId code="personalDetails_cancelPersonalDetails_button"> --%>
<!--                                     <button type="button" class="btn btn-default btn-block backToHome"> -->
<%--                                         <spring:theme code="text.account.profile.cancel" text="Cancel"/> --%>
<!--                                     </button> -->
<%--                                 </ycommerce:testId> --%>
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<%--                 </form:form> --%>
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->