package com.webrdaniel.collectmydata.Lists;

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

import com.webrdaniel.collectmydata.Activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.Activities.MainActivity;
import com.webrdaniel.collectmydata.Models.DataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;
import com.webrdaniel.collectmydata.Utils.DialogUtils;
import com.webrdaniel.collectmydata.Utils.KeyboardUtils;

import java.util.Date;
import java.util.concurrent.Callable;


class DataCollViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    TextView mDataCollname;
    ImageView mIcon;
    ImageButton mIbDialog;
    private ImageButton mIbmenu;

    private DataCollItem item;
    private MainActivity activity;

    DataCollViewHolder(View v, Context context){
        super(v);
        mDataCollname = v.findViewById(R.id.tv_card_name_data_coll);
        mIcon = v.findViewById(R.id.iv_card_data_coll);
        mIbDialog = v.findViewById(R.id.ib_card_data_coll_add);
        mIbmenu = v.findViewById(R.id.ib_card_data_coll_menu);
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
                item = activity.mDataCollItemsArrayList.get(DataCollViewHolder.this.getAdapterPosition());
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
        item = activity.mDataCollItemsArrayList.get(DataCollViewHolder.this.getAdapterPosition());
        Intent i = new Intent(activity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, item);
        activity.startActivity(i);
    }

    private void showPopupMenu() {
        item = activity.mDataCollItemsArrayList.get(DataCollViewHolder.this.getAdapterPosition());
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
        DialogUtils.getDialog(dialog,activity, methodDeleteDataColl,R.string.delete);
    }

    private void showDialogRename() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_data_coll_rename, (ViewGroup) activity.getWindow().getDecorView().getRootView(),false);
        final TextInputEditText et_name = dialog.findViewById(R.id.tiet_rename_data_coll);
        et_name.setText(item.getName());

        Callable methodRenameDataColl = new Callable() {
            @Override
            public Object call() throws Exception {
                renameDataColl(et_name.getText()+"");
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.getDialog(dialog,activity, methodRenameDataColl,R.string.rename);
        String name = activity.mDataCollItemsArrayList.get(DataCollViewHolder.this.getAdapterPosition()).getName();
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, et_name, name);
        et_name.requestFocus();
        et_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        DialogUtils.onEnterConfirm(et_name,alertDialog);
        KeyboardUtils.showKeyboard(activity);
    }

    private void showDialogNewRecord() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_data_coll_value, (ViewGroup) activity.getWindow().getDecorView().getRootView(),false);
        final TextInputEditText etValue = dialog.findViewById(R.id.tiet_data_coll_edii_value);
        final TextView tvDate = dialog.findViewById(R.id.tv_data_coll_date);
        tvDate.setText(DateUtils.dateToString(null, DateUtils.DATE_FORMAT_EDMM));

        Callable methodStoreValue = new Callable() {
            @Override
            public Object call() throws Exception {
                storeRecord(Double.parseDouble(etValue.getText().toString()));
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.getDialog(dialog,activity, methodStoreValue,R.string.add);
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, etValue, null);
        etValue.requestFocus();
        DialogUtils.onEnterConfirm(etValue,alertDialog);
        KeyboardUtils.showKeyboard(activity);
    }

    private void storeRecord(Double value) {
        Date date = DateUtils.dateToDateFormat(DateUtils.DATE_FORMAT_DMY);
        if(activity.mDatabaseHelper.getDates(item.getId()).contains(date)) {
            mIbDialog.setVisibility(View.GONE);
        }
        else{
            mIbDialog.setVisibility(View.VISIBLE);
        }
        activity.adapter.notifyItemChanged(DataCollViewHolder.this.getAdapterPosition());
        item = activity.mDataCollItemsArrayList.get(DataCollViewHolder.this.getAdapterPosition());
        activity.mDatabaseHelper.insertDataValue(item.getId(),
                value,
                DateUtils.dateToString(null, DateUtils.DATE_FORMAT_DMY));
    }

    private void renameDataColl(String name) {
        activity.mDatabaseHelper.renameDataColl(item.getId(),name);
        item.setName(name);
        activity.adapter.notifyItemChanged(DataCollViewHolder.this.getAdapterPosition());
    }

    private void deleteDataColl() {
        activity.mDatabaseHelper.deleteDataColl(item.getId());
        activity.mDataCollItemsArrayList.remove(item);
        activity.messageIfEmpty();
        activity.adapter.notifyItemRemoved(DataCollViewHolder.this.getAdapterPosition());
    }

}
