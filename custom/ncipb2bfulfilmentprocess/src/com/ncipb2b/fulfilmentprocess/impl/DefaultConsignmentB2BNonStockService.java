package com.ncipb2b.fulfilmentprocess.impl;

import com.ncipb2b.fulfilmentprocess.ConsignmentB2BNonStockService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultConsignmentB2BNonStockService implements ConsignmentB2BNonStockService {

  private static final Logger LOG = Logger
      .getLogger(DefaultConsignmentB2BNonStockService.class.getName());
  private ModelService modelService;
  //  private WarehouseService warehouseService;
  private static final String RANDOM_ALGORITHM = "SHA1PRNG";


  public DefaultConsignmentB2BNonStockService() {
  }

  @Override
  public ConsignmentModel createConsignment(AbstractOrderModel orderModel, String code,
      List<AbstractOrderEntryModel> orderEntryModels) throws ConsignmentCreationException {

    ConsignmentModel cons = (ConsignmentModel) this.modelService.create(ConsignmentModel.class);
    cons.setStatus(ConsignmentStatus.READY);
    cons.setConsignmentEntries(new HashSet());
    cons.setCode(code);
    if (orderModel != null) {
      cons.setShippingAddress(orderModel.getDeliveryAddress());
    }

    Iterator var6 = orderEntryModels.iterator();

    while (var6.hasNext()) {
      AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) var6.next();
      ConsignmentEntryModel entry = (ConsignmentEntryModel) this.modelService
          .create(ConsignmentEntryModel.class);
      entry.setOrderEntry(orderEntry);
      entry.setQuantity(orderEntry.getQuantity());
      entry.setConsignment(cons);
      cons.getConsignmentEntries().add(entry);
      cons.setDeliveryMode(orderEntry.getDeliveryMode());
    }

//    List<WarehouseModel> warehouses = this.warehouseService.getWarehouses(orderEntryModels);
//    if (warehouses.isEmpty()) {
//      throw new ConsignmentCreationException("No default warehouse found for consignment");
//    } else {
//      WarehouseModel warehouse = (WarehouseModel) warehouses.iterator().next();
//      cons.setWarehouse(warehouse);
    cons.setOrder(orderModel);
    return cons;
//    }
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }
}
