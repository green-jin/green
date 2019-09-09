/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with SAP.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Checks order's Permission result and updates order status
 */
public class CheckWorkflowResults extends AbstractSimpleB2BApproveOrderDecisionAction {
  private static final Logger LOG = Logger.getLogger(CheckWorkflowResults.class);

  private BusinessProcessService businessProcessService;
  private ModelService modelService;

  @Override
  public Transition executeAction(final B2BApprovalProcessModel process)
      throws RetryLaterException {

    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "##### [custom b2borderapproval.xml] CheckWorkflowResults executeAction Start ##### orderProcessCode:"
              + process.getOrder() + ",orderStatus:" + process.getOrder().getStatus());
    }

    OrderModel order = null;
    try {
      order = process.getOrder();
      if (order.getStatus().equals(OrderStatus.REJECTED)) {
        // create order history and exit process.
        return Transition.NOK;

      } else {
        // if order was approved delegate to PerformMerchantCheck action
        order.setStatus(OrderStatus.PENDING_APPROVAL_FROM_MERCHANT);
        this.modelService.save(order);

        if (LOG.isDebugEnabled()) {
          // add send mail
          LOG.debug(
              "##### [custom b2borderapproval.xml] CheckWorkflowResults send mail start processCode ##### "
                  + process.getCode());
        }

        final OrderModel orderModel = order;// process.getOrder();//orderPendingApprovalEvent.getProcess().getOrder();

        final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService()
            .createProcess("orderPendingApprovalEmailProcess" + "-" + orderModel.getCode() + "-"
                + System.currentTimeMillis(), "orderPendingApprovalEmailProcess");

        orderProcessModel.setOrder(orderModel);
        getModelService().save(orderProcessModel);
        getBusinessProcessService().startProcess(orderProcessModel);

        return Transition.OK;
      }
    } catch (final Exception e) {
      this.handleError(order, e);
      return Transition.NOK;
    }
  }

  protected void handleError(final OrderModel order, final Exception e) {
    if (order != null) {
      this.setOrderStatus(order, OrderStatus.B2B_PROCESSING_ERROR);
    }
    LOG.error(e.getMessage(), e);
  }

  /**
   * @return the businessProcessService
   */
  public BusinessProcessService getBusinessProcessService() {
    return businessProcessService;
  }

  /**
   * @param businessProcessService the businessProcessService to set
   */
  @Required
  public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }

  /**
   * @return the modelService
   */
  @Override
  public ModelService getModelService() {
    return modelService;
  }

  /**
   * @param modelService the modelService to set
   */
  @Override
  @Required
  public void setModelService(final ModelService modelService) {
    this.modelService = modelService;
  }
}
