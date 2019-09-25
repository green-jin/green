package com.ncip.core.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ncip.core.service.ZdataService;
import com.ncip.core.dao.ZdataDao;

import com.ncip.core.data.ZData;

/**
 * @author Aaron
 *
 */
public class DefaultZdataService implements ZdataService
{
	@Autowired
	private ZdataDao zdataDao;

	   public ZdataDao getZdataDao() {
	        return zdataDao;
	    }

	    public void setZdataDao(ZdataDao zdataDao) {
	        this.zdataDao = zdataDao;
	    }
	
	/**
     * Returns connection which is connected to Z Table
     *
     * @return connection of Z Table
     */
	@Override
	public void getConnection() throws SQLException
	{
		// XXX Auto-generated method stub

	}

	/**
     * Returns the result of inserting data into Z Table.
     *
     * @param zdataList
     *           - the List of data is got from Hybris Table.
     *
     * @return boolean value about data insertion consequence.
     */
	@Override
	public boolean insert(final List<ZData> zdataList) throws SQLException
	{
		// XXX Auto-generated method stub
		return zdataDao.insert(zdataList);
	}

	/**
     * Returns data of order in the Z Table.
     *
     * @return List of ZhyData from the Z Table.
     */
	@Override
	public List<ZData> getAll() throws SQLException
	{
		// XXX Auto-generated method stub
		return zdataDao.getAll();
	}

	/**
     * Close the connection with Z Table.
     */
	@Override
	public void closeConn() throws SQLException
	{
		// XXX Auto-generated method stub
		zdataDao.closeConn();

	}


}
