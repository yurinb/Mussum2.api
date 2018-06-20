/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.ftp.utils;

/**
 *
 * @author Yuri
 */
public class Strings {

    public static String getExtensionIfExists(String fileName) {
        int indexOfExtension = fileName.lastIndexOf(".");
        boolean extensionExists = indexOfExtension != -1;

        if (extensionExists) {
            return fileName.substring(indexOfExtension);
        }

        return null;
    }

}
