package com.ncip.core.service;

import java.sql.Connection;

public interface UpdateFutureStockService {
  void runService();
  
  Connection dbConnection();
}
