package com.webrdaniel.collectmydata.Lists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrdaniel.collectmydata.Fragments.RecordsListFragment;
import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;
import com.webrdaniel.collectmydata.Utils.Utils;

import java.util.ArrayList;

public class RecordsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecordsListFragment mRecordsListFragment;
    private ArrayList<Record> mRecords;

    public RecordsListAdapter(RecordsListFragment mRecordsListFragment) {
        this.mRecordsListFragment = mRecordsListFragment;
        this.mRecords = mRecordsListFragment.dataCollDetailActivity.records;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header_records_recyclre_view, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_records_recycler_view, parent, false);
            return new RecordViewHolder(mRecordsListFragment, view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            RecordViewHolder recordHolder = (RecordViewHolder) holder;
            recordHolder.mValue.setText(Utils.doubleToString(mRecords.get(position - 1).getValue()));
            recordHolder.mValueDate.setText(DateUtils.dateToString(mRecords.get(position - 1).getDate(), DateUtils.DATE_FORMAT_EDM));
        }
    }

    @Override
    public int getItemCount() {
        return mRecords.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0; //header
        } else {
            return 1; //normal
        }
    }

}
