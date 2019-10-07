package com.ncip.core.futureStock.dao;

import java.util.List;
import de.hybris.platform.b2b.model.FutureStockModel;

public interface NcipFutureStockDao {
  List<FutureStockModel> getAllFutureStockModels();
  List<FutureStockModel> getFutureStockModelsByProductCode(String code);
}
