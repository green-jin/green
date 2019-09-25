package com.ncip.core.service;

import java.sql.SQLException;
import java.util.List;

import com.ncip.core.data.ZData;


/**
 * @author Aaron
 *
 */
public interface ZdataService
{

  /**
   * Returns connection which is connected to Z Table
   *
   * @return connection of Z Table
   */
	void getConnection() throws SQLException;

	/**
     * Returns the result of inserting data into Z Table.
     *
     * @param zdataList
     *           - the List of data is got from Hybris Table.
     *
     * @return boolean value about data insertion consequence.
     */
	boolean insert(List<ZData> zdataList) throws SQLException;

	/**
     * Returns data of order in the Z Table.
     *
     * @return List of ZhyData from the Z Table.
     */
	List<ZData> getAll() throws SQLException;

	/**
     * Close the connection with Z Table.
     */
	void closeConn() throws SQLException;

}
