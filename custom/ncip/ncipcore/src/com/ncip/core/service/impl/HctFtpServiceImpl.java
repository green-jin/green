package com.ncip.core.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ncip.core.service.GetConsignmentDataService;
import com.ncip.core.service.HctFtpService;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

public class HctFtpServiceImpl implements HctFtpService {

	@Resource
	private ConfigurationService configurationService;
	@Resource
	private GetConsignmentDataService getConsignmentDataService;
	@Resource
	private ModelService modelService;

	private FTPClient ftpClient = new FTPClient();
	private static String encoding = System.getProperty("file.encoding");
	private String today;
	private String sendType;
	private String ftpFilePath;
	private String ftpFileName;
	private FileInputStream in;
	private Logger log = LoggerFactory.getLogger(HctFtpServiceImpl.class);
	private boolean isDebug = !log.isDebugEnabled();

	private List<String> excelTitle = new ArrayList<>();
	private List<List<String>> excelDatas = new ArrayList<>();

	/*
	 * public HctFtpServiceImpl() { final SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyyMM_ddHHmmss"); final Date date = new Date(); today
	 * = sdf.format(date); ftpFilePath =
	 * configurationService.getConfiguration().getString(
	 * "homeDeliverySendFolder", ""); this.sendType = sendType; if
	 * (sendType.trim().equals("HomeDelivery")) { ftpFilePath =
	 * configurationService.getConfiguration().getString(
	 * "homeDeliverySendFolder", ""); } else if
	 * (sendType.trim().equals("Fright")) { ftpFilePath =
	 * configurationService.getConfiguration().getString("freightSendFolder",
	 * ""); } else { ftpFilePath = "/"; } }
	 */
	@Override
	public void runService(String sendType) {
		System.out.println(isDebug);
		if(isDebug){
			log.debug("sendType = " + sendType);
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM_ddHHmmss");
		final Date date = new Date();
		today = sdf.format(date);
		this.sendType = sendType;
		if (sendType.trim().equals("HomeDelivery")) {
			ftpFilePath = configurationService.getConfiguration().getString("homeDeliverySendFolder", "");
		} else if (sendType.trim().equals("Fright")) {
			ftpFilePath = configurationService.getConfiguration().getString("frightSendFolder", "");
		} else if (sendType.trim().equals("Get")) {
			getFileFromFTP();
		} else {
			ftpFilePath = "/";
		}
		ftpFileName = "00000001" + today + ".xls";
		setExcelTitle();
		List<ConsignmentModel> consignmentModels = setConsignmentData();
		try {
			createExcel();
			connectFTP();
			sendFileToFTP(consignmentModels);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		List<ConsignmentModel> consignmentModels = getConsignmentDataService.getConsignmentData();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		final Date date = new Date();
		final String todayYear = sdf.format(date);

		for (int i = 0, len = consignmentModels.size(); i < len; i++) {
			final ConsignmentModel consignmentModel = consignmentModels.get(i);
			final List<ConsignmentEntryModel> consignmentEntryModels = new ArrayList<>();
			consignmentEntryModels.addAll(consignmentModel.getConsignmentEntries());
			/*
			 * if (sendType.trim().equals("HomeDelivery")) { final boolean
			 * isFright = false; for (final ConsignmentEntryModel cem :
			 * consignmentEntryModels) { final String productType =
			 * cem.getOrderEntry().getProduct().getItemtype().toString(); if
			 * (sendType.trim().equals("HomeDelivery") &&
			 * (productType.trim().equals("C") ||
			 * productType.trim().equals("E"))) { continue ConsignmentLoop; } }
			 * }
			 */
			final List<String> excelData = new ArrayList<>();
			if(isDebug)
				log.debug("################################Start############################\n");
			final StringBuilder builder = new StringBuilder();

			builder.append("");

			// final String code = todayYear + consignmentModel.getCode();

			if(isDebug)
				log.debug("consignmentEntryModels.size()      = " + consignmentEntryModels.size() + "\n");

			final AbstractOrderModel order = consignmentModel.getOrder();
			final List<Object> orderGroups = new ArrayList<>(order.getUser().getAllGroups());
			final PrincipalGroupModel pgm = (PrincipalGroupModel) orderGroups.get(0);
			List<PrincipalGroupModel> allGroups = new ArrayList<>(pgm.getAllGroups());
			List<PrincipalModel> allGroupss = new ArrayList<>(allGroups.get(0).getMembers());

			// consignmentModel.getDeliveryMode().getName();

			final String orderCode = order.getCode();
			excelData.add(todayYear + stringChange(orderCode));

			if(isDebug)
				log.debug("2019orderCode                      = " + todayYear + stringChange(orderCode) + "\n");
			final String itemType = consignmentModel.getItemtype();
			if(isDebug)
				log.debug("itemType                           = " + stringChange(itemType) + "\n");
			final String status = consignmentModel.getStatus().toString();
			if(isDebug)
				log.debug("status                             = " + stringChange(status) + "\n");
			final String statusDisplay = consignmentModel.getStatusDisplay();
			if(isDebug)
				log.debug("statusDisplay                      = " + stringChange(statusDisplay) + "\n");
			final String trackingID = consignmentModel.getTrackingID();
			if(isDebug)
				log.debug("trackingID                         = " + stringChange(trackingID) + "\n");
			final String creationtime = consignmentModel.getCreationtime().toString();
			if(isDebug)
				log.debug("creationtime                       = " + stringChange(creationtime) + "\n");
			final String pk = consignmentModel.getPk().toString();
			if(isDebug)
				log.debug("pk                                 = " + stringChange(pk));
			final String shippingDate = consignmentModel.getShippingDate().toString();
			if(isDebug)
				log.debug("shippingDate                       = " + stringChange(shippingDate) + "\n");
			final AddressModel shippingAddressModel = consignmentModel.getShippingAddress();
			final String firstName = shippingAddressModel.getFirstname();
			final String middleName1 = shippingAddressModel.getMiddlename();
			final String middleName2 = shippingAddressModel.getMiddlename2();
			final String middleName = stringChange(middleName1) + stringChange(middleName2);
			final String lastName = shippingAddressModel.getLastname();
			final String name = stringChange(firstName) + middleName + stringChange(lastName);

			excelData.add(name);
			if(isDebug)
				log.debug("Name                               = " + name + "\n");
			final String phone1 = shippingAddressModel.getPhone1();
			final String phone2 = shippingAddressModel.getPhone2();

			excelData.add(phone1);

			excelData.add(phone2);
			final String streetName = shippingAddressModel.getStreetname();
			final String streetNumber = shippingAddressModel.getStreetnumber();
			if(isDebug)
				log.debug(
					"phone1 + phone2                    = " + stringChange(phone1) + stringChange(phone2) + "\n");
			if(isDebug)
				log.debug("streetName + streetNumber          = " + stringChange(streetName)
					+ stringChange(streetNumber) + "\n");
			final String cellphone = shippingAddressModel.getCellphone();
			if(isDebug)
				log.debug("cellphone                          = " + stringChange(cellphone) + "\n");
			final String line1 = shippingAddressModel.getLine1();

			excelData.add(line1);
			if(isDebug)
				log.debug("line1                              = " + stringChange(line1) + "\n");
			final String line2 = shippingAddressModel.getLine2();
			if(isDebug)
				log.debug("line2                              = " + stringChange(line2) + "\n");

			if(isDebug)
				log.debug("---------------------------------Item----------------------------\n");
			int j = 0;

			for (final ConsignmentEntryModel cem : consignmentEntryModels) {

				final List<String> itemData = new ArrayList<>(excelData);

				itemData.add((j + 1) + "");
				// The price for one
				final double basePrice = cem.getOrderEntry().getBasePrice();
				final DeliveryModeModel deliveryModeModel = cem.getOrderEntry().getDeliveryMode();
				final String deliveryMode = deliveryModeModel != null ? deliveryModeModel.getDescription() : "";
				if(isDebug)
					log.debug("deliveryMode                       = " + stringChange(deliveryMode) + "\n");
				final String productCode = cem.getOrderEntry().getProduct().getCode();
				itemData.add(productCode);
				if(isDebug)
					log.debug("productCode                        = " + stringChange(productCode) + "\n");
				final long quantity = cem.getQuantity();

				itemData.add(quantity + "");
				if(isDebug)
					log.debug("ConsignmentEntryModel" + j + ":quantity    = " + quantity + "\n");

				itemData.add((int) (basePrice * quantity) + "");
				if(isDebug)
					log.debug("basePrice * quantity               = " + (basePrice * quantity) + "\n");
				itemData.add("");
				j++;
				excelDatas.add(itemData);
			}
			if(isDebug)
				log.debug("---------------------------------Item----------------------------\n");
			if(isDebug)
				log.debug("#################################End#############################\n");
		}
		return consignmentModels;
	}

	@SuppressWarnings("resource")
	@Override
	public void createExcel() throws Exception {
		if(isDebug)
			log.debug("excelTitle.size() = " + excelTitle.size());
		if(isDebug)
			log.debug("excelDatas.size() = " + excelDatas.size());
		for (final List<String> s : excelDatas) {
			if(isDebug)
				log.debug("s.size() = " + s.size());
		}
		String excelPath = "/00000001" + today + ".xls";
		Workbook workbook;
		Sheet sheet;
		final String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
		// 建立文件物件
		if (fileType.equals("xls")) {
			// 如果是.xls,就new HSSFWorkbook()
			workbook = new HSSFWorkbook();
		} else {
			throw new Exception("The file type is not 'xls'!!");
		}
		sheet = workbook.createSheet();
		if (excelDatas != null) {
			final int rowNumber = excelDatas.size() + 1;
			final Row row1 = sheet.createRow(0);

			// 將內容寫入指定的行號中
			for (int i = 0; i < rowNumber; i++) {
				final Row row = sheet.createRow(i);
				if (i == 0) {
					// 將excel抬頭加入excel
					for (int j = 0, len = excelTitle.size(); j < len; j++) {
						final Cell cell = row1.createCell(j);
						cell.setCellValue(excelTitle.get(j));
						sheet.autoSizeColumn(j);
					}
				} else {
					// 將excel資料加入excel
					for (int j = 0, len = excelDatas.get(i - 1).size(); j < len; j++) {
						final Cell cell = row.createCell(j);
						cell.setCellValue(excelDatas.get(i - 1).get(j));
					}
				}
			}
			final File file = new File(excelPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			final OutputStream stream = new FileOutputStream(file);
			in = new FileInputStream(file);
			workbook.write(stream);
			stream.close();
		} else {
		}
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
				if(isDebug)
					log.debug("連線失敗");
				ftpClient.disconnect();
				return null;
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return ftpClient;
		} catch (final SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String stringChange(String data) {
		return data == null ? "" : data;
	}

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
					flag = ftpClient.storeFile(new String(ftpFileName.getBytes(encoding), "iso-8859-1"), in);
					if (flag) {
						if(isDebug)
							log.debug("Upload File Success!");
						// Ciny會建立一個Service去回壓狀態，可以加在這裡。
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			in.close();
			ftpClient.logout();
			if(consignmentModels != null){
				for(ConsignmentModel cm : consignmentModels){
					cm.setStatus(ConsignmentStatus.READY_FOR_SHIPPING);
					modelService.save(cm);
				}
			}

		} catch (final SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		FTPClient ftpClient = connectFTP();
		String homeDeliveryBackPath = configurationService.getConfiguration().getString("homeDeliveryBackFolder", "");
		String homeDeliveryBackupPath = configurationService.getConfiguration()
				.getString("homeDeliveryBackBackupFolder", "");
		String frightBackPath = configurationService.getConfiguration().getString("frightBackFolder", "");
		String frightBackupPath = configurationService.getConfiguration().getString("frightBackBackupFolder", "");
		try {
			ftpClient.changeWorkingDirectory(homeDeliveryBackPath.substring(0, homeDeliveryBackPath.length()));
			FTPFile[] files = ftpClient.listFiles();
			if (files != null && files.length > 0) {
				for (FTPFile ff : files) {
					String name = ff.getName();
					File file = new File("/" + name);
					OutputStream os = new FileOutputStream(file);
					ftpClient.retrieveFile(name, os);
					if(file.exists()){
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
					if(file.exists()){
						readTXT(file);
					}
					os.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readTXT(File file) {
		List<String> txtDatas = new ArrayList<>();
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			while (reader.ready()) {
				txtDatas.add(reader.readLine());
			}
			analysisString(txtDatas);
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void analysisString(List<String> datas){
		if(datas != null && datas.size() > 0){
			for(int i = 1, len = datas.size(); i < len; i++){
				String[] dataStrings = datas.get(i).replaceAll(" ", "").split(",");
				if(dataStrings != null && dataStrings.length > 0){
					// OrderNo
					String orderNo = dataStrings[0].substring(4);
					ConsignmentModel consignmentModel = getConsignmentDataService.getConsignmentDataByOrderCode(orderNo) != null ? getConsignmentDataService.getConsignmentDataByOrderCode(orderNo).get(0) : null;
					if(consignmentModel != null){
						// ShipStatus
						String shipSataus = dataStrings[1];
						if(shipSataus != null && shipSataus.trim().length() > 0){
							// Shipping Number
							String shipping_number = dataStrings[2];
							// Arrival Data
							String arrival_Data = dataStrings[3];
							switch (shipSataus) {
							case "1":
								consignmentModel.setStatus(ConsignmentStatus.SHIPPED);
								consignmentModel.setShipping_number(shipping_number);
								break;
							case "2":
								consignmentModel.setStatus(ConsignmentStatus.DELIVERY_COMPLETED);
								consignmentModel.setShipping_number(shipping_number);
								consignmentModel.setArrivalDate(arrival_Data);
								break;
							case "3":
								break;
							default:
								modelService.save(consignmentModel);
							}
							
						}
						
					}
				}
				
			}
		}
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public GetConsignmentDataService getGetConsignmentDataService() {
		return getConsignmentDataService;
	}

	public void setGetConsignmentDataService(GetConsignmentDataService getConsignmentDataService) {
		this.getConsignmentDataService = getConsignmentDataService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}
