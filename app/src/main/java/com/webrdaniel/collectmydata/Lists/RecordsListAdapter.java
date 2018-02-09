package com.webrdaniel.collectmydata.Lists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrdaniel.collectmydata.Fragments.RecordsListFragment;
import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;

import java.util.ArrayList;

public class RecordsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    RecordsListFragment recordsRecyclerViewAdapter;
    private ArrayList<Record> items;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header_records_recyclre_view, parent, false);
            return new RecyclerView.ViewHolder(v) {

            };

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_records_recycler_view, parent, false);
            return new RecordViewHolder(this, v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
            recordViewHolder.mValue.setText(recordsRecyclerViewAdapter.parent.formatNoLastZero.format(items.get(position - 1).getValue()));
            recordViewHolder.mValueDate.setText(DateUtils.dateToString(items.get(position - 1).getDate(), DateUtils.DATE_FORMAT_EDM));
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public RecordsListAdapter(RecordsListFragment recordsRecyclerViewAdapter, ArrayList<Record> items) {
        this.recordsRecyclerViewAdapter = recordsRecyclerViewAdapter;
        this.items = items;
    }

}
