package com.ncipb2b.fulfilmentprocess;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;

public interface B2BOrderSplittingService {

  List<ConsignmentModel> splitOrderForConsignment(
      AbstractOrderModel var1, List<AbstractOrderEntryModel> var2)
      throws ConsignmentCreationException;

  List<ConsignmentModel> splitOrderForConsignmentNotPersist(AbstractOrderModel var1,
      List<AbstractOrderEntryModel> var2) throws ConsignmentCreationException;

}
