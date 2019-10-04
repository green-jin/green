package com.ncip.core.dao.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.ncip.core.dao.ConsignmentSyncDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

@Component(value = "consignmentSyncDao")
public class DefaultConsignmentSyncDao implements ConsignmentSyncDao {

  private static final Logger LOG = Logger.getLogger(DefaultConsignmentSyncDao.class);

  @Resource
  private FlexibleSearchService flexibleSearchService;
  @Resource
  private ModelService modelService;

  @Override
  public List<ConsignmentModel> prepareAllConsignmentsByStatus() {

    final StringBuffer sql = new StringBuffer();
    sql.append("select " + ConsignmentModel.PK + " from {" + ConsignmentModel._TYPECODE + "}");
    final SearchResult<ConsignmentModel> result =
        flexibleSearchService.search(new FlexibleSearchQuery(sql.toString()));

    final List<ConsignmentModel> list = result.getResult();

    if (list == null || list.size() == 0) {
      throw new RuntimeException("No Consignments Found");
    } else {
      LOG.info("Count of Consignments: " + list.size());
      return list;
    }

  }

  @Override
  public boolean codeExistsInHybris(final String code, final String order, final Long entry) {

    ConsignmentModel conModel = new ConsignmentModel();
    final ConsignmentEntryModel ceModel = new ConsignmentEntryModel();
    final OrderModel oModel = new OrderModel();

    conModel.setCode(code);

    try {
      conModel = flexibleSearchService.getModelByExample(conModel);
    } catch (final ModelNotFoundException e) {
      LOG.warn("No consignment code found.");
      return false;
    }

    if (conModel.getOrder().getCode().equals(order)) {
      for (final ConsignmentEntryModel conEntryModel : conModel.getConsignmentEntries()) {
        if (Long.valueOf(conEntryModel.getOrderEntry().getEntryNumber()) == entry) {
          return true;
        }
      }
    } else {
      LOG.warn("No order code found.");
      return false;
    }

    LOG.warn("No order entry found.");

    return false;
  }

  @Override
  public void updateLFDATByCode(final String code, final Date wadatIst) {

    ConsignmentModel model = new ConsignmentModel();
    model.setCode(code);
    try {
      model = flexibleSearchService.getModelByExample(model);
    } catch (final ModelNotFoundException e) {
      throw new RuntimeException("Consignment not found in Hybris. Code=" + code);
    }

    /* update from SAP Wadat_ist to Hybris LFDAT */
    model.setLfdat(wadatIst);
    modelService.save(model);

  }

}
