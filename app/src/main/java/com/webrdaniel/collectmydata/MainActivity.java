package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private BasicListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataCollItem> mDataCollItemsArrayList;
    private IOData mIOData;
    public static final String FILENAME = "datacollitems.json";
    public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.MainActivity";
    public static final String DATA_DELETED = "";
    private static final int NEW_DATA_COLL_ITEM = 100;
    private static final int SETTING = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mIOData = new IOData(this, FILENAME);
        mDataCollItemsArrayList = mIOData.getLocallyStoredData();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new BasicListAdapter(mDataCollItemsArrayList);
        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newDataColl= new Intent(MainActivity.this, NewDataCollActivity.class);
                DataCollItem item = new DataCollItem();
                newDataColl.putExtra(DATA_COLL_ITEM, item);
                startActivityForResult(newDataColl, NEW_DATA_COLL_ITEM);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivityForResult(settingIntent,SETTING);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NEW_DATA_COLL_ITEM:
                DataCollItem item = (DataCollItem) data.getSerializableExtra(DATA_COLL_ITEM);
                mDataCollItemsArrayList.add(item);
                adapter.notifyDataSetChanged();
                break;

            case SETTING:
                boolean dataDeleted = data.getBooleanExtra(DATA_DELETED,false);
                if(dataDeleted)
                {
                    mDataCollItemsArrayList.clear();
                    adapter.notifyDataSetChanged();
                    Log.d("TAG", "onActivityResult: vymazáno");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mIOData.saveToFile(mDataCollItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
        private ArrayList<DataCollItem> items;
        public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.MainActivity";

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
                        DataCollItem item = items.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(MainActivity.this, DataCollDetailActivity.class);
                        i.putExtra(DATA_COLL_ITEM, item);
                        startActivity(i);
                    }
                });
                mDataCollname = (TextView)v.findViewById(R.id.data_coll_name);
            }


        }
    }

}

