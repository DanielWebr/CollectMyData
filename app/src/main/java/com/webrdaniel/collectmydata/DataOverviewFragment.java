package com.webrdaniel.collectmydata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;


public class DataOverviewFragment extends Fragment {

    TextView tv_min, tv_max, tv_sum, tv_avg,tv_count;
    int dataCollId;
    double min, max, sum, avg;
    int count;
    private DataCollDetailActivity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getActivity();
        DatabaseHelper mDatabaseHelper = parent.mDatabaseHelper;
        dataCollId = parent.mDataCollItem.getId();
        count = (int)mDatabaseHelper.getValuesSelect("COUNT",dataCollId);
        if(count!=0) {
            min = mDatabaseHelper.getValuesSelect("MIN", dataCollId);
            max = mDatabaseHelper.getValuesSelect("MAX", dataCollId);
            sum = mDatabaseHelper.getValuesSelect("SUM", dataCollId);
            avg = mDatabaseHelper.getValuesSelect("AVG", dataCollId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.data_detail_overview, container, false);
        tv_count = linearLayout.findViewById(R.id.tv_count);
        tv_min = linearLayout.findViewById(R.id.tv_min);
        tv_max = linearLayout.findViewById(R.id.tv_max);
        tv_sum = linearLayout.findViewById(R.id.tv_sum);
        tv_avg = linearLayout.findViewById(R.id.tv_avg);
        tv_count.setText(String.valueOf(count));
        tv_min.setText(parent.formatNoLastZero.format(min));
        tv_max.setText(parent.formatNoLastZero.format(max));
        tv_sum.setText(parent.formatNoLastZero.format(sum));
        tv_avg.setText(parent.formatNoLastZero.format(avg));
        return linearLayout;

    }
}
