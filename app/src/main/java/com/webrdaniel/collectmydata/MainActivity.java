package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private ArrayList<DataCollItem> mDataCollItemsArrayList;
    private IOData mIOData;
    public static final String FILENAME = "datacollitems.json";
    public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.MainActivity";
    private static final int NEW_DATA_COLL_ITEM_ = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mIOData = new IOData(this, FILENAME);
        mDataCollItemsArrayList =  getLocallyStoredData(mIOData);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newDataColl= new Intent(MainActivity.this, NewDataCollActivity.class);
                DataCollItem item = new DataCollItem();
                newDataColl.putExtra(DATA_COLL_ITEM, item);
                startActivityForResult(newDataColl, NEW_DATA_COLL_ITEM_);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DataCollItem item = (DataCollItem) data.getSerializableExtra(DATA_COLL_ITEM);
    }

    public static ArrayList<DataCollItem> getLocallyStoredData(IOData storeRetrieveData){
        ArrayList<DataCollItem> items = null;
        try {
            items  = storeRetrieveData.loadFromFile();
            }
            catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if(items == null){
            items = new ArrayList<>();
        }
        return items;
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
}
