package com.ncip.core.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.ncip.core.consignment.ConsignmentService;
import com.ncip.core.constants.GeneratedNcipCoreConstants.Enumerations.ProductType;
import com.ncip.core.enums.DeliveryType;
import com.ncip.core.service.HctFtpService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

public class HctFtpServiceImpl implements HctFtpService {

  @Resource
  private ConfigurationService configurationService;
  @Resource
  private ConsignmentService ncipDefaultConsignmentService;
  /*@Resource
  ConsignmentService consignmentService;*/
  @Resource
  private ModelService modelService;

  private FTPClient ftpClient = new FTPClient();
  private static String encoding = System.getProperty("file.encoding");
  private static String fileEncoding = "utf-8";
  private String today;
  private String ftpFilePath;
  private String ftpFileName;
  private FileInputStream in;
  private Logger log = Logger.getLogger(HctFtpServiceImpl.class);
  private boolean isDebug;
  private String time;
  private String homeDeliveryBackPath;
  private String homeDeliveryBackupPath;
  private String frightBackPath;
  private String frightBackupPath;
  private String type;

  private List<String> excelTitle = new ArrayList<>();
  private List<List<String>> excelDatas = new ArrayList<>();

  @Override
  public void runService(String sendType) {
    isDebug = log.isDebugEnabled();
    System.out.println("isDebug = " + isDebug);
    if (isDebug) {
      log.debug("sendType = " + sendType);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    final Date date = new Date();
    today = sdf.format(date);
    String todayWithSymbol = "";
    sdf = new SimpleDateFormat("yyyy-MM-dd");
    todayWithSymbol = sdf.format(date);
    if (sendType.trim().equals("HomeDelivery")) {
      ftpFilePath = configurationService.getConfiguration().getString("homeDeliverySendFolder", "");
      time = todayWithSymbol;
      type = DeliveryType.LOGISTICS.toString();
    } else if (sendType.trim().equals("Fright")) {
      ftpFilePath = configurationService.getConfiguration().getString("frightSendFolder", "");
      time = todayWithSymbol;
      type = DeliveryType.FREIGHT.toString();
    } else if (sendType.trim().equals("Get")) {
      homeDeliveryBackPath =
          configurationService.getConfiguration().getString("homeDeliveryBackFolder", "");
      homeDeliveryBackupPath =
          configurationService.getConfiguration().getString("homeDeliveryBackBackupFolder", "");
      frightBackPath =
          configurationService.getConfiguration().getString("frightBackFolder", "");
      frightBackupPath =
          configurationService.getConfiguration().getString("frightBackBackupFolder", "");
      System.out.println("homeDeliveryBackPath = " + homeDeliveryBackPath);
      System.out.println("homeDeliveryBackupPath = " + homeDeliveryBackupPath);
      System.out.println("frightBackPath = " + frightBackPath);
      System.out.println("frightBackupPath = " + frightBackupPath);
      readBackFiles(homeDeliveryBackPath, homeDeliveryBackupPath);
      readBackFiles(frightBackPath, frightBackupPath);
      return;
    } else {
      ftpFilePath = "/";
    }
    ftpFileName = today + ".xls";
    setExcelTitle();
    List<ConsignmentModel> consignmentModels = setConsignmentData();
    if (excelDatas != null && excelDatas.size() > 0) {
      try {
        createExcel(consignmentModels);
        // connectFTP();
        // sendFileToFTP(consignmentModels);
      } catch (Exception e) {
         
        e.printStackTrace();
      }
    }
  }

  void setExcelTitle() {
    excelTitle.add("OrderNo       ");
    excelTitle.add("recipient_name");
    excelTitle.add("recipient_tel ");
    excelTitle.add("recipient_mob ");
    excelTitle.add("recipient_addr");
    excelTitle.add("line          ");
    excelTitle.add("ItemNo        ");
    excelTitle.add("quantity      ");
    excelTitle.add("Sales_amount  ");
    excelTitle.add("remark        ");
  }

  private List<ConsignmentModel> setConsignmentData() {
    // TODO ConsignmentService
    List<ConsignmentModel> consignmentModels = ncipDefaultConsignmentService.GetConsignmentsByTimeAndType(time, type);
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    final Date date = new Date();
    final String todayYear = sdf.format(date);
    if(consignmentModels != null) {
      for (int i = 0, len = consignmentModels.size(); i < len; i++) {
        
        
        final ConsignmentModel consignmentModel = consignmentModels.get(i);
        final List<ConsignmentEntryModel> consignmentEntryModels = new ArrayList<>();
        consignmentEntryModels.addAll(consignmentModel.getConsignmentEntries());
        final List<String> excelData = new ArrayList<>();
        if (isDebug)
          log.debug("################################Start############################\n");
  
        if (isDebug)
          log.debug("consignmentEntryModels.size()      = " + consignmentEntryModels.size() + "\n");
        
        // final String orderCode = order.getCode();
        final String orderCode = consignmentModel.getCode();
        // OrderNo
        excelData.add(todayYear + stringChange(orderCode));
        if (isDebug)
          log.debug(
              "2019orderCode                      = " + todayYear + stringChange(orderCode) + "\n");
        final String itemType = consignmentModel.getItemtype();
        if (isDebug)
          log.debug("itemType                           = " + stringChange(itemType) + "\n");
        final String status = consignmentModel.getStatus().toString();
        if (isDebug)
          log.debug("status                             = " + stringChange(status) + "\n");
        final String statusDisplay = consignmentModel.getStatusDisplay();
        if (isDebug)
          log.debug("statusDisplay                      = " + stringChange(statusDisplay) + "\n");
        // final String trackingID = consignmentModel.getTrackingID();
        // if (isDebug)
        //   log.debug("trackingID                         = " + stringChange(trackingID) + "\n");
        final String creationtime = consignmentModel.getCreationtime().toString();
        if (isDebug)
          log.debug("creationtime                       = " + stringChange(creationtime) + "\n");
        final String pk = consignmentModel.getPk().toString();
        if (isDebug)
          log.debug("pk                                 = " + stringChange(pk));
        final String shippingDate = consignmentModel.getShippingDate().toString();
        if (isDebug)
          log.debug("shippingDate                       = " + stringChange(shippingDate) + "\n");
        final AddressModel shippingAddressModel = consignmentModel.getShippingAddress();
        final String firstName = shippingAddressModel.getFirstname();
        final String middleName1 = shippingAddressModel.getMiddlename();
        final String middleName2 = shippingAddressModel.getMiddlename2();
        final String middleName = stringChange(middleName1) + stringChange(middleName2);
        final String lastName = shippingAddressModel.getLastname();
        final String name = stringChange(firstName) + middleName + stringChange(lastName);
  
        excelData.add(name);
        if (isDebug)
          log.debug("Name                               = " + name + "\n");
        final String phone1 = shippingAddressModel.getPhone1();
        final String phone2 = shippingAddressModel.getPhone2();
  
        excelData.add(phone1);
  
        excelData.add(phone2);
        final String streetName = shippingAddressModel.getStreetname();
        final String streetNumber = shippingAddressModel.getStreetnumber();
        if (isDebug)
          log.debug("phone1 + phone2                    = " + stringChange(phone1)
              + stringChange(phone2) + "\n");
        if (isDebug)
          log.debug("streetName + streetNumber          = " + stringChange(streetName)
              + stringChange(streetNumber) + "\n");
        final String cellphone = shippingAddressModel.getCellphone();
        if (isDebug)
          log.debug("cellphone                          = " + stringChange(cellphone) + "\n");
        final String line1 = shippingAddressModel.getLine1();
  
        excelData.add(line1);
        if (isDebug)
          log.debug("line1                              = " + stringChange(line1) + "\n");
        final String line2 = shippingAddressModel.getLine2();
        if (isDebug)
          log.debug("line2                              = " + stringChange(line2) + "\n");
  
        if (isDebug)
          log.debug("---------------------------------Item----------------------------\n");
        int j = 0;
  
        for (final ConsignmentEntryModel cem : consignmentEntryModels) {
          String productType = cem.getOrderEntry().getProduct().getMa_type();
          if(productType.equals("B")) {
            excelData.clear();
            continue;
          }
  
          final List<String> itemData = new ArrayList<>(excelData);
  
          itemData.add((j + 1) + "");
          // The price for one
          final double basePrice = cem.getOrderEntry().getBasePrice();
          final DeliveryModeModel deliveryModeModel = cem.getOrderEntry().getDeliveryMode();
          final String deliveryMode =
              deliveryModeModel != null ? deliveryModeModel.getDescription() : "";
          if (isDebug)
            log.debug("deliveryMode                       = " + stringChange(deliveryMode) + "\n");
          final String productCode = cem.getOrderEntry().getProduct().getCode();
          itemData.add(productCode);
          if (isDebug)
            log.debug("productCode                        = " + stringChange(productCode) + "\n");
          final long quantity = cem.getQuantity();
  
          itemData.add(quantity + "");
          if (isDebug)
            log.debug("ConsignmentEntryModel" + j + ":quantity    = " + quantity + "\n");
  
          itemData.add((int) (basePrice * quantity) + "");
          if (isDebug)
            log.debug("basePrice * quantity               = " + (basePrice * quantity) + "\n");
          itemData.add("");
          j++;
          excelDatas.add(itemData);
        }
        if (isDebug)
          log.debug("---------------------------------Item----------------------------\n");
        if (isDebug)
          log.debug("#################################End#############################\n");
      }
    }
    return consignmentModels;
  }

  @Override
  public void createExcel(List<ConsignmentModel> consignmentModels){
    if (isDebug)
      log.debug("excelTitle.size() = " + excelTitle.size());
    if (isDebug)
      log.debug("excelDatas.size() = " + excelDatas.size());
    for (final List<String> s : excelDatas) {
      if (isDebug)
        log.debug("s.size() = " + s.size());
    }
//    String excelPath = ftpFilePath + today + ".xls";
    String excelPath = ftpFilePath + today + ".csv";
    File file = new File(excelPath);
    if(!file.exists()){
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    String csvData = "";
    for(int i = 0, len = excelTitle.size(); i < len; i++){
      if(i < len - 1){
        csvData += excelTitle.get(i) + ",";
      } else {
        csvData += excelTitle.get(i) + "\r\n";
      }
    }
    
    for(int i = 0, len = excelDatas.size(); i < len; i++){
      List<String> datas = excelDatas.get(i);
      for(int j = 0, innerLen = excelDatas.get(i).size(); j < innerLen; j++){
        if(j < innerLen - 1){
          csvData += datas.get(j) + ",";
        } else {
          csvData += datas.get(j) + "\r\n";
        }
      }
    }
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(csvData);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    for(ConsignmentModel cm : consignmentModels){
      cm.setStatus(ConsignmentStatus.READY_FOR_SHIPPING);
      modelService.save(cm);
    }
//    Workbook workbook;
//    Sheet sheet;
//    final String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
//    // 建立文件物件
//    if (fileType.equals("xls")) {
//      // 如果是.xls,就new HSSFWorkbook()
//      workbook = new HSSFWorkbook();
//    } else {
//      throw new Exception("The file type is not 'xls'!!");
//    }
//    sheet = workbook.createSheet();
//    if (excelDatas != null) {
//      final int rowNumber = excelDatas.size() + 1;
//      final Row row1 = sheet.createRow(0);
//
//      // 將內容寫入指定的行號中
//      for (int i = 0; i < rowNumber; i++) {
//        final Row row = sheet.createRow(i);
//        if (i == 0) {
//          // 將excel抬頭加入excel
//          for (int j = 0, len = excelTitle.size(); j < len; j++) {
//            final Cell cell = row1.createCell(j);
//            cell.setCellValue(excelTitle.get(j));
//            sheet.autoSizeColumn(j);
//          }
//        } else {
//          // 將excel資料加入excel
//          for (int j = 0, len = excelDatas.get(i - 1).size(); j < len; j++) {
//            final Cell cell = row.createCell(j);
//            cell.setCellValue(excelDatas.get(i - 1).get(j));
//          }
//        }
//      }
//      final File file = new File(excelPath);
//      if (!file.exists()) {
//        file.createNewFile();
//      }
//      final OutputStream stream = new FileOutputStream(file);
//      in = new FileInputStream(file);
//      workbook.write(stream);
//      stream.close();
//    } else {
//    }
  }

  @Override
  public FTPClient connectFTP() {
    final String ftpURL = configurationService.getConfiguration().getString("ftpURL", "");
    final String ftpPort = configurationService.getConfiguration().getString("ftpPort", "");
    final String ftpUser = configurationService.getConfiguration().getString("ftpUser", "");
    final String ftpPassword = configurationService.getConfiguration().getString("ftpPassword", "");

    int reply;
    try {
      if (ftpPort.trim().equals("")) {
        ftpClient.connect(ftpURL);
      } else {
        try {
          ftpClient.connect(ftpURL, Integer.valueOf(ftpPort));
        } catch (NumberFormatException e) {
          e.printStackTrace();
          return null;
        }
      }
      ftpClient.login(ftpUser, ftpPassword);
      ftpClient.setControlEncoding(encoding);
      reply = ftpClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        if (isDebug)
          log.debug("連線失敗");
        ftpClient.disconnect();
        return null;
      }
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
      return ftpClient;
    } catch (final SocketException e) {
       
      e.printStackTrace();
    } catch (final IOException e) {
       
      e.printStackTrace();
    }
    return null;
  }

  private String stringChange(String data) {
    return data == null ? "" : data;
  }

  // 上傳檔案並且回壓狀態的地方
  @Override
  public void sendFileToFTP(List<ConsignmentModel> consignmentModels) {
    boolean flag = false;
    try {
      final String[] paths = ftpFilePath.split("/");
      boolean change = false;
      String path = paths[0];
      for (int i = 1, len = paths.length; i < len; i++) {
        path += "/" + paths[i];
        change = ftpClient.changeWorkingDirectory(path);
      }
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
      if (change) {
        try {
          flag = ftpClient.storeFile(new String(ftpFileName.getBytes(encoding), fileEncoding), in);
          if (flag) {
            if (isDebug)
              log.debug("Upload File Success!");
          }
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
      in.close();
      ftpClient.logout();

      // 檔案上傳完後回壓狀態的地方

      if (consignmentModels != null) {
        for (ConsignmentModel cm : consignmentModels) {
          cm.setStatus(ConsignmentStatus.READY_FOR_SHIPPING);
          modelService.save(cm);
        }
      }
      File file = new File("/");
      String[] files = file.list();
      if (files != null) {
        for (String fileName : files) {
          fileName = "/" + fileName;
          try {
            File deleteFile = new File(fileName);
            deleteFile.delete();
          } catch (NullPointerException e) {
            e.printStackTrace();
          }
        }
      }

      // 檔案上傳完後回壓狀態的地方


    } catch (final SocketException e) {
       
      e.printStackTrace();
    } catch (final IOException e) {
       
      e.printStackTrace();
    } finally {
      if (ftpClient.isConnected()) {
        try {
          ftpClient.disconnect();
        } catch (final IOException ioe) {
        }
      }
    }
  }

  @Override
  public void getFileFromFTP() {
    FTPClient ftpClient = connectFTP();
    String homeDeliveryBackPath =
        configurationService.getConfiguration().getString("homeDeliveryBackFolder", "");
    String homeDeliveryBackupPath =
        configurationService.getConfiguration().getString("homeDeliveryBackBackupFolder", "");
    String frightBackPath =
        configurationService.getConfiguration().getString("frightBackFolder", "");
    String frightBackupPath =
        configurationService.getConfiguration().getString("frightBackBackupFolder", "");
    try {
      ftpClient
          .changeWorkingDirectory(homeDeliveryBackPath.substring(0, homeDeliveryBackPath.length()));
      FTPFile[] files = ftpClient.listFiles();
      if (files != null && files.length > 0) {
        for (FTPFile ff : files) {
          String name = ff.getName();
          File file = new File("/" + name);
          OutputStream os = new FileOutputStream(file);
          ftpClient.retrieveFile(name, os);
          if (file.exists()) {
            readTXT(file);
          }
          ftpClient.rename(homeDeliveryBackPath + name, homeDeliveryBackupPath + name);
          os.close();
        }
      }

      ftpClient.changeWorkingDirectory(frightBackPath.substring(0, frightBackPath.length()));
      files = ftpClient.listFiles();
      if (files != null && files.length > 0) {
        for (FTPFile ff : files) {
          String name = ff.getName();
          File file = new File("/" + name);
          OutputStream os = new FileOutputStream(file);
          ftpClient.retrieveFile(name, os);
          ftpClient.rename(frightBackPath + name, frightBackupPath + name);
          if (file.exists()) {
            readTXT(file);
          }
          os.close();
        }
      }
    } catch (IOException e) {
       
      e.printStackTrace();
    }
  }
  
  private void readBackFiles(String filePath, String removeToPath){
    File file = new File(filePath);
    if(file.exists()){
      String[] filesName = file.list();
      for(String name : filesName){
        File backFile = new File(filePath + name);
        
        if(readTXT(backFile) && removeToPath != null && removeToPath.trim().length() > 0){
          backFile.renameTo(new File(removeToPath + name));
        }
      }
    }
  }

  @Override
  public boolean readTXT(File file) {
    boolean flag = true;
    List<String> txtDatas = new ArrayList<>();
    FileReader fr;
    try {
      fr = new FileReader(file);
      BufferedReader reader = new BufferedReader(fr);
      while (reader.ready()) {
        txtDatas.add(reader.readLine());
      }
      flag = analysisString(txtDatas);
      reader.close();
    } catch (FileNotFoundException e) {
       
      e.printStackTrace();
      flag = false;
    } catch (IOException e) {
       
      e.printStackTrace();
      flag = false;
    }
    return flag;
  }

  private boolean analysisString(List<String> datas) {
    boolean flag = true;
    if (datas != null && datas.size() > 0) {
      Datas:
      for (int i = 1, len = datas.size(); i < len; i++) {
        String[] dataStrings = datas.get(i).split(",", -1);
        if (dataStrings != null && dataStrings.length > 0) {
          // OrderNo
          String code = dataStrings[0];
          ConsignmentModel consignmentModel = 
               (ncipDefaultConsignmentService.GetConsignmentsByCode(code) != null && ncipDefaultConsignmentService.GetConsignmentsByCode(code).size() > 0)
                   ? ncipDefaultConsignmentService.GetConsignmentsByCode(code).get(0) : null;
          if (consignmentModel != null) {
            List<ConsignmentEntryModel> consignmentEntryModels = new ArrayList<>(consignmentModel.getConsignmentEntries());
            for(ConsignmentEntryModel cem : consignmentEntryModels) {
              if(cem.getOrderEntry().getProduct().getMa_type().equals("B")) {
                continue Datas;
              }
            }
            // ShipStatus
            String shipSataus = dataStrings[1];
            if (shipSataus != null && shipSataus.trim().length() > 0) {
              // Shipping Number
              String shipping_number = dataStrings[2];
              // Arrival Data
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              String arrival_Data = dataStrings[3];
              Date date;
              try {
                switch (shipSataus) {
                  case "1":
                    consignmentModel.setStatus(ConsignmentStatus.SHIPPED);
                    consignmentModel.setShipping_number(shipping_number);
                    break;
                  case "2":
                    if(arrival_Data != null && arrival_Data.length() > 0 && arrival_Data.matches("^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]) (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])$")) {
                      date = sdf.parse(arrival_Data);
                    } else {
                      date = null;
                      break;
                    }
                    consignmentModel.setShippingDate(date);
                    consignmentModel.setStatus(ConsignmentStatus.DELIVERY_COMPLETED);
                    consignmentModel.setShipping_number(shipping_number);
                    break;
                  case "3":
                    break;
                }
                modelService.save(consignmentModel);
              } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                flag = false;
              }

            } else {
              flag = false;
            }

          } else {
            flag = false;
          }
        } else {
          flag = false;
        }
      }
    } else {
      flag = false;
    }
    return flag;
  }

  public ConfigurationService getConfigurationService() {
    return configurationService;
  }

  public void setConfigurationService(final ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  public ConsignmentService getNcipDefaultConsignmentService() {
    return ncipDefaultConsignmentService;
  }

  public void setNcipDefaultConsignmentService(ConsignmentService ncipDefaultConsignmentService) {
    this.ncipDefaultConsignmentService = ncipDefaultConsignmentService;
  }

  public ModelService getModelService() {
    return modelService;
  }

  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  /*public ConsignmentService getConsignmentService() {
    return consignmentService;
  }

  public void setConsignmentService(ConsignmentService consignmentService) {
    this.consignmentService = consignmentService;
  }*/

}
