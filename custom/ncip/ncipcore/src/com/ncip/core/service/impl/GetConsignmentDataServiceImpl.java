package com.ncip.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ncip.core.service.GetConsignmentDataService;
import com.ncip.core.suggestion.dao.TestDao;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class GetConsignmentDataServiceImpl implements GetConsignmentDataService{

	@Resource
	private TestDao testDao;
	
	@Override
	public List<ConsignmentModel> getConsignmentData() {
		// TODO Auto-generated method stub
		return testDao.findAllConsignmentData();
	}

	public TestDao getTestDao() {
		return testDao;
	}

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

	@Override
	public List<ConsignmentModel> getConsignmentDataByOrderCode(String orderCode) {
		// TODO Auto-generated method stub
		return testDao.findConsignmentDataByOrderCode(orderCode);
	}

}
