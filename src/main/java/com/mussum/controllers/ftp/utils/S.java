/*
 * My LOG class
 */
package com.mussum.controllers.ftp.utils;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * @author yurib
 */
public class S {

    private static final String SEPARATOR = "_-> ";

    // only show up logs of classes in filter array
    // *if array is empty, all logs will show up*
    //private static final String[] CLASS_FILTER = {"LoginController", "TokenFilter"};
    private static final String[] CLASS_FILTER = {};

    public static void out(String txt, Object emitter) {
        String emitterClassLog = emitter.getClass().getSimpleName();

        if (CLASS_FILTER.length > 0 && !Arrays.asList(CLASS_FILTER).contains(emitterClassLog)) {
            return;
        }

        String output = "";

        String threadLog = "Thread::" + String.valueOf(Thread.currentThread().getId());

        output += SEPARATOR + stringWithLength(info_timeLog(), 17);
        output += SEPARATOR + stringWithLength(emitterClassLog, 17);
        output += SEPARATOR + stringWithLength(threadLog, 17);

        System.out.println(output + " :: " + txt);
    }

    private static String info_timeLog() {
        LocalDateTime time = LocalDateTime.now();
        int day = time.getDayOfMonth();
        int hour = time.getHour();
        int min = time.getMinute();
        int seg = time.getSecond();
        return "D:" + day + "." + hour + "h." + min + "m." + seg + "s.";
    }

    private static String buildTxtInsideTxt(String inside, String txt) {
        return inside + txt.substring(inside.length());
    }

    private static String stringWithLength(String info, int length) {
        String backgroundTxt = buildStringRepetingTimes(" ", length);
        boolean fit = info.length() <= length;

        if (fit) {
            return buildTxtInsideTxt(info, backgroundTxt);
        }

        return null;
    }

    private static String buildStringRepetingTimes(String txt, int times) {
        String generatedTxt = "";
        for (int i = 0; i < times; i++) {
            generatedTxt += txt;
        }
        return generatedTxt;
    }

}
