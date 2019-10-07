package com.ncip.core.job;

import javax.annotation.Resource;
import com.ncip.core.service.UpdateFutureStockService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class UpdateFutureStockJob extends AbstractJobPerformable<CronJobModel>{

  @Resource
  UpdateFutureStockService updateFutureStockService;
  
  @Override
  public PerformResult perform(CronJobModel arg0) {
    updateFutureStockService.runService();
    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

  public UpdateFutureStockService getUpdateFutureStockService() {
    return updateFutureStockService;
  }

  public void setUpdateFutureStockService(UpdateFutureStockService updateFutureStockService) {
    this.updateFutureStockService = updateFutureStockService;
  }
  

}
