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

import com.ncip.facades.product.data.FileData;
import com.ncip.facades.suggestion.ProductVideoFacade;
import de.hybris.platform.acceleratorfacades.futurestock.FutureStockFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.FutureStockForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.acceleratorstorefrontcommons.variants.VariantSortStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import com.ncip.facades.product.data.VideoData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;
import com.ncip.storefront.controllers.ControllerConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;


/**
 * Controller for product details page
 */
@Controller
@RequestMapping(value = "/**/p")
public class ProductPageController extends AbstractPageController {

  @SuppressWarnings("unused")
  private static final Logger LOG = Logger.getLogger(ProductPageController.class);

  /**
   * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly
   * extracted if it contains one or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164
   * for a discussion on the issue and future resolution.
   */
  private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
  private static final String REVIEWS_PATH_VARIABLE_PATTERN = "{numberOfReviews:.*}";

  private static final String FUTURE_STOCK_ENABLED = "storefront.products.futurestock.enabled";
  private static final String STOCK_SERVICE_UNAVAILABLE = "basket.page.viewFuture.unavailable";
  private static final String NOT_MULTISKU_ITEM_ERROR = "basket.page.viewFuture.not.multisku";

  @Resource(name = "productDataUrlResolver")
  private UrlResolver<ProductData> productDataUrlResolver;

  @Resource(name = "productVariantFacade")
  private ProductFacade productFacade;

  @Resource(name = "productService")
  private ProductService productService;

  @Resource(name = "productBreadcrumbBuilder")
  private ProductBreadcrumbBuilder productBreadcrumbBuilder;

  @Resource(name = "cmsPageService")
  private CMSPageService cmsPageService;

  @Resource(name = "variantSortStrategy")
  private VariantSortStrategy variantSortStrategy;

  @Resource(name = "reviewValidator")
  private ReviewValidator reviewValidator;

  @Resource(name = "futureStockFacade")
  private FutureStockFacade futureStockFacade;

  @Resource(name = "productVideoFacade")
  private ProductVideoFacade productVideoFacade;

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
  public String productDetail(@PathVariable("productCode") final String encodedProductCode,
      final Model model,
      final HttpServletRequest request, final HttpServletResponse response)
      throws CMSItemNotFoundException, UnsupportedEncodingException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final List<ProductOption> extraOptions = Arrays
        .asList(ProductOption.VARIANT_MATRIX_BASE, ProductOption.VARIANT_MATRIX_URL,
            ProductOption.VARIANT_MATRIX_MEDIA);

    final ProductData productData = productFacade
        .getProductForCodeAndOptions(productCode, extraOptions);

    final String redirection = checkRequestUrl(request, response,
        productDataUrlResolver.resolve(productData));
    if (StringUtils.isNotEmpty(redirection)) {
      return redirection;
    }

    updatePageTitle(productCode, model);

    populateProductDetailForDisplay(productCode, model, request, extraOptions);

    model.addAttribute(new ReviewForm());
    model.addAttribute("pageType", PageType.PRODUCT.name());
    model.addAttribute("futureStockEnabled",
        Boolean.valueOf(Config.getBoolean(FUTURE_STOCK_ENABLED, false)));
//    model.addAttribute(new FileDownloadForm());

    final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productData.getKeywords());
    final String metaDescription = MetaSanitizerUtil
        .sanitizeDescription(productData.getDescription());
    setUpMetaData(model, metaKeywords, metaDescription);
    return getViewForPage(model);
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/orderForm", method = RequestMethod.GET)
  public String productOrderForm(@PathVariable("productCode") final String encodedProductCode,
      final Model model,
      final HttpServletRequest request, final HttpServletResponse response)
      throws CMSItemNotFoundException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final List<ProductOption> extraOptions = Arrays.asList(ProductOption.VARIANT_MATRIX_BASE,
        ProductOption.VARIANT_MATRIX_PRICE, ProductOption.VARIANT_MATRIX_MEDIA,
        ProductOption.VARIANT_MATRIX_STOCK,
        ProductOption.URL);

    final ProductData productData = productFacade
        .getProductForCodeAndOptions(productCode, extraOptions);
    updatePageTitle(productCode, model);

    populateProductDetailForDisplay(productCode, model, request, extraOptions);

    if (!model.containsAttribute(WebConstants.MULTI_DIMENSIONAL_PRODUCT)) {
      return REDIRECT_PREFIX + productDataUrlResolver.resolve(productData);
    }

    return ControllerConstants.Views.Pages.Product.OrderForm;
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/zoomImages", method = RequestMethod.GET)
  public String showZoomImages(@PathVariable("productCode") final String encodedProductCode,
      @RequestParam(value = "galleryPosition", required = false) final String galleryPosition,
      final Model model) {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
        Collections.singleton(ProductOption.GALLERY));
    final List<Map<String, ImageData>> images = getGalleryImages(productData);
    populateProductData(productData, model);
    if (galleryPosition != null) {
      try {
        model.addAttribute("zoomImageUrl",
            images.get(Integer.parseInt(galleryPosition)).get("zoom").getUrl());
      } catch (final IndexOutOfBoundsException | NumberFormatException ex) {
        if (LOG.isDebugEnabled()) {
          LOG.debug(ex);
        }
        model.addAttribute("zoomImageUrl", "");
      }
    }
    return ControllerConstants.Views.Fragments.Product.ZoomImagesPopup;
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/quickView", method = RequestMethod.GET)
  public String showQuickView(@PathVariable("productCode") final String encodedProductCode,
      final Model model,
      final HttpServletRequest request) {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final ProductModel productModel = productService.getProductForCode(productCode);
    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
        Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY,
            ProductOption.DESCRIPTION,
            ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK,
            ProductOption.REVIEW,
            ProductOption.VARIANT_FULL, ProductOption.DELIVERY_MODE_AVAILABILITY));

    sortVariantOptionData(productData);
    populateProductData(productData, model);
    getRequestContextData(request).setProduct(productModel);

    return ControllerConstants.Views.Fragments.Product.QuickViewPopup;
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/review", method =
      {RequestMethod.GET, RequestMethod.POST})
  public String postReview(@PathVariable("productCode") final String encodedProductCode,
      final ReviewForm form,
      final BindingResult result, final Model model, final HttpServletRequest request,
      final RedirectAttributes redirectAttrs)
      throws CMSItemNotFoundException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    getReviewValidator().validate(form, result);

    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, null);
    if (result.hasErrors()) {
      updatePageTitle(productCode, model);
      GlobalMessages.addErrorMessage(model, "review.general.error");
      model.addAttribute("showReviewForm", Boolean.TRUE);
      populateProductDetailForDisplay(productCode, model, request, Collections.emptyList());
      storeCmsPageInModel(model, getPageForProduct(productCode));
      return getViewForPage(model);
    }

    final ReviewData review = new ReviewData();
    review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
    review.setComment(XSSFilterUtil.filter(form.getComment()));
    review.setRating(form.getRating());
    review.setAlias(XSSFilterUtil.filter(form.getAlias()));
    productFacade.postReview(productCode, review);
    GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER,
        "review.confirmation.thank.you.title");

    return REDIRECT_PREFIX + productDataUrlResolver.resolve(productData);
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/reviewhtml/"
      + REVIEWS_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
  public String reviewHtml(@PathVariable("productCode") final String encodedProductCode,
      @PathVariable("numberOfReviews") final String numberOfReviews, final Model model,
      final HttpServletRequest request) {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final ProductModel productModel = productService.getProductForCode(productCode);
    final List<ReviewData> reviews;
    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
        Arrays.asList(ProductOption.BASIC, ProductOption.REVIEW));

    if ("all".equals(numberOfReviews)) {
      reviews = productFacade.getReviews(productCode);
    } else {
      final int reviewCount = Math.min(Integer.parseInt(numberOfReviews),
          productData.getNumberOfReviews() == null ? 0
              : productData.getNumberOfReviews().intValue());
      reviews = productFacade.getReviews(productCode, Integer.valueOf(reviewCount));
    }

    getRequestContextData(request).setProduct(productModel);
    model.addAttribute("reviews", reviews);
    model.addAttribute("reviewsTotal", productData.getNumberOfReviews());
    model.addAttribute(new ReviewForm());

    return ControllerConstants.Views.Fragments.Product.ReviewsTab;
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/writeReview", method = RequestMethod.GET)
  public String writeReview(@PathVariable("productCode") final String encodedProductCode,
      final Model model)
      throws CMSItemNotFoundException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    model.addAttribute(new ReviewForm());
    setUpReviewPage(model, productCode);
    return ControllerConstants.Views.Pages.Product.WriteReview;
  }

  protected void setUpReviewPage(final Model model, final String productCode)
      throws CMSItemNotFoundException {
    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, null);
    final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productData.getKeywords());
    final String metaDescription = MetaSanitizerUtil
        .sanitizeDescription(productData.getDescription());
    setUpMetaData(model, metaKeywords, metaDescription);
    storeCmsPageInModel(model, getPageForProduct(productCode));
    model.addAttribute("product",
        productFacade.getProductForCodeAndOptions(productCode, Arrays.asList(ProductOption.BASIC)));
    updatePageTitle(productCode, model);
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/writeReview", method = RequestMethod.POST)
  public String writeReview(@PathVariable("productCode") final String encodedProductCode,
      final ReviewForm form,
      final BindingResult result, final Model model, final HttpServletRequest request,
      final RedirectAttributes redirectAttrs)
      throws CMSItemNotFoundException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    getReviewValidator().validate(form, result);

    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, null);

    if (result.hasErrors()) {
      GlobalMessages.addErrorMessage(model, "review.general.error");
      populateProductDetailForDisplay(productCode, model, request, Collections.emptyList());
      setUpReviewPage(model, productCode);
      return ControllerConstants.Views.Pages.Product.WriteReview;
    }

    final ReviewData review = new ReviewData();
    review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
    review.setComment(XSSFilterUtil.filter(form.getComment()));
    review.setRating(form.getRating());
    review.setAlias(XSSFilterUtil.filter(form.getAlias()));
    productFacade.postReview(productCode, review);
    GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER,
        "review.confirmation.thank.you.title");

    return REDIRECT_PREFIX + productDataUrlResolver.resolve(productData);
  }

  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
      + "/futureStock", method = RequestMethod.GET)
  public String productFutureStock(@PathVariable("productCode") final String encodedProductCode,
      final Model model,
      final HttpServletRequest request, final HttpServletResponse response)
      throws CMSItemNotFoundException {
    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
    final boolean futureStockEnabled = Config.getBoolean(FUTURE_STOCK_ENABLED, false);
    if (futureStockEnabled) {
      final List<FutureStockData> futureStockList = futureStockFacade
          .getFutureAvailability(productCode);
      if (futureStockList == null) {
        GlobalMessages.addErrorMessage(model, STOCK_SERVICE_UNAVAILABLE);
      } else if (futureStockList.isEmpty()) {
        GlobalMessages.addInfoMessage(model, "product.product.details.future.nostock");
      }

      populateProductDetailForDisplay(productCode, model, request, Collections.emptyList());
      model.addAttribute("futureStocks", futureStockList);

      return ControllerConstants.Views.Fragments.Product.FutureStockPopup;
    } else {
      return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
    }
  }

  @ResponseBody
  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/grid/skusFutureStock", method =
      {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
  public final Map<String, Object> productSkusFutureStock(final FutureStockForm form,
      final Model model) {
    final String productCode = form.getProductCode();
    final List<String> skus = form.getSkus();
    final boolean futureStockEnabled = Config.getBoolean(FUTURE_STOCK_ENABLED, false);

    Map<String, Object> result = new HashMap<>();
    if (futureStockEnabled && CollectionUtils.isNotEmpty(skus) && StringUtils
        .isNotBlank(productCode)) {
      final Map<String, List<FutureStockData>> futureStockData = futureStockFacade
          .getFutureAvailabilityForSelectedVariants(productCode, skus);

      if (futureStockData == null) {
        // future availability service is down, we show this to the user
        result = Maps.newHashMap();
        final String errorMessage = getMessageSource().getMessage(NOT_MULTISKU_ITEM_ERROR, null,
            getI18nService().getCurrentLocale());
        result.put(NOT_MULTISKU_ITEM_ERROR, errorMessage);
      } else {
        for (final Map.Entry<String, List<FutureStockData>> entry : futureStockData.entrySet()) {
          result.put(entry.getKey(), entry.getValue());
        }
      }
    }
    return result;
  }

  //Lancy Add for download PDF & DWG
//  @RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/downloadFile", method =  RequestMethod.POST)
//  public String downloadFiles(
//      @PathVariable("productCode") final String encodedProductCode,
//      final FileDownloadForm fileDownloadForm,
//      Model model
//      ,HttpServletRequest request
//      ,HttpServletResponse response
//  )
//      throws CMSItemNotFoundException, IOException {
//    final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
//    LOG.info("fromURL="+ fileDownloadForm.getFromURL());
//    LOG.info("downloadURL="+ fileDownloadForm.getDownloadURL());
//    LOG.info("fileName=" + fileDownloadForm.getFileName());
//
//    //下載檔案的網址
//    String path = "https://127.0.0.1:9002/ncipb2bstorefront/medias/?context=bWFzdGVyfHBkZnN8NDgzOTcxMnxhcHBsaWNhdGlvbi9wZGZ8cGRmcy9oZmIvaDFiLzg3OTY1ODk2MjEyNzgucGRmfGQ3ODU1MTc3MGExZThmOGEyZTc3ZDc1ZmUyZjE4NmZkYjkxYWU4ODMyMzUzMDIyNjI3NmNmMDg1ZjJkNDdkYmY";
//    String fileName ="LancytestDownlad.pdf";    //儲存檔名
//    String outputFile = "C:/Users/1800361.SYSTEX/Desktop/Download/"+fileName; // 要下載檔案的位置
//
//     File desFile = new File(outputFile);
//
//    if (desFile.exists())
//      desFile.delete();
//
//    URL url = new URL(path);
//
//    URLConnection connection = url.openConnection();
//
//    byte[] buffer = new byte[1024]; // InputStream
//
//    // 設定接收資料流來源 ,就是要下載的網址
//    BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
//    // 設定　儲存要下載檔案的位置.
//    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));
//    int length = -1;
//    while ((length = bufferedInputStream.read(buffer)) != -1) {
//      bufferedOutputStream.write(buffer,0,length);
//    }
//    bufferedOutputStream.flush();// 將緩衝區中的資料全部寫出
//    bufferedInputStream.close(); // 關閉資料流
//    bufferedOutputStream.close();
//    LOG.info("Download success!!");
//
////    storeCmsPageInModel(model, getPageForProduct(productCode));
//    return getViewForPage(model);
//
//}


  @ExceptionHandler(UnknownIdentifierException.class)
  public String handleUnknownIdentifierException(final UnknownIdentifierException exception,
      final HttpServletRequest request) {
    request.setAttribute("message", exception.getMessage());
    return FORWARD_PREFIX + "/404";
  }

  protected void updatePageTitle(final String productCode, final Model model) {
    storeContentPageTitleInModel(model,
        getPageTitleResolver().resolveProductPageTitle(productCode));
  }

  protected void populateProductDetailForDisplay(final String productCode, final Model model,
      final HttpServletRequest request,
      final List<ProductOption> extraOptions) throws CMSItemNotFoundException {
    final ProductModel productModel = productService.getProductForCode(productCode);

    getRequestContextData(request).setProduct(productModel);

    final List<ProductOption> options = new ArrayList<>(
        Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
            ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY,
            ProductOption.DESCRIPTION, ProductOption.GALLERY,
            ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
            ProductOption.CLASSIFICATION,
            ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES,
            ProductOption.PRICE_RANGE,
            ProductOption.DELIVERY_MODE_AVAILABILITY));

    options.addAll(extraOptions);

    final ProductData productData = productFacade.getProductForCodeAndOptions(productCode, options);

    sortVariantOptionData(productData);
    storeCmsPageInModel(model, getPageForProduct(productCode));
    populateProductData(productData, model);
    populateVideoForDisplay(productModel, model);
    model.addAttribute(WebConstants.BREADCRUMBS_KEY,
        productBreadcrumbBuilder.getBreadcrumbs(productCode));

    if (CollectionUtils.isNotEmpty(productData.getVariantMatrix())) {
      model.addAttribute(WebConstants.MULTI_DIMENSIONAL_PRODUCT,
          Boolean.valueOf(CollectionUtils.isNotEmpty(productData.getVariantMatrix())));
    }
  }

  protected void populateProductData(final ProductData productData, final Model model) {
//    model.addAttribute("galleryVideos", getGalleryVideos(productData));
    model.addAttribute("galleryImages", getGalleryImages(productData));
    model.addAttribute("product", productData);
    if (productData.getConfigurable()) {
      final List<ConfigurationInfoData> configurations = productFacade
          .getConfiguratorSettingsForCode(productData.getCode());
      if (CollectionUtils.isNotEmpty(configurations)) {
        model.addAttribute("configuratorType", configurations.get(0).getConfiguratorType());
      }
    }
  }

  protected void sortVariantOptionData(final ProductData productData) {
    if (CollectionUtils.isNotEmpty(productData.getBaseOptions())) {
      for (final BaseOptionData baseOptionData : productData.getBaseOptions()) {
        if (CollectionUtils.isNotEmpty(baseOptionData.getOptions())) {
          Collections.sort(baseOptionData.getOptions(), variantSortStrategy);
        }
      }
    }

    if (CollectionUtils.isNotEmpty(productData.getVariantOptions())) {
      Collections.sort(productData.getVariantOptions(), variantSortStrategy);
    }
  }

  protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData) {
    final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(productData.getImages())) {
      final List<ImageData> images = new ArrayList<>();
      for (final ImageData image : productData.getImages()) {
        if (ImageDataType.GALLERY.equals(image.getImageType())) {
          images.add(image);
        }
      }
      Collections.sort(images, new Comparator<ImageData>() {
        @Override
        public int compare(final ImageData image1, final ImageData image2) {
          return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
        }
      });

      if (CollectionUtils.isNotEmpty(images)) {
        addFormatsToGalleryImages(galleryImages, images);
      }
    }
    return galleryImages;
  }

  protected void addFormatsToGalleryImages(final List<Map<String, ImageData>> galleryImages,
      final List<ImageData> images) {
    int currentIndex = images.get(0).getGalleryIndex().intValue();
    Map<String, ImageData> formats = new HashMap<String, ImageData>();
    for (final ImageData image : images) {
      if (currentIndex != image.getGalleryIndex().intValue()) {
        galleryImages.add(formats);
        formats = new HashMap<>();
        currentIndex = image.getGalleryIndex().intValue();
      }
      formats.put(image.getFormat(), image);
    }
    if (!formats.isEmpty()) {
      galleryImages.add(formats);
    }
  }

  protected ReviewValidator getReviewValidator() {
    return reviewValidator;
  }

  protected AbstractPageModel getPageForProduct(final String productCode)
      throws CMSItemNotFoundException {
    final ProductModel productModel = productService.getProductForCode(productCode);
    return cmsPageService
        .getPageForProduct(productModel, getCmsPreviewService().getPagePreviewCriteria());
  }

  //	Add by Lancy
  protected void populateVideoForDisplay(final ProductModel productModel, final Model model) {

    final ProductData productData = productVideoFacade.getProductVideo(productModel);

    if (productData.getVideos() != null || CollectionUtils.isNotEmpty(productData.getFiles())) {
      model.addAttribute("galleryVideos",
          getGalleryVideos(productData.getVideos())); //galleryVideo: List<Map<String, VideoData>>
    }

    if (productData.getFiles() != null || CollectionUtils.isNotEmpty(productData.getFiles())) {
      model.addAttribute("galleryFiles",
          getGalleryFiles(productData.getFiles())); //galleryVideo: List<Map<String, VideoData>>
    }

  }

  protected List<VideoData> getGalleryVideos(final Collection<VideoData> videoDatas) {

    final List<VideoData> videos = new ArrayList<>();
    for (final VideoData video : videoDatas) {
      videos.add(video);
    }
    return videos;
  }

  protected List<FileData> getGalleryFiles(final Collection<FileData> fileDatas) {

    final List<FileData> files = new ArrayList<>();
    for (final FileData fileData : fileDatas) {
      files.add(fileData);
    }
    return files;
  }
}