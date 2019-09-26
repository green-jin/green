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
  public List<ConsignmentModel> GetConsignmentsByTimeAndType(String time, String type) throws ConsignmentException {
    
    return ncipDefaultConsignmentDao.GetConsignmentsByTimeAndType(time, type);
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

  @Override
  public List<ConsignmentModel> GetConsignmentsByCode(String code) throws ConsignmentException {
    return ncipDefaultConsignmentDao.GetConsignmentsByCode(code);
  }
}
