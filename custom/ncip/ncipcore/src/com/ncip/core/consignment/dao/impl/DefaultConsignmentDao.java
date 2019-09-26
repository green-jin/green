package com.ncip.core.consignment.dao.impl;

import com.ncip.core.consignment.dao.ConsignmentDao;
import com.ncip.core.enums.DeliveryType;
import com.ncip.core.exception.ConsignmentException;
import com.sun.org.apache.bcel.internal.classfile.Code;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
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
  final String CODE = "code";
  final String TYPE = "type";
  final String TIME_REGEX = "#^((19|20)?[0-9]{2}[- /.](0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01]))*$#";
  

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
  public List<ConsignmentModel> GetConsignmentsByTimeAndType(String time, String type) throws ConsignmentException {
    if(type != null && (type.trim().equals(DeliveryType.LOGISTICS.toString()) || type.trim().equals(DeliveryType.FREIGHT.toString()))) {
      if(time.matches(TIME_REGEX)) {
        final String selectString = "select {pk} from {consignment as c} where {c.shippingDate} between TO_DATE(?timeS,'YYYY-MM-DD HH24:MI:SS') and TO_DATE(?timeE,'YYYY-MM-DD HH24:MI:SS') and {c.ncipdelivery}=?type";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(selectString);
        query.addQueryParameter("timeS", time + " 00:00:00");
        query.addQueryParameter("timeE", time + " 23:59:59");
        query.addQueryParameter(TYPE, type);
        final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public List<ConsignmentModel> GetConsignmentsByType(String type) throws ConsignmentException {
    final String selectString = "select {pk} from {consignment as c} where {c.dely_type} = ?type";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(selectString);
    query.addQueryParameter(TYPE, type);
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

  @Override
  public List<ConsignmentModel> GetConsignmentsByCode(String code) throws ConsignmentException {
    final String selectString = "select {pk} from {consignment as c} where {c.code} = ?code";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(selectString);
    query.addQueryParameter(CODE, code);
    final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

}
