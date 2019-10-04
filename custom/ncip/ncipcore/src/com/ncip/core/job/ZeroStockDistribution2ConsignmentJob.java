package com.ncip.core.job;

import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.ncip.core.model.ZhydelyBean;
import com.ncip.core.service.ConsignmentSyncService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.time.TimeService;

public class ZeroStockDistribution2ConsignmentJob extends AbstractJobPerformable<CronJobModel> {

  private static final Logger LOG = Logger.getLogger(ZeroStockDistribution2ConsignmentJob.class);

  @Resource
  private ConsignmentSyncService consignmentSyncService;
  @Resource
  private TimeService timeService;

  @Override
  public PerformResult perform(final CronJobModel arg0) {

    LOG.info("ZeroStockDistribution2ConsignmentJob START. time=" + timeService.getCurrentTime());

    /* prepare consignments */
    final List<ZhydelyBean> sapList = consignmentSyncService.prepareZeroStockConsignmentsFromSAP();
    if (sapList == null || sapList.size() == 0) {
      // throw new RuntimeException("No SAP Consignments");
      LOG.warn("No SAP Consignments found.");
      return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    /* log list */
    LOG.info("The following consignments' shipping date need to be synced: ");
    for (final ZhydelyBean bean : sapList) {
      LOG.info("Code=" + bean.getVbeln_d() + " Order=" + bean.getVbeln() + " Entry Number="
          + bean.getPosnr());
    }

    /* sync zero stock distribution shipping date info to Hybris */
    consignmentSyncService.syncZeroStockDist(sapList);

    LOG.info("ZeroStockDistribution2ConsignmentJob END  time=" + timeService.getCurrentTime());

    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

}
