package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;


public class Utils {

    public static final String DATE_FORMAT_RAW ="HH:mm:ss d.M.yyyy";
    public static final String DATE_FORMAT_DAY_MONTH ="d. M.";
    public static final String DATE_FORMAT_MINUTE_HOUR ="mm:HH";

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

    public static void showKeyboard(Context activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }
    public  static void hideKeyboard(Context activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()) inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }

    public static AlertDialog getDialog(View dialog, Context context, final Callable method, int positiveButtonText)
    {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(dialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        try{
                            method.call();
                        }
                        catch (Exception e) {}
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });
        alertDialogAndroid.show();
        return alertDialogAndroid;

    }

    public static void lockPositiveButtonOnEmptyText(AlertDialog alertDialogAndroid, final TextInputEditText tv, final String previousContent)
    {
        final Button positiveButton = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    positiveButton.setEnabled(false);
                } else {
                    positiveButton.setEnabled(true);
                }
                if (previousContent!=null && s.toString().equals(previousContent)) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public static void OnEnterConfirm(final TextInputEditText editText, final AlertDialog alertDialogAndroid)
    {
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                Button button =  alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)&& button.isEnabled()) {
                    button.performClick();
                    return true;
                }
                return false;
            }
        });

    }

}
