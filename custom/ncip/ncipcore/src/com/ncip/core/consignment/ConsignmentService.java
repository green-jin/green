package com.ncip.core.consignment;

import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import java.util.List;

public interface ConsignmentService {

  ConsignmentModel GetConsignment(ConsignmentProcessModel consignmentProcessModel) throws ConsignmentException;

  List<ConsignmentModel> GetConsignments() throws ConsignmentException;
  List<ConsignmentModel> GetConsignmentsByTime(String time) throws ConsignmentException;
  List<ConsignmentModel> GetConsignmentsByType(String type) throws ConsignmentException;

}
