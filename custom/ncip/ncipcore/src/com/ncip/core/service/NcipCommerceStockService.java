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
package com.ncip.core.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Service that collects functionality for stock levels related with point of service (warehouse)
 */
public interface NcipCommerceStockService
{

	/**
	 * Returns stock level value for given product and base store
	 * 
	 * @param product
	 * @param baseStore
	 * @return actual stock level
	 */
	Long getStockLevelForProductAndBaseStore(ProductModel product, BaseStoreModel baseStore);
}
