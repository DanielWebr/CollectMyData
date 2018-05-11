package com.webrdaniel.collectmydata.activities;

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
import android.widget.Toast;

import com.webrdaniel.collectmydata.DatabaseHelper;
import com.webrdaniel.collectmydata.lists.DataCollListAdapter;
import com.webrdaniel.collectmydata.models.mDataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.utils.KeyboardUtils;
import com.webrdaniel.collectmydata.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.fab_main_activity) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_main_activity) RecyclerView mDataCollRv;
    @BindView(R.id.tv_empty_data_coll_list) TextView mEmptyRvTv;
    public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.models.records";
    static final int NEW_DATA_COLL_ITEM = 100;
    public DataCollListAdapter dataCollRvAdapter;
    public ArrayList<mDataCollItem> dataCollItems;
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_poll_orange_24dp);

        databaseHelper = new DatabaseHelper(this);
        dataCollItems = databaseHelper.getDataCollItems();
        mDataCollRv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mDataCollRv.setLayoutManager(mLayoutManager);
        dataCollRvAdapter = new DataCollListAdapter(MainActivity.this);
        mDataCollRv.setAdapter(dataCollRvAdapter);
        showTvIfRvIsEmpty();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newDataColl= new Intent(MainActivity.this, NewDataCollActivity.class);
                mDataCollItem item = new mDataCollItem();
                item.setColor(Utils.getMatColor(MainActivity.this));
                newDataColl.putExtra(DATA_COLL_ITEM, item);
                startActivityForResult(newDataColl, NEW_DATA_COLL_ITEM);
            }
        });
        Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_about_app) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) return;
        mDataCollItem item = (mDataCollItem) data.getSerializableExtra(DATA_COLL_ITEM);
        int id = databaseHelper.insertDataColl(item.getName(), item.getColor());
        item.setId(id);
        dataCollItems.add(item);
        showTvIfRvIsEmpty();
        dataCollRvAdapter.notifyItemInserted(dataCollRvAdapter.getItemCount());
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataCollRvAdapter.notifyDataSetChanged();
    }

    public void showTvIfRvIsEmpty() {
        if (dataCollItems.isEmpty()) {
            mDataCollRv.setVisibility(View.GONE);
            mEmptyRvTv.setVisibility(View.VISIBLE);
        }
        else {
            mDataCollRv.setVisibility(View.VISIBLE);
            mEmptyRvTv.setVisibility(View.GONE);
        }
    }
}

