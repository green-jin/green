package com.ncip.core.consignment.dao.impl;

import com.ncip.core.consignment.dao.ConsignmentDao;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import javax.annotation.Resource;

public class DefaultConsignmentDao implements ConsignmentDao {

  @Resource
  ModelService modelService;

  @Resource
  FlexibleSearchService flexibleSearchService;

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

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public FlexibleSearchService getFlexibleSearchService() {
    return flexibleSearchService;
  }

  public void setFlexibleSearchService(
      FlexibleSearchService flexibleSearchService) {
    this.flexibleSearchService = flexibleSearchService;
  }
}
