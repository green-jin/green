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
package com.ncip.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;


import org.springframework.beans.factory.annotation.Required;

import com.ncip.core.service.NcipCommerceAvailabilityCalculationStrategy;
import com.ncip.core.service.NcipCommerceStockService;

/**
 * Default implementation of {@link CommerceStockService}
 */
public class DefaultNcipCommerceStockService implements NcipCommerceStockService
{
	private StockService stockService;
	private NcipCommerceAvailabilityCalculationStrategy ncipCommerceStockLevelCalculationStrategy;
	private WarehouseSelectionStrategy warehouseSelectionStrategy;

	@Override
	public Long getStockLevelForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getNcipCommerceStockLevelCalculationStrategy().calculateAvailability(
				getStockService().getStockLevels(product, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore)));
	}

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

    

	public NcipCommerceAvailabilityCalculationStrategy getNcipCommerceStockLevelCalculationStrategy() {
		return ncipCommerceStockLevelCalculationStrategy;
	}
	
	@Required
	public void setNcipCommerceStockLevelCalculationStrategy(
			NcipCommerceAvailabilityCalculationStrategy ncipCommerceStockLevelCalculationStrategy) {
		this.ncipCommerceStockLevelCalculationStrategy = ncipCommerceStockLevelCalculationStrategy;
	}

	protected WarehouseSelectionStrategy getWarehouseSelectionStrategy()
	{
		return warehouseSelectionStrategy;
	}

	@Required
	public void setWarehouseSelectionStrategy(final WarehouseSelectionStrategy warehouseSelectionStrategy)
	{
		this.warehouseSelectionStrategy = warehouseSelectionStrategy;
	}

}
