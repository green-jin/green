/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with SAP.
 */
package com.ncip.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.user.UserModel} as source and
 * {@link de.hybris.platform.commercefacades.user.data.CustomerData} as target type.
 */
public class NcipCustomerPopulator implements Populator<CustomerModel, CustomerData> {
  private CommonI18NService commonI18NService;
  private Converter<CurrencyModel, CurrencyData> currencyConverter;
  private Converter<LanguageModel, LanguageData> languageConverter;
  private CustomerNameStrategy customerNameStrategy;

  protected Converter<CurrencyModel, CurrencyData> getCurrencyConverter() {
    return currencyConverter;
  }

  @Required
  public void setCurrencyConverter(final Converter<CurrencyModel, CurrencyData> currencyConverter) {
    this.currencyConverter = currencyConverter;
  }

  protected Converter<LanguageModel, LanguageData> getLanguageConverter() {
    return languageConverter;
  }

  @Required
  public void setLanguageConverter(final Converter<LanguageModel, LanguageData> languageConverter) {
    this.languageConverter = languageConverter;
  }

  protected CustomerNameStrategy getCustomerNameStrategy() {
    return customerNameStrategy;
  }

  @Required
  public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy) {
    this.customerNameStrategy = customerNameStrategy;
  }

  @Override
  public void populate(final CustomerModel source, final CustomerData target) {


    Assert.notNull(source, "Parameter source cannot be null.");
    Assert.notNull(target, "Parameter target cannot be null.");

    

    if (source instanceof B2BCustomerModel) {
      
      final B2BCustomerModel customer = (B2BCustomerModel) source;
      
      if (source.getSessionCurrency() != null) {
        target.setCurrency(getCurrencyConverter().convert(source.getSessionCurrency()));
      }

      if (source.getSessionLanguage() != null) {
        target.setLanguage(getLanguageConverter().convert(source.getSessionLanguage()));
      }

      final String[] names = getCustomerNameStrategy().splitName(source.getName());
      if (names != null) {
        target.setFirstName(names[0]);
        target.setLastName(names[1]);
      }

      target.setUid(customer.getUid());
      target.setName(customer.getName());

      if (customer.getTitle() != null) {
        target.setTitleCode(customer.getTitle().getCode());
      }

      target.setActive(Boolean.TRUE.equals(customer.getActive()));
      
      target.setName(source.getName());
      target.setName1(source.getName());
      target.setCustomerId(source.getCustomerID());
      target.setDeactivationDate(source.getDeactivationDate());
      target.setCell_number(customer.getCell_number()); // 手機
      target.setPoc_line(customer.getPoc_line()); // Line
      target.setPoc_wechat(customer.getPoc_wechat()); // Wechat
      target.setEmail(customer.getEmail()); //Email
      if(customer.getDefaultB2BUnit() != null) {
        target.setEdate(customer.getDefaultB2BUnit().getEDate()); // 合約到期日
        target.setConno(customer.getDefaultB2BUnit().getConno()); // 合約編號
        target.setDefaultb2bunit(customer.getDefaultB2BUnit().getUid()); // 統一編號
        target.setTel_number(customer.getDefaultB2BUnit().getPhone1()); // 公司電話
        target.setFax_number(customer.getDefaultB2BUnit().getFax()); // 公司傳真
      }
 
      
      if (customer.getOriginalUid() != null) {
        target.setDisplayUid(customer.getOriginalUid());
      }
    }



  }

  protected CommonI18NService getCommonI18NService() {
    return commonI18NService;
  }

  @Required
  public void setCommonI18NService(final CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }


  protected void setUid(final UserModel source, final CustomerData target) {
    target.setUid(source.getUid());
    if (source instanceof CustomerModel) {
      final CustomerModel customer = (CustomerModel) source;
      if (isOriginalUidAvailable(customer)) {
        target.setDisplayUid(customer.getOriginalUid());
      }
    }
  }

  protected boolean isOriginalUidAvailable(final CustomerModel source) {
    return source.getOriginalUid() != null;
  }
}
