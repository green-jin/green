package com.ncip.core.service.impl;

import java.util.List;


import com.ncip.core.service.TransferOrderService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.OrderModel;
import com.ncip.core.dao.TransferOrderDao;

public class DefaultTransferOrderService implements TransferOrderService {

	//@Autowired
	private TransferOrderDao transferOrderDao;
	
	public TransferOrderDao getTransferOrderDao() {
		return transferOrderDao;
	}

	/**
     * Query the order with the given specified time. This method focuses on query order data.
     *
     *
     * @param execTime
     *           the specified time
     * @return List<OrderModel>
     */
	public void setTransferOrderDao(TransferOrderDao transferOrderDao) {
		this.transferOrderDao = transferOrderDao;
	}

	@Override
	public List<OrderModel> getAllOldersAfterSpecifiedTime(final String execTime)
	{
		return transferOrderDao.findAllOldersAfterSpecifiedTime(execTime);
	}

	/**
     * Query the B2BCustomer data with the given user PK value. This method focuses on query B2BCustomer data.
     *
     * @param userPK
     *           the PK value of user from the order.
     * @return B2BCustomer data
     */
	@Override
	public B2BCustomerModel getCustomerUIDByUserPK(final String userPK)
	{
		return transferOrderDao.findCustomerUIDByUserPK(userPK);
	}

	/**
    *
    * Query the B2BUserGroup data with the given UserGroup UID. This method focuses on query B2BUserGroup data.
    *
    * @param uGUID
    *           the UID of user group from B2BCustomerModel
    * @return B2BUserGroup data
    */
	@Override
	public B2BUserGroupModel getB2BUserGroupByUGUID(final String uGUID)
	{
		return transferOrderDao.findB2BUserGroupByUGUID(uGUID);
	}
}
