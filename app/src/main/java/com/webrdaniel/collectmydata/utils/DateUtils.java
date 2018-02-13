package com.webrdaniel.collectmydata.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {
    public static final String DATE_FORMAT_DM ="d.M.";
    public static final String DATE_FORMAT_EDMM ="EEE d. MMMM";
    public static final String DATE_FORMAT_EDM ="EEE d. M.";
    public static final String DATE_FORMAT_DMY ="d. M. yyyy";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format((date!=null)?date:new Date());
    }

    public static Date dateToDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
        try {
            return sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return  sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static int getDaysPastSince(int period, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(period,value);
        Date date = new Date();
        long difference = date.getTime() - calendar.getTime().getTime();
        float daysBetween = (difference / (1000*60*60*24));
        return (int) daysBetween+1;
    }
}
