package com.ncip.core.dao;

import java.util.Date;
import java.util.List;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface ConsignmentSyncDao {

  List<ConsignmentModel> prepareAllConsignmentsByStatus();

  boolean codeExistsInHybris(String code, String order, Long entry);

  void updateLFDATByCode(String code, Date wadatIst);

}
