package com.webrdaniel.collectmydata.Lists;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DialogUtils;
import com.webrdaniel.collectmydata.Utils.KeyboardUtils;

import java.util.concurrent.Callable;

class RecordViewHolder extends RecyclerView.ViewHolder {
    private RecordsListAdapter recordsListAdapter;
    private View mView;
    TextView mValueDate;
    TextView mValue;
    private Record record;

    RecordViewHolder(RecordsListAdapter recordsListAdapter, View v) {
        super(v);
        this.recordsListAdapter = recordsListAdapter;
        mView = v;
        mValue = v.findViewById(R.id.tv_record_value);
        mValueDate = v.findViewById(R.id.tv_record_date);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEditValue();
            }
        });

    }

    private void showDialogEditValue() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(recordsListAdapter.recordsRecyclerViewAdapter.parent);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_record_value_edit, (ViewGroup) recordsListAdapter.recordsRecyclerViewAdapter.rootView, false);
        final TextInputEditText et_name = dialog.findViewById(R.id.tiet_record_value_edit);
        double value = recordsListAdapter.recordsRecyclerViewAdapter.parent.mRecords.get(RecordViewHolder.this.getAdapterPosition() - 1).getValue();
        et_name.setText(recordsListAdapter.recordsRecyclerViewAdapter.parent.formatNoLastZero.format(value));
        Callable methodEditValue = new Callable() {
            @Override
            public Object call() throws Exception {
                editValue(Double.parseDouble(et_name.getText().toString()));
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.getDialog(dialog, recordsListAdapter.recordsRecyclerViewAdapter.parent, methodEditValue, R.string.edit);
        DialogUtils.lockPositiveButtonOnEmptyText(alertDialog, et_name, String.valueOf(recordsListAdapter.recordsRecyclerViewAdapter.parent.formatNoLastZero.format(value)));
        et_name.requestFocus();
        DialogUtils.onEnterConfirm(et_name, alertDialog);
        KeyboardUtils.showKeyboard(recordsListAdapter.recordsRecyclerViewAdapter.parent);
    }

    private void editValue(Double value) {
        int position = RecordViewHolder.this.getAdapterPosition();
        record = recordsListAdapter.recordsRecyclerViewAdapter.parent.mRecords.get(position - 1);
        recordsListAdapter.recordsRecyclerViewAdapter.parent.mDatabaseHelper.editValue(record.getId(), value);
        record.setValue(value);
        recordsListAdapter.recordsRecyclerViewAdapter.mRecordsListAdapter.notifyItemChanged(position);
        recordsListAdapter.recordsRecyclerViewAdapter.parent.recordsOverviewFragment.updateData();
        recordsListAdapter.recordsRecyclerViewAdapter.parent.recordsOverviewFragment.updateLayout();

    }
}
