package com.ncip.core.futureStock.impl;

import java.util.List;
import javax.annotation.Resource;
import com.ncip.core.futureStock.NcipFutureStockService;
import com.ncip.core.futureStock.dao.NcipFutureStockDao;
import de.hybris.platform.acceleratorservices.futurestock.impl.DefaultFutureStockService;
import de.hybris.platform.b2b.model.FutureStockModel;

public class DefaultNcipFutureStockService extends DefaultFutureStockService implements NcipFutureStockService{
  @Resource
  NcipFutureStockDao ncipFutureStockDao;

  @Override
  public List<FutureStockModel> getAllFutureStockModels() {
    return ncipFutureStockDao.getAllFutureStockModels();
  }

  @Override
  public List<FutureStockModel> getFutureStockModelsByProductCode(String code) {
    return ncipFutureStockDao.getFutureStockModelsByProductCode(code);
  }

  public NcipFutureStockDao getNcipFutureStockDao() {
    return ncipFutureStockDao;
  }

  public void setNcipFutureStockDao(NcipFutureStockDao ncipFutureStockDao) {
    this.ncipFutureStockDao = ncipFutureStockDao;
  }
  
}
