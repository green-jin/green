package com.ncip.core.dao;


import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;


/**
 * @author green
 *
 */
public interface B2BCreditLimitDao
{
	B2BCreditLimitModel getB2BCreditLimitModelByCode(String Code);

	boolean hasB2BCreditLimitModelByCode(String Code);

	CurrencyModel getCurrencyModelByISOCODE(String isocode);

	boolean hasCurrencyModelByISOCODE(String isocode);
}
