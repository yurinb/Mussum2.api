/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.util;

import java.time.LocalDateTime;

/**
 *
 * @author Yuri
 */
public class S {

    public static void out(String txt, Object emitter) {
        String espaco = "                                       ";
        String emm = emitter.getClass().getSimpleName();
        //String method = emitter.getClass().getEnclosingMethod().toGenericString();

        //int metLen = method.length();
        int emmLen = emm.length();
        //espaco = emm + "_-_" + method + espaco.substring(emmLen + metLen + 3);

        LocalDateTime time = LocalDateTime.now();
        int day = time.getDayOfMonth();
        int hour = time.getHour();
        int min = time.getMinute();
        int seg = time.getSecond();
        String timeLog = "D:" + day + "." + hour + "h." + min + "m." + seg + "s.";

        int timLen = timeLog.length();

        espaco = timeLog + "_-_" + emm + espaco.substring(timLen + 3 + emmLen);

        System.out.println(espaco + " : " + txt);
    }

}
