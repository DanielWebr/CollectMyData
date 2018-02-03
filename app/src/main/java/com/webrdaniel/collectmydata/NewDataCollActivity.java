package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDataCollActivity extends AppCompatActivity {

    private DataCollItem mDataCollItem;
    private String mUserEnteredText;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.ti_et) TextInputEditText et_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_coll);
        ButterKnife.bind(this);
        mDataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mUserEnteredText))
                {
                    makeResult(RESULT_OK);
                    finish();
                }
                else
                {
                    et_name.setError(getResources().getString(R.string.name_error));
                }
            }
        });
        et_name.requestFocus();
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
        Utils.showKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        makeResult(RESULT_CANCELED);
        finish();
    }

    private void makeResult(int result) {
        Intent i = new Intent();
        if(result==RESULT_OK)
        {
            mDataCollItem.setName(mUserEnteredText);
            i.putExtra(MainActivity.DATA_COLL_ITEM, mDataCollItem);
        }
        setResult(result, i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(this);
    }
}
