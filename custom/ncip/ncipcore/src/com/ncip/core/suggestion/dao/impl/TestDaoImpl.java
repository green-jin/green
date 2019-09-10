package com.ncip.core.suggestion.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import com.ncip.core.suggestion.dao.TestDao;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class TestDaoImpl extends AbstractItemDao implements TestDao {

  @Resource
  ModelService modelService;

  @Resource
  FlexibleSearchService flexibleSearchService;
  
  private static final String NOW = "now";
  private static final String YESTERDAY = "yseterday";
  private static final String TYPE = "type";
  private static final String DELY_SIZE = "dely_size";

  @Override
  public List<ConsignmentModel> findAllConsignmentData() {
    final String searchString = "select {pk} from {consignment}";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<ConsignmentModel> findConsignmentDataByDate(String date, String dely_size) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String now = sdf.format(cal.getTime());
    cal.add(Calendar.DATE, -1);
    String yesterday = sdf.format(cal.getTime());
    final String searchString =
        "select {pk} from {consignment} where ({consignment.SHIPPINGDATE} between TO_DATE('?now','YYYY-MM-DD HH24:MI:SS') and TO_DATE('?yesterday','YYYY-MM-DD HH24:MI:SS')) and ({product.DELY_TYPE} = ?dely_size)";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    query.addQueryParameter(NOW, now);
    query.addQueryParameter(YESTERDAY, yesterday);
    query.addQueryParameter(DELY_SIZE, dely_size);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<ConsignmentModel> findConsignmentDataByOrderCode(String code) {
    final String searchString = "select {pk} from {consignment} where {consignment.code} = " + code;
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

}
