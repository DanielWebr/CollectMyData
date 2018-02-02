package com.webrdaniel.collectmydata;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

class CardAdapter extends RecyclerView.Adapter<MainViewHolder>{
    protected ArrayList<DataCollItem> mDataCollItemsArrayList;
    MainActivity activity;

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_coll, parent, false);
        return new MainViewHolder(v,activity);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        DataCollItem item = mDataCollItemsArrayList.get(position);
        holder.mDataCollname.setText(item.getName());
        int color = item.getColor();
        ImageViewCompat.setImageTintList( holder.mIcon, ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return mDataCollItemsArrayList.size();
    }

    public CardAdapter(Context context) {
        activity = (MainActivity) context;
        mDataCollItemsArrayList = activity.mDataCollItemsArrayList;
    }
}
