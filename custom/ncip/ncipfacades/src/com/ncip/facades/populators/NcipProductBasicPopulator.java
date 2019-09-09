/**
 *
 */
package com.ncip.facades.populators;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.product.ProductConfigurableChecker;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author 17003381peter
 *
 */
public class NcipProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductBasicPopulator<SOURCE, TARGET>
{

	private ProductConfigurableChecker productConfigurableChecker;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setListpr(productModel.getListpr());
		productData.setVkorg(productModel.getVkorg());
		productData.setPlifz(productModel.getPlifz());
		productData.setMy_type(productModel.getMy_type());
		productData.setName((String) getProductAttribute(productModel, ProductModel.NAME));
		productData.setManufacturer((String) getProductAttribute(productModel, ProductModel.MANUFACTURERNAME));

		productData.setAverageRating(productModel.getAverageRating());
		if (productModel.getVariantType() != null)
		{
			productData.setVariantType(productModel.getVariantType().getCode());
		}
		if (productModel instanceof VariantProductModel)
		{
			final VariantProductModel variantProduct = (VariantProductModel) productModel;
			productData.setBaseProduct(variantProduct.getBaseProduct() != null ? variantProduct.getBaseProduct().getCode() : null);
		}
		productData.setPurchasable(Boolean.valueOf(productModel.getVariantType() == null && isApproved(productModel)));
		productData.setConfigurable(Boolean.valueOf(getProductConfigurableChecker().isProductConfigurable(productModel)));
		productData.setConfiguratorType(getProductConfigurableChecker().getFirstConfiguratorType(productModel));
	}

	protected Object getProductAttribute1(final ProductModel productModel, final String attribute)
	{
		final Object value = getModelService().getAttributeValue(productModel, attribute);
		if (value == null && productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null)
			{
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

	@Override
	protected boolean isApproved(final SOURCE productModel)
	{
		final ArticleApprovalStatus approvalStatus = productModel.getApprovalStatus();
		return ArticleApprovalStatus.APPROVED.equals(approvalStatus);
	}

	@Override
	protected ProductConfigurableChecker getProductConfigurableChecker()
	{
		return productConfigurableChecker;
	}

	@Override
	@Required
	public void setProductConfigurableChecker(final ProductConfigurableChecker productConfigurableChecker)
	{
		this.productConfigurableChecker = productConfigurableChecker;
	}


}
