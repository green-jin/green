/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncipb2b.fulfilmentprocess.strategy.impl;

import com.ncip.core.enums.ProductType;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;


public class SplitByAvailableCount extends AbstractSplittingStrategy {

  private CommerceStockService commerceStockService;


  @Override
  public List<OrderEntryGroup> perform(List<OrderEntryGroup> orderEntryListList) {
    List<OrderEntryGroup> list = new LinkedList<>();
    OrderEntryGroup stockOrderEntryGroup = new OrderEntryGroup();
    stockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.STOCK_STATUS,
        Ncipb2bFulfilmentProcessConstants.STOCK);
    OrderEntryGroup unStockOrderEntryGroup = new OrderEntryGroup();
    unStockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.STOCK_STATUS,
        Ncipb2bFulfilmentProcessConstants.UNSTOCK);
//    OrderEntryGroup StockNormal

    for (OrderEntryGroup orderEntryGroup : orderEntryListList) {
      if (orderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE)
          .equals(ProductType.NORMAL)) {
        for (AbstractOrderEntryModel orderEntryModel : orderEntryGroup
        ) {
          Long stock = 0L;
          if (orderEntryModel.getDeliveryPointOfService() != null) {
            stock = getCommerceStockService()
                .getStockLevelForProductAndPointOfService(orderEntryModel.getProduct(),
                    orderEntryModel.getDeliveryPointOfService());
          } else {
            if (orderEntryModel.getOrder().getStore() != null) {
              stock = getCommerceStockService()
                  .getStockLevelForProductAndBaseStore(orderEntryModel.getProduct(),
                      orderEntryModel.getOrder().getStore());
            }
          }
          Long unstock =
              stock == null ? -1 * orderEntryModel.getQuantity() : stock - orderEntryModel.getQuantity();
//          Long unstock = stock - orderEntryModel.getQuantity();
          if (unstock > 0) {
            stockOrderEntryGroup.add(orderEntryModel);
          } else if (stock == null || stock == 0) {
            unStockOrderEntryGroup.add(orderEntryModel);
          } else {
            orderEntryModel.setQuantity(stock);
            stockOrderEntryGroup.add(orderEntryModel);
//            orderEntryModel.setQuantity(Math.abs(unstock));
//            unStockOrderEntryGroup.add(orderEntryModel);
//            orderEntryModel.setQuantity(Math.abs(unstock - 100));
//            unStockOrderEntryGroup.add(orderEntryModel);
          }
        }
      } else {
        orderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.STOCK_STATUS,
            Ncipb2bFulfilmentProcessConstants.UNSTOCK);
        list.add(orderEntryGroup);
      }
    }
    if (stockOrderEntryGroup.size() > 0) {
      list.add(stockOrderEntryGroup);
    }
    if (unStockOrderEntryGroup.size() > 0) {
      list.add(unStockOrderEntryGroup);
    }
    return list;
  }


  @Override
  public Object getGroupingObject(final AbstractOrderEntryModel orderEntry) {
    return null;
  }

  @Override
  public void afterSplitting(final Object groupingObject, final ConsignmentModel createdOne) {
    //nothing to do
  }

  protected CommerceStockService getCommerceStockService() {
    return commerceStockService;
  }

  @Required
  public void setCommerceStockService(final CommerceStockService commerceStockService) {
    this.commerceStockService = commerceStockService;
  }
}
