package com.ncipb2b.fulfilmentprocess.adapter;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface Shipping2ShippedAdapter {
  void waitforShipped(ConsignmentModel ConsignmentModel);
}
