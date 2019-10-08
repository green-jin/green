package com.ncip.core.dao;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;
/**
 * @author Aaron
 *
 */
public interface TransferOrderDao extends Dao{
	/**
	 * Returns order data from Hybris Table in a specified time.
	 *
	 * @param execTime
	 *           - specified time period.
	 *
	 * @return List of matching order data.
	 */
	List<OrderModel> findAllOrdersAfterSpecifiedTime(final String execTime);

	/**
	 * Returns value of B2BCustomerModel which is queried from Hybris Table by matching user PK value.
	 *
	 * @param userPK
	 *           -the PK value of user from the order.
	 *
	 * @return B2BCustomerModel value of matching user PK value.
	 */
	B2BCustomerModel findCustomerUIDByUserPK(final String userPK);

	/**
	 * Returns value of B2BUserGroupModel which is queried from Hybris Table by matching user group UID.
	 *
	 * @param uGUID
	 *           -the UID of user group from B2BCustomerModel
	 * @param order
	 *           - target order
	 *
	 * @return B2BUserGroupModel value of matching user group UID.
	 */
	B2BUserGroupModel findB2BUserGroupByUGUID(final String uGUID);


}
