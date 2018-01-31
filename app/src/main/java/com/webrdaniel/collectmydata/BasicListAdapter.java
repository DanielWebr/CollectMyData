package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dandu on 31.01.2018.
 */

class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
    private ArrayList<DataCollItem> items;

    @Override
    public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_coll, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
        DataCollItem item = items.get(position);
        holder.mDataCollname.setText(item.getmDataCollName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    BasicListAdapter(ArrayList<DataCollItem> items){

        this.items = items;
    }


    @SuppressWarnings("deprecation")
    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView mDataCollname;

        public ViewHolder(View v){
            super(v);
            mView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mDataCollname = (TextView)v.findViewById(R.id.data_coll_name);
        }


    }
}
