package com.webrdaniel.collectmydata.lists;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrdaniel.collectmydata.activities.MainActivity;
import com.webrdaniel.collectmydata.models.mDataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class DataCollListAdapter extends RecyclerView.Adapter<DataCollViewHolder>{
    private ArrayList<mDataCollItem> mDataCollItemsArrayList;
    private MainActivity mMainActivity;

    public DataCollListAdapter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mDataCollItemsArrayList = mMainActivity.dataCollItems;
    }

    @NonNull
    @Override
    public DataCollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_activity_main_recycler_view, parent, false);
        return new DataCollViewHolder(view, mMainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataCollViewHolder holder, int position) {
        mDataCollItem item = mDataCollItemsArrayList.get(position);
        holder.mNameTv.setText(item.getName());
        int color = item.getColor();
        ImageViewCompat.setImageTintList( holder.mIcon, ColorStateList.valueOf(color));

        Date date = DateUtils.dateToDateFormat(DateUtils.DATE_FORMAT_DMY);
        if(mMainActivity.databaseHelper.getDates(item.getId()).contains(date))
            holder.mAddRecordIb.setVisibility(View.GONE);
        else
            holder.mAddRecordIb.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mDataCollItemsArrayList.size();
    }
}
