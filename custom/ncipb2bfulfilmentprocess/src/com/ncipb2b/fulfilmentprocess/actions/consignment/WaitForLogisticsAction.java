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
package com.ncipb2b.fulfilmentprocess.actions.consignment;

import com.ncipb2b.fulfilmentprocess.adapter.Process2ShippingAdapter;
import com.ncipb2b.fulfilmentprocess.adapter.Ship2ProcessAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import javax.annotation.Resource;
import org.apache.log4j.Logger;


public class WaitForLogisticsAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(WaitForLogisticsAction.class);

	@Resource
	Ship2ProcessAdapter ship2ProcessAdapter;

	@Override
	public void executeAction(ConsignmentProcessModel consignmentProcessModel)
			throws RetryLaterException, Exception {
		ship2ProcessAdapter.waitForConsignment(consignmentProcessModel.getConsignment());
//		consignmentProcessModel.setWaitingForConsignment(true);
		getModelService().save(consignmentProcessModel);
		LOG.info("Setting waitForConsignment to true");
	}

	public Ship2ProcessAdapter getShip2ProcessAdapter() {
		return ship2ProcessAdapter;
	}

	public void setShip2ProcessAdapter(
			Ship2ProcessAdapter ship2ProcessAdapter) {
		this.ship2ProcessAdapter = ship2ProcessAdapter;
	}
}
