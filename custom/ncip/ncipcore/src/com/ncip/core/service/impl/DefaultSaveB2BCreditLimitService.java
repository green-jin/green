/**
 * @author green
 *
 *Sync From Sap Erp ZHYCL to Hybris  B2BCreditLimit
 * 
 *
 */
package com.ncip.core.service.impl;


import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import java.util.List;

import org.apache.log4j.Logger;
import com.ncip.core.dao.B2BCreditLimitDao;
import com.ncip.core.data.ZHYCLData;
import com.ncip.core.service.SaveB2BCreditLimitService;


/**
 * Implements the {@link SaveB2BCreditLimitService} on the generic, {@link B2BCreditLimitModel} type level.
 */
public class DefaultSaveB2BCreditLimitService extends AbstractBusinessService implements SaveB2BCreditLimitService
{
	private static final Logger LOG = Logger.getLogger(DefaultSaveB2BCreditLimitService.class);

	private B2BCreditLimitDao b2bCreditLimitDao;
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

	/**
	 * @return the b2bCreditLimitDao
	 */
	public B2BCreditLimitDao getB2bCreditLimitDao()
	{
		return b2bCreditLimitDao;
	}

	/**
	 * @param b2bCreditLimitDao
	 *           the b2bCreditLimitDao to set
	 */
	public void setB2bCreditLimitDao(final B2BCreditLimitDao b2bCreditLimitDao)
	{
		this.b2bCreditLimitDao = b2bCreditLimitDao;
	}

		@Override
	public ZHYCLData saveB2BCreditLimit(final ZHYCLData zhyclData)
	{
		if (zhyclData == null)
		{
			LOG.error(" ZHYCLData saveB2BCreditLimit(final ZHYCLData zhyclData)  zhyclData == null ");
			throw new IllegalArgumentException("stockLevel cannot be null");
		}
		//final B2BCreditLimitModel b2bCreditLimit = new B2BCreditLimitModel();
		B2BCreditLimitModel b2bCreditLimit = (B2BCreditLimitModel) getModelService().create(B2BCreditLimitModel.class);
		//zhyclData.getVkORG();
		LOG.debug(" zhyclData.getVkORG()= [" + zhyclData.getVkORG() + "]");
//		LOG.info(" zhyclData.getKunNR()= [" + zhyclData.getKunNR() + "]");
//		LOG.info(" zhyclData.getWaeRK()= [" + zhyclData.getWaeRK() + "]");
		final String Code = zhyclData.getKunNR();
		if (this.getB2bCreditLimitDao().hasB2BCreditLimitModelByCode(Code))
		{
			b2bCreditLimit = this.getB2bCreditLimitDao().getB2BCreditLimitModelByCode(Code);
			b2bCreditLimit.setIcType("MODIFY");
			zhyclData.setIcType("MODIFY");
		}
		else
		{
			b2bCreditLimit.setActive(Boolean.TRUE);
			b2bCreditLimit.setIcType("CREATE");
			zhyclData.setIcType("CREATE");
		}
		b2bCreditLimit.setVkORG(zhyclData.getVkORG());
		b2bCreditLimit.setCode(Code);
		b2bCreditLimit.setAmount(zhyclData.getAmount());
		if (this.getB2bCreditLimitDao().hasCurrencyModelByISOCODE(zhyclData.getWaeRK().toUpperCase()))
		{
			b2bCreditLimit.setCurrency(this.getB2bCreditLimitDao().getCurrencyModelByISOCODE(zhyclData.getWaeRK().toUpperCase()));

		}
		else
		{
			final String WAERK = configurationService.getConfiguration().getString("sap.zhycl.WAERK");//"SAP";
			LOG.warn(" zhyclData.getWaeRK()= [" + zhyclData.getWaeRK() + "]");
			LOG.warn(" zhyclData() whitout WAERK use default sap.zhycl.WAERK==" + WAERK);
			b2bCreditLimit.setCurrency(this.getB2bCreditLimitDao().getCurrencyModelByISOCODE(WAERK.toUpperCase()));

		}
		try
		{
			b2bCreditLimit.setDateRange(B2BPeriodRange.valueOf(zhyclData.getRange_DAT().toUpperCase()));
		}
		catch (final Exception e)
		{
			final String RANGE_DAT = configurationService.getConfiguration().getString("sap.zhycl.RANGE_DAT");//"SAP";
			LOG.warn(" zhyclData.getWaeRK()= [" + zhyclData.getWaeRK() + "]");
			LOG.warn(" zhyclData() whitout RANGE_DAT Exception = [" + e.getStackTrace() + "] use default sap.zhycl.RANGE_DAT=="
					+ RANGE_DAT);
			b2bCreditLimit.setDateRange(B2BPeriodRange.valueOf(RANGE_DAT.toUpperCase()));
		}

		b2bCreditLimit.setCreateDate(zhyclData.getCrt_DATE());
		b2bCreditLimit.setFromSYS(zhyclData.getFrm_SYS());
		b2bCreditLimit.setToSYS(zhyclData.getTo_SYS());


		try
		{
			b2bCreditLimit.setDataExchangStatus("S");
			getModelService().save(b2bCreditLimit);
			zhyclData.setStatus("S");
		}
		catch (final Exception e)
		{
			LOG.error(" zhyclData  Exception = [" + e.getStackTrace() + "]");
			zhyclData.setStatus("F");
		}
		return zhyclData;
	}

	@Override
	public List<ZHYCLData> saveB2BCreditLimit(final List<ZHYCLData> zhyclDataList)
	{
		if (zhyclDataList == null)
		{
			LOG.error(" List<ZHYCLData> saveB2BCreditLimit(final List<ZHYCLData> zhyclDataList)  zhyclDataList == null ");
			throw new IllegalArgumentException("stockLevel cannot be null");
		}

		for (int i = 0; i < zhyclDataList.size(); i++)
		{
			final ZHYCLData zhyclData = zhyclDataList.get(i);
			B2BCreditLimitModel b2bCreditLimit = (B2BCreditLimitModel) getModelService().create(B2BCreditLimitModel.class);
			//zhyclData.getVkORG();
			LOG.debug(" zhyclData(" + i + ").getRange_DAT()= [" + zhyclData.getRange_DAT() + "]");
//			LOG.info(" zhyclData(" + i + ").getKunNR()= [" + zhyclData.getKunNR() + "]");
			LOG.debug(" zhyclData(" + i + ").getWaeRK()= [" + zhyclData.getWaeRK() + "]");
			final String Code = zhyclData.getKunNR();
			if (this.getB2bCreditLimitDao().hasB2BCreditLimitModelByCode(Code))
			{
				b2bCreditLimit = this.getB2bCreditLimitDao().getB2BCreditLimitModelByCode(Code);
				b2bCreditLimit.setIcType("MODIFY");
				zhyclData.setIcType("MODIFY");
			}
			else
			{
				b2bCreditLimit.setActive(Boolean.TRUE);
				b2bCreditLimit.setIcType("CREATE");
				zhyclData.setIcType("CREATE");
			}
			b2bCreditLimit.setVkORG(zhyclData.getVkORG());
			b2bCreditLimit.setCode(Code);
			b2bCreditLimit.setAmount(zhyclData.getAmount());
			if (this.getB2bCreditLimitDao().hasCurrencyModelByISOCODE(zhyclData.getWaeRK().toUpperCase()))
			{
				b2bCreditLimit.setCurrency(this.getB2bCreditLimitDao().getCurrencyModelByISOCODE(zhyclData.getWaeRK().toUpperCase()));

			}
			else
			{
				final String WAERK = configurationService.getConfiguration().getString("sap.zhycl.WAERK");//"SAP";
				LOG.warn(" zhyclData(" + i + ").getWaeRK()= [" + zhyclData.getWaeRK() + "]");
				LOG.warn(" zhyclData(" + i + ") whitout WAERK use default sap.zhycl.WAERK==" + WAERK);
				b2bCreditLimit.setCurrency(this.getB2bCreditLimitDao().getCurrencyModelByISOCODE(WAERK.toUpperCase()));

			}
			try
			{
				b2bCreditLimit.setDateRange(B2BPeriodRange.valueOf(zhyclData.getRange_DAT().toUpperCase()));
			}
			catch (final Exception e)
			{
				final String RANGE_DAT = configurationService.getConfiguration().getString("sap.zhycl.RANGE_DAT");//"SAP";
				LOG.warn(" zhyclData(" + i + ").getRange_DAT()= [" + zhyclData.getRange_DAT() + "]");
				LOG.warn(" zhyclData(" + i + ") whitout RANGE_DAT Exception = [" + e.getStackTrace()
						+ "] use default sap.zhycl.RANGE_DAT==" + RANGE_DAT);
				b2bCreditLimit.setDateRange(B2BPeriodRange.valueOf(RANGE_DAT.toUpperCase()));
			}
			b2bCreditLimit.setCreateDate(zhyclData.getCrt_DATE());
			b2bCreditLimit.setFromSYS(zhyclData.getFrm_SYS());
			b2bCreditLimit.setToSYS(zhyclData.getTo_SYS());


			try
			{
				b2bCreditLimit.setDataExchangStatus("S");
				getModelService().save(b2bCreditLimit);
				zhyclData.setStatus("S");
			}
			catch (final Exception e)
			{
				LOG.error(" zhyclData(" + i + ") getModelService().save(b2bCreditLimit) Exception = [" + e.getStackTrace() + "]");
				zhyclData.setStatus("F");
			}

		}


		return zhyclDataList;
	}



}
