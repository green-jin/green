package com.ncip.core.service;


import java.util.List;

import com.ncip.core.data.ZHYSTKData;


public interface SaveStockLevelService
{
	/**
	 * Saves the given stockLevel model .
	 *
	 * @param stockLevel
	 *           stockLevel model
	 * @return saved and refreshed stockLevel model
	 */
	List<ZHYSTKData> saveStockLevel(final List<ZHYSTKData> zhystkData);

	ZHYSTKData saveStockLevel(final ZHYSTKData zhystkData);
}
