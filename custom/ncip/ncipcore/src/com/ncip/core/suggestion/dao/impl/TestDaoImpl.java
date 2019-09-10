package com.ncip.core.suggestion.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ncip.core.suggestion.dao.TestDao;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class TestDaoImpl extends AbstractItemDao implements TestDao{

	@Resource
	ModelService modelService;

	@Resource
	FlexibleSearchService flexibleSearchService;
	
	@Override
	public List<ConsignmentModel> findAllConsignmentData() {
		final String searchString = "select {pk} from {consignment}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
		final SearchResult<ConsignmentModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<ConsignmentModel> findConsignmentDataByDate(String date) {
		final String searchString = "select {pk} from {consignment} where {consignment.SHIPPINGDATE} between TO_DATE('" + date
				+ " 15:00:00','YYYY-MM-DD HH24:MI:SS') and TO_DATE('" + date + " 16:00:00','YYYY-MM-DD HH24:MI:SS')";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(searchString);
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
