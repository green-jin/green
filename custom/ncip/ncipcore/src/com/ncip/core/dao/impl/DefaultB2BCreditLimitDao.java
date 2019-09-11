package com.ncip.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.ncip.core.dao.B2BCreditLimitDao;
/**
 * @author green
 *
 */
public class DefaultB2BCreditLimitDao implements B2BCreditLimitDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/* (non-Javadoc)
	 * @see de.hybris.training.core.suggestion.dao.B2BCreditLimitDao#getB2BCreditLimitModelByCode(java.lang.String)
	 */
	@Override
	public B2BCreditLimitModel getB2BCreditLimitModelByCode(final String Code)
	{
		final String queryString = //
				"SELECT {p:" + B2BCreditLimitModel.PK + "} "//
						+ "FROM {" + B2BCreditLimitModel._TYPECODE + " AS p} " //
						+ "WHERE {code} = \'" + Code + "\' ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final List<B2BCreditLimitModel> b2BCreditLimitModelList = flexibleSearchService.<B2BCreditLimitModel> search(query)
				.getResult();
		if (b2BCreditLimitModelList != null && !b2BCreditLimitModelList.isEmpty())
		{
			return b2BCreditLimitModelList.get(0);
		}
		else
		{
			return null;
		}


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.training.core.suggestion.dao.B2BCreditLimitDao#hasB2BCreditLimitModelByCode(java.lang.String)
	 */
	@Override
	public boolean hasB2BCreditLimitModelByCode(final String Code)
	{
		final String queryString = //
				"SELECT {p:" + B2BCreditLimitModel.PK + "} "//
						+ "FROM {" + B2BCreditLimitModel._TYPECODE + " AS p} " //
						+ "WHERE {code} = \'" + Code + "\' ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final List<B2BCreditLimitModel> b2BCreditLimitModelList = flexibleSearchService.<B2BCreditLimitModel> search(query)
				.getResult();
		if (b2BCreditLimitModelList != null && !b2BCreditLimitModelList.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.training.core.suggestion.dao.B2BCreditLimitDao#getCurrencyModelByISOCODE(java.lang.String)
	 */
	@Override
	public CurrencyModel getCurrencyModelByISOCODE(final String isocode)
	{
		final String queryString = //
				"SELECT {p:" + CurrencyModel.PK + "} "//
						+ "FROM {" + CurrencyModel._TYPECODE + " AS p} " //
						+ "WHERE {isocode} = \'" + isocode + "\' ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final List<CurrencyModel> currencyModelList = flexibleSearchService.<CurrencyModel> search(query).getResult();
		if (currencyModelList != null && !currencyModelList.isEmpty())
		{
			return currencyModelList.get(0);
		}
		else
		{
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.training.core.suggestion.dao.B2BCreditLimitDao#hasCurrencyModelByISOCODE(java.lang.String)
	 */
	@Override
	public boolean hasCurrencyModelByISOCODE(final String isocode)
	{
		final String queryString = //
				"SELECT {p:" + CurrencyModel.PK + "} "//
						+ "FROM {" + CurrencyModel._TYPECODE + " AS p} " //
						+ "WHERE {isocode} = \'" + isocode + "\' ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final List<CurrencyModel> currencyModelList = flexibleSearchService.<CurrencyModel> search(query).getResult();
		if (currencyModelList != null && !currencyModelList.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
