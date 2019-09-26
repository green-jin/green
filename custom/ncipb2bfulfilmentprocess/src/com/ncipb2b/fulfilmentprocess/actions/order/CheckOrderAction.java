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
package com.ncipb2b.fulfilmentprocess.actions.order;

import com.ncipb2b.fulfilmentprocess.service.CheckB2BOrderService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

import org.apache.log4j.Logger;


/**
 * This example action checks the order for required data in the business process. Skipping this
 * action may result in failure in one of the subsequent steps of the process. The relation between
 * the order and the business process is defined in basecommerce extension through item
 * OrderProcess. Therefore if your business process has to access the order (a typical case), it is
 * recommended to use the OrderProcess as a parentClass instead of the plain BusinessProcess.
 */
public class CheckOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel> {

  private static final Logger LOG = Logger.getLogger(CheckOrderAction.class);

  private CheckB2BOrderService checkB2BOrderService;


  @Override
  public Transition executeAction(final OrderProcessModel process) {
    final OrderModel order = process.getOrder();

    if (order == null) {
      LOG.error("Missing the order, exiting the process");
      return Transition.NOK;
    }

    if (getCheckB2BOrderService().check(order)) {
      
      LOG.debug(" Vkorg:" +order.getStore().getVkorg()+" , Pernr:"+ order.getUnit().getPernr()+" , Lifnr:"+order.getUnit().getLifnr());
      
      order.setVkorg(order.getStore().getVkorg());//銷售組織
      order.setPernr(order.getUnit().getPernr());//負責業務
      order.setLifnr(order.getUnit().getLifnr());//負責經銷商
        
      setOrderStatus(order, OrderStatus.CHECKED_VALID);
      return Transition.OK;
    } else {
      setOrderStatus(order, OrderStatus.CHECKED_INVALID);
      return Transition.NOK;
    }
  }

  public CheckB2BOrderService getCheckB2BOrderService() {
    return checkB2BOrderService;
  }

  public void setCheckB2BOrderService(
      CheckB2BOrderService checkB2BOrderService) {
    this.checkB2BOrderService = checkB2BOrderService;
  }
}
