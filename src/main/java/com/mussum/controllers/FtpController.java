package com.mussum.controllers;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpController {

    private FTPClient ftp = new FTPClient();

    public String[] getFilesNames(String dir) {
	try {
	    return this.ftp.listNames();
	} catch (IOException e) {
	    return null;
	}
    }

    public void uploadFile(InputStream input, String dir, String fileName) throws Exception {
	ftp.makeDirectory(dir);
	this.ftp.storeFile(dir + fileName, input);
	input.close();
	System.out.println("salvo.");
    }

    public FtpController() {
	try {
	    this.ftp.connect("localhost");
	    boolean loginSucess = this.ftp.login("mussum", "pass");

	    if (loginSucess) {

		System.out.println("FTP Connected.");
		//Set File Type "Specially for PDF's"
		this.ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
		this.ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

	    }
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    public void disconnect() {
	if (this.ftp.isConnected()) {
	    try {
		this.ftp.logout();
		this.ftp.disconnect();
	    } catch (IOException f) {
		// do nothing as file is already saved to server
	    }
	}
    }

}
