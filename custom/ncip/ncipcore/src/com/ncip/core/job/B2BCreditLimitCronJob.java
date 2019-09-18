/**
 * Sync From Sap.ZHYCL to Hybris B2BCreditLimit
 * to do
 * select from Sap.ZHYCL
 * call SaveB2BCreditLimitStrategy
 *
 * update Sap.ZHYCL.status
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

import com.ncip.core.data.ZHYCLData;
import com.ncip.core.service.SaveB2BCreditLimitService;


public class B2BCreditLimitCronJob extends AbstractJobPerformable<CronJobModel>
{


	@Resource
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(B2BCreditLimitCronJob.class);
	private SaveB2BCreditLimitService saveB2BCreditLimitService;

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

			LOG.debug(" do PerformResult getSAPERPZHYToZHYCLData");
			final List<ZHYCLData> zhyclData = getSAPERPZHYToZHYCLData();
			//LOG.info(" do PerformResult List<ZHYCLData> zhyclData = [" + zhyclData + "]");
			//LOG.info(" do PerformResult saveB2BCreditLimitService.saveB2BCreditLimit(zhyclData)");
			if (zhyclData != null)
			{
				if (!zhyclData.isEmpty())
				{
					final List<ZHYCLData> rtzhyclData = saveB2BCreditLimitService.saveB2BCreditLimit(zhyclData);
					//LOG.info(" do PerformResult saveB2BCreditLimitService.saveB2BCreditLimit(zhyclData) = [" + zhyclData + "]");
					this.updateSAPERPZHYToZHYCLData(rtzhyclData);
					//LOG.info(" do PerformResult updateSAPERPZHYToZHYCLData  CronJobResult.SUCCESS, CronJobStatus.FINISHED");
					// In case of success return result: SUCCESS and status: FINISHED
					return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
				}
				else
				{
				  LOG.warn(" do PerformResult getSAPERPZHYToZHYCLData zhyclData.isEmpty() ");
					return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
				}
			}
			else
			{
			  LOG.error(" do PerformResult getSAPERPZHYToZHYCLData zhyclData is null ");
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}


		}
		catch (final Exception e)
		{

			// In case of exception return result: ERROR and status: ABORTED
		  LOG.error(" do PerformResult getSAPERPZHYToZHYCLData In case of exception return result: ERROR and status: ABORTED ");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);

		}
	}

	private void updateSAPERPZHYToZHYCLData(final List<ZHYCLData> zhyclDataList) throws SQLException
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
				for (int i = 0; i < zhyclDataList.size(); i++)
				{
					final ZHYCLData zhyclData = zhyclDataList.get(i);
					UPDATESQL = UPDATESQL + " UPDATE sap.zhycl ";
					UPDATESQL = UPDATESQL + " SET STATUS  = '" + zhyclData.getStatus() + "'";
					UPDATESQL = UPDATESQL + "  , ICTYPE  = '" + zhyclData.getIcType() + "'";
					UPDATESQL = UPDATESQL + " where VKORG = '" + zhyclData.getVkORG() + "'";
					UPDATESQL = UPDATESQL + " and KUNNR = '" + zhyclData.getKunNR() + "'";
					UPDATESQL = UPDATESQL + " and FRM_SYS = '" + zhyclData.getFrm_SYS() + "'";
					UPDATESQL = UPDATESQL + " and TO_SYS = '" + zhyclData.getTo_SYS() + "'";
					LOG.debug(" do PerformResult UPDATESQL = [" + UPDATESQL + "]");
					try
					{
						final PreparedStatement ps = conn.prepareStatement(UPDATESQL);
						ps.executeUpdate();
					}
					catch (final Exception e)
					{
						e.printStackTrace();
						LOG.error(" void updateSAPERPZHYToZHYCLData(final List<ZHYCLData> zhyclDataList) ps.executeUpdate() Exception = [" + e.getStackTrace() + "]");
						UPDATESQL = "";
					}

					UPDATESQL = "";
				}
			}
			catch (final SQLException e)
			{
				LOG.error(" void updateSAPERPZHYToZHYCLData(final List<ZHYCLData> zhyclDataList)  SQLException = [" + e.getStackTrace() + "]");
			
				e.printStackTrace();
			}

		}
		catch (final ClassNotFoundException e)
		{
			LOG.error(" void updateSAPERPZHYToZHYCLData(final List<ZHYCLData> zhyclDataList)  ClassNotFoundException = [" + e.getStackTrace() + "]");
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


	private List<ZHYCLData> getSAPERPZHYToZHYCLData() throws SQLException
	{
		final String url = configurationService.getConfiguration().getString("jdbc.mysql.url");
		final String username = configurationService.getConfiguration().getString("jdbc.mysql.username");
		final String password = configurationService.getConfiguration().getString("jdbc.mysql.password");
		String SELECTSQL = "select * from sap.zhycl ";
		final String FRSYS = configurationService.getConfiguration().getString("sap.zhycl.fr_sys");//"SAP";
		final String TOSYS = configurationService.getConfiguration().getString("sap.zhycl.to_sys");//"Hybris";
		final String STATUS = configurationService.getConfiguration().getString("sap.zhycl.status");//"-";
		LOG.debug(" do getSAPERPZHYToZHYCLData FRSYS = [" + FRSYS + "]");
//		LOG.info(" do getSAPERPZHYToZHYCLData TOSYS = [" + TOSYS + "]");
//		LOG.info(" do getSAPERPZHYToZHYCLData STATUS = [" + STATUS + "] getconnection");
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
			LOG.debug(" do PerformResult SELECTSQL = [" + SELECTSQL + "]");
			//final ResultSet rs = stmt.executeQuery(SELECTSQL);
			try (Connection conn = DriverManager.getConnection(url, username, password);
					PreparedStatement ps = conn.prepareStatement(SELECTSQL);
					ResultSet rs = ps.executeQuery();)
			{
//				LOG.info(" do getSAPERPZHYToZHYCLData rs = [" + rs + "] getconnection");

				final List<ZHYCLData> ZHYCLDataList = new ArrayList<ZHYCLData>();
				String vkORG = "";
				String kunNR = "";
				String range_DAT = "";
				java.math.BigDecimal amount;
				String waeRK = "";
				String crt_DATE = "";
				String frm_SYS = "";
				String to_SYS = "";
				String icType = "";
				String status = "";
				ZHYCLData zhyclData;
				int i = 0;
				while (rs.next())
				{
					// Read values using column name
					vkORG = rs.getString("VKORG");
					kunNR = rs.getString("KUNNR");
					range_DAT = rs.getString("RANGE_DAT");
					amount = rs.getBigDecimal("AMOUNT");
					waeRK = rs.getString("WAERK");
//					LOG.info(" do crt_DATE rs(" + i + ")= [" + rs.getString("CRT_DATE") + "] .getString(\"CRT_DATE\")");
					crt_DATE = rs.getString("CRT_DATE");
//					LOG.info(" do crt_DATE rs(" + i + ")crt_DATE= [" + crt_DATE + "] ");
					frm_SYS = rs.getString("FRM_SYS");
					to_SYS = rs.getString("TO_SYS");
					icType = rs.getString("ICTYPE");
					status = rs.getString("STATUS");
					zhyclData = new ZHYCLData();
					zhyclData.setVkORG(vkORG);
					zhyclData.setKunNR(kunNR);
					zhyclData.setRange_DAT(range_DAT);
					zhyclData.setAmount(amount);
					zhyclData.setWaeRK(waeRK);
					zhyclData.setCrt_DATE(crt_DATE);
					zhyclData.setFrm_SYS(frm_SYS);
					zhyclData.setTo_SYS(to_SYS);
					zhyclData.setIcType(icType);
					zhyclData.setStatus(status);
					ZHYCLDataList.add(zhyclData);
//					LOG.info(" do zhyData(" + i + ").getCrt_DATE()= [" + zhyclData.getCrt_DATE() + "] ");
					i++;
				}
				return ZHYCLDataList;


			}
			catch (final SQLException e)
			{
				e.printStackTrace();
				LOG.error(" List<ZHYCLData> getSAPERPZHYToZHYCLData()  SQLException = [" + e.getStackTrace() + "]");
				return null;
			}

		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
			LOG.error(" List<ZHYCLData> getSAPERPZHYToZHYCLData()  ClassNotFoundException = [" + e.getStackTrace() + "]");
			return null;
		}
	}

	/**
	 * @return the saveB2BCreditLimitService
	 */
	public SaveB2BCreditLimitService getSaveB2BCreditLimitService()
	{
		return saveB2BCreditLimitService;
	}

	/**
	 * @param saveB2BCreditLimitService
	 *           the saveB2BCreditLimitService to set
	 */
	public void setSaveB2BCreditLimitService(final SaveB2BCreditLimitService saveB2BCreditLimitService)
	{
		this.saveB2BCreditLimitService = saveB2BCreditLimitService;
	}

}
