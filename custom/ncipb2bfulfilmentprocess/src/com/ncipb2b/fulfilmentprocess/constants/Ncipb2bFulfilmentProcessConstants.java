/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncipb2b.fulfilmentprocess.constants;

@SuppressWarnings("deprecation")
public final class Ncipb2bFulfilmentProcessConstants extends GeneratedNcipb2bFulfilmentProcessConstants
{
    public static final String CONSIGNMENT_SUBPROCESS_END_EVENT_NAME = "ConsignmentSubprocessEnd";
    public static final String ORDER_PROCESS_NAME = "order-b2bprocess";
    public static final String CONSIGNMENT_SUBPROCESS_NAME = "consignment-b2bprocess";
    public static final String WAIT_FOR_WAREHOUSE = "WaitForWarehouse";
    public static final String CONSIGNMENT_PICKUP = "ConsignmentPickup";
    public static final String WAIT_FOR_SHIPPING = "WaitForShipping";
    public static final String ALLREADY_SHIPPED = "Allready_Shipped";
    //for consignmentProcess
    public static final String WAREHOUSE_LIST = "WAREHOUSE_LIST";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String DELIVERY_MODE = "DELIVERY_MODE";

    public static final String NORMAL_LOGISTICS   = "A";  //MA_TYPE:一般商品
    public static final String NORMAL_FREIGHT     = "C";  //MA_TYPE:一般品-大型商品 
    public static final String CUSTOM_ITEM        = "B";  //MA_TYPE:訂製品 
    public static final String PREORDER_LOGISTICS = "D";  //MA_TYPE:預購品
    public static final String PREORDER_FREIGHT   = "E";  //MA_TYPE:預購品-大型商品
    
  
    public static final String CUSTOM_ITEM_VALUE    = "1";// dely_type 1 ：訂製品(無庫存,不拋物流)
    public static final String NORMAL_STOCK_VALUE   = "2";// dely_type 2：一般品有庫存
    public static final String NORMAL_UNSTOCK_VALUE = "3";// dely_type 3：一般品無庫存
    public static final String PREORDER_VALUE       = "4";// dely_type 4：預購品(無庫存)

    public static final String STOCK_STATUS = "STOCK_STATUS";
    public static final String UNSTOCK = "UNSTOCK";
    public static final String STOCK = "STOCK";
    public static final String STOCK_QUANTITY = "STOCK_QUANTITY";
    public static final String MT_TYPE ="MT_TYPE";
 
    public static final String CONSIGNMENT_COUNTER = "CONSIGNMENT_COUNTER";
    public static final String PARENT_PROCESS = "PARENT_PROCESS";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String BASE_STORE = "BASE_STORE";
}
