package com.webrdaniel.collectmydata.Lists;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrdaniel.collectmydata.Activities.MainActivity;
import com.webrdaniel.collectmydata.Models.DataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class DataCollListAdapter extends RecyclerView.Adapter<DataCollViewHolder>{
    private ArrayList<DataCollItem> mDataCollItemsArrayList;
    private MainActivity activity;

    @Override
    public DataCollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_activity_main_recycler_view, parent, false);
        return new DataCollViewHolder(v,activity);
    }

    @Override
    public void onBindViewHolder(final DataCollViewHolder holder, int position) {
        DataCollItem item = mDataCollItemsArrayList.get(position);
        holder.mDataCollname.setText(item.getName());
        int color = item.getColor();
        ImageViewCompat.setImageTintList( holder.mIcon, ColorStateList.valueOf(color));
        Date date = DateUtils.dateToDateFormat(DateUtils.DATE_FORMAT_DMY);
        if(activity.mDatabaseHelper.getDates(item.getId()).contains(date)) {
            holder.mIbDialog.setVisibility(View.GONE);
        }
        else{
            holder.mIbDialog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataCollItemsArrayList.size();
    }

    public DataCollListAdapter(Context context) {
        activity = (MainActivity) context;
        mDataCollItemsArrayList = activity.mDataCollItemsArrayList;
    }
}
