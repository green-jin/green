package com.ncip.core.consignment.dao;

import com.ncip.core.enums.DeliveryType;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.Calendar;
import java.util.List;

public interface NcipConsignmentDao {

  List<ConsignmentModel> GetConsignments() throws ConsignmentException;
  List<ConsignmentModel> GetConsignmentsByTimeAndType(Calendar calendar,  DeliveryType deliveryType) throws ConsignmentException;

}
