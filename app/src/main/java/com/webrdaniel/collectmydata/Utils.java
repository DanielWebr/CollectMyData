package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Utils {

    public static final String DATE_FORMAT_RAW ="ss:mm:HH d.M.yyyy";
    public static final String DATE_FORMAT_DAY ="d. M. yyyy";

    public static int getMatColor(Context context)
    {
        int arrayId = context.getResources().getIdentifier("mdcolor", "array", context.getApplicationContext().getPackageName());
        TypedArray colors = context.getResources().obtainTypedArray(arrayId);
        int index = (int) (Math.random() * colors.length());
        int returnColor = colors.getColor(index, Color.BLACK);
        colors.recycle();
        return returnColor;
    }

    public static Date stringToDate(String date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return  sdf.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String dateToString(Date date, String format)
    {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format((date!=null)?date:new Date());
    }

    public static void keyboardOptions(Context activity, View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view == null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        else
        {
            inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}