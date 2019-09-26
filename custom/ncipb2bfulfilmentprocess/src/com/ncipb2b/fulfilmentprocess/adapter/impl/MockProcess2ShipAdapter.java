package com.ncipb2b.fulfilmentprocess.adapter.impl;

import com.ncipb2b.fulfilmentprocess.adapter.Process2ShippingAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;

public class MockProcess2ShipAdapter implements Process2ShippingAdapter {

  private ModelService modelService;
  private TimeService timeService;

  @Override
  public void checkConsigmentStatus(ConsignmentModel var1) {

  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }


  public void setTimeService(TimeService timeService) {
    this.timeService = timeService;
  }
}
