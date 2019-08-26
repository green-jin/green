package com.ncipb2b.fulfilmentprocess;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;

public interface ConsignmentB2BNonStockService  {
  ConsignmentModel createConsignment(
      AbstractOrderModel orderModel, String code, List<AbstractOrderEntryModel> orderEntryModels) throws ConsignmentCreationException;

}
