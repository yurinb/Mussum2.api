/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class TxtWritter {

    public InputStream writeNewTxt(String username, String fileName, String content) {
        try {
            //Whatever the file path is.
            File statText = new File("tmp/" + username + "@@" + fileName + ".txt");
            if (!statText.exists()) {
                statText.createNewFile();
            }
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            try (Writer w = new BufferedWriter(osw)) {
                w.write(content);
                w.flush();
                w.close();
            }
            InputStream fis = new FileInputStream(statText);
            return fis;
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
            System.out.println(e);
            return null;
        }
    }

}
