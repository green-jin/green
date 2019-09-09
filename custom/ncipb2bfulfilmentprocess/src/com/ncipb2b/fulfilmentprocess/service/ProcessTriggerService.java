package com.ncipb2b.fulfilmentprocess.service;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface ProcessTriggerService {

  boolean triggerConsignment(ConsignmentModel consignment);
}
