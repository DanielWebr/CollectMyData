package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

/**
 * Created by dandu on 31.01.2018.
 */

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
}
