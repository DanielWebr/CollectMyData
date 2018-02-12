package com.webrdaniel.collectmydata.Lists;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
    TextView mNameTv;
    ImageView mIcon;
    ImageButton mAddRecordIb;
    private ImageButton mMenuIb;
    private DataCollItem mDataCollItem;
    private MainActivity mMainActivity;

    DataCollViewHolder(View view, MainActivity mainActivity){
        super(view);
        mNameTv = view.findViewById(R.id.tv_card_name_data_coll);
        mIcon = view.findViewById(R.id.iv_card_data_coll);
        mAddRecordIb = view.findViewById(R.id.ib_card_data_coll_add);
        mMenuIb = view.findViewById(R.id.ib_card_data_coll_menu);
        mMainActivity = mainActivity;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDataCollDetailActivity();
            }
        });

        mAddRecordIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataCollItem = mMainActivity.dataCollItems.get(DataCollViewHolder.this.getAdapterPosition());
                showDialogNewRecord();
            }
        });
        mMenuIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
    }

    private void startDataCollDetailActivity() {
        mDataCollItem = mMainActivity.dataCollItems.get(DataCollViewHolder.this.getAdapterPosition());
        Intent i = new Intent(mMainActivity, DataCollDetailActivity.class);
        i.putExtra(MainActivity.DATA_COLL_ITEM, mDataCollItem);
        mMainActivity.startActivity(i);
    }

    private void showPopupMenu() {
        mDataCollItem = mMainActivity.dataCollItems.get(DataCollViewHolder.this.getAdapterPosition());
        PopupMenu popup = new PopupMenu(mMainActivity, mMenuIb);
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
        LayoutInflater layoutInflater = LayoutInflater.from(mMainActivity);
        View dialog = layoutInflater.inflate(R.layout.dialog_delete,(ViewGroup) mMainActivity.getWindow().getDecorView().getRootView(),false);
        Callable methodDeleteDataColl = new Callable() {
            @Override
            public Object call() throws Exception {
                deleteDataColl();
                return null;
            }
        };
        DialogUtils.showDialog(dialog, mMainActivity, methodDeleteDataColl,R.string.delete);
    }

    private void showDialogRename() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mMainActivity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_data_coll_rename, (ViewGroup) mMainActivity.getWindow().getDecorView().getRootView(),false);
        final TextInputEditText newDataCollNameTiet = dialog.findViewById(R.id.tiet_rename_data_coll);
        newDataCollNameTiet.setText(mDataCollItem.getName());

        Callable methodRenameDataColl = new Callable() {
            @Override
            public Object call() throws Exception {
                renameDataColl(newDataCollNameTiet.getText()+"");
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.showDialog(dialog, mMainActivity, methodRenameDataColl,R.string.rename);
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, newDataCollNameTiet, mDataCollItem.getName());
        newDataCollNameTiet.requestFocus();
        DialogUtils.onEnterConfirm(newDataCollNameTiet,alertDialog);
        KeyboardUtils.showKeyboard(mMainActivity);
    }

    private void showDialogNewRecord() {
        LayoutInflater layoutInflater = LayoutInflater.from(mMainActivity);
        View dialog = layoutInflater.inflate(R.layout.input_dialog_data_coll_value, (ViewGroup) mMainActivity.getWindow().getDecorView().getRootView(),false);
        final TextInputEditText newRecordValeTiet = dialog.findViewById(R.id.tiet_data_coll_add_record_value);
        final TextView actualDateTv = dialog.findViewById(R.id.tv_data_coll_date);
        actualDateTv.setText(DateUtils.dateToString(null, DateUtils.DATE_FORMAT_EDMM));

        Callable methodStoreValue = new Callable() {
            @Override
            public Object call() throws Exception {
                storeRecord(Double.parseDouble(newRecordValeTiet.getText().toString()));
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.showDialog(dialog, mMainActivity, methodStoreValue,R.string.add);
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, newRecordValeTiet, null);
        newRecordValeTiet.requestFocus();
        DialogUtils.onEnterConfirm(newRecordValeTiet,alertDialog);
        KeyboardUtils.showKeyboard(mMainActivity);
    }

    private void storeRecord(Double value) {
        Date date = DateUtils.dateToDateFormat(DateUtils.DATE_FORMAT_DMY);
        if(mMainActivity.databaseHelper.getDates(mDataCollItem.getId()).contains(date)) {
            mAddRecordIb.setVisibility(View.GONE);
        }
        else{
            mAddRecordIb.setVisibility(View.VISIBLE);
        }
        mMainActivity.dataCollRvAdapter.notifyItemChanged(DataCollViewHolder.this.getAdapterPosition());
        mDataCollItem = mMainActivity.dataCollItems.get(DataCollViewHolder.this.getAdapterPosition());
        mMainActivity.databaseHelper.insertRecord(mDataCollItem.getId(), value, DateUtils.dateToString(null, DateUtils.DATE_FORMAT_DMY));
    }

    private void renameDataColl(String name) {
        mMainActivity.databaseHelper.renameDataColl(mDataCollItem.getId(),name);
        mDataCollItem.setName(name);
        mMainActivity.dataCollRvAdapter.notifyItemChanged(DataCollViewHolder.this.getAdapterPosition());
    }

    private void deleteDataColl() {
        mMainActivity.databaseHelper.deleteDataColl(mDataCollItem.getId());
        mMainActivity.dataCollItems.remove(mDataCollItem);
        mMainActivity.messageIfEmpty();
        mMainActivity.dataCollRvAdapter.notifyItemRemoved(DataCollViewHolder.this.getAdapterPosition());
    }

}
