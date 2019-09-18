/**
 * Sync From Sap.ZHYSTK to Hybris baseCommerce StockLevel
 * to do
 * select from Sap.ZHYSTK
 * call SaveStockLevelStrategy
 *   if exist product and warehouse but not exist in stocklevel then create it in stocklevel
 *   if exist product and warehouse and stocklevel then update it in stocklevel
 *   if not exist in product or warehouse then do nothing
 * update Sap.ZHYSTK.status
 */
package com.ncip.core.job;


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.ncip.core.data.ZHYSTKData;
import com.ncip.core.service.SaveStockLevelService;
import com.ncip.core.service.impl.DefaultSaveStockLevelService;

public class StockLevelCronJob extends AbstractJobPerformable<CronJobModel>
{


		@Resource
		private ConfigurationService configurationService;

		private static final Logger LOG = Logger.getLogger(DefaultSaveStockLevelService.class);
		private SaveStockLevelService saveStockLevelService;

		private String getConnetcionURL()
		 {
			LOG.debug(" do PerformResult getConnetcionURL");
			final String url = configurationService.getConfiguration().getString("jdbc.mysql.url");
			final String username = "user=" + configurationService.getConfiguration().getString("jdbc.mysql.username");
			final String password = "password=" + configurationService.getConfiguration().getString("jdbc.mysql.password");
			LOG.debug(" do PerformResult getConnetcionURL  url=" + url + " username=" + username + " password=" + password);
			return url + username + password;
		 }

		 @Override
		public PerformResult perform(final CronJobModel cronJob)
		 {

		  try
		  {
				//LOG.debug(" do PerformResult = [" + zhyData.getVkORG() + "]");
				LOG.debug(" do PerformResult getSAPERPZHYToZHYData");
				final List<ZHYSTKData> zhystkData = getSAPERPZHYToZHYSTKData();
//				LOG.info(" do PerformResult List<ZHYSTKData> zhyData = [" + zhystkData + "]");
//				LOG.info(" do PerformResult saveStockLevelService.saveStockLevel(zhyData)");
				if (zhystkData != null)
				{
					if (!zhystkData.isEmpty())
					{
						final List<ZHYSTKData> rtzhystkData = saveStockLevelService.saveStockLevel(zhystkData);
//						LOG.info(" do PerformResult saveStockLevelService.saveStockLevel(zhystkData) = [" + zhystkData + "]");
						//stockLevelSyncService.saveStockLevel(zhystkData);
						this.updateSAPERPZHYToZHYSTKData(rtzhystkData);
//						LOG.info(" do PerformResult updateSAPERPZHYToZHYData  CronJobResult.SUCCESS, CronJobStatus.FINISHED");
						// In case of success return result: SUCCESS and status: FINISHED
						return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
					}
					else
					{
					  LOG.warn(" do PerformResult getSAPERPZHYToZHYData zhystkData.isEmpty() ");
						return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
					}
				}
				else
				{
				  LOG.error(" do PerformResult getSAPERPZHYToZHYData zhystkData is null ");
					return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
				}


		  }
		  catch (final Exception e)
		  {

		   // In case of exception return result: ERROR and status: ABORTED
		    LOG.error(" do PerformResult PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED) Exception=="+e.getStackTrace());
		   return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);

		  }
		 }

		private void updateSAPERPZHYToZHYSTKData(final List<ZHYSTKData> zhystkDataList) throws SQLException
		{
			String UPDATESQL = " ";
			LOG.debug(" do PerformResult UPDATESQL = [" + UPDATESQL + "] getconnection");

			final String url = configurationService.getConfiguration().getString("jdbc.mysql.url");
			final String username = configurationService.getConfiguration().getString("jdbc.mysql.username");
			final String password = configurationService.getConfiguration().getString("jdbc.mysql.password");

			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");

				try (Connection conn = DriverManager.getConnection(url, username, password);)
				{
					for (int i = 0; i < zhystkDataList.size(); i++)
					{
						final ZHYSTKData zhystkData = zhystkDataList.get(i);
						UPDATESQL = UPDATESQL + " UPDATE sap.zhystk ";
						UPDATESQL = UPDATESQL + " SET STATUS  = '" + zhystkData.getStatus() + "'";
					UPDATESQL = UPDATESQL + "  , ICTYPE  = '" + zhystkData.getIcType() + "'";
						UPDATESQL = UPDATESQL + " where VKORG = '" + zhystkData.getVkORG() + "'";
						UPDATESQL = UPDATESQL + " and MATNR = '" + zhystkData.getMatNR() + "'";
						UPDATESQL = UPDATESQL + " and LGORT = '" + zhystkData.getLgORT() + "'";
						UPDATESQL = UPDATESQL + " and FRM_SYS = '" + zhystkData.getFrm_SYS() + "'";
						UPDATESQL = UPDATESQL + " and TO_SYS = '" + zhystkData.getTo_SYS() + "'";
						LOG.debug(" do PerformResult UPDATESQL = [" + UPDATESQL + "]");
						try
						{
							final PreparedStatement ps = conn.prepareStatement(UPDATESQL);
							ps.executeUpdate();
						}
						catch (final Exception e)
						{
							e.printStackTrace();
							LOG.error(" do updateSAPERPZHYToZHYSTKData Exception=="+e.getStackTrace());
					           UPDATESQL = "";
						}

						UPDATESQL = "";
					}
				}
				catch (final SQLException e)
				{
				  LOG.error(" do updateSAPERPZHYToZHYSTKData SQLException=="+e.getStackTrace());
                  e.printStackTrace();
				}

			}
			catch (final ClassNotFoundException e)
			{
			  LOG.error(" do updateSAPERPZHYToZHYSTKData ClassNotFoundException=="+e.getStackTrace());
              e.printStackTrace();
			}
		}

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

		/**
		 * @return the saveStockLevelStrategy
		 */
		public SaveStockLevelService getSaveStockLevelService()
		{
			return saveStockLevelService;
		}

		/**
		 * @param saveStockLevelStrategy
		 *           the saveStockLevelStrategy to set
		 */
		public void setSaveStockLevelService(final SaveStockLevelService saveStockLevelService)
		{
			this.saveStockLevelService = saveStockLevelService;
		}

		private List<ZHYSTKData> getSAPERPZHYToZHYSTKData() throws SQLException
		 {
			final String url = configurationService.getConfiguration().getString("jdbc.mysql.url");
			final String username = configurationService.getConfiguration().getString("jdbc.mysql.username");
			final String password = configurationService.getConfiguration().getString("jdbc.mysql.password");
			String SELECTSQL = "select * from sap.zhystk ";
			final String FRSYS = configurationService.getConfiguration().getString("sap.zhystk.fr_sys");//"SAP";
			final String TOSYS = configurationService.getConfiguration().getString("sap.zhystk.to_sys");//"Hybris";
			final String STATUS = configurationService.getConfiguration().getString("sap.zhystk.status");//"-";
			LOG.debug(" do getSAPERPZHYToZHYSTKData FRSYS = [" + FRSYS + "]");
//			LOG.info(" do getSAPERPZHYToZHYSTKData TOSYS = [" + TOSYS + "]");
//			LOG.info(" do getSAPERPZHYToZHYSTKData STATUS = [" + STATUS + "] getconnection");
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				//Get current date time
				final LocalDateTime now = LocalDateTime.now();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				final String formatDateTime = now.format(formatter);

				SELECTSQL = SELECTSQL + " where STATUS = '" + STATUS + "'";
				SELECTSQL = SELECTSQL + "and FRM_SYS = '" + FRSYS + "'";
				SELECTSQL = SELECTSQL + "and TO_SYS = '" + TOSYS + "'";
				//SELECTSQL = SELECTSQL + "and {CRT_DAT} like '" + formatDateTime + "%'";
//				LOG.info(" do PerformResult SELECTSQL = [" + SELECTSQL + "]");
					//final ResultSet rs = stmt.executeQuery(SELECTSQL);
				try (Connection conn = DriverManager.getConnection(url, username, password);
						PreparedStatement ps = conn.prepareStatement(SELECTSQL);
						ResultSet rs = ps.executeQuery();)
				{
//					LOG.info(" do getSAPERPZHYToZHYSTKData rs = [" + rs + "] getconnection");

					final List<ZHYSTKData> ZHYSTKDataList = new ArrayList<ZHYSTKData>();
					String vkORG = "";
					String matNR = "";
					String lgORT = "";
					int menGE = 0;
					String stk_STAT = "";
					String crt_DATE = "";
					String frm_SYS = "";
					String to_SYS = "";
					String icType = "";
					String status = "";
					ZHYSTKData zhystkData;
					int i = 0;
					while (rs.next())
					{
					// Read values using column name
						vkORG = rs.getString("VKORG");
						matNR = rs.getString("MATNR");
						lgORT = rs.getString("LGORT");
						menGE = rs.getInt("MENGE");
						stk_STAT = rs.getString("STK_STAT");
//						LOG.info(" do crt_DATE rs(" + i + ")= [" + rs.getString("CRT_DATE") + "] .getString(\"CRT_DATE\")");
						crt_DATE = rs.getString("CRT_DATE");
//						LOG.info(" do crt_DATE rs(" + i + ")crt_DATE= [" + crt_DATE + "] ");
						frm_SYS = rs.getString("FRM_SYS");
						to_SYS = rs.getString("TO_SYS");
						icType = rs.getString("ICTYPE");
						status = rs.getString("STATUS");
						zhystkData = new ZHYSTKData();
						zhystkData.setVkORG(vkORG);
						zhystkData.setMatNR(matNR);
						zhystkData.setLgORT(lgORT);
						zhystkData.setMenGE(menGE);
						zhystkData.setStk_STAT(stk_STAT);
						zhystkData.setCrt_DATE(crt_DATE);
						zhystkData.setFrm_SYS(frm_SYS);
						zhystkData.setTo_SYS(to_SYS);
						zhystkData.setIcType(icType);
						zhystkData.setStatus(status);
						ZHYSTKDataList.add(zhystkData);
//						LOG.info(" do zhyData(" + i + ").getCrt_DATE()= [" + zhystkData.getCrt_DATE() + "] ");
						i++;
					}
					return ZHYSTKDataList;


				}
				catch (final SQLException e)
				{
				  LOG.error(" do getSAPERPZHYToZHYSTKData SQLException=="+e.getStackTrace());
	              e.printStackTrace();
					return null;
				}

			}
			catch (final ClassNotFoundException e)
			{
			  LOG.error(" do getSAPERPZHYToZHYSTKData ClassNotFoundException=="+e.getStackTrace());
              e.printStackTrace();
				return null;
			}
		}

}
