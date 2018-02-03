package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Callable;


public class MainViewHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView mDataCollname;
    ImageView mIcon;
    ImageButton mIbDialog, mIbmenu;

    DataCollItem item;
    MainActivity activity;

    public MainViewHolder(View v,Context context){
        super(v);
        mDataCollname = v.findViewById(R.id.tv_name);
        mIcon = v.findViewById(R.id.iv_icon);
        mIbDialog = v.findViewById(R.id.ib_dialog);
        mIbmenu = v.findViewById(R.id.ib_menu);
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
                item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
                showDialogSetValue();
            }
        });
        mIbmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
    }

    public void lockButton(AlertDialog alertDialogAndroid,final TextInputEditText tv,final String forbiddenText)
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
                if (s.toString().equals(forbiddenText)) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                }
        });
    }

    public void showDialogSetValue()
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_value, null);
        final TextInputEditText etValue = dialog.findViewById(R.id.ti_et);
        final TextView tvDate = dialog.findViewById(R.id.tv_date);
        tvDate.setText(Utils.dateToString(null, Utils.DATE_FORMAT_DAY));

        Callable method = new Callable() {
            @Override
            public Object call() throws Exception {
                storeValue(etValue);
                return null;
            }
        };
        setDialog(dialog,etValue,method,R.string.add);
    }

    public void startDataCollDetailActivity()
    {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        Intent i = new Intent(activity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, item);
        activity.startActivity(i);
    }

    public void enterListener(final TextInputEditText editText,final AlertDialog alertDialogAndroid)
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

    public void storeValue(TextInputEditText editText)
    {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        activity.mDatabaseHelper.insertDataValue(item.getId(),
                Integer.parseInt(editText.getText().toString()),
                Utils.dateToString(null, Utils.DATE_FORMAT_RAW));
    }

    public void renameDataColl(String name)
    {
        activity.mDatabaseHelper.renameDataColl(item.getId(),name);
        item.setName(name);
        activity.adapter.notifyDataSetChanged();
    }

    public void deleteDataColl()
    {
        activity.mDatabaseHelper.deleteDataColl(item.getId());
        activity.mDataCollItemsArrayList.remove(item);
        activity.adapter.notifyDataSetChanged();
    }

    public void showPopupMenu()
    {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        PopupMenu popup = new PopupMenu(activity,mIbmenu );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_main_card_rename:
                        showDialogRename();
                        return true;
                    case R.id.menu_main_card_delete:
                        showDialogDelete();
                        return true;
                    default: return false;
                }
            }
        });
        popup.show();
    }

    private void showDialogDelete() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_delete, null);
        Callable methodDeleteDataColl = new Callable() {
            @Override
            public Object call() throws Exception {
                deleteDataColl();
                return null;
            }
        };
        setDialog(dialog,null, methodDeleteDataColl,R.string.delete);
    }

    private void showDialogRename() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_rename, null);
        final TextInputEditText et_name = dialog.findViewById(R.id.ti_et);
        et_name.setText(item.getName());

        Callable methodRenameDataColl = new Callable() {
            @Override
            public Object call() throws Exception {
                renameDataColl(et_name.getText()+"");
                return null;
            }
        };
        setDialog(dialog,et_name, methodRenameDataColl,R.string.rename);

    }
    private void setDialog(View dialog, TextInputEditText editText, final Callable method, int positiveButtonText)
    {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
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
        if(editText!=null)
        {
            String name = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition()).getName();
            lockButton(alertDialogAndroid, editText, name);
            editText.requestFocus();
            enterListener(editText,alertDialogAndroid);
            Utils.showKeyboard(activity);
        }
    }

}
