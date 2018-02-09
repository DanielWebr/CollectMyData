package com.webrdaniel.collectmydata.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.webrdaniel.collectmydata.DatabaseHelper;
import com.webrdaniel.collectmydata.Lists.DataCollListAdapter;
import com.webrdaniel.collectmydata.Models.DataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.KeyboardUtils;
import com.webrdaniel.collectmydata.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.fab_main_activity) public FloatingActionButton fab;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.recycler_view_main_activity) public RecyclerView mRecyclerView;
    @BindView(R.id.empty_records_list) public TextView empty_view;
    public DataCollListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<DataCollItem> mDataCollItemsArrayList;
    public DatabaseHelper mDatabaseHelper;
    public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.Activities.MainActivity";
    private static final int NEW_DATA_COLL_ITEM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_poll_orange_24dp);

        mDatabaseHelper = new DatabaseHelper(this);

        mDataCollItemsArrayList = mDatabaseHelper.getDataCollItems();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new DataCollListAdapter(MainActivity.this);
        mRecyclerView.setAdapter(adapter);
        messageIfEmpty();

        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
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
            Intent settingIntent = new Intent(this, AboutAppActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) return;
        DataCollItem item = (DataCollItem) data.getSerializableExtra(DATA_COLL_ITEM);
        int id = mDatabaseHelper.insertDataColl(item.getName(), item.getColor());
        item.setId(id);
        mDataCollItemsArrayList.add(item);
        messageIfEmpty();
        adapter.notifyItemInserted(adapter.getItemCount());

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(this);
    }

    public void messageIfEmpty() {
        if (mDataCollItemsArrayList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }
    }

}

