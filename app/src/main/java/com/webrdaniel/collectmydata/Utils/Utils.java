package com.webrdaniel.collectmydata.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;


public class Utils {
    public static int getMatColor(Context context) {
        int arrayId = context.getResources().getIdentifier("mdcolor", "array", context.getApplicationContext().getPackageName());
        TypedArray colors = context.getResources().obtainTypedArray(arrayId);
        int index = (int) (Math.random() * colors.length());
        int returnColor = colors.getColor(index, Color.BLACK);
        colors.recycle();
        return returnColor;
    }
}
