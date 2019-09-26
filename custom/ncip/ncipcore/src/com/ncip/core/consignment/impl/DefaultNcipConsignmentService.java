package com.ncip.core.consignment.impl;

import com.ncip.core.consignment.NcipConsignmentService;
import com.ncip.core.consignment.dao.NcipConsignmentDao;
import com.ncip.core.enums.DeliveryType;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.impl.DefaultConsignmentService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;

public class DefaultNcipConsignmentService extends DefaultConsignmentService implements
    NcipConsignmentService {

  @Resource
  NcipConsignmentDao ncipConsignmentDao;



  @Override
  public List<ConsignmentModel> GetConsignments() throws ConsignmentException {
    return ncipConsignmentDao.GetConsignments();
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByTimeAndType(Calendar calendar,
      DeliveryType deliveryType)
      throws ConsignmentException {
    return ncipConsignmentDao.GetConsignmentsByTimeAndType(calendar, deliveryType);
  }

  public NcipConsignmentDao getNcipConsignmentDao() {
    return ncipConsignmentDao;
  }

  public void setNcipConsignmentDao(NcipConsignmentDao ncipConsignmentDao) {
    this.ncipConsignmentDao = ncipConsignmentDao;
  }
}
