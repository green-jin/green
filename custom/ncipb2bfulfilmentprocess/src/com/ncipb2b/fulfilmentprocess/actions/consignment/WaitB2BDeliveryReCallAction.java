package com.ncipb2b.fulfilmentprocess.actions.consignment;

import com.ncipb2b.fulfilmentprocess.adapter.Process2ReadyShipAdapter;
import com.ncipb2b.fulfilmentprocess.adapter.Shipping2ShippedAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class WaitB2BDeliveryReCallAction extends AbstractProceduralAction<ConsignmentProcessModel> {

  private static final Logger LOG = Logger.getLogger(WaitForLogisticsAction.class);

  @Resource
  Shipping2ShippedAdapter shipping2ShippedAdapter;

  @Override
  public void executeAction(ConsignmentProcessModel consignmentProcessModel)
      throws RetryLaterException, Exception {
    // TODO: 2019/9/15 修改特製品的跳過狀態

    shipping2ShippedAdapter.waitforShipped(consignmentProcessModel.getConsignment());
    consignmentProcessModel.setWaitingForConsignment(true);
    getModelService().save(consignmentProcessModel); 
  }

  public Shipping2ShippedAdapter getShipping2ShippedAdapter() {
    return shipping2ShippedAdapter;
  }

  public void setShipping2ShippedAdapter(
      Shipping2ShippedAdapter shipping2ShippedAdapter) {
    this.shipping2ShippedAdapter = shipping2ShippedAdapter;
  }
}

