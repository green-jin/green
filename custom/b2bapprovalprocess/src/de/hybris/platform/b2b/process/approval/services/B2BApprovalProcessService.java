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
package de.hybris.platform.b2b.process.approval.services;

import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;


/**
 * The Interface B2BApprovalProcessService.
 */
public interface B2BApprovalProcessService
{
	/**
	 * Gets the processes.
	 *
	 * @param store
	 *           the store
	 * @return the processes
	 */
	Map<String, String> getProcesses(BaseStoreModel store);
}
