package com.ncip.core.consignment.dao.impl;

import com.ncip.core.consignment.dao.NcipConsignmentDao;
import com.ncip.core.enums.DeliveryType;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;

public class DefaultNcipConsignmentDao implements NcipConsignmentDao {

  @Resource
  ModelService modelService;

  @Resource
  FlexibleSearchService flexibleSearchService;

  private final static String NOW = "now";
  private final static String YESTERDAY = "now";
  private final static String TYPE = "type";


  @Override
  public List<ConsignmentModel> GetConsignments() throws ConsignmentException {
    final String searchString = "select {pk} from {consignment}";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByTimeAndType(Calendar calendar,
      DeliveryType deliveryType)
      throws ConsignmentException {

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String now = sdf.format(cal.getTime());
    cal.add(Calendar.DATE, -1);
    String yesterday = sdf.format(cal.getTime());
    final String searchString =
        "select {pk} from {consignment} where ({consignment.SHIPPINGDATE} between TO_DATE('"
            + "?" + NOW + "','YYYY-MM-DD HH24:MI:SS') and TO_DATE('"
            + "?" + YESTERDAY + "','YYYY-MM-DD HH24:MI:SS')) and ({product.type} = "
            + "?" + TYPE + ")";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    query.addQueryParameter(NOW, now);
    query.addQueryParameter(YESTERDAY, yesterday);
    query.addQueryParameter(TYPE, deliveryType.getCode());
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();

  }


  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public FlexibleSearchService getFlexibleSearchService() {
    return flexibleSearchService;
  }

  public void setFlexibleSearchService(
      FlexibleSearchService flexibleSearchService) {
    this.flexibleSearchService = flexibleSearchService;
  }
}
