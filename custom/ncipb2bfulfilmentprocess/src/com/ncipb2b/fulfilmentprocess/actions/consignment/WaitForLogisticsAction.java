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

import com.ncipb2b.fulfilmentprocess.adapter.Process2ReadyShipAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import javax.annotation.Resource;
import org.apache.log4j.Logger;


public class WaitForLogisticsAction extends AbstractProceduralAction<ConsignmentProcessModel> {

  private static final Logger LOG = Logger.getLogger(WaitForLogisticsAction.class);

  @Resource
  Process2ReadyShipAdapter process2ReadyShipAdapter;

  @Override
  public void executeAction(ConsignmentProcessModel consignmentProcessModel)
      throws RetryLaterException, Exception {
    // TODO: 2019/9/15 修改特製品的跳過狀態

    process2ReadyShipAdapter.waitForConsignment(consignmentProcessModel.getConsignment());
    consignmentProcessModel.setWaitingForConsignment(true);
    getModelService().save(consignmentProcessModel); 
  }

  public Process2ReadyShipAdapter getProcess2ReadyShipAdapter() {
    return process2ReadyShipAdapter;
  }

  public void setProcess2ReadyShipAdapter(
      Process2ReadyShipAdapter process2ReadyShipAdapter) {
    this.process2ReadyShipAdapter = process2ReadyShipAdapter;
  }
}
