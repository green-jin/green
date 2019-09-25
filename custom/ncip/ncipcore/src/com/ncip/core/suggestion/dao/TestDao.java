package com.ncip.core.suggestion.dao;

import java.util.List;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface TestDao {
	List<ConsignmentModel> findAllConsignmentData();

	List<ConsignmentModel> findConsignmentDataByDate(String date, String type);

	List<ConsignmentModel> findConsignmentDataByOrderCode(String code);
}
