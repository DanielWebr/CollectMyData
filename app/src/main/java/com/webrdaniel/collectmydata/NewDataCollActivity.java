package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDataCollActivity extends AppCompatActivity {

    private DataCollItem mDataCollItem;
    private String mUserEnteredText;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.et_name) EditText et_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_coll);
        ButterKnife.bind(this);
        mDataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeResult(RESULT_OK);
                finish();
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void makeResult(int result) {
        Intent i = new Intent();
        mDataCollItem.setmDataCollName(mUserEnteredText);
        i.putExtra(MainActivity.DATA_COLL_ITEM, mDataCollItem);
        setResult(result,i);
    }
}
