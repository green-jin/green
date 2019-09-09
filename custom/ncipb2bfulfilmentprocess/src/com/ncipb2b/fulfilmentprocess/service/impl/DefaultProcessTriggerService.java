package com.ncipb2b.fulfilmentprocess.service.impl;

import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import com.ncipb2b.fulfilmentprocess.service.ProcessTriggerService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProcessTriggerService implements ProcessTriggerService {

  private static final Logger LOG = Logger.getLogger(DefaultProcessTriggerService.class);

  private ModelService modelService;
  private BusinessProcessService businessProcessService;



  @Override
  public boolean triggerConsignment(ConsignmentModel consignment) {

    try {
      ConsignmentModel model = getModelService().get(consignment.getPk());

      model.setStatus(ConsignmentStatus.READY_FOR_SHIPPING);
      modelService.save(model);

      for (ConsignmentProcessModel consignmentProcessModel : consignment.getConsignmentProcesses()
      ) {
        businessProcessService.triggerEvent(consignmentProcessModel.getCode() + "_"
            + Ncipb2bFulfilmentProcessConstants.WAIT_FOR_SHIPPING);
      }
    } catch (Exception e) {
      LOG.info(e.toString());
      return false;
    }
    return true;
  }

  public ModelService getModelService() {
    return modelService;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public BusinessProcessService getBusinessProcessService() {
    return businessProcessService;
  }

  @Required
  public void setBusinessProcessService(
      BusinessProcessService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }
}
