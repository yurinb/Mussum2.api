package com.mussum.controllers.ftp;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpController {

    private String host = "localhost";
    private String user = "mussum";
    private String pass = "pass";

    private FTPClient ftp = new FTPClient();

    public String[] getContentFrom(String dir) {
        try {
            return this.ftp.listNames(dir);
        } catch (IOException e) {
            return null;
        }
    }

    public void uploadFile(InputStream input, String dir, String fileName) throws Exception {
        ftp.makeDirectory(dir);
        this.ftp.storeFile(dir + fileName, input);
        input.close();
    }

    public FtpController() {
        try {
            this.ftp.connect(host);
            boolean loginSucess = this.ftp.login(user, pass);

            if (loginSucess) {

                System.out.println("FTP Connected.");
                //Set File Type "Specially for PDF's"
                this.ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                this.ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            } else {
                System.out.println("ERROR: FTP.LOGIN");
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

    public InputStream getFile(String dir, String file) throws IOException {
        return ftp.retrieveFileStream(dir + file);
    }

    public FTPClient getFtp() {
        return ftp;
    }

}
