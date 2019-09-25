package com.ncipb2b.fulfilmentprocess.adapter.impl;

import com.ncipb2b.fulfilmentprocess.adapter.Shipping2ShippedAdapter;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DefaultShipping2ShippedAdapter implements Shipping2ShippedAdapter {

  @Resource
  ModelService modelService;
  @Resource
  TimeService timeService;
  @Resource
  BusinessProcessService businessProcessService;

  @Override
  public void waitforShipped(ConsignmentModel ConsignmentModel) {

    //ConsignmentModel.setStatus(ConsignmentStatus.READY);
    //getModelService().save(ConsignmentModel);
    final Runnable runnable = new Shipping(
        Registry.getCurrentTenant().getTenantID(), ConsignmentModel.getPk().getLongValue());
    new Thread(runnable).start();
  }

  // TODO: 2019/9/12 考慮是否要用Thread
  public class Shipping implements Runnable {

    private Logger LOG = Logger.getLogger(DefaultProcess2ReadyShipAdapter.Shipping.class.getName());

    //    private Logger LOG = (Shipping.class.toString());
    private final long consignment;
    private final String tenant;

    Shipping(final String tenant, final long consignment) {
      super();
      LOG.info("Consignment wait shipping Start");

      this.consignment = consignment;
      this.tenant = tenant;
//      this.consignmentProcess = consignmentProcess;
    }

    @Override
    public void run() {
      Registry.setCurrentTenant(Registry.getTenantByID(tenant));
      try {

        if (LOG.isDebugEnabled()) {
          LOG.info("Consignment wait shipping Start");
          LOG.info("WAIT_FOR_SHIPPING Start.");
        }

        // TODO: 2019/8/29 Check Consignment Status
        while (true) {
          ConsignmentModel consignmentModel = getModelService().get(PK.fromLong(consignment));

          if (LOG.isDebugEnabled()) {
            LOG.info(consignmentModel.getCode() + " IS WAITING_FOR_SHIPPING Start : ");
          }
//          model = getModelService().get(PK.fromLong(processModel.getPk().getLongValue()));

//       todo 0902 測試觸發事件
          if (consignmentModel.getStatus()
              .equals(ConsignmentStatus.DELIVERY_COMPLETED)) {
            for (ConsignmentProcessModel processModel : consignmentModel.getConsignmentProcesses()
            ) {
              getBusinessProcessService().triggerEvent(
                  processModel.getCode() + "_"
                      + Ncipb2bFulfilmentProcessConstants.ALLREADY_SHIPPED);
            }
            break;
          } else {
            Thread.sleep(10000);
          }
        }
        if (LOG.isDebugEnabled()) {

          LOG.info("WAIT_FOR_SHIPPING END");
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        Registry.unsetCurrentTenant();
      }
    }
  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public TimeService getTimeService() {
    return timeService;
  }

  public void setTimeService(TimeService timeService) {
    this.timeService = timeService;
  }

  public BusinessProcessService getBusinessProcessService() {
    return businessProcessService;
  }

  public void setBusinessProcessService(
      BusinessProcessService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }
}

