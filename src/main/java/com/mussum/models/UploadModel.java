/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.models;

import org.springframework.web.multipart.MultipartFile;

public class UploadModel {

    private String dir;

    private MultipartFile[] files;

    public String getDir() {
        return dir;
    }

    public void setDir(String extraField) {
        this.dir = extraField;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

}
