package com.webrdaniel.collectmydata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class DataOverviewFragment extends Fragment {

    TextView tv_min, tv_max, tv_sum, tv_avg,tv_count;
    int dataCollId;
    double min, max, sum, avg;
    int count;
    private DataCollDetailActivity parent;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
        updateData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        linearLayout = (LinearLayout) inflater.inflate(R.layout.data_detail_overview, container, false);
        tv_count = linearLayout.findViewById(R.id.tv_count);
        tv_min = linearLayout.findViewById(R.id.tv_min);
        tv_max = linearLayout.findViewById(R.id.tv_max);
        tv_sum = linearLayout.findViewById(R.id.tv_sum);
        tv_avg = linearLayout.findViewById(R.id.tv_avg);
        tv_count.setText(String.valueOf(count));
        updateLayout();
        return linearLayout;
    }

  /* public void updateData()
    {
        count = (int)parent.mDatabaseHelper.getValuesSelect("COUNT",dataCollId);
        if(count!=0) {
            min = parent.mDatabaseHelper.getValuesSelect("MIN", dataCollId);
            max = parent.mDatabaseHelper.getValuesSelect("MAX", dataCollId);
            sum = parent.mDatabaseHelper.getValuesSelect("SUM", dataCollId);
            avg = parent.mDatabaseHelper.getValuesSelect("AVG", dataCollId);
        }
    }*/

    public void updateData()
    {
        count = parent.mRecords.size();
        if(count!=0) getValues();
    }

    public void getValues()
    {
        sum = 0;
        max = parent.mRecords.get(0).getValue();
        min = parent.mRecords.get(0).getValue();
        for(Record record : parent.mRecords)
        {
            double value = record.getValue();
            sum+=value;
            if(value<min)min = value;
            else if(value>max)max = value;
        }
        avg = sum / parent.mRecords.size();
    }

    public void updateLayout()
    {
        tv_count.setText(String.valueOf(count));
        if(count!=0){
            tv_min.setText(parent.formatNoLastZero.format(min));
            tv_max.setText(parent.formatNoLastZero.format(max));
            tv_sum.setText(parent.formatNoLastZero.format(sum));
            tv_avg.setText(parent.formatNoLastZero.format(avg));
        }
        else
        {
            tv_min.setText("-");
            tv_max.setText("-");
            tv_sum.setText("-");
            tv_avg.setText("-");
        }
    }
}
