/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.ftp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

public class TxtWritter {

    private String lastCreatedTmpFile = "";

    public InputStream getInputStreamOfNewTxtFile(String username, String fileName, String content) {
	try {
	    //Whatever the file path is.
	    this.lastCreatedTmpFile = username + "@@" + fileName + ".txt";
	    File txtDir = new File("tmp/");
	    txtDir.mkdir();
	    File txtFile = new File("tmp/" + lastCreatedTmpFile);
	    if (!txtFile.exists()) {
		txtFile.createNewFile();
	    }
	    FileOutputStream is = new FileOutputStream(txtFile);
	    OutputStreamWriter osw = new OutputStreamWriter(is);
	    try (Writer w = new BufferedWriter(osw)) {
		w.write(content);
		w.flush();
		w.close();
	    }
	    InputStream fis = new FileInputStream(txtFile);
	    //deleteLastCreatedFile();
	    return fis;
	} catch (IOException e) {
	    S.out("ERRO: " + e.getMessage(), e);
	    return null;
	}
    }

    public void deleteLastCreatedFile() {
	File tmpFile = new File("tmp/" + lastCreatedTmpFile);
	try {
	    Files.delete(tmpFile.toPath());
	    S.out("tmp File deleted.", this);
	} catch (IOException ex) {
	    S.out("ERRO: tmp File not deleted.", this);
	}
    }

}
