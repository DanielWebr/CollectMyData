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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.Callable;


class MainActivityViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    TextView mDataCollname;
    ImageView mIcon;
    ImageButton mIbDialog;
    private ImageButton mIbmenu;

    private DataCollItem item;
    private MainActivity activity;

    MainActivityViewHolder(View v, Context context){
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
                item = activity.mDataCollItemsArrayList.get(MainActivityViewHolder.this.getAdapterPosition());
                mIbDialog.setEnabled(false);
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
        item = activity.mDataCollItemsArrayList.get(MainActivityViewHolder.this.getAdapterPosition());
        Intent i = new Intent(activity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, item);
        activity.startActivity(i);
    }

    private void showPopupMenu() {
        item = activity.mDataCollItemsArrayList.get(MainActivityViewHolder.this.getAdapterPosition());
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
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_delete,(ViewGroup) activity.getWindow().getDecorView().getRootView(),false);
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
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_rename_data_coll, (ViewGroup) activity.getWindow().getDecorView().getRootView(),false);
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
        String name = activity.mDataCollItemsArrayList.get(MainActivityViewHolder.this.getAdapterPosition()).getName();
        Utils.lockPositiveButtonOnEmptyText(alertDialog, et_name, name);
        et_name.requestFocus();
        et_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Utils.onEnterConfirm(et_name,alertDialog);
        Utils.showKeyboard(activity);
    }

    private void showDialogNewRecord() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_record_value, (ViewGroup) activity.getWindow().getDecorView().getRootView(),false);
        final TextInputEditText etValue = dialog.findViewById(R.id.ti_et);
        final TextView tvDate = dialog.findViewById(R.id.tv_date);
        tvDate.setText(Utils.dateToString(null, Utils.DATE_FORMAT_EDMM));

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
        Date date = Utils.dateToDateFormat(Utils.DATE_FORMAT_DMY);
        if(activity.mDatabaseHelper.getDates(item.getId()).contains(date)) {
            mIbDialog.setEnabled(false);
        }
        else{
            mIbDialog.setEnabled(true);
        }
        activity.adapter.notifyItemChanged(MainActivityViewHolder.this.getAdapterPosition());
        item = activity.mDataCollItemsArrayList.get(MainActivityViewHolder.this.getAdapterPosition());
        activity.mDatabaseHelper.insertDataValue(item.getId(),
                value,
                Utils.dateToString(null, Utils.DATE_FORMAT_DMY));
    }

    private void renameDataColl(String name) {
        activity.mDatabaseHelper.renameDataColl(item.getId(),name);
        item.setName(name);
        activity.adapter.notifyItemChanged(MainActivityViewHolder.this.getAdapterPosition());
    }

    private void deleteDataColl() {
        activity.mDatabaseHelper.deleteDataColl(item.getId());
        activity.mDataCollItemsArrayList.remove(item);
        activity.messageIfEmpty();
        activity.adapter.notifyItemRemoved(MainActivityViewHolder.this.getAdapterPosition());
    }

}
