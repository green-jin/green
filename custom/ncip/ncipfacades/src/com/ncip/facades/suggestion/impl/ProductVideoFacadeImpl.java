package com.ncip.facades.suggestion.impl;

import com.ncip.facades.populators.VideoDataPopulator;
import com.ncip.facades.suggestion.ProductVideoFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import javax.annotation.Resource;


public class ProductVideoFacadeImpl implements ProductVideoFacade {

  @Resource
  private VideoDataPopulator videoDataPopulator;


  @Override
  public ProductData getProductVideo(ProductModel productModel) {

    final ProductData productData = new ProductData();
    if (productModel != null){
      videoDataPopulator.populate(productModel,productData);

    }

    return productData;
  }

}
