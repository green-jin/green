package com.ncip.core.service;

import java.util.List;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface GetConsignmentDataService {
	List<ConsignmentModel> getConsignmentData();
	
	List<ConsignmentModel> getConsignmentDataByOrderCode(String orderCode);
}
