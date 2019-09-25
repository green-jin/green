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
 
import com.ncip.core.enums.DeliveryType; 
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import java.util.LinkedList;
import java.util.List; 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import com.ncip.core.service.NcipCommerceStockService;


public class SplitByAvailableCount extends AbstractSplittingStrategy {

  private CommerceStockService commerceStockService;
  
  private NcipCommerceStockService ncipCommerceStockService;
 
  private static final Logger LOG = Logger.getLogger(SplitByAvailableCount.class);
  
  @Override
  public List<OrderEntryGroup> perform(List<OrderEntryGroup> orderEntryListList) {
     
    List<OrderEntryGroup> list = new LinkedList<>();
     
    //有庫存
    OrderEntryGroup stockOrderEntryGroup = new OrderEntryGroup();
    
    //無庫存
    OrderEntryGroup unStockOrderEntryGroup = new OrderEntryGroup();
    
    int index = 0;
    Long stock = 0L;
    for (OrderEntryGroup orderEntryGroup : orderEntryListList) { 
      // NORMAL_LOGISTICS :A 一般商品  ; NORMAL_FREIGHT :C 一般品-大型商品
      if ( orderEntryGroup.get(index).getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_FREIGHT) || 
           orderEntryGroup.get(index).getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_LOGISTICS) ){
        
        for (AbstractOrderEntryModel orderEntryModel : orderEntryGroup) {
          
          LOG.debug(index +": orderCode:"+orderEntryModel.getOrder().getCode()+" orderEntry:"+orderEntryModel.getPk()+" orderEntry Quantity:"+orderEntryModel.getQuantity()+ " productCodt:"+orderEntryModel.getProduct().getCode()+" orderEntry.product.ma_type:"+orderEntryModel.getProduct().getMa_type());
          
          stock = 0L;
          //Long unstock = 0L;
          Long quantity = orderEntryModel.getQuantity(); 
            if (orderEntryModel.getOrder().getStore() != null) {
              stock = getNcipCommerceStockService().getStockLevelForProductAndBaseStore(orderEntryModel.getProduct(), orderEntryModel.getOrder().getStore());
            } 
            
              //無庫存
              if (stock != null && stock - quantity < 0 ) { 
                //unstock =  (stock - quantity ) * -1  ;
                unStockOrderEntryGroup.add(orderEntryModel);
                //出貨單類型 3(一般品無庫存) : NORMAL_LOGISTICS :A 一般商品 or NORMAL_FREIGHT: C一般品-大型商品
                unStockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE,Ncipb2bFulfilmentProcessConstants.NORMAL_UNSTOCK_VALUE);   
                
                //NORMAL_FREIGHT:C 一般品-大型商品
                if(orderEntryModel.getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_FREIGHT)) {
                  unStockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.FREIGHT); 
                }else if(orderEntryModel.getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_LOGISTICS)) {
                  //NORMAL_LOGISTICS :A 一般商品
                  unStockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.LOGISTICS); 
                }  
              } 
              
              //有庫存
              if (stock != null && stock > 0) {
                stockOrderEntryGroup.add(orderEntryModel); 
                //出貨單類型 2(一般品有庫存) : NORMAL_LOGISTICS :A 一般商品 or NORMAL_FREIGHT: C一般品-大型商品
                stockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE,Ncipb2bFulfilmentProcessConstants.NORMAL_STOCK_VALUE);   
                
                //NORMAL_FREIGHT :C 一般品-大型商品
                if(orderEntryModel.getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_FREIGHT)) {
                  stockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.FREIGHT); 
                }else if(orderEntryModel.getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.NORMAL_LOGISTICS)) {
                //NORMAL_LOGISTICS :A 一般商品
                  stockOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.LOGISTICS); 
                } 
              }
          } 
      }
      index ++;
    } 
    
    if (stockOrderEntryGroup.size() > 0) {
      list.add(stockOrderEntryGroup);
    }
    if (unStockOrderEntryGroup.size() > 0) {
      list.add(unStockOrderEntryGroup);
    }
    
//    int count = 0;
//    int count1 = 0;
//    
//    for(Iterator<OrderEntryGroup> OrderEntryGroup = list.iterator(); OrderEntryGroup.hasNext();) { 
//      OrderEntryGroup test = OrderEntryGroup.next();
//      
//      for(Iterator<AbstractOrderEntryModel> OrderEntryGroup1 = test.iterator(); OrderEntryGroup1.hasNext();) {
//        stock = 0L;
//        AbstractOrderEntryModel OrderEntryGroup2 = OrderEntryGroup1.next();
//        
//        if (OrderEntryGroup2.getOrder().getStore() != null) {
//          stock = getNcipCommerceStockService().getStockLevelForProductAndBaseStore(OrderEntryGroup2.getProduct(), OrderEntryGroup2.getOrder().getStore());
//          LOG.info("[query stock]"+ stock); 
//        } 
//        
//        if (test.getParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE).equals("2") && OrderEntryGroup2.getQuantity() > stock ) {
//          OrderEntryGroup2.setQuantity(stock);
//        } else if (test.getParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE).equals("2") && OrderEntryGroup2.getQuantity() <= stock){
//          OrderEntryGroup2.setQuantity(OrderEntryGroup2.getQuantity());
//        } else {
//          OrderEntryGroup2.setQuantity(OrderEntryGroup2.getQuantity() - stock);
//        }
//        
////        if (count1 == 0) {
////          OrderEntryGroup2.setQuantity(Long.parseLong("100"));
////        } else {
////          OrderEntryGroup2.setQuantity(Long.parseLong("200"));
////        }
//        LOG.info("[PRODUCT code]"+OrderEntryGroup2.getProduct().getCode());
//        LOG.info("[PRODUCT Quantity]"+OrderEntryGroup2.getQuantity());
//        LOG.info("[PRODUCT mt_type]"+OrderEntryGroup2.getProduct().getMa_type());
//       // count1++;
//      }
      //LOG.info("[PRODUCT_TYPE]"+OrderEntryGroup.getParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE));
      //LOG.info("[PRODUCT_TYPE]"+test.getParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE));  
      //count++;
    //}
    
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
   
  public NcipCommerceStockService getNcipCommerceStockService() {
    return ncipCommerceStockService;
  }
 
  @Required
  public void setNcipCommerceStockService(NcipCommerceStockService ncipCommerceStockService) {
    this.ncipCommerceStockService = ncipCommerceStockService;
  }
  
  
}
