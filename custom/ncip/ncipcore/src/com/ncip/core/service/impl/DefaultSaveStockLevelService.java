/**
 * @author green
 *
 *Sync From Sap Erp ZHYSTK to Hybris baseCommerce StockLevel
 * if exist product and warehouse but not exist in stocklevel then create it in stocklevel
 * if exist product and warehouse and stocklevel then update it in stocklevel
 * if not exist in product or warehouse then do nothing
 *
 */
package com.ncip.core.service.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.model.StockLevelHistoryEntryModel;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import com.ncip.core.dao.StockLevelSyncDao;
import com.ncip.core.data.ZHYSTKData;
import com.ncip.core.service.SaveStockLevelService;

/**
 * Implements the {@link SaveStockLevelService} on the generic, {@link StockLevelModel} type level.
 */
public class DefaultSaveStockLevelService extends AbstractBusinessService implements SaveStockLevelService
{


	private ConfigurationService configurationService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}




		private static final Logger LOG = Logger.getLogger(DefaultSaveStockLevelService.class);



		@Resource
		private StockService stockService;

		private StockLevelSyncDao stockLevelSyncDao;






		/**
		 * @return the stockLevelSyncDao
		 */
		public StockLevelSyncDao getStockLevelSyncDao()
		{
			return stockLevelSyncDao;
		}

		/**
		 * @param stockLevelSyncDao
		 *           the stockLevelSyncDao to set
		 */
		public void setStockLevelSyncDao(final StockLevelSyncDao stockLevelSyncDao)
		{
			this.stockLevelSyncDao = stockLevelSyncDao;
		}




		private WarehouseService warehouseService;

		private ProductDao productDao;


		/**
		 * @return the stockService
		 */
		public StockService getStockService()
		{
			return stockService;
		}

		/**
		 * @param stockService
		 *           the stockService to set
		 */
		public void setStockService(final StockService stockService)
		{
			this.stockService = stockService;
		}

		/**
		 * @return the warehouseService
		 */
		public WarehouseService getWarehouseService()
		{
			return warehouseService;
		}

		/**
		 * @param warehouseService
		 *           the warehouseService to set
		 */
		public void setWarehouseService(final WarehouseService warehouseService)
		{
			this.warehouseService = warehouseService;
		}



		private ProductModel getProductForCode(final String code)
		{
			ProductModel rtProductModel = null;
			LOG.debug(" do getProductForCode code = [" + code + "]validateParameterNotNull");
			validateParameterNotNull(code, "Parameter code must not be null");


			try
			{
//				LOG.info(" do getProductForCode this.getProductDao().findProductsByCode code = [" + code + "]");
				final List<ProductModel> products = this.getProductDao().findProductsByCode(code);//productService.getProductForCode(code);
//				LOG.info(" do getProductForCode product = [" + products + "]");
				if (products != null && !products.isEmpty())
				{

					rtProductModel = products.get(0);

				}
			}
			catch (final Exception e)
			{
				LOG.error(" do getProductForCode Exception = ["+e.toString()+ "]");
				return null;
			}
			return rtProductModel;
		}

		/**
		 * @return the productDao
		 */
		public ProductDao getProductDao()
		{
			return productDao;
		}

		/**
		 * @param productDao
		 *           the productDao to set
		 */
		public void setProductDao(final ProductDao productDao)
		{
			this.productDao = productDao;
		}

		private WarehouseModel getWarehouseForCode(final String code)
		{
			final WarehouseModel rtpk;
			validateParameterNotNull(code, "Parameter code must not be null");
			try
			{
				LOG.debug(" do getWarehouseForCode code = [" + code + "]");
				rtpk = this.getWarehouseService().getWarehouseForCode(code);
				if (rtpk.getPk() != null)
				{
					return rtpk;
				}
				else
				{
					return null;
				}
				//return this.getWarehouseService().getWarehouseForCode(code);//warehouseService.getWarehouseForCode(code);
			}
			catch (final Exception e)
			{
				LOG.error(" do WarehouseModel getWarehouseForCode(final String code) Exception = ["+e.toString()+ "]");
				return null;
			}
		}



		@Override
		public ZHYSTKData saveStockLevel(final ZHYSTKData zhystkData)
		{
			if (zhystkData == null)
			{
				LOG.error(" ZHYSTKData saveStockLevel(final ZHYSTKData zhystkData)  zhystkData == null ");
				throw new IllegalArgumentException("stockLevel cannot be null");
			}
			StockLevelModel stocklevel = new StockLevelModel();

			zhystkData.getVkORG();
			final ProductModel tmpProductModel = this.getProductForCode(zhystkData.getMatNR());
			if (tmpProductModel != null)
			{
				final WarehouseModel tmpwarehouse = this.getWarehouseForCode(zhystkData.getLgORT());
				if (tmpwarehouse != null)
				{
					final StockLevelModel chkStocklevel = getStockLevel(tmpProductModel, tmpwarehouse);
					if (chkStocklevel == null)
					{
					try
					{
						stocklevel.setInStockStatus(InStockStatus.valueOf(zhystkData.getStk_STAT().trim().toUpperCase()));
					}
					catch (final Exception e)
					{
						final String STK_STAT = configurationService.getConfiguration().getString("sap.zhystk.STK_STAT");//"SAP";
						LOG.warn(" zhyclData() whitout RANGE_DAT Exception = [" + e.getStackTrace()
								+ "] use default sap.zhycl.RANGE_DAT==" + STK_STAT);
						stocklevel.setInStockStatus(InStockStatus.valueOf(STK_STAT.toUpperCase()));
					}
					}
					else
					{
						stocklevel = chkStocklevel;
					}
					stocklevel.setVkORG(zhystkData.getVkORG());
					stocklevel.setWarehouse(tmpwarehouse);
					stocklevel.setProduct(tmpProductModel);
					stocklevel.setProductCode(zhystkData.getMatNR());
					stocklevel.setAvailable(zhystkData.getMenGE());
				stocklevel.setReserved(0);
					stocklevel.setCreateDate(zhystkData.getCrt_DATE());
					stocklevel.setFromSYS(zhystkData.getFrm_SYS());
					stocklevel.setToSYS(zhystkData.getTo_SYS());
					stocklevel.setIcType("CREATE");
				stocklevel.setDataExchangStatus("S");
					if (chkStocklevel == null)
					{

						stocklevel.setOverSelling(0);

						stocklevel.setMaxStockLevelHistoryCount(-1);
						stocklevel.setTreatNegativeAsZero(true);
						try
						{
							final List<StockLevelHistoryEntryModel> historyEntries = new ArrayList<StockLevelHistoryEntryModel>();
							final StockLevelHistoryEntryModel entry = this.createStockLevelHistoryEntry(stocklevel,
									stocklevel.getAvailable(), 0, StockLevelUpdateType.WAREHOUSE, "ERP SAP new in stock");
							historyEntries.add(entry);
							stocklevel.setStockLevelHistoryEntries(historyEntries);
							getModelService().save(stocklevel);
							zhystkData.setIcType("CREATE");
							zhystkData.setStatus("S");
						}
						catch (final Exception e)
						{
							LOG.error(" ZHYSTKData saveStockLevel(final ZHYSTKData zhystkData) CREATE data Exception = ["+e.toString()+ "]");
							zhystkData.setStatus("F");
						}

					}
					else
					{

						try
						{

						stocklevel.setIcType("MODIFY");
						getStockLevelSyncDao().updateSyncAmountBySAP(stocklevel, zhystkData.getMenGE());
//						LOG.info(" zhystkData().setIcType(\"MODIFY\")= [MODIFY] after getModelService().save(stocklevel)");

						//getModelService().save(stocklevel);
						clearCacheForItem(stocklevel);
							final List<StockLevelHistoryEntryModel> historyEntries = new ArrayList<StockLevelHistoryEntryModel>();
							final StockLevelHistoryEntryModel entry = this.createStockLevelHistoryEntry(stocklevel,
									stocklevel.getAvailable(), 0, StockLevelUpdateType.WAREHOUSE, "ERP SAP update in stock");
							historyEntries.add(entry);
							zhystkData.setIcType("MODIFY");
							zhystkData.setStatus("S");
						}
						catch (final Exception e)
						{
							LOG.error(" ZHYSTKData saveStockLevel(final ZHYSTKData zhystkData) MODIFY data Exception = ["+e.toString()+ "]");
							zhystkData.setStatus("F");
						}

					}
				}
				else
				{
					zhystkData.setStatus("F");
				}

			}
			else
			{
				zhystkData.setStatus("F");
			}
			return zhystkData;
		}

		@Override
		public List<ZHYSTKData> saveStockLevel(final List<ZHYSTKData> zhystkDataList)
		{
		if (zhystkDataList == null)
			{
			LOG.error(" List<ZHYSTKData> saveStockLevel(final List<ZHYSTKData> zhystkDataList)  zhystkDataList == null ");
			throw new IllegalArgumentException("stockLevel cannot be null");
			}

			for (int i = 0; i < zhystkDataList.size(); i++)
			{
				final ZHYSTKData zhystkData = zhystkDataList.get(i);
				StockLevelModel stocklevel = (StockLevelModel) getModelService().create(StockLevelModel.class);
				zhystkData.getVkORG();
				LOG.debug(" zhystkData(" + i + ").getVkORG()= [" + zhystkData.getVkORG() + "]");
//				LOG.info(" zhystkData(" + i + ").getMatNR()= [" + zhystkData.getMatNR() + "]");
//				LOG.info(" zhystkData(" + i + ").getLgORT()= [" + zhystkData.getLgORT() + "]");
				final ProductModel tmpProductModel = this.getProductForCode(zhystkData.getMatNR());
//				LOG.info(" zhystkData(" + i + ").tmpProductModel= [" + tmpProductModel + "]");
				if (tmpProductModel != null)
				{
					final WarehouseModel tmpwarehouse = this.getWarehouseForCode(zhystkData.getLgORT());
//					LOG.info(" zhystkData(" + i + ").tmpwarehouse= [" + tmpwarehouse + "]");
					if (tmpwarehouse != null)
					{

						final StockLevelModel chkStocklevel = getStockLevel(tmpProductModel, tmpwarehouse);
//						LOG.info(" zhystkData(" + i + ").chkStocklevel= [" + chkStocklevel + "]");
						if (chkStocklevel == null)
						{
//							LOG.info(" zhystkData(" + i + ").getStk_STAT= [" + zhystkData.getStk_STAT() + "]");
						

						try
						{
							stocklevel.setInStockStatus(InStockStatus.valueOf(zhystkData.getStk_STAT().trim().toUpperCase()));
						}
						catch (final Exception e)
						{
							final String STK_STAT = configurationService.getConfiguration().getString("sap.zhystk.STK_STAT");//"SAP";
							LOG.warn(" zhyclData(" + i + ").getStk_STAT() whitout InStockStatus="+zhystkData.getStk_STAT()+" Exception = [" + e.getStackTrace()
									+ "] use default sap.zhycl.RANGE_DAT==" + STK_STAT);
							stocklevel.setInStockStatus(InStockStatus.valueOf(STK_STAT.toUpperCase()));
						}
						}
						else
						{
							stocklevel = chkStocklevel;
						}
						stocklevel.setVkORG(zhystkData.getVkORG());

						stocklevel.setProductCode(zhystkData.getMatNR());
						stocklevel.setAvailable(zhystkData.getMenGE());
					stocklevel.setReserved(0);
						stocklevel.setCreateDate(zhystkData.getCrt_DATE());
						stocklevel.setFromSYS(zhystkData.getFrm_SYS());
						stocklevel.setToSYS(zhystkData.getTo_SYS());
					stocklevel.setDataExchangStatus("S");
						if (chkStocklevel == null)
						{
							stocklevel.setWarehouse(tmpwarehouse);
							stocklevel.setProduct(tmpProductModel);
						stocklevel.setOverSelling(0);
							stocklevel.setMaxStockLevelHistoryCount(-1);
							stocklevel.setTreatNegativeAsZero(true);
							try
							{
							stocklevel.setIcType("CREATE");
								final List<StockLevelHistoryEntryModel> historyEntries = new ArrayList<StockLevelHistoryEntryModel>();
								final StockLevelHistoryEntryModel entry = this.createStockLevelHistoryEntry(stocklevel,
										stocklevel.getAvailable(), 0, StockLevelUpdateType.WAREHOUSE, "ERP SAP new in stock");
								historyEntries.add(entry);
								stocklevel.setStockLevelHistoryEntries(historyEntries);
//								LOG.info(" zhystkData(" + i + ") CREATE  stocklevel.getWarehouse()= [" + stocklevel.getWarehouse() + "]");
//								LOG.info(" stocklevel.setIcType(\"CREATE\")= [CREATE] to do getModelService().save(stocklevel)");
								getModelService().save(stocklevel);
//								LOG.info(" zhystkData(" + i + ").setIcType(\"CREATE\")= [CREATE] after getModelService().save(stocklevel)");
								zhystkData.setIcType("CREATE");
								zhystkData.setStatus("S");
							}
							catch (final Exception e)
							{
								//LOG.info(e.getStackTrace());
								LOG.error(" List<ZHYSTKData> saveStockLevel(final List<ZHYSTKData> zhystkDataList)  Exception = [" + e.getStackTrace() + "]");
								zhystkData.setStatus("F");
							}

						}
						else
						{

							try
							{

							//stocklevel.setStockLevelHistoryEntries(historyEntries);
								stocklevel.setIcType("MODIFY");
//								LOG.info(" zhystkData(" + i + ").getCrt_DATE()= [" + zhystkData.getCrt_DATE() + "]");
//								LOG.info(" zhystkData(" + i + ") MODIFY  stocklevel.getCreateDate()= [" + stocklevel.getCreateDate() + "]");
//
//								LOG.info(" zhystkData(" + i + ") MODIFY  stocklevel.getWarehouse()= [" + stocklevel.getWarehouse() + "]");
//								LOG.info(" stocklevel.setIcType(\"MODIFY\")= [MODIFY] to do updateSyncAmountBySAP(stocklevel,zhyData.getMenGE())");
								try
								{
									getStockLevelSyncDao().updateSyncAmountBySAP(stocklevel, zhystkData.getMenGE());
									//getModelService().save(stocklevel);
//									LOG.info(" zhystkData(" + i + ").setIcType(\"MODIFY\")= [MODIFY] after getModelService().save(stocklevel)");
								clearCacheForItem(stocklevel);
								final List<StockLevelHistoryEntryModel> historyEntries = new ArrayList<StockLevelHistoryEntryModel>();
								final StockLevelHistoryEntryModel entry = this.createStockLevelHistoryEntry(stocklevel,
										stocklevel.getAvailable(), 0, StockLevelUpdateType.WAREHOUSE, "ERP SAP update in stock");
								historyEntries.add(entry);
								zhystkData.setIcType("MODIFY");
									zhystkData.setStatus("S");
								}
								catch (final ModelSavingException e)
								{
									e.printStackTrace();
								LOG.error("MODIFY stocklevel ModelSavingException  -->" + e.getLocalizedMessage());
									
									zhystkData.setIcType("MODIFY");
									zhystkData.setStatus("F");
								}


							}
							catch (final Exception e)
							{
							LOG.error("MODIFY stocklevel Exception  -->" + e.getStackTrace());
								zhystkData.setIcType("MODIFY");
								zhystkData.setStatus("F");
							}

						}
					}
					else
					{
						zhystkData.setStatus("F");
					}

				}
				else
				{
					zhystkData.setStatus("F");
				}


			}


			return zhystkDataList;
		}


		private void saveStockLevelEntries(final List<StockLevelModel> entries)
		{
			if (entries != null)
			{

				final ListIterator listIterator = entries.listIterator(entries.size());
				while (listIterator.hasPrevious())
				{
					final StockLevelModel entry = (StockLevelModel) listIterator.previous();

					getModelService().save(entry);


				}

			}
		}


	private void clearCacheForItem(final StockLevelModel stockLevel)
	{
		Utilities.invalidateCache(stockLevel.getPk());
		getModelService().refresh(stockLevel);
	}
		public StockLevelModel getStockLevel(final ProductModel product, final WarehouseModel warehouse)
		{
			return stockService.getStockLevel(product, warehouse);
		}




		private StockLevelHistoryEntryModel createStockLevelHistoryEntry(final StockLevelModel stockLevel, final int actual,
				final int reserved, final StockLevelUpdateType updateType, final String comment)
		{
			if (stockLevel == null)
			{
				throw new IllegalArgumentException("stock level cannot be null.");
			}
			if (actual < 0)
			{
				throw new IllegalArgumentException("actual amount cannot be negative.");
			}

			if (stockLevel.getMaxStockLevelHistoryCount() != 0 && (stockLevel.getMaxStockLevelHistoryCount() < 0
					|| stockLevel.getMaxStockLevelHistoryCount() > (stockLevel.getStockLevelHistoryEntries() == null ? 0
							: stockLevel.getStockLevelHistoryEntries().size())))
			{
				final StockLevelHistoryEntryModel historyEntry = getModelService().create(StockLevelHistoryEntryModel.class);
				historyEntry.setStockLevel(stockLevel);
				historyEntry.setActual(actual);
				historyEntry.setReserved(reserved);
				historyEntry.setUpdateType(updateType);
				if (comment != null)
				{
					historyEntry.setComment(comment);
				}
				historyEntry.setUpdateDate(new Date());
				getModelService().save(historyEntry);
				return historyEntry;
			}
			return null;
		}
}
