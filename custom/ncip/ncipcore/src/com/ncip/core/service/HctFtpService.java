package com.ncip.core.service;

import java.io.File;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface HctFtpService {
	
	public void runService(String sendType);
	
	FTPClient connectFTP();
	
	void createExcel(List<ConsignmentModel> consignmentModels);
	
	void sendFileToFTP(List<ConsignmentModel> consignmentModels);
	
	void getFileFromFTP();
	
	void readTXT(File file);

}
