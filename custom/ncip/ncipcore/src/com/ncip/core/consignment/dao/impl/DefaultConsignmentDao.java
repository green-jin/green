package com.ncip.core.consignment.dao.impl;

import com.ncip.core.consignment.dao.ConsignmentDao;
import com.ncip.core.exception.ConsignmentException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;

public class DefaultConsignmentDao implements ConsignmentDao {

  @Resource
  private ModelService modelService;

  @Resource
  private FlexibleSearchService flexibleSearchService;
  
  final String TIME = "time";
  final String YESTERDAY = "yesterday";
  

  @Override
  public ConsignmentModel GetConsignment(ConsignmentProcessModel consignmentProcessModel)
      throws ConsignmentException {
    return null;
  }

  @Override
  public List<ConsignmentModel> GetConsignments() throws ConsignmentException {
    final String selectString = "select {pk} from {consignment}";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(selectString);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByTime(String time) throws ConsignmentException {
    String sendTime = time.substring(time.indexOf(" "));
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      cal.setTime(sdf.parse(time));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    cal.add(Calendar.DATE, -1);
    String yesterday = sdf.format(cal.getTime());
    final String selectString = "select {pk} from {consignment as c} where {c.shippingDate} between TO_DATE(?yesterday,'YYYY-MM-DD HH24:MI:SS') and TO_DATE(?time,'YYYY-MM-DD HH24:MI:SS')";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(selectString);
    query.addQueryParameter(YESTERDAY, yesterday + sendTime);
    query.addQueryParameter(TIME, time);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByType(String type) throws ConsignmentException {
    return null;
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
