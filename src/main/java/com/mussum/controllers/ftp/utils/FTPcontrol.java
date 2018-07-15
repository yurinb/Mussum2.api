package com.mussum.controllers.ftp.utils;

import com.mussum.models.ftp.Arquivo;
import com.mussum.util.S;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPcontrol {

    private final String host = "localhost";
    private final String user = "mussum";
    private final String pass = "pass";

    private final FTPClient ftp = new FTPClient();

    public String[] getContentFrom(String dir) {
	try {
	    String foundFiles = "";
	    FTPFile[] files = this.ftp.listFiles(dir);
	    for (FTPFile file : files) {
		if (file.isFile()) {
		    foundFiles += file.getName() + ",";
		}
	    }
	    return foundFiles.split(",");
	} catch (IOException e) {
	    return null;
	}
    }

    public void uploadFile(InputStream input, Arquivo arquivo) throws Exception {
	ftp.makeDirectory(arquivo.getDir());
	this.ftp.storeFile(arquivo.getDir() + "/" + arquivo.getNome(), input);
	input.close();
    }

//    public FTPcontrol() {
//        connect();
//    }
    public void connect() {
	try {
	    this.ftp.setControlEncoding("UTF-8");
	    this.ftp.connect(host);
	    boolean loginSucess = this.ftp.login(user, pass);

	    if (loginSucess) {

		//System.out.println("FTP Connected.");
		//Set File Type "Specially for PDF's"
		this.ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
		this.ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

	    } else {
		S.out("ERROR: FTP.LOGIN", this);
	    }
	} catch (IOException e) {
	    try {
		S.out(e.getMessage() + " ... trying to connect again in 1seg...", this);
		Thread.sleep(1000);
	    } catch (InterruptedException ex) {
		Logger.getLogger(FTPcontrol.class.getName()).log(Level.SEVERE, null, ex);
	    }
		connect();
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

    public InputStream getFile(String dir, String file) throws IOException {
	return ftp.retrieveFileStream(dir + file);
    }

    public FTPClient getFtp() {
	return ftp;
    }

    public boolean checkDirectoryExists(String dirPath) throws IOException {
	ftp.changeWorkingDirectory(dirPath);
	return ftp.getReplyCode() != 550;
    }

    public boolean checkFileExists(String filePath) throws IOException {
	InputStream inputStream = ftp.retrieveFileStream(filePath);
	return !(inputStream == null || ftp.getReplyCode() == 550);
    }

}
