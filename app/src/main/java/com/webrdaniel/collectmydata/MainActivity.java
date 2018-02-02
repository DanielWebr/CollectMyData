package com.webrdaniel.collectmydata;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private CardAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataCollItem> mDataCollItemsArrayList;
    private DatabaseHelper mDatabaseHelper;
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
        adapter = new CardAdapter(mDataCollItemsArrayList);
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
                mDataCollItemsArrayList.add(item);
                adapter.notifyDataSetChanged();
                item.setId(mDatabaseHelper.insertDataColl(item.getName(), item.getColor(), item.getReminderTimeString()));
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


    class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
        private ArrayList<DataCollItem> items;
        public static final String DATA_COLL_ITEM = "com.webrdaniel.collectmydata.MainActivity";

        @Override
        public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_coll, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CardAdapter.ViewHolder holder, final int position) {
            DataCollItem item = items.get(position);
            holder.mDataCollname.setText(item.getName());
            int color = item.getColor();
           ImageViewCompat.setImageTintList( holder.mIcon, ColorStateList.valueOf(color));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        CardAdapter(ArrayList<DataCollItem> items){
            this.items = items;
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder{
            View mView;
            TextView mDataCollname;
            ImageView mIcon;
            ImageButton mIbDialog;

            public ViewHolder(View v){
                super(v);
                mView = v;
                mDataCollname = v.findViewById(R.id.tv_name);
                mIcon = v.findViewById(R.id.iv_icon);
                mIbDialog = v.findViewById(R.id.ib_dialog);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCollItem item = items.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(MainActivity.this, DataCollDetailActivity.class);
                        i.putExtra(DATA_COLL_ITEM, item);
                        startActivity(i);
                    }
                });
                mIbDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DataCollItem item = items.get(ViewHolder.this.getAdapterPosition());
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilderUserInput.setView(mView);

                        final EditText etValue = mView.findViewById(R.id.et_value);
                        final EditText etDate = mView.findViewById(R.id.et_date);
                        alertDialogBuilderUserInput
                                .setCancelable(false)
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        mDatabaseHelper.insertDataValue(item.getId(),Integer.parseInt(etValue.getText().toString()),etDate.getText().toString());
                                    }
                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                dialogBox.cancel();
                                            }
                                        });

                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                    }
                });

            }


        }
    }

}

