package com.ncipb2b.fulfilmentprocess.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;
import com.ncip.core.enums.DeliveryType;

public interface NcipB2BConsignmentService {
  ConsignmentModel createConsignment(AbstractOrderModel var1, String var2, List<AbstractOrderEntryModel> var3, String var4, DeliveryType var5) throws ConsignmentCreationException;

}
