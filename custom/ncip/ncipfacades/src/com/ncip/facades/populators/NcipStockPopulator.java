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
package com.ncip.facades.populators;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.converters.populator.StockPopulator;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Required;

import com.ncip.core.service.NcipCommerceStockService;


/**
 * Populate the product data with stock information
 */
public class NcipStockPopulator<SOURCE extends ProductModel, TARGET extends StockData> extends StockPopulator<SOURCE, TARGET>
{
	private CommerceStockService commerceStockService;
	private BaseStoreService baseStoreService;
	
	private NcipCommerceStockService ncipCommerceStockService;

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}
	
	public NcipCommerceStockService getNcipCommerceStockService() {
		return ncipCommerceStockService;
	}
	
	@Required
	public void setNcipCommerceStockService(NcipCommerceStockService ncipCommerceStockService) {
		this.ncipCommerceStockService = ncipCommerceStockService;
	}

	@Override
	public void populate(final SOURCE productModel, final TARGET stockData) throws ConversionException
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		
		stockData.setStockAmount(BigDecimal.valueOf(getNcipCommerceStockService().getStockLevelForProductAndBaseStore(productModel, baseStore)));
		
		if (!isStockSystemEnabled(baseStore))
		{
			stockData.setStockLevelStatus(StockLevelStatus.INSTOCK);
			stockData.setStockLevel(Long.valueOf(0));
		}
		else
		{
			stockData.setStockLevel(getCommerceStockService().getStockLevelForProductAndBaseStore(productModel, baseStore));
			stockData.setStockLevelStatus(getCommerceStockService().getStockLevelStatusForProductAndBaseStore(productModel,
					baseStore));
		}
	}

	protected boolean isStockSystemEnabled()
	{
		return getCommerceStockService().isStockSystemEnabled(getBaseStoreService().getCurrentBaseStore());
	}

	protected boolean isStockSystemEnabled(final BaseStoreModel baseStore)
	{
		return getCommerceStockService().isStockSystemEnabled(baseStore);
	}
}
