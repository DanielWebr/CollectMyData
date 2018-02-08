package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
    @BindView(R.id.ti_layout) TextInputLayout ti_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_coll);
        ButterKnife.bind(this);
        mDataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mUserEnteredText)) {
                    makeResult(RESULT_OK);
                    finish();
                }
                else {
                    ti_layout.setError(getResources().getString(R.string.name_error));
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

        et_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Utils.showKeyboard(this);
        if(getSupportActionBar()!=null)getSupportActionBar().setTitle(getString(R.string.new_dataColl));
    }

    @Override
    public void onBackPressed() {
        makeResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(this);
    }

    private void makeResult(int result) {
        Intent i = new Intent();
        if(result==RESULT_OK) {
            mDataCollItem.setName(mUserEnteredText);
            i.putExtra(MainActivity.DATA_COLL_ITEM, mDataCollItem);
        }
        setResult(result, i);
    }
}
