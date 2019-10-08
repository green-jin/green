package com.ncip.core.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.ncip.core.constants.ConsignmentSyncConstants;
import com.ncip.core.dao.ConsignmentSyncDao;
import com.ncip.core.dao.ZhydelyDao;
import com.ncip.core.model.ZhydelyBean;
import com.ncip.core.service.ConsignmentSyncService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.tx.Transaction;

public class DefaultConsignmentSyncService implements ConsignmentSyncService {

  private static final Logger LOG = Logger.getLogger(DefaultConsignmentSyncService.class);

  @Resource
  private TimeService timeService;
  @Resource
  private ConsignmentSyncDao consignmentSyncDao;
  @Resource
  private ZhydelyDao zhydelyDao;
  @Resource
  private ConfigurationService configurationService;

  @Override
  public List<ConsignmentModel> prepareConsignments() {

    List<ConsignmentModel> allList;
    final List<ConsignmentModel> list = new ArrayList<>();
    ConsignmentStatus status;

    /* prepare all consignments */
    allList = consignmentSyncDao.prepareAllConsignmentsByStatus();

    /* prepare by status */
    for (final ConsignmentModel model : allList) {
      status = model.getStatus();
      /* adjust conditions later */
      if (ConsignmentStatus.READY_FOR_SHIPPING.equals(status)
          || ConsignmentStatus.READY.equals(status)
          || ConsignmentStatus.CANCELLED.equals(status)) {
        list.add(model);
      }
    }

    if (list != null && list.size() > 0) {
      LOG.info("Number of consignments that needs to be synced: " + list.size());
      return list;
    } else {
      LOG.info("No consignments that needs to be synced.");
      return list;
    }

  }

  @Override
  public void syncZTable(final List<ConsignmentModel> list) {

    final Transaction tx = Transaction.current();
    boolean txSucceed;
    Date currentTimestamp;
    final DateFormat fmt = new SimpleDateFormat("yyyyMMddhhmmss");
    String currentTimestampString;
    Set<ConsignmentEntryModel> entries = new HashSet<>();
    ZhydelyBean zModel;
    String vkorg;
    String vbeln_d;
    /* 用戶端代碼(MANDT)正式區固定888 , 測試區250，從local.properties取值 */
    final String manDt = configurationService.getConfiguration().getString("erp.mandt");


    /* iterate consignments */
    for (final ConsignmentModel model : list) {

      zModel = new ZhydelyBean();

      entries = model.getConsignmentEntries();
      /* iterate consignment entries */
      for (final ConsignmentEntryModel entry : entries) {

        /* log org and code */
        vkorg = model.getOrder().getStore().getUid();
        vbeln_d = model.getCode();

        /* set date and time */
        currentTimestamp = timeService.getCurrentTime();
        currentTimestampString = fmt.format(currentTimestamp);
        zModel.setCrt_date(currentTimestampString);

        /* set source and target */
        zModel.setFrm_sys(ConsignmentSyncConstants.HYBRIS);
        zModel.setTo_sys(ConsignmentSyncConstants.SAP);

        zModel.setVkorg(vkorg);
        zModel.setVbeln_d(vbeln_d);
        zModel.setVbeln(model.getOrder().getCode());
        zModel.setPosnr(Long.valueOf(entry.getOrderEntry().getEntryNumber()));
        zModel.setMatnr(entry.getOrderEntry().getProduct().getCode());
        zModel.setLfimg(entry.getQuantity());
        zModel.setMeins(entry.getOrderEntry().getUnit().getName());
        zModel.setWadat_ist(model.getShippingDate());
        zModel.setLgort(model.getWarehouse().getCode());
        zModel.setLfstk(model.getStatus().getCode());
        zModel.setDely_type(model.getDely_type());
        zModel.setLfdat(model.getLfdat());
        zModel.setStatus(ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_UNPROCESSED);
        zModel.setMandt(manDt);

        LOG.info("Bean=" + zModel);

        /* check if existed in ZTable */
        if (zhydelyDao.isExistedInZTable(zModel.getVkorg(), zModel.getVbeln_d(),
            zModel.getPosnr())) {
          zModel.setIctype(ConsignmentSyncConstants.MODIFY);
        } else {
          zModel.setIctype(ConsignmentSyncConstants.CREATE);
        }

        // zTableDao.test();
        tx.begin();
        txSucceed = false;
        try {

          /* 不論有無存在ZTable，一律都用Insert，ZTable新舊紀錄同時存在 */
          // if (ConsignmentSyncConstants.CREATE.equals(zModel.getIctype())) {
          // zhydelyDao.insertByModel(zModel);
          // } else if (ConsignmentSyncConstants.MODIFY.equals(zModel.getIctype())) {
          // zhydelyDao.updateByModel(zModel);
          // }
          zhydelyDao.insertByModel(zModel);

          txSucceed = true;
        } catch (final Exception e) {
          e.printStackTrace();
          txSucceed = false;
        } finally {
          if (txSucceed == true) {
            tx.commit();
            tx.flushDelayedStore();
            LOG.info("Update single data done. VKORG = " + vkorg + " VBELN_D = " + vbeln_d);
          } else {
            tx.rollback();
            LOG.warn("Update single data failed. VKORG = " + vkorg + " VBELN_D = " + vbeln_d);
          }
        }

      }
    }

  }

  @Override
  public List<ZhydelyBean> prepareZeroStockConsignmentsFromSAP() {

    String code;
    String order;
    Long entry;
    List<ZhydelyBean> allList = new ArrayList<>();
    final List<ZhydelyBean> list = new ArrayList<>();

    /* prepare consignments from SAP */
    allList = zhydelyDao.queryForZeroStockDistSync();

    /* check if existed in Hybris, if not then update status */
    for (final ZhydelyBean bean : allList) {
      code = bean.getVbeln_d();
      order = bean.getVbeln();
      entry = bean.getPosnr();

      if (consignmentSyncDao.codeExistsInHybris(code, order, entry)) {
        list.add(bean);
      } else {
        LOG.warn("This record does not exist in Hybris, won't be processed. Consignment Code="
            + code + " Order Code="
            + order + " Entry Number=" + entry);
        zhydelyDao.updateStatus(code, ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_FAILED);
      }
    }

    return list;
  }

  @Override
  public void syncZeroStockDist(final List<ZhydelyBean> sapList) {

    final Transaction tx = Transaction.current();
    boolean succeed = false;
    final Map<String, Date> sapInfos = new HashMap<>();
    String code;
    Date wadatIst;


    /* distinct consignment code then store date */
    for (final ZhydelyBean bean : sapList) {
      sapInfos.put(bean.getVbeln_d(), bean.getWadat_ist());
    }

    /*
     * iterate every SAP record and sync shipping date to Hybris, then update sap's status at the
     * end
     */
    sapInfos.entrySet();
    for (final Map.Entry<String, Date> sapInfo : sapInfos.entrySet()) {

      succeed = false;
      code = sapInfo.getKey();
      wadatIst = sapInfo.getValue();

      tx.begin();
      try {
        consignmentSyncDao.updateLFDATByCode(code, wadatIst);
        succeed = true;
      } catch (final Exception e) {
        LOG.error("Update Failed. Code=" + code);
        e.printStackTrace();
        succeed = false;
      } finally {
        if (succeed == Boolean.TRUE) {
          tx.commit();
          tx.flushDelayedStore();
          zhydelyDao.updateStatus(code, ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_SUCCEEDED);
          LOG.info("Sync Consignment LFDAT done. Code=" + sapInfo.getKey());
        } else {
          tx.rollback();
          zhydelyDao.updateStatus(code, ConsignmentSyncConstants.SAP_ZHYDELY_STATUS_FAILED);
          LOG.error("Sync Consignment LFDAT failed. Code=" + sapInfo.getKey());
        }
      }

    }

  }

}
