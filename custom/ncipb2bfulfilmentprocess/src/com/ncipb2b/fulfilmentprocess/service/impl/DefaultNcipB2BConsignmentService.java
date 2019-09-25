package com.ncipb2b.fulfilmentprocess.service.impl;


import com.ncip.core.enums.DeliveryType;
import com.ncip.core.service.NcipCommerceStockService; 
import com.ncipb2b.fulfilmentprocess.service.NcipB2BConsignmentService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DefaultNcipB2BConsignmentService implements NcipB2BConsignmentService {

  private static final Logger LOG = Logger.getLogger(DefaultNcipB2BConsignmentService.class.getName());
  
  @Resource
  private ModelService modelService;
  @Resource
  private WarehouseService warehouseService;
  @Resource
  private NcipCommerceStockService ncipCommerceStockService;
  
  public ConsignmentModel createConsignment(AbstractOrderModel order, String code,List<AbstractOrderEntryModel> orderEntries,
      String mtType,DeliveryType delivryModel) throws ConsignmentCreationException {

    ConsignmentModel cons = this.modelService.create(ConsignmentModel.class);
    
    cons.setDely_type(mtType);
    cons.setNcipdelivery(delivryModel);
    cons.setStatus(ConsignmentStatus.READY);
    cons.setConsignmentEntries(new HashSet());
    cons.setCode(code);
     
    if (order != null) {
      cons.setShippingAddress(order.getDeliveryAddress());
    }

    Iterator var6 = orderEntries.iterator();

    while (var6.hasNext()) {      
      AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) var6.next();
      ConsignmentEntryModel entry = this.modelService.create(ConsignmentEntryModel.class);
      entry.setOrderEntry(orderEntry);
      entry.setQuantity(orderEntry.getQuantity()); 
      entry.setConsignment(cons);
      cons.getConsignmentEntries().add(entry);
      cons.setDeliveryMode(orderEntry.getDeliveryMode());
    }
    
    //qeruy warehouses productCode Available Amount
    List<WarehouseModel> warehouses = this.warehouseService.getWarehouses(orderEntries);
    if (warehouses.isEmpty()) {
      LOG.error("No default warehouse found for consignment");
      throw new ConsignmentCreationException("No default warehouse found for consignment");
    } else {
      for (Iterator<WarehouseModel> ws = warehouses.iterator(); ws.hasNext();) {
        WarehouseModel warehouse = ws.next();
        cons.setWarehouse(warehouse);
      }
      //WarehouseModel warehouse = (WarehouseModel) warehouses.iterator().next();
      cons.setOrder(order);
      return cons;
    }
  }


  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public void setWarehouseService(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }
  
  public NcipCommerceStockService getNcipCommerceStockService() {
    return ncipCommerceStockService;
  }
 
  public void setNcipCommerceStockService(NcipCommerceStockService ncipCommerceStockService) {
    this.ncipCommerceStockService = ncipCommerceStockService;
  }

}
