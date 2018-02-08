package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

class MainActivityCardAdapter extends RecyclerView.Adapter<MainActivityViewHolder>{
    private ArrayList<DataCollItem> mDataCollItemsArrayList;
    private MainActivity activity;

    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_activity_main_recycler_view, parent, false);
        return new MainActivityViewHolder(v,activity);
    }

    @Override
    public void onBindViewHolder(final MainActivityViewHolder holder, int position) {
        DataCollItem item = mDataCollItemsArrayList.get(position);
        holder.mDataCollname.setText(item.getName());
        int color = item.getColor();
        ImageViewCompat.setImageTintList( holder.mIcon, ColorStateList.valueOf(color));
        Date date = Utils.dateToDateFormat(Utils.DATE_FORMAT_DMY);
        if(activity.mDatabaseHelper.getDates(item.getId()).contains(date)) {
            holder.mIbDialog.setEnabled(false);
        }
        else{
            holder.mIbDialog.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataCollItemsArrayList.size();
    }

    MainActivityCardAdapter(Context context) {
        activity = (MainActivity) context;
        mDataCollItemsArrayList = activity.mDataCollItemsArrayList;
    }
}
