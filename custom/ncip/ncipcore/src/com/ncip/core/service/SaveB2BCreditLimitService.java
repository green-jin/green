package com.ncip.core.service;

import java.util.List;

import com.ncip.core.data.ZHYCLData;


public interface SaveB2BCreditLimitService
{
	/**
	 * Saves the given stockLevel model .
	 *
	 * @param stockLevel
	 *           stockLevel model
	 * @return saved and refreshed stockLevel model
	 */
	List<ZHYCLData> saveB2BCreditLimit(final List<ZHYCLData> zhyclData);

	ZHYCLData saveB2BCreditLimit(final ZHYCLData zhyclData);
}
