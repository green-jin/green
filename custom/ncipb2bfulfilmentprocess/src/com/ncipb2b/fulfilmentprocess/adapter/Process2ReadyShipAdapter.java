package com.ncipb2b.fulfilmentprocess.adapter;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface Process2ReadyShipAdapter {

  void waitForConsignment(ConsignmentModel var1);
}
