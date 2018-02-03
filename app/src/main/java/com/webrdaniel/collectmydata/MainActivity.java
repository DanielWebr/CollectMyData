package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    protected CardAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<DataCollItem> mDataCollItemsArrayList;
    protected DatabaseHelper mDatabaseHelper;
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

        mDatabaseHelper = new DatabaseHelper(this);

        mDataCollItemsArrayList = mDatabaseHelper.getDataCollItems();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new CardAdapter(MainActivity.this);
        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newDataColl= new Intent(MainActivity.this, NewDataCollActivity.class);
                DataCollItem item = new DataCollItem();
                item.setColor(Utils.getMatColor(MainActivity.this));
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
        if(resultCode == RESULT_CANCELED) return;
        switch (requestCode) {
            case NEW_DATA_COLL_ITEM:
                DataCollItem item = (DataCollItem) data.getSerializableExtra(DATA_COLL_ITEM);
                int id = mDatabaseHelper.insertDataColl(item.getName(), item.getColor(), item.getReminderTimeString());
                item.setId(id);
                mDataCollItemsArrayList.add(item);
                adapter.notifyDataSetChanged();
                break;

            case SETTING:
                boolean dataDeleted = data.getBooleanExtra(DATA_DELETED,false);
                if(dataDeleted)
                {
                    mDataCollItemsArrayList.clear();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(this);
    }

}

