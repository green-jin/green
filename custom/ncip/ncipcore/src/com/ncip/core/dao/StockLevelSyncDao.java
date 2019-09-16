package com.ncip.core.dao;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


/**
 * @author green
 *
 */
public interface StockLevelSyncDao extends Dao
{
	public abstract void updateSyncAmountBySAP(StockLevelModel stocklevelmodel, int i);
}
