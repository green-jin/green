package com.ncip.core.service.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import com.ncip.core.futureStock.NcipFutureStockService;
import com.ncip.core.service.UpdateFutureStockService;
import de.hybris.platform.b2b.model.FutureStockModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

public class UpdateFutureStockServiceImpl implements UpdateFutureStockService{

  @Resource
  ConfigurationService configurationService;
  
  @Resource
  ModelService modelService;
  
  @Resource
  NcipFutureStockService ncipFutureStockService;
  
  String DB_DRIVER;
  String DB_URL;
  String USER;
  String PASSWORD;
  
  final String FRMSYS = "SAP";
  final String TOSYS = "hybris";
  final String STATUS = "-";
  
  private boolean flag = true;
  
  @Override
  public void runService() {
    DB_DRIVER = configurationService.getConfiguration().getString("dbDriver", "");
    DB_URL = configurationService.getConfiguration().getString("dbURL", "");
    USER = configurationService.getConfiguration().getString("user", "");
    PASSWORD = configurationService.getConfiguration().getString("password", "");
    Connection conn = dbConnection();
    System.out.println("flag = " + flag);
    if(flag) {
      getDBData(conn);
    }
  }

  @Override
  public Connection dbConnection() {
    Connection conn = null;
    try {
      Class.forName(DB_DRIVER);
      try {
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        return conn;
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      
      e.printStackTrace();
    }
    flag = false;
    return null;
  }

  private void getDBData(Connection conn) {
    deleteAllFutureStockDatas();
    try {
      
      String queryString = "select * from product where product.FRM_SYS='SAP' and product.TO_SYS='hybris' and product.STATUS='-';";
      PreparedStatement ps = conn.prepareStatement(queryString);
      ResultSet rs = ps.executeQuery(queryString);
      while (rs.next()) {
        String matnr = rs.getString("MATNR") != null ? rs.getString("MATNR") : "";
        List<FutureStockModel> ncipDatas = ncipFutureStockService.getFutureStockModelsByProductCode(matnr);
        if(ncipDatas != null && ncipDatas.size() > 0) {
          FutureStockModel fsm = new FutureStockModel();
          String productCodeString = matnr;
          int quantity = rs.getInt("MENGE");
          Date date = rs.getDate("WDATE");
          fsm.setProductCode(productCodeString);
          fsm.setQuantity(quantity);
          fsm.setDate(date);
          modelService.save(fsm);
          String updateString = "update product set product.STATUS='S' where product.MATNR=?";
          ps = conn.prepareStatement(updateString);
          ps.setString(1, matnr);
          ps.executeUpdate();
        } else {
          String updateString = "update product set product.STATUS='F' where product.MATNR=?";
          ps = conn.prepareStatement(updateString);
          ps.setString(1, matnr);
          ps.executeUpdate();
        }
      }
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void deleteAllFutureStockDatas() {
    modelService.removeAll(ncipFutureStockService.getAllFutureStockModels());
  }

  public ConfigurationService getConfigurationService() {
    return configurationService;
  }

  public void setConfigurationService(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public NcipFutureStockService getNcipFutureStockService() {
    return ncipFutureStockService;
  }

  public void setNcipFutureStockService(NcipFutureStockService ncipFutureStockService) {
    this.ncipFutureStockService = ncipFutureStockService;
  }
  
}
