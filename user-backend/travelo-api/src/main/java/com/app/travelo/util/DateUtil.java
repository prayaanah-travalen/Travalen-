package com.app.travelo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public static Date stringToDate(String dte) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dte);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
