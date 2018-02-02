package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Utils {


    public static int getMatColor(Context context)
    {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor", "array", context.getApplicationContext().getPackageName());
        TypedArray colors = context.getResources().obtainTypedArray(arrayId);
        int index = (int) (Math.random() * colors.length());
        returnColor = colors.getColor(index, Color.BLACK);
        colors.recycle();
        return returnColor;
    }

    public static Date stringToDate(String date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.d("Util parse", "stringToDate: nono");
            return new Date();
        }
    }

    public static String dateToString(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
