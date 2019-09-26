package com.ncip.core.job;

import javax.annotation.Resource;
import com.ncip.core.service.HctFtpService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class LogisticsInfoFromHCTJob extends AbstractJobPerformable<CronJobModel>{
  
  @Resource
  private HctFtpService hctFtpService;
  
  @Override
  public PerformResult perform(CronJobModel arg0) {
    // TODO Auto-generated method stub
    hctFtpService.runService("Get");
    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

  public HctFtpService getHctFtpService() {
    return hctFtpService;
  }

  public void setHctFtpService(HctFtpService hctFtpService) {
    this.hctFtpService = hctFtpService;
  }
  
}
