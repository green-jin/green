package com.ncip.core.job;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.ncip.core.data.ZData;
import com.ncip.core.service.TransferOrderService;
import com.ncip.core.service.ZdataService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class TransferOrderJob extends AbstractJobPerformable<CronJobModel>{

  /*
   *parameters setting
   */
  private static final Logger LOG = Logger.getLogger(TransferOrderJob.class);
  private TransferOrderService transferOrderService;
  @Resource
  private ConfigurationService configurationService;
  private ZdataService zdataService;
  List<ZData> oriZdataList = new ArrayList();



  public void setTransferOrderService(final TransferOrderService transferOrderService)
  {
      this.transferOrderService = transferOrderService;
  }


  public void setZdataService(final ZdataService zdataService)
  {
      this.zdataService = zdataService;
  }
  public ConfigurationService getConfigurationService() {
    return configurationService;
}


public void setConfigurationService(final ConfigurationService configurationService) {
    this.configurationService = configurationService;
}


public TransferOrderService getTransferOrderService() {
    return transferOrderService;
}


public ZdataService getZdataService() {
    return zdataService;
}

  /***
   * Mainly function for transfer Order data,it's to get data from Hybris Table in a specified time, and also to check the status and
   * whether has existed in ZTable or not. Afterward, It will set data into the ZData bean and then set into ZDataList.
   * Next,To call zdataService to insert data into ZTable.
   */
  @Override
  public PerformResult perform(final CronJobModel cronjob) {

    final String exectime = getExecutionPeriod();
    final List<OrderModel> orderItems = transferOrderService.getAllOldersAfterSpecifiedTime(exectime);
    final HashMap<Integer, ZData> zdataHM = new HashMap<>();
    final List<ZData> zdataList = new ArrayList();


    if (orderItems.isEmpty())
    {
        LOG.info("No order items for this Period, skipping insert data into Z Table");
        return new PerformResult(CronJobResult.FAILURE, CronJobStatus.PAUSED);
    }

    try
    {
        oriZdataList = zdataService.getAll();
    }
    catch (final SQLException e1)
    {
        e1.printStackTrace();
    }


    for (final OrderModel order : orderItems)
    {
        if (checkStatus(order.getStatus().getCode(), order.getCode()))
        {
            final List<AbstractOrderEntryModel> entries = order.getEntries();
            for (final AbstractOrderEntryModel entry : entries)
            {
                final ZData zdata = new ZData();

                zdata.setVkORG(order.getStore().getUid());
                final B2BCustomerModel b2BC = transferOrderService.getCustomerUIDByUserPK(order.getUser().getPk().toString());

                for (final PrincipalGroupModel group : b2BC.getGroups())
                {
                    if (group.getClass().equals(B2BUserGroupModel.class))
                    {
                        zdata.setVkGRP(group.getUid());

                    }
                }
                zdata.setCustomerID(b2BC.getCustomerID());

                final B2BUserGroupModel b2BUGroup = transferOrderService.getB2BUserGroupByUGUID(zdata.getVkGRP());


          // zdata.setZTERM(b2BUGroup.getUnit().getZTerm());
                zdata.setInCO1(b2BUGroup.getUnit().getInco1());
                zdata.setInCO2(b2BUGroup.getUnit().getInco2());
                zdata.setPerNR(b2BUGroup.getUnit().getPernr());
                zdata.setLifNR(b2BUGroup.getUnit().getLifnr());

                zdata.setCode(order.getCode());
                zdata.setCurrency(order.getCurrency().getIsocode());
                zdata.setStatus(order.getStatus().getCode());
                zdata.setUnit(order.getUnit().getId());
                zdata.setFrm_SYS(configurationService.getConfiguration().getString("FRM_SYS"));
                zdata.setTo_SYS(configurationService.getConfiguration().getString("TO_SYS"));
                zdata.setIcType(order.getIcType());
                zdata.setDataEXCHANGESTATUS(configurationService.getConfiguration().getString("DATAEXCHANGSTATUS"));
                zdata.setCrt_DATE(zDataDateTransform(order.getDate().toString()));
                zdata.setEntryNUMBER(entry.getEntryNumber());
                zdata.setProduct(Double.valueOf(entry.getProduct().getCode()));
                zdata.setBasePrice(entry.getBasePrice());
                zdata.setQuantity(entry.getQuantity());
                zdata.setMa_TYPE(entry.getProduct().getMaType());

                zdataList.add(zdata);
            }

        }
        else
        {
            continue;
        }

    }
    try
    {
        if (zdataService.insert(zdataList))
        {
            LOG.info("Order items for this period was inserted into the Z Table successfully");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        else
        {
            LOG.error("Order items for this period was not inserted into the Z Table");
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.PAUSED);
        }
    }
    catch (final SQLException e)
    {
        e.printStackTrace();
        LOG.error("Order items for this period was not inserted into the Z Table");
        return new PerformResult(CronJobResult.FAILURE, CronJobStatus.PAUSED);
    }
  }



/***
 * The function is to parse the data-Create_date from Hybris table to "YYYYMMddHHmmss" form.
 *
 * @param date
 * @return the data is parsed to "YYYYMMddHHmmss" form.
 */
private String zDataDateTransform(final String date)
{
    final SimpleDateFormat tempSdfForZdataDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
    Date tempDateFormat = null;
    try
    {
        tempDateFormat = tempSdfForZdataDate.parse(date);
    }
    catch (final ParseException e)
    {
        e.printStackTrace();
    }
    final String sdfForZdataDate = new SimpleDateFormat("YYYYMMddHHmmss").format(tempDateFormat);

    return sdfForZdataDate;
}


/***
 * The function is to check the execution time and return the different date data
 *
 * @return the execTime for system to add ModifyTime condition in flexibleSearch.
 * @throws ParseException
 */
private String getExecutionPeriod()
{
    final Date date = new Date();
    final Calendar currentDay = Calendar.getInstance();
    currentDay.add(Calendar.DATE, -1);
    final String sdfForYesterDate = new SimpleDateFormat("yyyy/MM/dd").format(currentDay.getTime());
    final SimpleDateFormat sdfForDate = new SimpleDateFormat("yyyy/MM/dd");
    final SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm:ss");

    final String formatedExecTime = sdfForTime.format(date);
    final String sdfForToday = sdfForDate.format(date);


     Calendar currentTime= null;
     Calendar sectionAt11= null;
     Calendar sectionAt14= null;
     Calendar sectionAt23= null;
     try {
      currentTime = Calendar.getInstance();
      currentTime.setTime((sdfForTime.parse(sdfForTime.format(date))));
      sectionAt11 = Calendar.getInstance();
      sectionAt11.setTime(sdfForTime.parse(
          configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt11")));
      sectionAt14 = Calendar.getInstance();
      sectionAt14.setTime(sdfForTime.parse(
          configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt14")));
      sectionAt23 = Calendar.getInstance();
      sectionAt23.setTime(sdfForTime.parse(configurationService
          .getConfiguration().getString("sendOrdersExecutionSectionAt23")));
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    String execTime = null;

    System.out.println(currentTime);
    if(currentTime.after(sectionAt11) && currentTime.before(sectionAt14)) {
      execTime = sdfForYesterDate + " "
          + configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt23")+";"+
          sdfForToday + " " + configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt11");

    }else if(currentTime.after(sectionAt14) && currentTime.before(sectionAt23)) {
      execTime = sdfForToday + " " + configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt11")+";"+
      sdfForToday + " " + configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt14");

    }else {
      execTime = sdfForToday + " " + configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt14")+";"+
      sdfForToday+" "+ configurationService.getConfiguration().getString("sendOrdersExecutionSectionAt23");
    }


    return execTime;

}


/***
 * The function is to check the Order's status first,and then if status is "CREATED",the function will immediately
 * return true. if status is "CANCELLED",the function will call checkZtable(OrderCode) to check whether the data has
 * existed in the ZTable. if data has existed, the function will return true.Also, if hasn't existed, return false.
 *
 * @param orderstatus
 *           is order's status
 * @param ordercode
 *           is order's order code
 * @return Boolean value for main function check whether the data should be inserted into the ZTable.
 */
private Boolean checkStatus(final String orderstatus, final String ordercode)
{

    if ((orderstatus.toUpperCase()).equals("CREATED"))
    {
        return true;
    }
    else if ((orderstatus.toUpperCase()).equals("CANCELLED"))
    {
        if (checkZtable(ordercode))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    else
    {
        return false;
    }
}

/***
 * The function is to check whether the order has existed in ZTable by condition-OrderCode.
 *
 * @param ordercode
 *           is Order's code.
 *
 * @return Boolean value
 */
private Boolean checkZtable(final String ordercode)
{
    for (final ZData oriZdata : oriZdataList)
    {

        if (ordercode.equals(oriZdata.getCode()))
        {
            return true;
        }
    }

    return false;
}





}
