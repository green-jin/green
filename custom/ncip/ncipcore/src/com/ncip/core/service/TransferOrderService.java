package com.ncip.core.service;
import java.util.List;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.OrderModel;
public interface TransferOrderService {
	/**
	 * Query the order with the given specified time. This method focuses on query order data.
	 *
	 *
	 * @param execTime
	 *           the specified time
	 * @return List<OrderModel>
	 */
	List<OrderModel> getAllOldersAfterSpecifiedTime(final String execTime);

	/**
	 * Query the B2BCustomer data with the given user PK value. This method focuses on query B2BCustomer data.
	 *
	 * @param userPK
	 *           the PK value of user from the order.
	 * @return B2BCustomer data
	 */
	B2BCustomerModel getCustomerUIDByUserPK(final String userPK);

	/**
	 *
	 * Query the B2BUserGroup data with the given UserGroup UID. This method focuses on query B2BUserGroup data.
	 *
	 * @param uGUID
	 *           the UID of user group from B2BCustomerModel
	 * @return B2BUserGroup data
	 */
	B2BUserGroupModel getB2BUserGroupByUGUID(final String uGUID);

}
