/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncipb2b.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.ncipb2b.fulfilmentprocess.constants.Ncipb2bFulfilmentProcessConstants;

public class Ncipb2bFulfilmentProcessManager extends GeneratedNcipb2bFulfilmentProcessManager
{
	public static final Ncipb2bFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Ncipb2bFulfilmentProcessManager) em.getExtension(Ncipb2bFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
