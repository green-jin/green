package com.ncip.core.futureStock.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import com.ncip.core.futureStock.dao.NcipFutureStockDao;
import de.hybris.platform.b2b.model.FutureStockModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultFutureStockDao implements NcipFutureStockDao{

  @Resource
  ModelService modelService;

  @Resource
  FlexibleSearchService flexibleSearchService;
  
  @Override
  public List<FutureStockModel> getAllFutureStockModels() {
    final String searchString = "select {pk} from {futureStock}";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    final SearchResult<FutureStockModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }

  @Override
  public List<FutureStockModel> getFutureStockModelsByProductCode(String code) {
    // TODO
    final String searchString = "select {pk} from {futureStock as f} where {f.productCode}=?code";
    final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
    query.addQueryParameter("code", code);
    final SearchResult<FutureStockModel> result = getFlexibleSearchService().search(query);
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

  public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
    this.flexibleSearchService = flexibleSearchService;
  }

}
