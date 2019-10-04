package com.ncip.core.job;

import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.ncip.core.service.ConsignmentSyncService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class ConsignmentSyncJobPerformable extends AbstractJobPerformable<CronJobModel> {

  private static final Logger LOG = Logger.getLogger(ConsignmentSyncJobPerformable.class);

  @Resource
  private ConsignmentSyncService consignmentSyncService;

  @Override
  public PerformResult perform(final CronJobModel cron) {

    LOG.info("$$$$$ Start");

    List<ConsignmentModel> list;

    /* prepare consignments */
    try {
      list = consignmentSyncService.prepareConsignments();
    } catch (final Exception e) {
      e.printStackTrace();
      LOG.error("Prepare Consignments failed.");
      return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
    }


    for (final ConsignmentModel model : list) {
      LOG.info(model);
    }

    if (list != null && list.size() == 0) {
      LOG.info("No Sync this time.");
      return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    } else {
      /* sync to zTable */
      try {
        consignmentSyncService.syncZTable(list);
      } catch (final Exception e) {
        e.printStackTrace();
        LOG.error("Sync failed.");
        return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
      }
    }

    LOG.info("$$$$$ End");

    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

}
