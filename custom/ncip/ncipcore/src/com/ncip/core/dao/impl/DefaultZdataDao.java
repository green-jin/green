package com.ncip.core.dao.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ncip.core.dao.ZdataDao;

import com.ncip.core.data.ZData;

/**
 * @author Aaron
 *
 */
public class DefaultZdataDao implements ZdataDao
{
    /*
     * Parameters Setting
     */
	@Resource
	private ConfigurationService configurationService;
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	private static final String INSERT_STMT = //SQL inser
			"INSERT INTO ZHYDELY VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String GET_ALL_STMT = //SQL select
			"SELECT * FROM ZHYDELY";

	private Connection conn = null;

	/**
     * Returns connection which is connected to Z Table
     *
     * @return connection of Z Table
     */
	@Override
	public Connection getConnection() throws SQLException
	{
		final String url = configurationService.getConfiguration().getString("jdbc.mysql.url");
		final String username = configurationService.getConfiguration().getString("jdbc.mysql.username");
		final String password = configurationService.getConfiguration().getString("jdbc.mysql.password");
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");

			try
			{
				final Connection conn = DriverManager.getConnection(url, username, password);
				System.out.println("DB connected successful");
				return conn;

			}
			catch (final SQLException e)
			{
				e.printStackTrace();
			}

		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;


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
		try
		{
			conn = getConnection();
			int updateCount = 0;
			final PreparedStatement pstmt = conn.prepareStatement(INSERT_STMT);
			for (final ZData zData : zdataList)
			{
				System.out.println(zData.getEntryNUMBER());
				pstmt.setString(1, zData.getVkORG());
				pstmt.setString(2, zData.getVkGRP());
				pstmt.setString(3, zData.getCustomerID());
				pstmt.setString(4, zData.getZTERM());
				pstmt.setString(5, zData.getInCO1());
				pstmt.setString(6, zData.getInCO2());
				pstmt.setString(7, zData.getCode());
				pstmt.setInt(8, zData.getEntryNUMBER());
				pstmt.setDouble(9, zData.getProduct());
				pstmt.setLong(10, zData.getQuantity());
				pstmt.setString(11, zData.getUnit());
				pstmt.setDouble(12, zData.getBasePrice());
				pstmt.setString(13, zData.getCurrency());
				pstmt.setString(14, zData.getPerNR());
				pstmt.setString(15, zData.getLifNR());
				pstmt.setString(16, zData.getStatus());
				pstmt.setString(17, zData.getMa_TYPE());
				pstmt.setString(18, zData.getCrt_DATE());
				pstmt.setString(19, zData.getFrm_SYS());
				pstmt.setString(20, zData.getTo_SYS());
				pstmt.setString(21, zData.getIcType());
				pstmt.setString(22, zData.getDataEXCHANGESTATUS());
				updateCount = pstmt.executeUpdate();
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			closeConn();
			return false;
		}
		closeConn();
		return true;
	}

	/**
     * Returns data of order in the Z Table.
     *
     * @return List of ZhyData from the Z Table.
     */
	@Override
	public List<ZData> getAll() throws SQLException
	{
		conn = getConnection();
		ZData zdata = null;
		final List<ZData> zdatalist = new ArrayList<ZData>();
		final PreparedStatement pstmt = conn.prepareStatement(GET_ALL_STMT);
		final ResultSet rs = pstmt.executeQuery();
		while (rs.next())
		{
			zdata = new ZData();
			zdata.setVkORG(rs.getString("vkORG"));
			zdata.setVkGRP(rs.getString("vkGRP"));
			zdata.setCustomerID(rs.getString("kUNNR"));
			zdata.setZTERM(rs.getString("zTERM"));
			zdata.setInCO1(rs.getString("inCO1"));
			zdata.setInCO2(rs.getString("inCO2"));
			zdata.setCode(rs.getString("vBeln"));
			zdata.setEntryNUMBER(rs.getInt("pOSNR"));
			zdata.setProduct(rs.getDouble("mATNR"));
			zdata.setQuantity(rs.getLong("kWMENG"));
			zdata.setUnit(rs.getString("vRKME"));
			zdata.setBasePrice(rs.getDouble("nETWR"));
			zdata.setCurrency(rs.getString("wAERK"));
			zdata.setPerNR(rs.getString("perNR"));
			zdata.setLifNR(rs.getString("lifNR"));
			zdata.setStatus(rs.getString("lFSTK"));
			zdata.setMa_TYPE(rs.getString("ma_TYPE"));
			zdata.setCrt_DATE(rs.getString("crt_DATE"));
			zdata.setFrm_SYS(rs.getString("frm_SYS"));
			zdata.setTo_SYS(rs.getString("to_SYS"));
			zdata.setIcType(rs.getString("icType"));
			zdata.setDataEXCHANGESTATUS(rs.getString("dataEXCHANGESTATUSS"));
			zdatalist.add(zdata);
		}
		closeConn();
		return zdatalist;

	}

	/**
     * Close the connection with Z Table.
     */
	@Override
	public void closeConn() throws SQLException
	{
		if (conn != null)
		{
			conn.close();
		}
	}

}
