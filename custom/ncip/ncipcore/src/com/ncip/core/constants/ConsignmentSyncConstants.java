package com.ncip.core.constants;

public class ConsignmentSyncConstants {

  private ConsignmentSyncConstants() {};

  public static final String CREATE = "CREATE";
  public static final String MODIFY = "MODIFY";
  public static final String HYBRIS = "Hybris";
  public static final String SAP = "SAP";

  /* SAP ERP抛送的分配無庫存出貨單為一般商品無庫存(DELY_TYPE=3)及預購品(DELY_TYPE=4) */
  public static final String ZERO_STOCK_2_CONSIGNMENT_DELY_TYPE = "3,4";

  /* ZHYDELY status */
  public static final String SAP_ZHYDELY_STATUS_UNPROCESSED = "-";
  public static final String SAP_ZHYDELY_STATUS_SUCCEEDED = "S";
  public static final String SAP_ZHYDELY_STATUS_FAILED = "F";
  public static final String SAP_ZHYDELY_STATUS_PROCESSING = "P";

}
