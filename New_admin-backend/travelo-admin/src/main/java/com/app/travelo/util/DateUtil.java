package com.app.travelo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateUtil {

    public static String toDateString(Date dte){
        if(Objects.nonNull(dte)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            return dateFormatter.format(dte).toString();
        }
        return null;
    }

    public static String toDateStringYYYYMMDD(Date dte){
        if(Objects.nonNull(dte)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormatter.format(dte).toString();
        }
        return null;
    }


    public static Date stringToDate(String dte) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(dte);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date stringToDateTime(String dte) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(dte);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
