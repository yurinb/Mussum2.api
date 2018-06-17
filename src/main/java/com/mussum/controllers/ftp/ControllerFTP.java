package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.util.S;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class ControllerFTP {

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

    public void uploadFile(InputStream input, Arquivo arquivo) throws Exception {
        ftp.makeDirectory(arquivo.getDir());
        this.ftp.storeFile(arquivo.getDir() + "/" + arquivo.getNome(), input);
        input.close();
    }

//    public ControllerFTP() {
//        connect();
//    }
    public void connect() {
        try {
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
                S.out(e.getMessage(), this);
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
        int returnCode = ftp.getReplyCode();
        if (returnCode == 550) {
            return false;
        }
        return true;
    }

    boolean checkFileExists(String filePath) throws IOException {
        InputStream inputStream = ftp.retrieveFileStream(filePath);
        int returnCode = ftp.getReplyCode();
        if (inputStream == null || returnCode == 550) {
            return false;
        }
        return true;
    }

}
