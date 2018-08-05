package com.mussum.controllers.ftp.utils;

import com.mussum.models.ftp.Arquivo;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPcontrol {

    private final String host = "localhost";
    private final String user = "mussum";
    private final String pass = "pass";

    private final FTPClient ftp = new FTPClient();

    public String[] listFiles(String dir) {
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
            S.out("ERRO: listFiles " + e.getMessage(), this);
            return null;
        }
    }

    public String uploadFile(InputStream input, Arquivo arquivo, boolean override) throws Exception {
        //ftp.makeDirectory(arquivo.getDir());
        makeDirectoryOneByOne(arquivo.getDir()); //new mkdir for linux
        if (!override) {
            uniqueName(arquivo, listFiles(arquivo.getDir()));
        }

        this.ftp.storeFile(arquivo.getDir() + "/" + arquivo.getNome(), input);
        input.close();
        return arquivo.getNome();
    }

    private void uniqueName(Arquivo arquivo, String[] list) {
        if (Arrays.asList(list).contains(arquivo.getNome())) {
            arquivo.setNome(renameCopy(arquivo.getNome()));
            uniqueName(arquivo, list);
        }
    }

    private String renameCopy(String str) {
        try {
            String extension = str.substring(str.lastIndexOf("."));
            String name = str.replace(extension, "");
            String inte = name.substring(name.lastIndexOf(" ") + 1);
            int copyNum = Integer.parseInt(inte);
            copyNum++;
            name = name.replace(inte, String.valueOf(copyNum));
            name += extension;
            return name;
        } catch (NumberFormatException e) {
            S.out(e.getMessage(), this);
            String extension = str.substring(str.lastIndexOf("."));
            str = str.replace(extension, "");
            str = str + " 2" + extension;
            return str;
        }
    }

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
                S.out(" ... trying to connect again in 0.1seg...", this);
                try {
                    Thread.sleep(100);
                    connect();
                } catch (InterruptedException ex) {
                }
            }
        } catch (IOException e) {
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
        try (InputStream inputStream = ftp.retrieveFileStream(filePath)) {
            if (inputStream == null) {
                return false;
            }
        }
        return true;
    }

    private void makeDirectoryOneByOne(String dir) {
        try {
            String[] folders = dir.split("/");
            for (int i = 0; i < folders.length; i++) {
                boolean foi = this.ftp.makeDirectory(folders[i]);
                S.out("created path... : " + folders[i] + " : ... " + foi, this);
                boolean foi2 = this.ftp.changeWorkingDirectory(folders[i]);
                S.out("cd to... : " + folders[i] + " : ... " + foi2, this);
            }
            this.ftp.changeWorkingDirectory("/");
        } catch (Exception e) {
            try {
                boolean foi = this.ftp.makeDirectory(dir);
                S.out("created path... " + dir + " : ... " + foi, this);
            } catch (Exception ex) {
            }
        }
    }

}
