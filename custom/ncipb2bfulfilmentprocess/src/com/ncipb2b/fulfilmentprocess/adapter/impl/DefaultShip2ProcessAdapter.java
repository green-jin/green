package com.ncipb2b.fulfilmentprocess.adapter.impl;

import com.ncipb2b.fulfilmentprocess.adapter.Ship2ProcessAdapter;
import com.ncipb2b.fulfilmentprocess.service.ProcessTriggerService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DefaultShip2ProcessAdapter implements Ship2ProcessAdapter {

  @Resource
  ModelService modelService;
  @Resource
  TimeService timeService;
  @Resource
  ProcessTriggerService processTriggerService;


  @Override
  public void waitForConsignment(ConsignmentModel consignment) {
//    if(consignment.getStatus() == ConsignmentStatus.READY)
    consignment.setStatus(ConsignmentStatus.READY);
    getModelService().save(consignment);
    final Runnable runnable = new Shipping(
        Registry.getCurrentTenant().getTenantID(), consignment.getPk().getLongValue());
    new Thread(runnable).start();

//    try {
//      Thread.sleep(3000);
//    } catch (final InterruptedException e) {
//      //nothing to do
//    }
  }


  public class Shipping implements Runnable {

    private Logger LOG = Logger.getLogger(Shipping.class.getName());

    //    private Logger LOG = (Shipping.class.toString());
    private final long consignment;
    private final String tenant;

    Shipping(final String tenant, final long consignment) {
      super();
      LOG.info("Consignment wait shipping Start");

      this.consignment = consignment;
      this.tenant = tenant;
    }

    @Override
    public void run() {
      Registry.setCurrentTenant(Registry.getTenantByID(tenant));
      try {

        LOG.info("Consignment wait shipping Start");
        ConsignmentModel model = getModelService().get(PK.fromLong(consignment));

        // TODO: 2019/8/29 Check Consignment Status
        for (ConsignmentProcessModel processModel : model.getConsignmentProcesses()
        ) {
          LOG.info("WAIT_FOR_SHIPPING Start : " + processModel.getCode());
//            model = modelService.get(getModelService().get(PK.fromLong(model.getPk().getLongValue())));
//            if (model.getStatus().equals(ConsignmentStatus.READY_FOR_SHIPPING)) {
//              getBusinessProcessService().triggerEvent(
//                  processModel.getCode() + "_"
//                      + Ncipb2bFulfilmentProcessConstants.WAIT_FOR_SHIPPING);
//            }
//            else {
//              Thread.sleep(10000);
//              break;
//            }
        }
//       todo 0902 測試觸發事件
        processTriggerService.triggerConsignment(model);

        LOG.info("WAIT_FOR_SHIPPING END : " + model.getCode());


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

  public ProcessTriggerService getProcessTriggerService() {
    return processTriggerService;
  }

  public void setProcessTriggerService(
      ProcessTriggerService processTriggerService) {
    this.processTriggerService = processTriggerService;
  }
}

