package com.ncipb2b.fulfilmentprocess.strategy.impl;

import com.ncip.core.enums.DeliveryType;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class SpiltByProductType extends AbstractSplittingStrategy {

  private static final Logger LOG = Logger.getLogger(SpiltByProductType.class);

  @Override
  public List<OrderEntryGroup> perform(List<OrderEntryGroup> orderEntryListList) {
     
    List<OrderEntryGroup> list = new ArrayList<>();
    
    //訂製品:不拋物流 product.ma_type = B
    OrderEntryGroup customOrderEntryGroup = new OrderEntryGroup();
    customOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.FREIGHT);
     
    //預購品 :(大型商品[貨運 product.ma_type = E ] or 非大型商品 [宅配 product.ma_type = D])
    OrderEntryGroup preOrderEntryGroup = new OrderEntryGroup();
     
    //  Set Product Type and Delivery Mode
    int index = 0;
    for (OrderEntryGroup orderEntryGroup : orderEntryListList) {
      for (AbstractOrderEntryModel orderEntryModel : orderEntryGroup) { 
        //CUSTOM_ITEM(B)  訂製品 ,PREORDER_LOGISTICS(D) 預購品, PREORDER_FREIGHT(E) 預購品-大型商品
        if ( orderEntryGroup.get(index).getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.CUSTOM_ITEM) ||  
             orderEntryGroup.get(index).getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.PREORDER_LOGISTICS) ||
             orderEntryGroup.get(index).getProduct().getMa_type().equals(Ncipb2bFulfilmentProcessConstants.PREORDER_FREIGHT)) {
          
          LOG.info("orderCode:"+orderEntryModel.getOrder().getCode()+" orderEntry:"+orderEntryModel.getPk()+" orderEntry Quantity:"+orderEntryModel.getQuantity()+ " productCodt:"+orderEntryModel.getProduct().getCode()+" orderEntry.product.ma_type:"+orderEntryModel.getProduct().getMa_type());
          
            switch (orderEntryModel.getProduct().getMa_type()) {
              //CUSTOM_ITEM (B) 訂製品
             case Ncipb2bFulfilmentProcessConstants.CUSTOM_ITEM: 
               //出貨單類型 1(訂製品無庫存) : CUSTOM_ITEM: B 訂製品
               customOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE,Ncipb2bFulfilmentProcessConstants.CUSTOM_ITEM_VALUE);   
               break; 
               
             // PREORDER_FREIGHT(E) 預購品-大型商品 
             case Ncipb2bFulfilmentProcessConstants.PREORDER_FREIGHT:
               preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.FREIGHT); //貨運
               preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE,Ncipb2bFulfilmentProcessConstants.PREORDER_VALUE);
               break;
               
             //PREORDER_LOGISTICS(D) 預購品
             case Ncipb2bFulfilmentProcessConstants.PREORDER_LOGISTICS:
               preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,DeliveryType.LOGISTICS); //宅配 
               preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.MT_TYPE,Ncipb2bFulfilmentProcessConstants.PREORDER_VALUE); 
               break;
  
             default:
               LOG.error("Product My_type Code Error");
               break;
           } 
        } 
      }
    }
 
    if (customOrderEntryGroup.size() > 0) {
      list.add(customOrderEntryGroup);
    }
    if (preOrderEntryGroup.size() > 0) {
      list.add(preOrderEntryGroup);
    }
    return list;

  }

  @Override
  public Object getGroupingObject(AbstractOrderEntryModel abstractOrderEntryModel) {
    return null;
  }

  @Override
  public void afterSplitting(Object o, ConsignmentModel consignmentModel) {

  }

}
