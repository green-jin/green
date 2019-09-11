package com.ncip.core.consignment;

import com.ncip.core.enums.DeliveryType;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.Calendar;
import java.util.List;

public interface NcipConsignmentService {
  List<ConsignmentModel> GetConsignments() throws ConsignmentException;

  List<ConsignmentModel> GetConsignmentsByTimeAndType(Calendar calendar,
      DeliveryType deliveryType);
}
