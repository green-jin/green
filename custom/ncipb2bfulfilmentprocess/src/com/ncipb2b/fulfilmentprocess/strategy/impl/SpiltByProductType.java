package com.ncipb2b.fulfilmentprocess.strategy.impl;

import static com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants.CUSTOM_ITEM;
import static com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants.NORMAL_FREIGHT;
import static com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants.NORMAL_LOGISTICS;
import static com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants.PREORDER_FREIGHT;
import static com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants.PREORDER_LOGISTICS;

import com.ncip.core.enums.DeliveryType;
import com.ncip.core.enums.ProductType;
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

    OrderEntryGroup normalOrderEntryGroup = new OrderEntryGroup();
    normalOrderEntryGroup
        .setParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE, ProductType.NORMAL);

    OrderEntryGroup customOrderEntryGroup = new OrderEntryGroup();
    customOrderEntryGroup
        .setParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE, ProductType.CUSTOM);
    customOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
        DeliveryType.FREIGHT);

    OrderEntryGroup preOrderEntryGroup = new OrderEntryGroup();
    preOrderEntryGroup
        .setParameter(Ncipb2bFulfilmentProcessConstants.PRODUCT_TYPE, ProductType.PREORDER);

//      Set Product Type and Delivery Mode
    for (OrderEntryGroup orderEntryGroup : orderEntryListList
    ) {
      for (AbstractOrderEntryModel orderEntryModel : orderEntryGroup
      ) {
        if (orderEntryModel.getProduct().getMy_type() == null) {
          normalOrderEntryGroup.add(orderEntryModel);
          normalOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
              DeliveryType.LOGISTICS);
        } else {
          switch (orderEntryModel.getProduct().getMy_type()) {
            case NORMAL_FREIGHT:
              normalOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
                  DeliveryType.FREIGHT);
            case NORMAL_LOGISTICS:
              if (normalOrderEntryGroup
                  .getParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE)
                  == null) {
                normalOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
                    DeliveryType.LOGISTICS);
              }
              normalOrderEntryGroup.add(orderEntryModel);
              break;
            case CUSTOM_ITEM:

              customOrderEntryGroup.add(orderEntryModel);
              break;
            case PREORDER_FREIGHT:
              preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
                  DeliveryType.FREIGHT);
            case PREORDER_LOGISTICS:
              if (preOrderEntryGroup
                  .getParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE)
                  == null) {
                preOrderEntryGroup.setParameter(Ncipb2bFulfilmentProcessConstants.DELIVERY_MODE,
                    DeliveryType.LOGISTICS);
              }
              preOrderEntryGroup.add(orderEntryModel);
              break;

            default:
              if (LOG.isDebugEnabled()) {
                LOG.info("Product My_type Code Error");
              }
              break;
          }
        }
      }
    }

    if (normalOrderEntryGroup.size() > 0) {
      list.add(normalOrderEntryGroup);
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
