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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class RecordsOverviewFragment extends Fragment {
    TextView tv_min, tv_max, tv_sum, tv_avg,tv_count, txt_graph;
    double min, max, sum, avg;
    int count;
    private DataCollDetailActivity parent;
    GraphView graph;
    BarGraphSeries<DataPoint> series;
    StaticLabelsFormatter staticLabelsFormatter;
    GridLabelRenderer glr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
        updateData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_records_overview, container, false);
        tv_count = linearLayout.findViewById(R.id.tv_count);
        tv_min = linearLayout.findViewById(R.id.tv_min);
        tv_max = linearLayout.findViewById(R.id.tv_max);
        tv_sum = linearLayout.findViewById(R.id.tv_sum);
        tv_avg = linearLayout.findViewById(R.id.tv_avg);
        txt_graph = linearLayout.findViewById(R.id.txt_graph);
        tv_count.setText(String.valueOf(count));
        graph = linearLayout.findViewById(R.id.graph);
        glr = graph.getGridLabelRenderer();
        updateLayout();
        return linearLayout;
    }

    public void updateData() {
        count = parent.mRecords.size();
        if(count!=0) getValues();
    }

    public void getValues() {
        sum = 0;
        max = parent.mRecords.get(0).getValue();
        min = parent.mRecords.get(0).getValue();
        for(Record record : parent.mRecords) {
            double value = record.getValue();
            sum+=value;
            if(value<min)min = value;
            else if(value>max)max = value;
        }
        avg = sum / parent.mRecords.size();
    }

    public void updateLayout() {
        if(count>=1)setGraph();
        else hideGraph();
        tv_count.setText(this.getString(R.string.count,count));
        if(count!=0){
            tv_min.setText(this.getString(R.string.min,parent.formatNoLastZero.format(min)));
            tv_max.setText(this.getString(R.string.max,parent.formatNoLastZero.format(max)));
            tv_sum.setText(this.getString(R.string.sum,parent.formatNoLastZero.format(sum)));
            tv_avg.setText(this.getString(R.string.avg,parent.formatNoLastZero.format(avg)));
        }
        else {
            tv_min.setText(this.getString(R.string.min,"-"));
            tv_max.setText(this.getString(R.string.max,"-"));
            tv_sum.setText(this.getString(R.string.sum,"-"));
            tv_avg.setText(this.getString(R.string.avg,"-"));
        }
    }

    private void setGraph() {
        ArrayList<Record> filledRecords = getFilledRecords();
        int size = filledRecords.size();
        String[] hLabels = new String[size+1];
        hLabels[size]="";
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
        glr.resetStyles();
        if(staticLabelsFormatter == null) {
            graph.getViewport().setMinX(0);
            staticLabelsFormatter = new StaticLabelsFormatter(graph);
            series = new BarGraphSeries<>(dataPoints);
            graph.addSeries(series);
            staticLabelsFormatter.setHorizontalLabels(hLabels);
            glr.setLabelFormatter(staticLabelsFormatter);
            series.setColor(getResources().getColor(R.color.primaryDarkColor));
            graph.getViewport().setYAxisBoundsManual(true);
            series.setSpacing(40);
        }
        else {
            staticLabelsFormatter.setHorizontalLabels(hLabels);
            series.resetData(dataPoints);
        }

        glr.setHorizontalLabelsAngle(1);
        glr.setNumHorizontalLabels(hLabels.length);
        glr.reloadStyles();

        if(min>0) graph.getViewport().setMinY(0);
        else graph.getViewport().setMinY(min - min/10);

        graph.getViewport().setMaxY(max + max/10);

        showGraph();

    }

    private ArrayList<Record> getFilledRecords() {
        ArrayList<Record> filledRecords = new ArrayList<>();
        Date date = parent.mRecords.get(0).getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        int size = parent.mRecords.size();
        int id = 0;
        for(int i = 0; i < size; i++) {
            Record record = parent.mRecords.get(id);
            c.add(Calendar.DATE, -1);
            if (record.getDate().compareTo(c.getTime())==0) {
                filledRecords.add(record);
                id++;
            }
            else {
                filledRecords.add(new Record(c.getTime(),0));
                size++;
            }
        }
        filledRecords.add(new Record(new Date(),0));
        Collections.reverse(filledRecords);
        return filledRecords;
    }

    private void hideGraph() {
        graph.setVisibility(View.GONE);
        txt_graph.setVisibility(View.VISIBLE);
    }

    private void showGraph() {
        graph.setVisibility(View.VISIBLE);
        txt_graph.setVisibility(View.GONE);
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
}
