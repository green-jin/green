package com.ncipb2b.fulfilmentprocess.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;

public interface NcipB2BConsignmentService {
  ConsignmentModel createConsignment(AbstractOrderModel var1, String var2, List<AbstractOrderEntryModel> var3) throws ConsignmentCreationException;

}
