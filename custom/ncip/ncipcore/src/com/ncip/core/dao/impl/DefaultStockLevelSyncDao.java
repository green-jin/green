package com.ncip.core.dao.impl;


import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ncip.core.dao.StockLevelSyncDao;


/**
 * @author green
 *
 */
public class DefaultStockLevelSyncDao extends AbstractItemDao implements StockLevelSyncDao
{
	private JdbcTemplate jdbcTemplate;
	private TypeService typeService;

	private class StockLevelColumns
	{

		private final String tableName;
		private final String pkCol;
		private final String reservedCol;
		private final String availableCol;
		private final String oversellingCol;
		private final String vkORGCol;
		private final String createDateCol;
		private final String fromSYSCol;
		private final String toSYSCol;
		private final String icTypeCol;
		private final String dataExchangStatusCol;
		//final DefaultStockLevelSyncDao this$0;






		private StockLevelColumns(final TypeService typeService)
		{
			//this$0 = DefaultStockLevelSyncDao.this;
			//super();
			final ComposedTypeModel stockLevelType = typeService.getComposedTypeForClass(StockLevelModel.class);
			tableName = stockLevelType.getTable();
			pkCol = typeService.getAttributeDescriptor(stockLevelType, "pk").getDatabaseColumn();
			reservedCol = typeService.getAttributeDescriptor(stockLevelType, "reserved").getDatabaseColumn();
			availableCol = typeService.getAttributeDescriptor(stockLevelType, "available").getDatabaseColumn();
			oversellingCol = typeService.getAttributeDescriptor(stockLevelType, "overSelling").getDatabaseColumn();
			vkORGCol = typeService.getAttributeDescriptor(stockLevelType, "vkORG").getDatabaseColumn();
			createDateCol = typeService.getAttributeDescriptor(stockLevelType, "createDate").getDatabaseColumn();
			fromSYSCol = typeService.getAttributeDescriptor(stockLevelType, "fromSYS").getDatabaseColumn();
			toSYSCol = typeService.getAttributeDescriptor(stockLevelType, "toSYS").getDatabaseColumn();
			icTypeCol = typeService.getAttributeDescriptor(stockLevelType, "icType").getDatabaseColumn();
			dataExchangStatusCol = typeService.getAttributeDescriptor(stockLevelType, "dataExchangStatus").getDatabaseColumn();

		}

		StockLevelColumns(final TypeService typeservice, final StockLevelColumns stocklevelcolumns)
		{
			this(typeservice);
		}
	}
	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}


	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}



	private static final Logger LOG = Logger.getLogger(DefaultStockLevelSyncDao.class);
	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}


	/**
	 * @param jdbcTemplate
	 *           the jdbcTemplate to set
	 */
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}


	public void updateSyncAmountBySAP(final StockLevelModel stockLevel, final int actualAmount)
	{

		try
		{
			final int rows = runJdbcQuery(assembleUpdateStockLevelQuery(stockLevel), actualAmount, stockLevel);
			//LOG.info("updateSyncAmountBySAP =rows=" + rows);
			if (rows > 1)
			{
				throw new IllegalStateException((new StringBuilder("more stock level rows found for the update: ["))
						.append(stockLevel.getPk()).append("]").toString());
			}
		}
		catch (final DataAccessException dae)
		{
			throw new SystemException(dae);
		}



	}


	private int runJdbcQuery(final String query, final int amount, final StockLevelModel stockLevel)
	{
		final Integer theAmount = Integer.valueOf(amount);
		LOG.debug("runJdbcQuery =theAmount=" + theAmount);
		final Long pk = Long.valueOf(stockLevel.getPk().getLongValue());
//		LOG.info("runJdbcQuery =pk=" + pk);
//		LOG.info("runJdbcQuery =stockLevel.getVkORG()=" + stockLevel.getVkORG());
//		LOG.info("runJdbcQuery =stockLevel.getCreateDate()=" + stockLevel.getCreateDate());
//		LOG.info("runJdbcQuery =stockLevel.getFromSYS()=" + stockLevel.getFromSYS());
//		LOG.info("runJdbcQuery =stockLevel.getToSYS()=" + stockLevel.getToSYS());
//		LOG.info("runJdbcQuery =stockLevel.getIcType()=" + stockLevel.getIcType());
//		LOG.info("runJdbcQuery =stockLevel.getDataExchangStatus()=" + stockLevel.getDataExchangStatus());
//		LOG.info("runJdbcQuery =stockLevel.getReserved()=" + stockLevel.getReserved());
		final int rows = jdbcTemplate.update(query, new Object[]
		{ theAmount, stockLevel.getVkORG(), stockLevel.getCreateDate(), stockLevel.getFromSYS(), stockLevel.getToSYS(),
				stockLevel.getIcType(), stockLevel.getDataExchangStatus(), stockLevel.getReserved(), pk });
//		LOG.info("runJdbcQuery =rows=" + rows);
		return rows;
	}

	private void prepareStockLevelColumns()
	{
		if (stockLevelColumns == null)
		{
			stockLevelColumns = new StockLevelColumns(typeService, null);
		}
	}

	private String assembleUpdateStockLevelQuery(final StockLevelModel stockLevel)
	{
		prepareStockLevelColumns();
		final StringBuilder query = (new StringBuilder("UPDATE ")).append(stockLevelColumns.tableName);
		query.append(" SET ").append(stockLevelColumns.availableCol).append(" =?, ").append(stockLevelColumns.vkORGCol)
				.append(" =?, ").append(stockLevelColumns.createDateCol).append(" =?, ").append(stockLevelColumns.fromSYSCol)
				.append(" =?, ").append(stockLevelColumns.toSYSCol).append(" =?,").append(stockLevelColumns.icTypeCol).append(" =?, ")
				.append(stockLevelColumns.dataExchangStatusCol).append(" =?, ").append(stockLevelColumns.reservedCol).append(" =? ");
		query.append(" WHERE ").append(stockLevelColumns.pkCol).append("=? ");
		LOG.debug("query.toString()==" + query.toString());
		return query.toString();
	}

	private StockLevelColumns stockLevelColumns;



}