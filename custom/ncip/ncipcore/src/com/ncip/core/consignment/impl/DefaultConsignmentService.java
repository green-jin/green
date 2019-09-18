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
  private ConsignmentDao ncipDefaultConsignmentDao;

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
    
    return ncipDefaultConsignmentDao.GetConsignmentsByTime(time);
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByType(String type) throws ConsignmentException {
    return null;
  }

  public ConsignmentDao getNcipDefaultConsignmentDao() {
    return ncipDefaultConsignmentDao;
  }

  public void setNcipDefaultConsignmentDao(ConsignmentDao ncipDefaultConsignmentDao) {
    this.ncipDefaultConsignmentDao = ncipDefaultConsignmentDao;
  }
}
