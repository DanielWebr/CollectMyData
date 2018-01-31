package com.webrdaniel.collectmydata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    public static final String FILENAME = "datacollitems.json";
    private boolean dataDeleted;
    private IOData mIOData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dataDeleted = false;
        makeResult(RESULT_OK);
    }

    public void deleteData(View view) {
        mIOData = new IOData(this,FILENAME);
        mIOData.deleteData();
        dataDeleted = true;
        makeResult(RESULT_OK);
    }

    private void makeResult(int result) {
        Intent i = new Intent();
        i.putExtra(MainActivity.DATA_DELETED, dataDeleted);
        setResult(result,i);
    }

}
