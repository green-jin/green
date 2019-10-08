/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncip.storefront.controllers.pages;

import com.ncip.facades.account.impl.DefaultNcipCustomerFacade;
import com.ncip.facades.product.data.DownloadPdfData;
import de.hybris.platform.acceleratorfacades.ordergridform.OrderGridFormFacade;
import de.hybris.platform.acceleratorfacades.product.data.ReadOnlyOrderGridData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EmailValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.PasswordValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.verification.AddressVerificationResultHandler;
import de.hybris.platform.acceleratorstorefrontcommons.strategy.CustomerConsentDataStrategy;
import de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.consent.exceptions.CommerceConsentGivenException;
import de.hybris.platform.commerceservices.consent.exceptions.CommerceConsentWithdrawnException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.CountryType;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import com.ncip.storefront.controllers.ControllerConstants;
import com.ncip.storefront.forms.NcipUpdateProfileForm;
import com.ncip.storefront.forms.validation.impl.NcipProfileValidator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;


import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/my-account")
public class AccountPageController extends AbstractSearchPageController {

  private static final String TEXT_ACCOUNT_ADDRESS_BOOK = "text.account.addressBook";
  private static final String BREADCRUMBS_ATTR = "breadcrumbs";
  private static final String IS_DEFAULT_ADDRESS_ATTR = "isDefaultAddress";
  private static final String COUNTRY_DATA_ATTR = "countryData";
  private static final String ADDRESS_BOOK_EMPTY_ATTR = "addressBookEmpty";
  private static final String TITLE_DATA_ATTR = "titleData";
  private static final String FORM_GLOBAL_ERROR = "form.global.error";
  private static final String PROFILE_CURRENT_PASSWORD_INVALID = "profile.currentPassword.invalid";
  private static final String TEXT_ACCOUNT_PROFILE = "text.account.profile";
  private static final String ADDRESS_DATA_ATTR = "addressData";
  private static final String ADDRESS_FORM_ATTR = "addressForm";
  private static final String COUNTRY_ATTR = "country";
  private static final String REGIONS_ATTR = "regions";
  private static final String MY_ACCOUNT_ADDRESS_BOOK_URL = "/my-account/address-book";
  private static final String TEXT_ACCOUNT_CONSENT_MANAGEMENT = "text.account.consent.consentManagement";
  private static final String TEXT_ACCOUNT_CONSENT_GIVEN = "text.account.consent.given";
  private static final String TEXT_ACCOUNT_CONSENT_WITHDRAWN = "text.account.consent.withdrawn";
  private static final String TEXT_ACCOUNT_CONSENT_NOT_FOUND = "text.account.consent.notFound";
  private static final String TEXT_ACCOUNT_CONSENT_TEMPLATE_NOT_FOUND = "text.account.consent.template.notFound";
  private static final String TEXT_ACCOUNT_CLOSE = "text.account.close";
  private static final String TEXT_ACCOUNT_CONSENT_ALREADY_GIVEN = "text.account.consent.already.given";
  private static final String TEXT_ACCOUNT_CONSENT_ALREADY_WITHDRAWN = "text.account.consent.already.withdrawn";

  // Internal Redirects
  private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE =
      REDIRECT_PREFIX + MY_ACCOUNT_ADDRESS_BOOK_URL;
  private static final String REDIRECT_TO_PAYMENT_INFO_PAGE =
      REDIRECT_PREFIX + "/my-account/payment-details";
  private static final String REDIRECT_TO_EDIT_ADDRESS_PAGE =
      REDIRECT_PREFIX + "/my-account/edit-address/";
  private static final String REDIRECT_TO_UPDATE_EMAIL_PAGE =
      REDIRECT_PREFIX + "/my-account/update-email";
  private static final String REDIRECT_TO_UPDATE_PROFILE =
      REDIRECT_PREFIX + "/my-account/update-profile";
  private static final String REDIRECT_TO_PASSWORD_UPDATE_PAGE =
      REDIRECT_PREFIX + "/my-account/update-password";
  private static final String REDIRECT_TO_ORDER_HISTORY_PAGE =
      REDIRECT_PREFIX + "/my-account/orders";
  private static final String REDIRECT_TO_CONSENT_MANAGEMENT =
      REDIRECT_PREFIX + "/my-account/consents";
  private static final String REDIRECT_TO_ACCOUNTING_DETAIL =
      REDIRECT_PREFIX + "/my-account/accounting-detail";
  private static final String REDIRECT_TO_ACCOUNTING_DETAIL_DOWNLOAD =
      "/my-account/accounting-detail/download";
  /**
   * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly
   * extracted if it contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164
   * for a discussion on the issue and future resolution.
   */
  private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
  private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";

  // CMS Pages
  private static final String ACCOUNT_CMS_PAGE = "account";
  private static final String PROFILE_CMS_PAGE = "profile";
  private static final String UPDATE_PASSWORD_CMS_PAGE = "updatePassword";
  private static final String UPDATE_PROFILE_CMS_PAGE = "update-profile";
  private static final String UPDATE_EMAIL_CMS_PAGE = "update-email";
  private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
  private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
  private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
  private static final String ORDER_HISTORY_CMS_PAGE = "orders";
  private static final String ORDER_DETAIL_CMS_PAGE = "order";
  private static final String CONSENT_MANAGEMENT_CMS_PAGE = "consents";
  private static final String CLOSE_ACCOUNT_CMS_PAGE = "close-account";
  private static final String ACCOUNTING_DETAIL_PAGE = "accounting-detail";

  private static final Logger LOG = Logger.getLogger(AccountPageController.class);


  @Resource(name = "orderFacade")
  private OrderFacade orderFacade;

  @Resource(name = "acceleratorCheckoutFacade")
  private CheckoutFacade checkoutFacade;

  @Resource(name = "userFacade")
  private UserFacade userFacade;

  @Resource(name = "customerFacade")
  private CustomerFacade customerFacade;

  @Resource(name = "accountBreadcrumbBuilder")
  private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

  @Resource(name = "passwordValidator")
  private PasswordValidator passwordValidator;

  @Resource(name = "addressValidator")
  private AddressValidator addressValidator;

  @Resource(name = "profileValidator")
  private ProfileValidator profileValidator;

  @Resource(name = "ncipProfileValidator")
  private NcipProfileValidator ncipProfileValidator;

  @Resource(name = "emailValidator")
  private EmailValidator emailValidator;

  @Resource(name = "i18NFacade")
  private I18NFacade i18NFacade;

  @Resource(name = "addressVerificationFacade")
  private AddressVerificationFacade addressVerificationFacade;

  @Resource(name = "addressVerificationResultHandler")
  private AddressVerificationResultHandler addressVerificationResultHandler;

  @Resource(name = "orderGridFormFacade")
  private OrderGridFormFacade orderGridFormFacade;

  @Resource(name = "customerConsentDataStrategy")
  protected CustomerConsentDataStrategy customerConsentDataStrategy;

  @Resource(name = "addressDataUtil")
  private AddressDataUtil addressDataUtil;

  @Resource
  private ConfigurationService configurationService;

  @Resource
  private UserService userService;

  @Resource
  private DefaultNcipCustomerFacade ncipCustomerFacade;

  protected PasswordValidator getPasswordValidator() {
    return passwordValidator;
  }

  protected AddressValidator getAddressValidator() {
    return addressValidator;
  }

  protected ProfileValidator getProfileValidator() {
    return profileValidator;
  }

  protected NcipProfileValidator getNcipProfileValidator() {
    return ncipProfileValidator;
  }

  protected EmailValidator getEmailValidator() {
    return emailValidator;
  }

  protected I18NFacade getI18NFacade() {
    return i18NFacade;
  }

  protected AddressVerificationFacade getAddressVerificationFacade() {
    return addressVerificationFacade;
  }

  protected AddressVerificationResultHandler getAddressVerificationResultHandler() {
    return addressVerificationResultHandler;
  }

  @ModelAttribute("countries")
  public Collection<CountryData> getCountries() {
    return checkoutFacade.getCountries(CountryType.SHIPPING);
  }

  @ModelAttribute("titles")
  public Collection<TitleData> getTitles() {
    return userFacade.getTitles();
  }

  @ModelAttribute("countryDataMap")
  public Map<String, CountryData> getCountryDataMap() {
    final Map<String, CountryData> countryDataMap = new HashMap<>();
    for (final CountryData countryData : getCountries()) {
      countryDataMap.put(countryData.getIsocode(), countryData);
    }
    return countryDataMap;
  }
  
  @RequestMapping(value = "/accounting-detail", method = { RequestMethod.GET, RequestMethod.POST })
  @RequireHardLogIn
  public String AccountingDetail(final Model model, HttpServletRequest request,
      HttpServletResponse response)
      throws CMSItemNotFoundException, IOException {

    int num = 12; //default 撈取期間設定為一年

    //篩選條件
    String numValue = request.getParameter("num");
    if(numValue != null && !numValue.equals("")){
      num = Integer.valueOf(numValue).intValue();
    }

    //客戶資料
    CustomerData customerData = ncipCustomerFacade.getCurrentCustomer();
    //客戶代碼

    String customerid = customerData.getCustomerId();
    model.addAttribute("customerData", customerData);

    //系統日期
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMM");

    List<DownloadPdfData> datas = new ArrayList<>();

    //For篩選日期用(3,6,12),取一整年的日期
    Date date = new Date(); //本月
    Calendar calendar = Calendar.getInstance();

    //取得路徑下的FileName (檔名命名規則: 客戶代碼+編號)
    for (int i = 0; i < num ; i++) {
      calendar.setTime(date);
      calendar.add(Calendar.MONTH, -i);
      String strDate = sdFormat.format(calendar.getTime());

      String pdfPath =
          configurationService.getConfiguration().getString("accountingDetailFolder", "")
          + File.separator
          + strDate;

      File allfiles = new File(pdfPath); //路徑下所有file

      String[] files = allfiles.list();
      for (String file : files) {
        if (file.contains(customerid)) {
          DownloadPdfData downloadPdfData = new DownloadPdfData();
          downloadPdfData.setFilename(file);
          downloadPdfData.setUrl(pdfPath + File.separator + file);
          downloadPdfData.setDate_year(strDate.substring(0, 4));
          downloadPdfData.setDate_month(strDate.substring(4));
          if (i < 3) {
            downloadPdfData.setFilterdate("3");
          } else if (i < 6 && i >= 3) {
            downloadPdfData.setFilterdate("6");
          } else {
            downloadPdfData.setFilterdate("12");
          }
          datas.add(downloadPdfData);
       }
      }
    }

    model.addAttribute("allFiles", datas);
    model.addAttribute("filternum",num);
    //For Header Breadcrumb
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs("text.account.accountingDetail"));

    // Download PDF
    model.addAttribute("redirectUrl", REDIRECT_TO_ACCOUNTING_DETAIL_DOWNLOAD);

    //get page
    storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNTING_DETAIL_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNTING_DETAIL_PAGE));
    return getViewForPage(model);
  }

  @RequestMapping(value = "/accounting-detail/download", method = RequestMethod.GET)
  public String downloadPDFfile(
      Model model,
      HttpServletRequest request,
    HttpServletResponse response)
      throws CMSItemNotFoundException, IOException {

//下載檔案的網址
    String fileindex =request.getParameter("accounting_index");    //儲存檔名
    String path = request.getParameter("stu_n");
    String fileName =request.getParameter("accounting_fileName");    //儲存檔名

    String outputFile =
        configurationService.getConfiguration().getString("accountingDetailSaveFileFolder", "")
            + File.separator
            + fileName;

    //设置响应头，控制浏览器下载该文件
    response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

    File desFile = new File(outputFile);

    if (desFile.exists())
      desFile.delete();

    FileInputStream fis = new FileInputStream(path);

    byte[] buffer = new byte[1024]; // InputStream

    // 設定接收資料流來源 ,就是要下載的網址
    BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
    // 設定　儲存要下載檔案的位置.
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));
    int length = -1;
    while ((length = bufferedInputStream.read(buffer)) != -1) {
      bufferedOutputStream.write(buffer,0,length);
    }
    bufferedOutputStream.flush();// 將緩衝區中的資料全部寫出
    bufferedInputStream.close(); // 關閉資料流
    bufferedOutputStream.close();

    return getViewForPage(model);
  }


  @RequestMapping(value = "/addressform", method = RequestMethod.GET)
  public String getCountryAddressForm(@RequestParam("addressCode") final String addressCode,
      @RequestParam("countryIsoCode") final String countryIsoCode, final Model model) {
    model.addAttribute("supportedCountries", getCountries());
    populateModelRegionAndCountry(model, countryIsoCode);

    final AddressForm addressForm = new AddressForm();
    model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
    for (final AddressData addressData : userFacade.getAddressBook()) {
      if (addressData.getId() != null && addressData.getId().equals(addressCode)
          && countryIsoCode.equals(addressData.getCountry().getIsocode())) {
        model.addAttribute(ADDRESS_DATA_ATTR, addressData);
        addressDataUtil.convert(addressData, addressForm);
        break;
      }
    }
    return ControllerConstants.Views.Fragments.Account.CountryAddressForm;
  }

  protected void populateModelRegionAndCountry(final Model model, final String countryIsoCode) {
    model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(countryIsoCode));
    model.addAttribute(COUNTRY_ATTR, countryIsoCode);
  }

  @RequestMapping(method = RequestMethod.GET)
  @RequireHardLogIn
  public String account(final Model model, final RedirectAttributes redirectModel)
      throws CMSItemNotFoundException {
    if (ResponsiveUtils.isResponsive()) {
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          "system.error.page.not.found", null);
      return REDIRECT_PREFIX + "/";
    }
    storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(null));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/orders", method = RequestMethod.GET)
  @RequireHardLogIn
  public String orders(@RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
      @RequestParam(value = "sort", required = false) final String sortCode, final Model model)
      throws CMSItemNotFoundException {
    // Handle paged search results
    final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
    final SearchPageData<OrderHistoryData> searchPageData = orderFacade
        .getPagedOrderHistoryForStatuses(pageableData);
    populateModel(model, searchPageData, showMode);

    storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs("text.account.orderHistory"));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
  @RequireHardLogIn
  public String order(@PathVariable("orderCode") final String orderCode, final Model model,
      final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
    try {
      final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
      model.addAttribute("orderData", orderDetails);

      final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
      breadcrumbs.add(new Breadcrumb("/my-account/orders",
          getMessageSource()
              .getMessage("text.account.orderHistory", null, getI18nService().getCurrentLocale()),
          null));
      breadcrumbs.add(new Breadcrumb("#",
          getMessageSource().getMessage("text.account.order.orderBreadcrumb", new Object[]
              {orderDetails.getCode()}, "Order {0}", getI18nService().getCurrentLocale()), null));
      model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);

    } catch (final UnknownIdentifierException e) {
      LOG.warn("Attempted to load a order that does not exist or is not visible", e);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          "system.error.page.not.found", null);
      return REDIRECT_TO_ORDER_HISTORY_PAGE;
    }
    storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
    return getViewForPage(model);
  }

  @RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN
      + "/getReadOnlyProductVariantMatrix", method = RequestMethod.GET)
  @RequireHardLogIn
  public String getProductVariantMatrixForResponsive(
      @PathVariable("orderCode") final String orderCode,
      @RequestParam("productCode") final String productCode, final Model model) {
    final OrderData orderData = orderFacade.getOrderDetailsForCodeWithoutUser(orderCode);

    final Map<String, ReadOnlyOrderGridData> readOnlyMultiDMap = orderGridFormFacade
        .getReadOnlyOrderGridForProductInOrder(
            productCode, Arrays.asList(ProductOption.BASIC, ProductOption.CATEGORIES), orderData);
    model.addAttribute("readOnlyMultiDMap", readOnlyMultiDMap);

    return ControllerConstants.Views.Fragments.Checkout.ReadOnlyExpandedOrderForm;
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  @RequireHardLogIn
  public String profile(final Model model) throws CMSItemNotFoundException {
    final List<TitleData> titles = userFacade.getTitles();

    final CustomerData customerData = customerFacade.getCurrentCustomer();
    if (customerData.getTitleCode() != null) {
      model.addAttribute("title", findTitleForCode(titles, customerData.getTitleCode()));
    }

    model.addAttribute("customerData", customerData);

    storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  protected TitleData findTitleForCode(final List<TitleData> titles, final String code) {
    if (code != null && !code.isEmpty() && titles != null && !titles.isEmpty()) {
      for (final TitleData title : titles) {
        if (code.equals(title.getCode())) {
          return title;
        }
      }
    }
    return null;
  }

  @RequestMapping(value = "/update-email", method = RequestMethod.GET)
  @RequireHardLogIn
  public String editEmail(final Model model) throws CMSItemNotFoundException {
    final CustomerData customerData = customerFacade.getCurrentCustomer();
    final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

    updateEmailForm.setEmail(customerData.getDisplayUid());

    model.addAttribute("updateEmailForm", updateEmailForm);

    storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/update-email", method = RequestMethod.POST)
  @RequireHardLogIn
  public String updateEmail(final UpdateEmailForm updateEmailForm,
      final BindingResult bindingResult, final Model model,
      final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
    getEmailValidator().validate(updateEmailForm, bindingResult);
    String returnAction = REDIRECT_TO_UPDATE_EMAIL_PAGE;

    if (!bindingResult.hasErrors() && !updateEmailForm.getEmail()
        .equals(updateEmailForm.getChkEmail())) {
      bindingResult.rejectValue("chkEmail", "validation.checkEmail.equals", new Object[]{},
          "validation.checkEmail.equals");
    }

    if (bindingResult.hasErrors()) {
      returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
    } else {
      try {
        customerFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
        GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
            "text.account.profile.confirmationUpdated", null);

        // Replace the spring security authentication with the new UID
        final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
        final Authentication oldAuthentication = SecurityContextHolder.getContext()
            .getAuthentication();
        final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
            newUid, null,
            oldAuthentication.getAuthorities());
        newAuthentication.setDetails(oldAuthentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
      } catch (final DuplicateUidException e) {
        bindingResult.rejectValue("email", "profile.email.unique");
        returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
      } catch (final PasswordMismatchException passwordMismatchException) {
        bindingResult.rejectValue("password", PROFILE_CURRENT_PASSWORD_INVALID);
        returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
      }
    }

    return returnAction;
  }

  protected String setErrorMessagesAndCMSPage(final Model model, final String cmsPageLabelOrId)
      throws CMSItemNotFoundException {
    GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
    storeCmsPageInModel(model, getContentPageForLabelOrId(cmsPageLabelOrId));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(cmsPageLabelOrId));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
    return getViewForPage(model);
  }


  @RequestMapping(value = "/update-profile", method = RequestMethod.GET)
  @RequireHardLogIn
  public String editProfile(final Model model) throws CMSItemNotFoundException {
    model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());

    final CustomerData customerData = customerFacade.getCurrentCustomer();

    final NcipUpdateProfileForm updateProfileForm = new NcipUpdateProfileForm();

    updateProfileForm.setTitleCode(customerData.getTitleCode());
    updateProfileForm.setFirstName(customerData.getFirstName());
    updateProfileForm.setLastName(customerData.getLastName());
    updateProfileForm.setDefaultb2bunit(customerData.getDefaultb2bunit());
    updateProfileForm.setPoc_line(customerData.getPoc_line());
    updateProfileForm.setTel_number(customerData.getTel_number());
    updateProfileForm.setFax_number(customerData.getFax_number());
    updateProfileForm.setName(customerData.getName());
    updateProfileForm.setName1(customerData.getName1());
    updateProfileForm.setConno(customerData.getConno());
    updateProfileForm.setEdate(customerData.getEdate());
    updateProfileForm.setCell_number(customerData.getCell_number());
    updateProfileForm.setPoc_line(customerData.getPoc_line());
    updateProfileForm.setPoc_wechat(customerData.getPoc_wechat());
    updateProfileForm.setUid(customerData.getUid());
    updateProfileForm.setEmail(customerData.getEmail());

    model.addAttribute("updateProfileForm", updateProfileForm);

    storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/update-profile", method = RequestMethod.POST)
  @RequireHardLogIn
  public String updateProfile(final NcipUpdateProfileForm updateProfileForm,
      final BindingResult bindingResult, final Model model,
      final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
    getNcipProfileValidator().validate(updateProfileForm, bindingResult);
//		getProfileValidator().validate(updateProfileForm, bindingResult);

    String returnAction = REDIRECT_TO_UPDATE_PROFILE;
    final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
    final CustomerData customerData = new CustomerData();
    customerData.setTitleCode(updateProfileForm.getTitleCode());
    customerData.setFirstName(updateProfileForm.getFirstName());
    customerData.setLastName(updateProfileForm.getLastName());
    customerData.setDefaultb2bunit(updateProfileForm.getDefaultb2bunit());
    customerData.setPoc_line(updateProfileForm.getPoc_line());
    customerData.setTel_number(updateProfileForm.getTel_number());
    customerData.setFax_number(updateProfileForm.getFax_number());
    customerData.setName(updateProfileForm.getName());
    customerData.setName1(updateProfileForm.getName1());
    customerData.setConno(updateProfileForm.getConno());
    customerData.setEdate(updateProfileForm.getEdate());
    customerData.setCell_number(updateProfileForm.getCell_number());
    customerData.setPoc_line(updateProfileForm.getPoc_line());
    customerData.setPoc_wechat(updateProfileForm.getPoc_wechat());
    customerData.setEmail(updateProfileForm.getEmail());

    customerData.setUid(currentCustomerData.getUid());
    customerData.setDisplayUid(currentCustomerData.getDisplayUid());

    model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());

    storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

    if (bindingResult.hasErrors()) {
//			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
      GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
          "profile.validation.error", null);
    } else {
      try {
        customerFacade.updateProfile(customerData);
        GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
            "text.account.profile.confirmationUpdated", null);

      } catch (final DuplicateUidException e) {
        GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
            "registration.error.account.exists.title", null);
//				bindingResult.rejectValue("email", "registration.error.account.exists.title");
//				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
      }
    }

    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
    return returnAction;
  }

  @RequestMapping(value = "/update-password", method = RequestMethod.GET)
  @RequireHardLogIn
  public String updatePassword(final Model model) throws CMSItemNotFoundException {
    final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

    model.addAttribute("updatePasswordForm", updatePasswordForm);

    storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/update-password", method = RequestMethod.POST)
  @RequireHardLogIn
  public String updatePassword(final UpdatePasswordForm updatePasswordForm,
      final BindingResult bindingResult, final Model model,
      final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
    getPasswordValidator().validate(updatePasswordForm, bindingResult);
    if (!bindingResult.hasErrors()) {
      if (updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword())) {
        try {
          customerFacade.changePassword(updatePasswordForm.getCurrentPassword(),
              updatePasswordForm.getNewPassword());
        } catch (final PasswordMismatchException localException) {
          bindingResult
              .rejectValue("currentPassword", PROFILE_CURRENT_PASSWORD_INVALID, new Object[]{},
                  PROFILE_CURRENT_PASSWORD_INVALID);
        }
      } else {
        bindingResult.rejectValue("checkNewPassword", "validation.checkPwd.equals", new Object[]{},
            "validation.checkPwd.equals");
      }
    }

    if (bindingResult.hasErrors()) {
      GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
      storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
      setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

      model.addAttribute(BREADCRUMBS_ATTR,
          accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
      return getViewForPage(model);
    } else {
      GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
          "text.account.confirmation.password.updated", null);
      return REDIRECT_TO_PASSWORD_UPDATE_PAGE;
    }
  }

  @RequestMapping(value = "/address-book", method = RequestMethod.GET)
  @RequireHardLogIn
  public String getAddressBook(final Model model) throws CMSItemNotFoundException {
    model.addAttribute(ADDRESS_DATA_ATTR, userFacade.getAddressBook());

    storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_ADDRESS_BOOK));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/add-address", method = RequestMethod.GET)
  @RequireHardLogIn
  public String addAddress(final Model model) throws CMSItemNotFoundException {
    model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getCountries(CountryType.SHIPPING));
    model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
    final AddressForm addressForm = getPreparedAddressForm();
    model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
    model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR,
        Boolean.valueOf(CollectionUtils.isEmpty(userFacade.getAddressBook())));
    model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.FALSE);
    storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

    final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
    breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ADDRESS_BOOK_URL,
        getMessageSource()
            .getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null, getI18nService().getCurrentLocale()),
        null));
    breadcrumbs.add(new Breadcrumb("#",
        getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
            getI18nService().getCurrentLocale()),
        null));
    model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  protected AddressForm getPreparedAddressForm() {
    final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
    final AddressForm addressForm = new AddressForm();
    addressForm.setFirstName(currentCustomerData.getFirstName());
    addressForm.setLastName(currentCustomerData.getLastName());
    addressForm.setTitleCode(currentCustomerData.getTitleCode());
    return addressForm;
  }

  @RequestMapping(value = "/add-address", method = RequestMethod.POST)
  @RequireHardLogIn
  public String addAddress(final AddressForm addressForm, final BindingResult bindingResult,
      final Model model,
      final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
    getAddressValidator().validate(addressForm, bindingResult);
    if (bindingResult.hasErrors()) {
      GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
      storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpAddressFormAfterError(addressForm, model);
      return getViewForPage(model);
    }

    final AddressData newAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

    if (CollectionUtils.isEmpty(userFacade.getAddressBook())) {
      newAddress.setDefaultAddress(true);
    } else {
      newAddress.setDefaultAddress(
          addressForm.getDefaultAddress() != null && addressForm.getDefaultAddress()
              .booleanValue());
    }

    final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
        .verifyAddressData(newAddress);
    final boolean addressRequiresReview = getAddressVerificationResultHandler()
        .handleResult(verificationResult, newAddress,
            model, redirectModel, bindingResult,
            getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
            "checkout.multi.address.added");

    populateModelRegionAndCountry(model, addressForm.getCountryIso());
    model.addAttribute("edit", Boolean.FALSE);
    model.addAttribute(IS_DEFAULT_ADDRESS_ATTR,
        Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));

    if (addressRequiresReview) {
      storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      return getViewForPage(model);
    }

    userFacade.addAddress(newAddress);

    GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
        "account.confirmation.address.added",
        null);

    return REDIRECT_TO_EDIT_ADDRESS_PAGE + newAddress.getId();
  }

  protected void setUpAddressFormAfterError(final AddressForm addressForm, final Model model) {
    model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getCountries(CountryType.SHIPPING));
    model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
    model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR,
        Boolean.valueOf(CollectionUtils.isEmpty(userFacade.getAddressBook())));
    model.addAttribute(IS_DEFAULT_ADDRESS_ATTR,
        Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));
    if (addressForm.getCountryIso() != null) {
      populateModelRegionAndCountry(model, addressForm.getCountryIso());
    }
  }

  @RequestMapping(value = "/edit-address/"
      + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
  @RequireHardLogIn
  public String editAddress(@PathVariable("addressCode") final String addressCode,
      final Model model)
      throws CMSItemNotFoundException {
    final AddressForm addressForm = new AddressForm();
    model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getCountries(CountryType.SHIPPING));
    model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
    model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
    final List<AddressData> addressBook = userFacade.getAddressBook();
    model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR,
        Boolean.valueOf(CollectionUtils.isEmpty(addressBook)));

    for (final AddressData addressData : addressBook) {
      if (addressData.getId() != null && addressData.getId().equals(addressCode)) {
        model.addAttribute(REGIONS_ATTR,
            getI18NFacade().getRegionsForCountryIso(addressData.getCountry().getIsocode()));
        model.addAttribute(COUNTRY_ATTR, addressData.getCountry().getIsocode());
        model.addAttribute(ADDRESS_DATA_ATTR, addressData);
        addressDataUtil.convert(addressData, addressForm);

        if (userFacade.isDefaultAddress(addressData.getId())) {
          addressForm.setDefaultAddress(Boolean.TRUE);
          model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.TRUE);
        } else {
          addressForm.setDefaultAddress(Boolean.FALSE);
          model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.FALSE);
        }
        break;
      }
    }

    storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

    final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
    breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ADDRESS_BOOK_URL,
        getMessageSource()
            .getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null, getI18nService().getCurrentLocale()),
        null));
    breadcrumbs.add(new Breadcrumb("#",
        getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
            getI18nService().getCurrentLocale()),
        null));
    model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    model.addAttribute("edit", Boolean.TRUE);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/edit-address/"
      + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
  @RequireHardLogIn
  public String editAddress(final AddressForm addressForm, final BindingResult bindingResult,
      final Model model,
      final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
    getAddressValidator().validate(addressForm, bindingResult);
    if (bindingResult.hasErrors()) {
      GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
      storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpAddressFormAfterError(addressForm, model);
      return getViewForPage(model);
    }

    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

    final AddressData newAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

    if (Boolean.TRUE.equals(addressForm.getDefaultAddress())
        || userFacade.getAddressBook().size() <= 1) {
      newAddress.setDefaultAddress(true);
    }

    final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
        .verifyAddressData(newAddress);
    final boolean addressRequiresReview = getAddressVerificationResultHandler()
        .handleResult(verificationResult, newAddress,
            model, redirectModel, bindingResult,
            getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
            "checkout.multi.address.updated");

    model.addAttribute(REGIONS_ATTR,
        getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
    model.addAttribute(COUNTRY_ATTR, addressForm.getCountryIso());
    model.addAttribute("edit", Boolean.TRUE);
    model.addAttribute(IS_DEFAULT_ADDRESS_ATTR,
        Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));

    if (addressRequiresReview) {
      storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
      return getViewForPage(model);
    }

    userFacade.editAddress(newAddress);

    GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
        "account.confirmation.address.updated",
        null);
    return REDIRECT_TO_EDIT_ADDRESS_PAGE + newAddress.getId();
  }

  @RequestMapping(value = "/select-suggested-address", method = RequestMethod.POST)
  public String doSelectSuggestedAddress(final AddressForm addressForm,
      final RedirectAttributes redirectModel) {
    final Set<String> resolveCountryRegions = org.springframework.util.StringUtils
        .commaDelimitedListToSet(Config.getParameter("resolve.country.regions"));

    final AddressData selectedAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

    final CountryData countryData = selectedAddress.getCountry();

    if (!resolveCountryRegions.contains(countryData.getIsocode())) {
      selectedAddress.setRegion(null);
    }

    if (Boolean.TRUE.equals(addressForm.getEditAddress())) {
      userFacade.editAddress(selectedAddress);
    } else {
      userFacade.addAddress(selectedAddress);
    }

    GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
        "account.confirmation.address.added");

    return REDIRECT_TO_ADDRESS_BOOK_PAGE;
  }

  @RequestMapping(value = "/remove-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method =
      {RequestMethod.GET, RequestMethod.POST})
  @RequireHardLogIn
  public String removeAddress(@PathVariable("addressCode") final String addressCode,
      final RedirectAttributes redirectModel) {
    final AddressData addressData = new AddressData();
    addressData.setId(addressCode);
    userFacade.removeAddress(addressData);

    GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
        "account.confirmation.address.removed");
    return REDIRECT_TO_ADDRESS_BOOK_PAGE;
  }

  @RequestMapping(value = "/set-default-address/"
      + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
  @RequireHardLogIn
  public String setDefaultAddress(@PathVariable("addressCode") final String addressCode,
      final RedirectAttributes redirectModel) {
    final AddressData addressData = new AddressData();
    addressData.setDefaultAddress(true);
    addressData.setVisibleInAddressBook(true);
    addressData.setId(addressCode);
    userFacade.setDefaultAddress(addressData);
    GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
        "account.confirmation.default.address.changed");
    return REDIRECT_TO_ADDRESS_BOOK_PAGE;
  }

  @RequestMapping(value = "/payment-details", method = RequestMethod.GET)
  @RequireHardLogIn
  public String paymentDetails(final Model model) throws CMSItemNotFoundException {
    model.addAttribute("customerData", customerFacade.getCurrentCustomer());
    model.addAttribute("paymentInfoData", userFacade.getCCPaymentInfos(true));
    storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/set-default-payment-details", method = RequestMethod.POST)
  @RequireHardLogIn
  public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId) {
    CCPaymentInfoData paymentInfoData = null;
    if (StringUtils.isNotBlank(paymentInfoId)) {
      paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
    }
    userFacade.setDefaultPaymentInfo(paymentInfoData);
    return REDIRECT_TO_PAYMENT_INFO_PAGE;
  }

  @RequestMapping(value = "/remove-payment-method", method = RequestMethod.POST)
  @RequireHardLogIn
  public String removePaymentMethod(
      @RequestParam(value = "paymentInfoId") final String paymentMethodId,
      final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
    userFacade.unlinkCCPaymentInfo(paymentMethodId);
    GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
        "text.account.profile.paymentCart.removed");
    return REDIRECT_TO_PAYMENT_INFO_PAGE;
  }

  @RequestMapping(value = "/consents", method = RequestMethod.GET)
  @RequireHardLogIn
  public String consentManagement(final Model model) throws CMSItemNotFoundException {
    model.addAttribute("consentTemplateDataList",
        getConsentFacade().getConsentTemplatesWithConsents());
    storeCmsPageInModel(model, getContentPageForLabelOrId(CONSENT_MANAGEMENT_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONSENT_MANAGEMENT_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_CONSENT_MANAGEMENT));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/consents/give/{consentTemplateId}/{version}", method = RequestMethod.POST)
  @RequireHardLogIn
  public String giveConsent(@PathVariable final String consentTemplateId,
      @PathVariable final Integer version,
      final RedirectAttributes redirectModel) {
    try {
      getConsentFacade().giveConsent(consentTemplateId, version);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_GIVEN);
    } catch (final ModelNotFoundException | AmbiguousIdentifierException e) {
      LOG.warn(String.format("ConsentTemplate with code [%s] and version [%s] was not found",
          consentTemplateId, version), e);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_TEMPLATE_NOT_FOUND, null);
    } catch (final CommerceConsentGivenException e) {
      LOG.warn(String
          .format("ConsentTemplate with code [%s] and version [%s] already has a given consent",
              consentTemplateId,
              version), e);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_ALREADY_GIVEN,
          null);
    }
    customerConsentDataStrategy.populateCustomerConsentDataInSession();
    return REDIRECT_TO_CONSENT_MANAGEMENT;
  }

  @RequestMapping(value = "/consents/withdraw/{consentCode}", method = RequestMethod.POST)
  @RequireHardLogIn
  public String withdrawConsent(@PathVariable final String consentCode,
      final RedirectAttributes redirectModel)
      throws CMSItemNotFoundException {
    try {
      getConsentFacade().withdrawConsent(consentCode);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_WITHDRAWN);
    } catch (final ModelNotFoundException e) {
      LOG.warn(String.format("Consent with code [%s] was not found", consentCode), e);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_NOT_FOUND,
          null);
    } catch (final CommerceConsentWithdrawnException e) {
      LOG.error(String.format("Consent with code [%s] is already withdrawn", consentCode), e);
      GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
          TEXT_ACCOUNT_CONSENT_ALREADY_WITHDRAWN, null);
    }
    customerConsentDataStrategy.populateCustomerConsentDataInSession();
    return REDIRECT_TO_CONSENT_MANAGEMENT;
  }

  @RequestMapping(value = "/close-account", method = RequestMethod.GET)
  @RequireHardLogIn
  public String showCloseAccountPage(final Model model) throws CMSItemNotFoundException {
    storeCmsPageInModel(model, getContentPageForLabelOrId(CLOSE_ACCOUNT_CMS_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CLOSE_ACCOUNT_CMS_PAGE));
    model.addAttribute(BREADCRUMBS_ATTR,
        accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_CLOSE));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS,
        ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    return getViewForPage(model);
  }

  @RequestMapping(value = "/close-account", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  @RequireHardLogIn
  public void closeAccount(final HttpServletRequest request)
      throws CMSItemNotFoundException, ServletException {
    customerFacade.closeAccount();
    request.logout();
  }
}
