package com.ncip.core.consignment.impl;

import com.ncip.core.consignment.ConsignmentService;
import com.ncip.core.consignment.dao.ConsignmentDao;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;

public class DefaultConsignmentService implements ConsignmentService {

  @Resource
  ConsignmentDao consignmentDao;

  @Resource
  FlexibleSearchService flexibleSearchService;

  private static final String NOW = "now";
  private static final String YESTERDAY = "yseterday";
  private static final String DELY_SIZE = "dely_size";

  @Override
  public ConsignmentModel GetConsignment(ConsignmentProcessModel consignmentProcessModel)
      throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignments() throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByTimeAndType(Calendar time, String dely_size)
      throws ConsignmentException {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String now = sdf.format(cal.getTime());
    cal.add(Calendar.DATE, -1);
    String yesterday = sdf.format(cal.getTime());
    final String searchString =
        "select {pk} from {consignment as c} where ({c.SHIPPINGDATE} between TO_DATE('?now','YYYY-MM-DD HH24:MI:SS') and TO_DATE('?yesterday','YYYY-MM-DD HH24:MI:SS')) and ({product.DELY_SIZE} = ?dely_size)";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    query.addQueryParameter(NOW, now);
    query.addQueryParameter(YESTERDAY, yesterday);
    query.addQueryParameter(DELY_SIZE, dely_size);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  public ConsignmentDao getConsignmentDao() {
    return consignmentDao;
  }

  public void setConsignmentDao(ConsignmentDao consignmentDao) {
    this.consignmentDao = consignmentDao;
  }

  public FlexibleSearchService getFlexibleSearchService() {
    return flexibleSearchService;
  }

  public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
    this.flexibleSearchService = flexibleSearchService;
  }

}
