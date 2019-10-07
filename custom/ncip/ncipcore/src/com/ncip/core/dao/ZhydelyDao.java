package com.ncip.core.dao;

import java.util.List;
import com.ncip.core.model.ZhydelyBean;

public interface ZhydelyDao {

  void insertByModel(ZhydelyBean model);

  boolean isExistedInZTable(String org, String code, Long entryNum);

  void updateByModel(ZhydelyBean model);

  List<ZhydelyBean> queryForZeroStockDistSync();

  void updateStatus(String code, String status);

}
