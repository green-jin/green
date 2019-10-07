package com.ncipb2b.fulfilmentprocess.service.impl;

import com.ncip.core.enums.DeliveryType;
import com.ncip.core.service.NcipCommerceStockService;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import com.ncipb2b.fulfilmentprocess.service.B2BOrderSplittingService;
import com.ncipb2b.fulfilmentprocess.service.NcipB2BConsignmentService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.impl.DefaultOrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.impl.StockLevelDao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultB2BOrderSplittingService implements B2BOrderSplittingService {

  private static final Logger LOG = Logger.getLogger(DefaultOrderSplittingService.class);
  
  private List<SplittingStrategy> strategiesList = new LinkedList<SplittingStrategy>();
  private StockLevelDao stockLevelDao;
  private ModelService modelService;
  private NcipB2BConsignmentService ncipB2BConsignmentService;
  private CommerceStockService commerceStockService;
  private NcipCommerceStockService ncipCommerceStockService;


  public List<ConsignmentModel> splitOrderForConsignment(AbstractOrderModel order,
      List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException {
    
    List<ConsignmentModel> listConsignmentModel = this.splitOrderForConsignmentNotPersist(order, orderEntryList);
    
    Iterator var5 = listConsignmentModel.iterator();
    
    //for query stockLevel Reserved
    Iterator var6 = listConsignmentModel.iterator();
    
    SimpleDateFormat simple = new java.text.SimpleDateFormat();
    simple.applyPattern("yyyy-MM-dd HH:mm:ss");
    
    while (var5.hasNext()) {
      ConsignmentModel  consignment = (ConsignmentModel) var5.next();
       
      Long stock = 0L;
      int addDay = 0;
      DeliveryType consignmentDeliveryType = DeliveryType.LOGISTICS;
      for (Iterator<ConsignmentEntryModel> cosignmentEntry = consignment.getConsignmentEntries().iterator() ; cosignmentEntry.hasNext();) {
        ConsignmentEntryModel cosignmentEntry1 = cosignmentEntry.next();
          if (consignment.getOrder().getStore() != null) {
              stock = getNcipCommerceStockService().getStockLevelForProductAndBaseStore(cosignmentEntry1.getOrderEntry().getProduct(), consignment.getOrder().getStore());
          }
          
          //一般品有庫存 
          if (consignment.getDely_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_STOCK_VALUE) && cosignmentEntry1.getOrderEntry().getQuantity() > stock) {
            cosignmentEntry1.setQuantity(stock);
          } else if (consignment.getDely_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_STOCK_VALUE) && cosignmentEntry1.getOrderEntry().getQuantity() <= stock) {
            cosignmentEntry1.setQuantity(cosignmentEntry1.getOrderEntry().getQuantity());
          } else {
            //一般品無庫存
            cosignmentEntry1.setQuantity(cosignmentEntry1.getOrderEntry().getQuantity() - stock);
          }
          
          //query max product listpr day
          if (cosignmentEntry1.getOrderEntry().getProduct().getListpr() != null) {
            if (Integer.valueOf(cosignmentEntry1.getOrderEntry().getProduct().getListpr()) > addDay) {
              addDay = Integer.valueOf(cosignmentEntry1.getOrderEntry().getProduct().getListpr());
            }
          }
          
          //C一般品-大型商品 or E 預購品-大型商品 為貨運  
          if(cosignmentEntry1.getOrderEntry().getProduct().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_FREIGHT) || 
             cosignmentEntry1.getOrderEntry().getProduct().equals(Ncipb2bFulfilmentProcessConstants.PREORDER_FREIGHT) ) {            
            consignmentDeliveryType = DeliveryType.FREIGHT;
          }
      }
      
      Calendar cal = Calendar.getInstance();
      Date logistic_time = null;
      Date freight_time = null;
      cal.setTime(order.getCreationtime());
      String Order_Date = simple.format(order.getCreationtime()).substring(0, 10);
      try {
        logistic_time = simple.parse(Order_Date + " 11:00:00"); //貨運單時間
        freight_time = simple.parse(Order_Date + " 14:00:00");  //宅配單時間
      } catch (ParseException e) {
        LOG.error(e.getMessage());
        e.printStackTrace();
      }
     
      //set consignment unstock setLfdat
      if(consignment.getDely_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_STOCK_VALUE)){
        //有庫存出貨單：
        //(1) 貨運單：11：00前成立的訂單，預計出貨日期=訂單成立日期，11：00以後成立的訂單，預計出貨日期=訂單成立日期+1天。
        //(2) 宅配單：14：00前成立的訂單，預計出貨日期=訂單成立日期，14：00以預計出貨日期後成立的訂單訂，預計出貨日期=訂單成立日期+1天。
        int newDay = 0;
        if(consignmentDeliveryType.equals(DeliveryType.FREIGHT) && order.getCreationtime().after(freight_time)) {
               newDay = 1;
        } else if(consignmentDeliveryType.equals(DeliveryType.LOGISTICS) && order.getCreationtime().after(logistic_time)) {
               newDay = 1;
        } else {
               newDay = 0;
        }
        cal.add(Calendar.DAY_OF_MONTH, newDay);      
      } else {
        //無庫存出貨單預設的預計出貨日期： 訂單日期+產品主檔的天數
        cal.add(Calendar.DAY_OF_MONTH, addDay);
      }
      consignment.setLfdat(cal.getTime());
      this.modelService.save(consignment); 
    }
    
    while (var6.hasNext()) {
      ConsignmentModel  consignment1 = (ConsignmentModel) var6.next();
      
      //一般品有庫存 stockLevel Reserved
      if (consignment1.getDely_type() != null && consignment1.getDely_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_STOCK_VALUE)) {
        for(Iterator<ConsignmentEntryModel> cos1 = consignment1.getConsignmentEntries().iterator(); cos1.hasNext(); ) {
          ConsignmentEntryModel cos2 = (ConsignmentEntryModel)cos1.next(); 
          WarehouseModel warehouse = consignment1.getWarehouse();
          Collection<StockLevelModel> stockLevels = this.getStockLevelDao().findStockLevels(cos2.getOrderEntry().getProduct().getCode(), Collections.singleton(warehouse));
          LOG.debug("stockLevels for resrved result => " + stockLevels);
          for(Iterator<StockLevelModel> stockinfo = stockLevels.iterator(); stockinfo.hasNext(); ) {
            StockLevelModel stockResrved = stockinfo.next();
            LOG.debug("Stock Level for Warehouse name => " + stockResrved.getWarehouse().getName());
            stockResrved.setReserved(stockResrved.getReserved() + cos2.getQuantity().intValue());
            modelService.save(stockResrved);
          }
        }
      }
    }
    
    this.modelService.save(order);
    return listConsignmentModel;
  }

  // TODO: 2019/8/22  Split Order Consignment
  public List<ConsignmentModel> splitOrderForConsignmentNotPersist(AbstractOrderModel order,
      List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException {
    
    List<OrderEntryGroup> splitedList = new ArrayList<>();
    
    OrderEntryGroup tmpOrderEntryList = new OrderEntryGroup();
    tmpOrderEntryList.addAll(orderEntryList);
    splitedList.add(tmpOrderEntryList);
    
    
    if (this.strategiesList == null || this.strategiesList.isEmpty()) {
      LOG.warn("No splitting strategies were configured!");
    }

    // TODO: 2019/9/12 執行對應order的Strategy
    List<OrderEntryGroup> splitedList1 = new ArrayList<>();
    List<OrderEntryGroup> splitedList2 = new ArrayList<>();
    //OrderEntryGroup tmpOrderEntryList1 = new OrderEntryGroup();
    //SplittingStrategy strategy;
    
    for (SplittingStrategy strategy : this.strategiesList) {
      
      splitedList1 = strategy.perform(splitedList);
      LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "] splitedList1: " +splitedList1.size());
      
      for(OrderEntryGroup orderEntryGroup : splitedList1 ) {
        OrderEntryGroup tmpOrderEntryList1 = new OrderEntryGroup();
        tmpOrderEntryList1 = orderEntryGroup;
        splitedList2.add(tmpOrderEntryList1);  
      } 
    }
    
    //Create Consignments 
    String orderCode;
    if (order == null) {
      orderCode = this.getUniqueNumber("ORDER", 10, "GEN0001");
    } else {
      orderCode = order.getCode();
    }
    
    LOG.debug(" before create consignment ---order code  :"+orderCode.toString() );
    
    ConsignmentModel consignmentModel;
    List<ConsignmentModel> consignmentList = new ArrayList<>();
    char prefixCode = 'a';
    
    for (OrderEntryGroup orderEntryGroup : splitedList2 ) {  
         
      consignmentModel = createStockConsignment(orderEntryGroup, order, prefixCode, orderCode); 
      ++prefixCode;
      
      if (consignmentModel != null) {
        consignmentList.add(consignmentModel);
      }
    }
    
    return consignmentList;
    
//    for(Iterator var6 = this.strategiesList.iterator(); var6.hasNext(); ) {
//      strategy = (SplittingStrategy)var6.next();
//     
//      LOG.info("strategy name: " +strategy.getClass().getName()); 
//      
//      splitedList = strategy.perform(splitedList);
//      
//      LOG.info("splitedList: " +splitedList.size());
//      
//      for(Iterator<OrderEntryGroup> var16 = splitedList.iterator(); var16.hasNext(); ) {
//          tmpOrderEntryList1 = (OrderEntryGroup)var16.next(); 
//        
//          LOG.info("tmpOrderEntryList1-->ProductType.NORMAL:"+tmpOrderEntryList1.getParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE)); 
//          LOG.info("tmpOrderEntryList1-->DELIVERY_MODE :" + tmpOrderEntryList1.getParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE));
//          LOG.info("tmpOrderEntryList1-->STOCK_STATUS :" + tmpOrderEntryList1.getParameter(Ncipb2bFulfilmentProcessConstants.STOCK_STATUS));
//         
//          splitedList1.add(tmpOrderEntryList1); 
//       
//        LOG.info("splitedList1: " +splitedList1.size());
//      }
//
//      //splitedList = strategy.perform(splitedList);
//      
//      splitedList = strategy.perform(splitedList);
//      
//      if (LOG.isDebugEnabled()) {
//        LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
//      }
//    }
//
//
////    var6.hasNext()
//    strategy = (SplittingStrategy) var6.next();
//    if (strategy.getClass().getName().equals(SpiltByProductType.class.getName())) {
//      orderEntryGroupList = strategy.perform(orderEntryGroups);
//
//      for (int i = 0; i < orderEntryGroups.size(); i++) {
//        orderEntryGroups.get(0).setParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE,
//            orderEntryGroupList.get(0)
//                .getParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE));
//        orderEntryGroups.get(0).setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
//            orderEntryGroupList.get(0)
//                .getParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE));
//      }
//    }
//
//    // TODO: 2019/9/12 新增出數與未出數
//    strategy = (SplittingStrategy) var6.next();
//    if (strategy.getClass().getName().equals(SplitByAvailableCount.class.getName())) {
//      orderEntryGroupList = strategy.perform(orderEntryGroups);
//
//    }
//
//    // TODO: 2019/9/12 物料By倉庫分拆
//    strategy = (SplittingStrategy) var6.next();
//    if (strategy.getClass().getName().equals(SplitByWarehouse.class.getName())) {
//      orderEntryGroupList = strategy.perform(orderEntryGroups);
//      // TODO: 2019/9/12 回壓保留數
//    }



    //--------------------------------------------------------------
    // TODO: 2019/9/11 寫入 Order Entry Group 欄normal位資料
    //--------------------------------------------------------------
//    List<OrderEntryGroup> list = new ArrayList<>();
//
//    OrderEntryGroup normalStockOEGroup = new OrderEntryGroup();
//    for (OrderEntryGroup orderEntryGroup : orderEntryGroups) {
//      // TODO: 2019/9/12 判斷條件 該物件屬於 一般有庫存商品
//      normalStockOEGroup.add(orderEntryGroup.get(0));
//    }
//    if (normalStockOEGroup.size() > 0) {
//      list.add(normalStockOEGroup);
//    }
//
//    OrderEntryGroup normalNoNStockOEGroup = new OrderEntryGroup();
//    for (OrderEntryGroup orderEntryGroup : orderEntryGroups) {
//      // TODO: 2019/9/12 判斷條件 該物件屬於 一般無庫存商品
//      normalNoNStockOEGroup.add(orderEntryGroup.get(0));
//    }
//    if (normalNoNStockOEGroup.size() > 0) {
//      list.add(normalNoNStockOEGroup);
//    }
//
//    OrderEntryGroup customOEGroup = new OrderEntryGroup();
//    for (OrderEntryGroup orderEntryGroup : orderEntryGroups) {
//      // TODO: 2019/9/12 判斷條件 該物件屬於 客製化商品
//      customOEGroup.add(orderEntryGroup.get(0));
//    }
//    if (customOEGroup.size() > 0) {
//      list.add(customOEGroup);
//    }
//
//    OrderEntryGroup preOrderOEGroup = new OrderEntryGroup();
//    for (OrderEntryGroup orderEntryGroup : orderEntryGroups) {
//      // TODO: 2019/9/12 判斷條件 該物件屬於 預購庫存商品
//      preOrderOEGroup.add(orderEntryGroup.get(0));
//    }
//    if (preOrderOEGroup.size() > 0) {
//      list.add(preOrderOEGroup);
//    } 
  }

  protected String getUniqueNumber(String code, int digits, String startValue) {
    try {
      NumberSeriesManager.getInstance().getNumberSeries(code);
    } catch (JaloInvalidParameterException var5) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Invalid Parameter Exception" + var5);
      }

      NumberSeriesManager.getInstance()
          .createNumberSeries(code, startValue, 1, digits, (String) null);
    }

    return NumberSeriesManager.getInstance().getUniqueNumber(code, digits);
  }

  private ConsignmentModel createStockConsignment(OrderEntryGroup orderEntryGroup,
      AbstractOrderModel order, char prefixCode, String orderCode)
      throws ConsignmentCreationException {
    
     String mtType = null;
     DeliveryType deliveryMode = null;
     String stockStatus = null;
     String stockQuantity = null; 
    
    if (orderEntryGroup.size() == 0) {
      return null;
    } else {
      
      mtType   = orderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE)   != null ? orderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE).toString()   : "";
      deliveryMode  = (DeliveryType) orderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE) ;

      LOG.debug("orderEntryGroup-->mtType:" +mtType +",deliveryMode :" + deliveryMode);
      
      ConsignmentModel consignment = getNcipB2BConsignmentService().createConsignment(order, prefixCode + orderCode, orderEntryGroup, mtType , deliveryMode);
      
//    Iterator var12 = this.strategiesList.iterator();
//    while (var12.hasNext()) {
//      SplittingStrategy strategy = (SplittingStrategy) var12.next();
//      strategy.afterSplitting(orderEntryGroup, consignment);
//    }
      return consignment;
    }

  }
  // TODO: 2019/9/12 日後有需要再增加的無庫存建立出貨單
//  private ConsignmentModel createNoNStockConsignment(OrderEntryGroup orderEntryGroup,
//      AbstractOrderModel order, char prefixCode, String orderCode)
//      throws ConsignmentCreationException {
//
//    if (orderEntryGroup.size() == 0) {
//      return null;
//    } else {
//      ConsignmentModel consignment = getNcipB2BConsignmentService()
//          .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
////    Iterator var12 = this.strategiesList.iterator();
////    while (var12.hasNext()) {
////      SplittingStrategy strategy = (SplittingStrategy) var12.next();
////      strategy.afterSplitting(orderEntryGroup, consignment);
////    }
//      return consignment;
//    }
//  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }


  public CommerceStockService getCommerceStockService() {
    return commerceStockService;
  }

  public void setCommerceStockService(
      CommerceStockService commerceStockService) {
    this.commerceStockService = commerceStockService;
  }

  public List<SplittingStrategy> getStrategiesList() {
    return strategiesList;
  }

  public void setStrategiesList(
      List<SplittingStrategy> strategiesList) {
    this.strategiesList = strategiesList;
  }

  public NcipB2BConsignmentService getNcipB2BConsignmentService() {
    return ncipB2BConsignmentService;
  }

  public void setNcipB2BConsignmentService(
      NcipB2BConsignmentService ncipB2BConsignmentService) {
    this.ncipB2BConsignmentService = ncipB2BConsignmentService;
  }
  
  protected StockLevelDao getStockLevelDao() {
    return this.stockLevelDao;
  }

  @Required
  public void setStockLevelDao(StockLevelDao stockLevelDao) {
     this.stockLevelDao = stockLevelDao;
  }
  
  public NcipCommerceStockService getNcipCommerceStockService() {
    return ncipCommerceStockService;
  }
 
  @Required
  public void setNcipCommerceStockService(NcipCommerceStockService ncipCommerceStockService) {
    this.ncipCommerceStockService = ncipCommerceStockService;
  }
    
  
}

