/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with SAP.
 */
package com.ncipb2b.fulfilmentprocess.actions.order;

import com.ncipb2b.fulfilmentprocess.service.B2BOrderSplittingService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SplitOrderAction extends AbstractProceduralAction<OrderProcessModel> {
  private static final Logger LOG = Logger.getLogger(SplitOrderAction.class);

  private B2BOrderSplittingService b2BOrderSplittingService;
  private BusinessProcessService businessProcessService;

  @Override
  public void executeAction(final OrderProcessModel process) throws Exception {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Process: " + process.getCode() + " in step " + getClass());
    }

    // find the order's entries that are not already allocated to consignments
    final List<AbstractOrderEntryModel> entriesToSplit = new ArrayList<AbstractOrderEntryModel>();

    if (LOG.isDebugEnabled()) {
      LOG.debug(process.getOrder().getCode() + " process.getOrder(): "
          + process.getOrder().getEntries().size());
    }

    for (final AbstractOrderEntryModel entry : process.getOrder().getEntries()) {
      if (entry.getConsignmentEntries() == null || entry.getConsignmentEntries().isEmpty()) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("[orderCode]" + process.getOrder().getCode() + "[orderEntry PK]" + entry.getPk()
              + " [orderEntry Quantity]" + entry.getQuantity() + " [productCodt]"
              + entry.getProduct().getCode() + " [productMa_type]"
              + entry.getProduct().getMa_type());
        }
        entriesToSplit.add(entry);
      }
    }

    // TODO: 2019/8/22 Spilt Consignment start
    final List<ConsignmentModel> consignments =
        getB2BOrderSplittingService().splitOrderForConsignment(process.getOrder(), entriesToSplit);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Splitting order into " + consignments.size() + " consignments.");
    }

    int index = 0;
    for (final ConsignmentModel consignment : consignments) {
      final ConsignmentProcessModel subProcess =
          getBusinessProcessService().<ConsignmentProcessModel>createProcess(
              process.getCode() + "_" + (++index),
              Ncipb2bFulfilmentProcessConstants.CONSIGNMENT_SUBPROCESS_NAME);

      subProcess.setParentProcess(process);
      subProcess.setConsignment(consignment);
      save(subProcess);

      getBusinessProcessService().startProcess(subProcess);

    }
    setOrderStatus(process.getOrder(), OrderStatus.ORDER_SPLIT);
  }

  public B2BOrderSplittingService getB2BOrderSplittingService() {
    return b2BOrderSplittingService;
  }

  @Required
  public void setB2BOrderSplittingService(B2BOrderSplittingService b2BOrderSplittingService) {
    this.b2BOrderSplittingService = b2BOrderSplittingService;
  }

  protected BusinessProcessService getBusinessProcessService() {
    return businessProcessService;
  }

  @Required
  public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }
}
