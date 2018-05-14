package com.webrdaniel.collectmydata.lists;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webrdaniel.collectmydata.activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.fragments.RecordsListFragment;
import com.webrdaniel.collectmydata.models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.utils.DialogUtils;
import com.webrdaniel.collectmydata.utils.KeyboardUtils;
import com.webrdaniel.collectmydata.utils.Utils;

import java.util.concurrent.Callable;

class RecordViewHolder extends RecyclerView.ViewHolder {
    private RecordsListFragment mRecordsListFragment;
    private DataCollDetailActivity dataCollDetailActivity;
    TextView mValueDate;
    TextView mValue;
    private Record record;

    RecordViewHolder(RecordsListFragment RecordsListFragment, View view) {
        super(view);
        this.mRecordsListFragment = RecordsListFragment;
        dataCollDetailActivity = mRecordsListFragment.dataCollDetailActivity;
        mValue = view.findViewById(R.id.tv_record_value);
        mValueDate = view.findViewById(R.id.tv_record_date);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEditValue();
            }
        });
    }

    private void showDialogEditValue() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(dataCollDetailActivity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_record_value_edit, (ViewGroup) mRecordsListFragment.mRootView, false);
        final TextInputEditText valueTiet = dialog.findViewById(R.id.tiet_record_value_edit);
        record = dataCollDetailActivity.records.get(RecordViewHolder.this.getAdapterPosition() - 1);
        valueTiet.setText(Utils.doubleToString(record.getValue()).replaceFirst(",","."));
        Callable methodEditValue = new Callable() {
            @Override
            public Object call() throws Exception {
                editValue(Double.parseDouble(valueTiet.getText().toString()));
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.showDialog(dialog, dataCollDetailActivity, methodEditValue, R.string.edit);
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, valueTiet, Utils.doubleToString(record.getValue()));
        valueTiet.requestFocus();
        DialogUtils.onEnterConfirm(valueTiet, alertDialog);
        KeyboardUtils.showKeyboard(mRecordsListFragment.dataCollDetailActivity);
    }

    private void editValue(Double value) {
        int position = RecordViewHolder.this.getAdapterPosition();
        dataCollDetailActivity.databaseHelper.editValue(record.getId(), value);
        record.setValue(value);
        mRecordsListFragment.recordsListAdapter.notifyItemChanged(position);
        dataCollDetailActivity.recordsOverviewFragment.updateData();
        dataCollDetailActivity.recordsOverviewFragment.updateLayout();

    }
}
