package com.webrdaniel.collectmydata;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BasicListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private HashMap<Date,Integer> mValueHashMap;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCollDetailActivity parent = (DataCollDetailActivity) getActivity();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mValueHashMap = mDatabaseHelper.getValues(parent.getmDataCollItem().getId());
        Log.d("TAG", "onCreate: "+mValueHashMap.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        rootView.setTag("DataListFragment");
        mRecyclerView =  rootView.findViewById(R.id.recycler_view);

        mAdapter = new BasicListAdapter(mValueHashMap);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return mRecyclerView;
    }
    class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
        private HashMap<Date,Integer> items;
        private ArrayList<Date> keys;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_value, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mValue.setText(Integer.toString(items.get(keys.get(position))));
            holder.mValueDate.setText(Utils.dateToString(keys.get(position),"dd.MM."));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(HashMap<Date,Integer> items){
            this.items = items;
            this.keys = new ArrayList<>(items.keySet());
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder{
            View mView;
            TextView mValueDate;
            TextView mValue;

            public ViewHolder(View v){
                super(v);
                mView = v;
                mValue = v.findViewById(R.id.tv_value);
                mValueDate = v.findViewById(R.id.tv_value_date);
            }
        }
    }
}


