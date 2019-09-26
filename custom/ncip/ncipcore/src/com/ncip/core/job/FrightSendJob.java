package com.ncip.core.job;

import com.ncip.core.service.HctFtpService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class FrightSendJob extends AbstractJobPerformable<CronJobModel>{
	private HctFtpService hctFtpService;
	@Override
	public PerformResult perform(CronJobModel arg0) {
		hctFtpService.runService("Fright");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	public HctFtpService getHctFtpService() {
		return hctFtpService;
	}
	public void setHctFtpService(HctFtpService hctFtpService) {
		this.hctFtpService = hctFtpService;
	}

}
