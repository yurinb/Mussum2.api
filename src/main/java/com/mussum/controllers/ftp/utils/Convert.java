/*
 * 
 */
package com.mussum.controllers.ftp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.springframework.util.StreamUtils;

/**
 *
 * @author yurib
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
