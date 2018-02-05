package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
                showDialogNewRecord();
            }
        });
        mIbmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
    }

    private void startDataCollDetailActivity() {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        Intent i = new Intent(activity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, item);
        activity.startActivity(i);
    }

    private void showPopupMenu() {
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
        Utils.getDialog(dialog,activity, methodDeleteDataColl,R.string.delete);
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
        AlertDialog alertDialog = Utils.getDialog(dialog,activity, methodRenameDataColl,R.string.rename);
        String name = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition()).getName();
        Utils.lockPositiveButtonOnEmptyText(alertDialog, et_name, name);
        et_name.requestFocus();
        et_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Utils.onEnterConfirm(et_name,alertDialog);
        Utils.showKeyboard(activity);
    }

    private void showDialogNewRecord() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_value, null);
        final TextInputEditText etValue = dialog.findViewById(R.id.ti_et);
        final TextView tvDate = dialog.findViewById(R.id.tv_date);
        tvDate.setText(Utils.dateToString(null, Utils.DATE_FORMAT_DM));

        Callable methodStoreValue = new Callable() {
            @Override
            public Object call() throws Exception {
                storeRecord(Double.parseDouble(etValue.getText().toString()));
                return null;
            }
        };
        AlertDialog alertDialog = Utils.getDialog(dialog,activity, methodStoreValue,R.string.add);
        Utils.lockPositiveButtonOnEmptyText(alertDialog, etValue, null);
        etValue.requestFocus();
        Utils.onEnterConfirm(etValue,alertDialog);
        Utils.showKeyboard(activity);
    }

    private void storeRecord(Double value) {
        item = activity.mDataCollItemsArrayList.get(MainViewHolder.this.getAdapterPosition());
        activity.mDatabaseHelper.insertDataValue(item.getId(),
                value,
                Utils.dateToString(null, Utils.DATE_FORMAT_DMY));
    }

    private void renameDataColl(String name) {
        activity.mDatabaseHelper.renameDataColl(item.getId(),name);
        item.setName(name);
        activity.adapter.notifyItemChanged(MainViewHolder.this.getAdapterPosition());
    }

    private void deleteDataColl() {
        activity.mDatabaseHelper.deleteDataColl(item.getId());
        activity.mDataCollItemsArrayList.remove(item);
        activity.messageIfEmpty();
        activity.adapter.notifyItemRemoved(MainViewHolder.this.getAdapterPosition());
    }

}
