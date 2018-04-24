package com.mussum.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpController {

    public static void main(String args[]) {
	try {
	    //Create a FTP Client
	    FTPClient ftp = new FTPClient();

	    //Set Host Information
	    ftp.connect("localhost");
	    boolean login = ftp.login("mussum", "pass");

	    if (login) {
		
		//get File from the FTP location
		System.out.println(ftp.printWorkingDirectory());
		String fileNames[] = ftp.listNames();
		List<String> fTPFileNameList = new ArrayList();

		//Iterate to get File Names
		if (fileNames != null && fileNames.length > 0) {
		    for (String fName : fileNames) {
			fTPFileNameList.add((new File(fName)).getName());
		    }
		}
		
		for (int i = 0; i < fTPFileNameList.size(); i++) {
		    System.out.println(fTPFileNameList.get(i));
		}

	    }

	    //Set File Type "Specially for PDF's"
	    ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
	    ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

	    //Change the current Directory to a desired one
	    //ftp.changeWorkingDirectory(SOME_DIRECTORY);
	    // Check the file is present on FTP
//	    if (fileNames != null && fileNames.length > 0 && fTPFileNameList.contains(FILE_NAME)) {
//		//File is present on FTP
//	    } else {
//		//File is not present on FTP
//		String httpURL = "YourDesiredURL";
//		InputStream fis;
//		try {
//		    fis = new URL(httpURL).openStream();
//		} catch (FileNotFoundException e) {
//		    //File is not present on the URL
//		}
//		ftp.storeFile(FILE_NAME, fis);
//		fis.close();
//	    }
	    ftp.logout();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
