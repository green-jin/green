package com.ncip.core.consignment.impl;

import com.ncip.core.consignment.ConsignmentService;
import com.ncip.core.consignment.dao.ConsignmentDao;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import java.util.List;
import javax.annotation.Resource;

public class DefaultConsignmentService implements ConsignmentService {

  @Resource
  ConsignmentDao consignmentDao;

  @Override
  public ConsignmentModel GetConsignment(ConsignmentProcessModel consignmentProcessModel)
      throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignments() throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByTime(String time) throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByType(String type) throws ConsignmentException {
    return null;
  }

  public ConsignmentDao getConsignmentDao() {
    return consignmentDao;
  }

  public void setConsignmentDao(ConsignmentDao consignmentDao) {
    this.consignmentDao = consignmentDao;
  }
}
