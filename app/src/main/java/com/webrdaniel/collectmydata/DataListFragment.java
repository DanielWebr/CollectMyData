package com.webrdaniel.collectmydata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class DataListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BasicListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinkedHashMap<Date,Integer> mValueHashMap;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCollDetailActivity parent = (DataCollDetailActivity) getActivity();
        mDatabaseHelper = ((DataCollDetailActivity) getActivity()).mDatabaseHelper;
        mValueHashMap = mDatabaseHelper.getValues(parent.mDataCollItem.getId());
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return mRecyclerView;
    }
    class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
        private LinkedHashMap<Date,Integer> items;
        private ArrayList<Date> keys;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_value, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mValue.setText(String.valueOf(items.get(keys.get(position))));
            holder.mValueDate.setText(Utils.dateToString(keys.get(position),"d. M."));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(LinkedHashMap<Date,Integer> items){
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


