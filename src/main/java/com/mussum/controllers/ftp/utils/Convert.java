/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.ftp.utils;

import com.mussum.util.S;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.springframework.util.StreamUtils;

/**
 *
 * @author Yuri
 */
public class Convert {

    public static String inputStreamToBASE64(InputStream inputstream) {
        try {
            return Base64.getEncoder().encodeToString(StreamUtils.copyToByteArray(inputstream));
        } catch (IOException ex) {
            S.out("ERRO: ", Convert.class);
            return null;
        }
    }

}
