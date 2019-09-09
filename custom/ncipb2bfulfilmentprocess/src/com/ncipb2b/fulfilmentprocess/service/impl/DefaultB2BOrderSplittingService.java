package com.ncipb2b.fulfilmentprocess.service.impl;

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

    List<ConsignmentModel> consignmentList = new ArrayList<>();
    OrderEntryGroup orderEntryNormalGroup = new OrderEntryGroup();
//    OrderEntryGroup orderEntryNormalNonStockGroup = new OrderEntryGroup();
    OrderEntryGroup orderEntryCustomGroup = new OrderEntryGroup();
    OrderEntryGroup orderEntryPreOrderGroup = new OrderEntryGroup();

    String orderCode;
    if (order == null) {
      orderCode = this.getUniqueNumber("ORDER", 10, "GEN0001");
    } else {
      orderCode = order.getCode();
    }

    for (AbstractOrderEntryModel orderEntryModel : orderEntryList
    ) {
//      Set Producttype
      if (orderEntryModel.getProduct().getDely_type() == null) {
        orderEntryNormalGroup.add(orderEntryModel);
      } else {
        switch (orderEntryModel.getProduct().getDely_type().getCode()) {
          case "GENERAL":
            orderEntryNormalGroup.add(orderEntryModel);
            break;
          case "CUSTOM":
            orderEntryCustomGroup.add(orderEntryModel);
            break;
          case "PREORDER":
            orderEntryPreOrderGroup.add(orderEntryModel);
            break;
          default:
            LOG.info("Product Dely_type Code Error");
            break;
        }
      }

      List<OrderEntryGroup> orderEntryGroup1 = new ArrayList<>();
      orderEntryGroup1.add(orderEntryNormalGroup);

      List<OrderEntryGroup> orderEntryGroup2 = new ArrayList<>();
      orderEntryGroup2.add(orderEntryCustomGroup);

      List<OrderEntryGroup> orderEntryGroup3 = new ArrayList();
      orderEntryGroup3.add(orderEntryPreOrderGroup);

      SplittingStrategy strategy;
      for (Iterator var6 = this.strategiesList.iterator(); var6.hasNext();
          orderEntryGroup1 = strategy.perform(orderEntryGroup1)) {
        strategy = (SplittingStrategy) var6.next();
        for (OrderEntryGroup orderEntryGroup : orderEntryGroup1
        ) {
          LOG.info(orderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.WAREHOUSE_LIST)
              .toString());
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
        }
      }
      try {
        List<Object> warehouseModels = (List<Object>) orderEntryGroup1.get(0)
            .getParameter(Ncipb2bFulfilmentProcessConstants.WAREHOUSE_LIST);

      } catch (Exception e) {
      }

      for (Iterator var6 = this.strategiesList.iterator(); var6.hasNext();
          orderEntryGroup2 = strategy.perform(orderEntryGroup2)) {
        strategy = (SplittingStrategy) var6.next();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
        }
      }
      for (Iterator var6 = this.strategiesList.iterator(); var6.hasNext();
          orderEntryGroup3 = strategy.perform(orderEntryGroup3)) {
        strategy = (SplittingStrategy) var6.next();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
        }
      }
    }

    ConsignmentModel consignmentModel;
    char prefixCode = 'a';
    if (orderEntryNormalGroup.size() > 0) {
      consignmentModel = createNormalConsignmentList(orderEntryNormalGroup, order, prefixCode,
          orderCode);
      ++prefixCode;
      if (consignmentModel != null) {
        consignmentList.add(consignmentModel);
      }
    }
    if (orderEntryNormalGroup.size() > 0) {
      consignmentModel = createNonStockConsignmentList(orderEntryCustomGroup, order, prefixCode,
          orderCode);
      ++prefixCode;
      if (consignmentModel != null) {
        consignmentList.add(consignmentModel);
      }
    }
    if (orderEntryNormalGroup.size() > 0) {
      consignmentModel = createNonStockConsignmentList(orderEntryPreOrderGroup, order, prefixCode,
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

  private ConsignmentModel createNormalConsignmentList(OrderEntryGroup orderEntryGroup,
      AbstractOrderModel order, char prefixCode, String orderCode)
      throws ConsignmentCreationException {
    if (orderEntryGroup.size() == 0) {
      return null;
    } else {
      ConsignmentModel consignment = this.ncipB2BConsignmentService
          .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
//    Iterator var12 = this.strategiesList.iterator();
//    while (var12.hasNext()) {
//      SplittingStrategy strategy = (SplittingStrategy) var12.next();
//      strategy.afterSplitting(orderEntryGroup, consignment);
//    }
      return consignment;
    }

  }

  private ConsignmentModel createNonStockConsignmentList(OrderEntryGroup orderEntryGroup,
      AbstractOrderModel order, char prefixCode, String orderCode)
      throws ConsignmentCreationException {

    if (orderEntryGroup.size() == 0) {
      return null;
    } else {
      ConsignmentModel consignment = this.ncipB2BConsignmentService
          .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
//    Iterator var12 = this.strategiesList.iterator();
//    while (var12.hasNext()) {
//      SplittingStrategy strategy = (SplittingStrategy) var12.next();
//      strategy.afterSplitting(orderEntryGroup, consignment);
//    }
      return consignment;
    }
  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public NcipB2BConsignmentService getConsignmentService() {
    return ncipB2BConsignmentService;
  }

  public void setConsignmentService(
      NcipB2BConsignmentService ncipB2BConsignmentService) {
    this.ncipB2BConsignmentService = ncipB2BConsignmentService;
  }

  public CommerceStockService getCommerceStockService() {
    return commerceStockService;
  }

  public void setCommerceStockService(
      CommerceStockService commerceStockService) {
    this.commerceStockService = commerceStockService;
  }

  public void setStrategiesList(
      List<SplittingStrategy> strategiesList) {
    this.strategiesList = strategiesList;
  }

  public void setNcipB2BConsignmentService(
      NcipB2BConsignmentService ncipB2BConsignmentService) {
    this.ncipB2BConsignmentService = ncipB2BConsignmentService;
  }
}

