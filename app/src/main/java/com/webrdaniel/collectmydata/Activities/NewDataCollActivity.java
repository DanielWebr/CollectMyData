package com.webrdaniel.collectmydata.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.webrdaniel.collectmydata.Models.DataCollItem;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDataCollActivity extends AppCompatActivity {
    @BindView(R.id.fab_new_data_coll) FloatingActionButton fab;
    @BindView(R.id.tiet_new_data_coll) TextInputEditText newDataCollNameTiet;
    @BindView(R.id.til_new_data_coll) TextInputLayout newDataCollNameTil;
    private DataCollItem mDataCollItem;
    private String mUserEnteredText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data_coll);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null)getSupportActionBar().setTitle(getString(R.string.new_dataColl));
        mDataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mUserEnteredText)) {
                    makeResult(RESULT_OK);
                    finish();
                }
                else {
                    newDataCollNameTil.setError(getResources().getString(R.string.name_error));
                }
            }
        });

        newDataCollNameTiet.requestFocus();
        newDataCollNameTiet.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newDataCollNameTiet.addTextChangedListener(new TextWatcher() {
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

        KeyboardUtils.showKeyboard(this);

    }

    @Override
    public void onBackPressed() {
        makeResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(this);
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
