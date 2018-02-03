package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;




public class MainViewHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView mDataCollname;
    ImageView mIcon;
    ImageButton mIbDialog;

    DataCollItem item;
    MainActivity activity;

    public MainViewHolder(View v,Context context){
        super(v);
        mDataCollname = v.findViewById(R.id.tv_name);
        mIcon = v.findViewById(R.id.iv_icon);
        mIbDialog = v.findViewById(R.id.ib_dialog);
        activity = (MainActivity) context;
        mView = v;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDataCollDetailActivity();
            }
        });
        mIbDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    public void lockButton(AlertDialog alertDialogAndroid, TextView tv)
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
                }
        });
    }

    public void createDialog()
    {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(dialog);

        final EditText etValue = dialog.findViewById(R.id.et_value);
        final TextView tvDate = dialog.findViewById(R.id.tv_date);
        etValue.requestFocus();
        tvDate.setText(Utils.dateToString(null, Utils.DATE_FORMAT_DAY));

        alertDialogBuilderUserInput
        .setCancelable(false)
        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            storeValue(etValue);
                        }
                        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            dialogBox.cancel();
                        }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        enterListener(etValue,alertDialogAndroid);
        alertDialogAndroid.show();
        lockButton(alertDialogAndroid, etValue);
        Utils.showKeyboard(activity);
    }

    public void startDataCollDetailActivity()
    {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        Intent i = new Intent(activity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, item);
        activity.startActivity(i);
    }

    public void enterListener(final EditText editText,final AlertDialog alertDialogAndroid)
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

    public void storeValue(EditText editText)
    {
        activity.mDatabaseHelper.insertDataValue(item.getId(),
        Integer.parseInt(editText.getText().toString()),
        Utils.dateToString(null, Utils.DATE_FORMAT_RAW));
    }


}
