package com.ncip.core.service;

import java.util.List;
import com.ncip.core.model.ZhydelyBean;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface ConsignmentSyncService {

  List<ConsignmentModel> prepareConsignments();

  void syncZTable(List<ConsignmentModel> list);

  List<ZhydelyBean> prepareZeroStockConsignmentsFromSAP();

  void syncZeroStockDist(List<ZhydelyBean> sapList);

}
