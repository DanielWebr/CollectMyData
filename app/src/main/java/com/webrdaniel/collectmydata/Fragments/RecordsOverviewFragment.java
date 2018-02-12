package com.webrdaniel.collectmydata.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.webrdaniel.collectmydata.Activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;
import com.webrdaniel.collectmydata.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class RecordsOverviewFragment extends Fragment {
    private TextView mMinTv, mMaxTv, mSumTv, mAvgTv, mCountTv;
    private TextView mNoGraphTv;
    private double mMin, mMax, mSum, mAvg;
    private int mCount;
    private DataCollDetailActivity dataCollDetailActivity;
    private GraphView mGraph;
    private BarGraphSeries<DataPoint> mBarGraphSeries;
    private StaticLabelsFormatter mStaticLabelsFormatter;
    private GridLabelRenderer mGridLabelRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCollDetailActivity = (DataCollDetailActivity) getContext();
        updateData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_records_overview, container, false);
        mCountTv = linearLayout.findViewById(R.id.tv_count);
        mMinTv = linearLayout.findViewById(R.id.tv_min);
        mMaxTv = linearLayout.findViewById(R.id.tv_max);
        mSumTv = linearLayout.findViewById(R.id.tv_sum);
        mAvgTv = linearLayout.findViewById(R.id.tv_avg);
        mNoGraphTv = linearLayout.findViewById(R.id.txt_graph);
        mGraph = linearLayout.findViewById(R.id.graph);
        mCountTv.setText(String.valueOf(mCount));

        mStaticLabelsFormatter = new StaticLabelsFormatter(mGraph);
        mGridLabelRenderer = mGraph.getGridLabelRenderer();
        mGridLabelRenderer.setLabelFormatter(mStaticLabelsFormatter);
        mBarGraphSeries = new BarGraphSeries<>();
        mBarGraphSeries.setColor(getResources().getColor(R.color.primaryDarkColor));
        mBarGraphSeries.setSpacing(40);
        mGraph.addSeries(mBarGraphSeries);
        mGraph.getViewport().setYAxisBoundsManual(true);

        updateLayout();
        return linearLayout;
    }

    public void updateData() {
        mCount = dataCollDetailActivity.records.size();
        if(mCount !=0) getStats();
    }

    public void getStats() {
        mSum = 0;
        mMax = dataCollDetailActivity.records.get(0).getValue();
        mMin = dataCollDetailActivity.records.get(0).getValue();
        for(Record record : dataCollDetailActivity.records) {
            double value = record.getValue();
            mSum +=value;
            if(value< mMin) mMin = value;
            else if(value> mMax) mMax = value;
        }
        mAvg = mSum / dataCollDetailActivity.records.size();
    }

    public void updateLayout() {
        if(mCount >=1)setGraph();
        else graphVisible(false);
        mCountTv.setText(this.getString(R.string.count, mCount));
        if(mCount !=0){
            mMinTv.setText(this.getString(R.string.min, Utils.doubleToString(mMin)));
            mMaxTv.setText(this.getString(R.string.max, Utils.doubleToString(mMax)));
            mSumTv.setText(this.getString(R.string.sum, Utils.doubleToString(mSum)));
            mAvgTv.setText(this.getString(R.string.avg, Utils.doubleToString(mAvg)));
        }
        else {
            mMinTv.setText(this.getString(R.string.min,"-"));
            mMaxTv.setText(this.getString(R.string.max,"-"));
            mSumTv.setText(this.getString(R.string.sum,"-"));
            mAvgTv.setText(this.getString(R.string.avg,"-"));
        }
    }

    private void setGraph() {
        ArrayList<Record> filledRecords = getFilledRecords();
        int size = filledRecords.size();
        String[] hLabels = new String[size+1];
        DataPoint[] dataPoints = new DataPoint[size+1];
        dataPoints[size]=new DataPoint(size,0);
        int density = (int) Math.ceil(size/7.0);
        for(int i = 0; i < size; i++) {
            Record record = filledRecords.get(i);
            dataPoints[i] = new DataPoint(i, record.getValue());
            if(((i+density-1)%density==0 && i!=0)||i==1){
                hLabels[i] = DateUtils.dateToString(record.getDate(),DateUtils.DATE_FORMAT_DM);
            }
        }
        mBarGraphSeries.resetData(dataPoints);
        mGridLabelRenderer.resetStyles();

        mStaticLabelsFormatter.setHorizontalLabels(hLabels);
        mGridLabelRenderer.setNumHorizontalLabels(hLabels.length);

        if(mMin >0) mGraph.getViewport().setMinY(0);
        else mGraph.getViewport().setMinY(mMin - mMin /10);

        mGraph.getViewport().setMaxY(mMax + mMax /10);

        mGridLabelRenderer.reloadStyles();
        graphVisible(true);
    }

    private ArrayList<Record> getFilledRecords() {
        ArrayList<Record> filledRecords = new ArrayList<>();
        Date date = dataCollDetailActivity.records.get(0).getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        int size = dataCollDetailActivity.records.size();
        int id = 0;
        for(int i = 0; i < size; i++) {
            Record record = dataCollDetailActivity.records.get(id);
            calendar.add(Calendar.DATE, -1);
            if (record.getDate().compareTo(calendar.getTime())==0) {
                filledRecords.add(record);
                id++;
            }
            else {
                filledRecords.add(new Record(calendar.getTime(),0));
                size++;
            }
        }
        filledRecords.add(new Record(new Date(),0));
        Collections.reverse(filledRecords);
        return filledRecords;
    }

    private void graphVisible(boolean visible) {
        if(visible) {
            mGraph.setVisibility(View.VISIBLE);
            mNoGraphTv.setVisibility(View.GONE);
        }
        else{
            mGraph.setVisibility(View.GONE);
            mNoGraphTv.setVisibility(View.VISIBLE);
        }
    }
}
