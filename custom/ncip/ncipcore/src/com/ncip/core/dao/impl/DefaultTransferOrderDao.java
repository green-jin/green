package com.ncip.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import com.ncip.core.dao.TransferOrderDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Aaron
 *
 */

import com.ncip.core.dao.TransferOrderDao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.core.model.order.OrderModel;

public class DefaultTransferOrderDao extends AbstractItemDao implements TransferOrderDao {

	@Autowired
	private FlexibleSearchService flexibleSearchService;
    @Override
    public FlexibleSearchService getFlexibleSearchService()
   {
       return flexibleSearchService;
   }

   @Override
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
   {
       this.flexibleSearchService = flexibleSearchService;
   }
   
	/**
     * Returns connection which is connected to Z Table
     *
     * @return connection of Z Table
     */
	@Override
	public List<OrderModel> findAllOrdersAfterSpecifiedTime(String execTime) {
	  final String startTime=execTime.substring(0, execTime.indexOf(";"));
	  final String endTime=execTime.substring(execTime.indexOf(";")+1);
		final StringBuilder query = new StringBuilder(
				"select {PK} from {Order as o}  where {o.MODIFIEDTIME} between to_date('" + startTime + "', 'YYYY/MM/DD HH24:MI:SS') and to_date('"
				   + endTime + "', 'YYYY/MM/DD HH24:MI:SS')");
		
		System.out.println(query);
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		System.out.println(searchQuery);
		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

	/**
     * Returns value of B2BCustomerModel which is queried from Hybris Table by matching user PK value.
     *
     * @param userPK
     *           -the PK value of user from the order.
     *
     * @return B2BCustomerModel value of matching user PK value.
     */
	@Override
	public B2BCustomerModel findCustomerUIDByUserPK(String userPK) {
		final StringBuilder query = new StringBuilder(
		"SELECT {PK} FROM {B2BCustomer AS BC} WHERE {BC.PK}='" + userPK + "'");

        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
        final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
        return (B2BCustomerModel) searchResult.getResult().get(0);
	}


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
	@Override
	public B2BUserGroupModel findB2BUserGroupByUGUID(String uGUID) {
		final StringBuilder query = new StringBuilder("SELECT {PK} FROM {B2BUSERGROUP AS BUG} WHERE {BUG.UID}='" + uGUID + "'");

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return (B2BUserGroupModel) searchResult.getResult().get(0);
	}


}
