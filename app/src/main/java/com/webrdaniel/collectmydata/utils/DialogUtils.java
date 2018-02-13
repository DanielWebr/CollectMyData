package com.webrdaniel.collectmydata.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.webrdaniel.collectmydata.R;

import java.util.concurrent.Callable;

public class DialogUtils {
    public static AlertDialog showDialog(View dialog, Context context, final Callable method, int positiveButtonText){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialog);
        alertDialogBuilder
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        try{
                            method.call();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilder.create();
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

    public static void lockPositiveButtonOnEmptyText(AlertDialog alertDialogAndroid, final TextInputEditText tv, final String previousContent) {
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
                if(previousContent!=null){
                    if (s.toString().equals(previousContent)|| TextUtils.isEmpty(s)) {
                        tv.setTextColor(Color.GRAY);
                        positiveButton.setEnabled(false);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        positiveButton.setEnabled(true);
                    }
                }
                else {
                    if (TextUtils.isEmpty(s)) {
                        positiveButton.setEnabled(false);
                    } else {
                        positiveButton.setEnabled(true);
                    }
                }
            }
        });
    }

    public static void onEnterConfirm(final TextInputEditText editText, final AlertDialog alertDialogAndroid) {
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
