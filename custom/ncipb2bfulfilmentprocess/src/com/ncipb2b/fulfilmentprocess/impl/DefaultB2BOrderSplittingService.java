package com.ncipb2b.fulfilmentprocess.impl;

import com.ncipb2b.fulfilmentprocess.B2BOrderSplittingService;
import com.ncipb2b.fulfilmentprocess.ConsignmentB2BNonStockService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.ConsignmentService;
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
  private ConsignmentService consignmentService;
  private CommerceStockService commerceStockService;
  private ConsignmentB2BNonStockService consignmentB2BNonStockService;

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
    OrderEntryGroup orderEntryNormalNonStockGroup = new OrderEntryGroup();
    OrderEntryGroup orderEntryCustomGroup = new OrderEntryGroup();
    OrderEntryGroup orderEntryFutureGroup = new OrderEntryGroup();

    for (AbstractOrderEntryModel orderEntryModel: orderEntryList
    ) {
//      Set Producttype
      //orderEntryModel.getProduct().getCode()

    }

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

      ConsignmentModel consignment = this.consignmentService
          .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
      ++prefixCode;
      Iterator var12 = this.strategiesList.iterator();

      while (var12.hasNext()) {
        SplittingStrategy strategy = (SplittingStrategy) var12.next();
        strategy.afterSplitting(orderEntryGroup, consignment);
      }
      return consignment;

  }
  private ConsignmentModel createNonStockConsignmentList(OrderEntryGroup orderEntryGroup,
      AbstractOrderModel order, char prefixCode, String orderCode)
      throws ConsignmentCreationException {

    ConsignmentModel consignment = this.consignmentService
        .createConsignment(order, prefixCode + orderCode, orderEntryGroup);
    ++prefixCode;
    Iterator var12 = this.strategiesList.iterator();

    while (var12.hasNext()) {
      SplittingStrategy strategy = (SplittingStrategy) var12.next();
      strategy.afterSplitting(orderEntryGroup, consignment);
    }
    return consignment;

  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public ConsignmentService getConsignmentService() {
    return consignmentService;
  }

  public void setConsignmentService(
      ConsignmentService consignmentService) {
    this.consignmentService = consignmentService;
  }

  public CommerceStockService getCommerceStockService() {
    return commerceStockService;
  }

  public void setCommerceStockService(
      CommerceStockService commerceStockService) {
    this.commerceStockService = commerceStockService;
  }

  public ConsignmentB2BNonStockService getConsignmentB2BNonStockService() {
    return consignmentB2BNonStockService;
  }

  public void setConsignmentB2BNonStockService(
      ConsignmentB2BNonStockService consignmentB2BNonStockService) {
    this.consignmentB2BNonStockService = consignmentB2BNonStockService;
  }
}
