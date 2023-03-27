package com.dale.health.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtils {

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    public static String getFormatTime(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

}
