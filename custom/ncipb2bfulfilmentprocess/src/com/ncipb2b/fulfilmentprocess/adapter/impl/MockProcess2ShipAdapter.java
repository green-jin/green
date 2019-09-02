package com.ncipb2b.fulfilmentprocess.adapter.impl;

import com.ncipb2b.fulfilmentprocess.adapter.Process2ShippingAdapter;
import com.ncipb2b.fulfilmentprocess.adapter.Ship2ProcessAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import javax.annotation.Resource;

public class MockProcess2ShipAdapter implements Process2ShippingAdapter {

  private ModelService modelService;
  private Ship2ProcessAdapter ship2ProcessAdapter;
  private TimeService timeService;

  @Override
  public void checkConsigmentStatus(ConsignmentModel var1) {

  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public void setShip2ProcessAdapter(
      Ship2ProcessAdapter ship2ProcessAdapter) {
    this.ship2ProcessAdapter = ship2ProcessAdapter;
  }

  public void setTimeService(TimeService timeService) {
    this.timeService = timeService;
  }
}
