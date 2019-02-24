package com.si.sunshine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getFormattedDate(long date) {
        Date parsedDate = new Date(date * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(parsedDate);
    }
}
