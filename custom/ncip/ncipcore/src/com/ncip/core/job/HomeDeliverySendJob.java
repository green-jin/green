package com.ncip.core.job;

import com.ncip.core.service.HctFtpService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class HomeDeliverySendJob extends AbstractJobPerformable<CronJobModel>{
	private HctFtpService hctFtpService;
	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel)
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		hctFtpService.runService("HomeDelivery");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	public HctFtpService getHctFtpService() {
		return hctFtpService;
	}
	public void setHctFtpService(HctFtpService hctFtpService) {
		this.hctFtpService = hctFtpService;
	}

}
