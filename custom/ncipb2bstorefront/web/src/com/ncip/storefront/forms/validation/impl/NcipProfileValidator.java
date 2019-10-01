/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with SAP.
 */
package com.ncip.storefront.forms.validation.impl;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.ncip.storefront.forms.NcipUpdateProfileForm;


/**
 * Validator for profile forms.
 */
@Component("ncipProfileValidator")
public class NcipProfileValidator implements Validator {

  @Override
  public boolean supports(final Class<?> aClass) {
    return NcipUpdateProfileForm.class.equals(aClass);
  }

  @Override
  public void validate(final Object object, final Errors errors) {
    final NcipUpdateProfileForm profileForm = (NcipUpdateProfileForm) object;

    final String cell_number = profileForm.getCell_number(); // 手機
    final String name = profileForm.getName(); // 會員名稱
    final String name1 = profileForm.getName1(); // 帳號管理人
    final String email = profileForm.getEmail(); // Email
    final String tel_number = profileForm.getTel_number(); // 公司電話

    if (StringUtils.isEmpty(cell_number)) {
      errors.rejectValue("cell_number", "profile.title.invalid");
    } else if (StringUtils.length(cell_number) > 255) {
      errors.rejectValue("cell_number", "profile.title.invalid");
    }

    if (StringUtils.isEmpty(name)) {
      errors.rejectValue("name", "profile.title.invalid");
    } else if (StringUtils.length(name) > 255) {
      errors.rejectValue("name", "profile.title.invalid");
    }

    if (StringUtils.isEmpty(name1)) {
      errors.rejectValue("name1", "profile.title.invalid");
    } else if (StringUtils.length(name1) > 255) {
      errors.rejectValue("name1", "profile.title.invalid");
    }

    if (StringUtils.isEmpty(email)) {
      errors.rejectValue("email", "profile.title.invalid");
    } else if (StringUtils.length(email) > 255) {
      errors.rejectValue("email", "profile.title.invalid");
    }

    if (StringUtils.isEmpty(tel_number)) {
      errors.rejectValue("tel_number", "profile.title.invalid");
    } else if (StringUtils.length(tel_number) > 255) {
      errors.rejectValue("tel_number", "profile.title.invalid");
    }


    final String title = profileForm.getTitleCode();
    final String firstName = profileForm.getFirstName();
    final String lastName = profileForm.getLastName();

    if (StringUtils.isEmpty(title)) {
      errors.rejectValue("titleCode", "profile.title.invalid");
    } else if (StringUtils.length(title) > 255) {
      errors.rejectValue("titleCode", "profile.title.invalid");
    }

    if (StringUtils.isBlank(firstName)) {
      errors.rejectValue("firstName", "profile.firstName.invalid");
    } else if (StringUtils.length(firstName) > 255) {
      errors.rejectValue("firstName", "profile.firstName.invalid");
    }

    if (StringUtils.isBlank(lastName)) {
      errors.rejectValue("lastName", "profile.lastName.invalid");
    } else if (StringUtils.length(lastName) > 255) {
      errors.rejectValue("lastName", "profile.lastName.invalid");
    }
  }

}
