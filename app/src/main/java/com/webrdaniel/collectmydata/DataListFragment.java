package com.webrdaniel.collectmydata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class DataListFragment extends Fragment {

    private ArrayList<Pair<Date, Double>> mValueHashMap;
    private DataCollDetailActivity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getActivity();
        mValueHashMap = parent.mDatabaseHelper.getValues(parent.mDataCollItem.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        rootView.setTag("DataListFragment");
        RecyclerView mRecyclerView =  rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new BasicListAdapter(mValueHashMap));
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parent,  DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return mRecyclerView;
    }
    class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
        private ArrayList<Pair<Date, Double>> items;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_value, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,int position) {
            holder.mValue.setText(parent.formatNoLastZero.format(items.get(position).second));
            holder.mValueDate.setText(Utils.dateToString(items.get(position).first,"d. M."));
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(ArrayList<Pair<Date, Double>> items){
            this.items = items;
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


