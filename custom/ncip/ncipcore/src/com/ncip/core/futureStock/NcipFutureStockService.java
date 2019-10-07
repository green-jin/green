package com.ncip.core.futureStock;

import java.util.List;
import de.hybris.platform.b2b.model.FutureStockModel;

public interface NcipFutureStockService {
  List<FutureStockModel> getAllFutureStockModels();
  List<FutureStockModel> getFutureStockModelsByProductCode(String code);
}
