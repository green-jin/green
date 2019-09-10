package com.ncip.core.jalo;

import com.ncip.core.constants.NcipCoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class NcipCoreManager extends GeneratedNcipCoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NcipCoreManager.class.getName() );
	
	public static final NcipCoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NcipCoreManager) em.getExtension(NcipCoreConstants.EXTENSIONNAME);
	}
	
}
