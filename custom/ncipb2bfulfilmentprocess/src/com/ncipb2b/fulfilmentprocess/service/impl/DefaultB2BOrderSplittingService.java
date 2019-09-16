package com.ncipb2b.fulfilmentprocess.service.impl;

import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import com.ncipb2b.fulfilmentprocess.service.B2BOrderSplittingService;
import com.ncipb2b.fulfilmentprocess.service.NcipB2BConsignmentService;
import com.ncipb2b.fulfilmentprocess.strategy.impl.SpiltByProductType;
import com.ncipb2b.fulfilmentprocess.strategy.impl.SplitByAvailableCount;
import com.ncipb2b.fulfilmentprocess.strategy.impl.SplitByWarehouse;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.impl.DefaultOrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultB2BOrderSplittingService implements B2BOrderSplittingService {

  private static final Logger LOG = Logger.getLogger(DefaultOrderSplittingService.class);
  private List<SplittingStrategy> strategiesList = new LinkedList();

  private ModelService modelService;
  private NcipB2BConsignmentService ncipB2BConsignmentService;
  private CommerceStockService commerceStockService;

  public DefaultB2BOrderSplittingService() {
  }


  public List<ConsignmentModel> splitOrderForConsignment(AbstractOrderModel order,
      List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException {
    List<ConsignmentModel> listConsignmentModel = this
        .splitOrderForConsignmentNotPersist(order, orderEntryList);
    Iterator var5 = listConsignmentModel.iterator();

    while (var5.hasNext()) {
      ConsignmentModel consignment = (ConsignmentModel) var5.next();
      this.modelService.save(consignment);
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





//    List<OrderEntryGroup> orderEntryGroups = new LinkedList<>();
//    for (AbstractOrderEntryModel orderEntryModel : orderEntryList) {
//
//      OrderEntryGroup orderEntryGroup = new OrderEntryGroup();
//      orderEntryGroup.add(orderEntryModel);
//
//      orderEntryGroups.add(orderEntryGroup);
//    }


    // TODO: 2019/9/12 執行對應order的Strategy
    SplittingStrategy strategy;
    for(Iterator var6 = this.strategiesList.iterator(); var6.hasNext(); ) {
      strategy = (SplittingStrategy)var6.next();
      splitedList = strategy.perform(splitedList);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
      }
    }
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

    //建立 Consignments

    String orderCode;
    if (order == null) {
      orderCode = this.getUniqueNumber("ORDER", 10, "GEN0001");
    } else {
      orderCode = order.getCode();
    }
    ConsignmentModel consignmentModel;
    List<ConsignmentModel> consignmentList = new ArrayList<>();
    char prefixCode = 'a';
    for (OrderEntryGroup orderEntryGroup : splitedList
    ) {
      consignmentModel = createStockConsignment(orderEntryGroup, order, prefixCode,
          orderCode);
      ++prefixCode;
      if (consignmentModel != null) {
        consignmentList.add(consignmentModel);
      }
    }

    return consignmentList;
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
    if (orderEntryGroup.size() == 0) {
      return null;
    } else {
      ConsignmentModel consignment = getNcipB2BConsignmentService()
          .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
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
}

