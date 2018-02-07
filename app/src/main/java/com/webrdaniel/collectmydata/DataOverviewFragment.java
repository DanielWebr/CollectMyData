package com.webrdaniel.collectmydata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class DataOverviewFragment extends Fragment {

    TextView tv_min, tv_max, tv_sum, tv_avg,tv_count, txt_graph;
    double min, max, sum, avg;
    int count;
    private DataCollDetailActivity parent;
    private LinearLayout linearLayout;
    GraphView graph;
    BarGraphSeries<DataPoint> series;
    StaticLabelsFormatter staticLabelsFormatter;

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
        txt_graph = linearLayout.findViewById(R.id.txt_graph);
        tv_count.setText(String.valueOf(count));
        graph = linearLayout.findViewById(R.id.graph);
        updateLayout();

        return linearLayout;
    }

    private void setGraph() {
        ArrayList<Record> filledRecords = getFilledRecords();
        int size = filledRecords.size();
        String[] hLabels = new String[size+1];
        hLabels[size]="";
        DataPoint[] dataPoints = new DataPoint[size+1];
        dataPoints[size]=new DataPoint(size,0);
        int density = (int) Math.ceil(size/20.0);
        int labelsCount = 0;
        for(int i = 0; i < size; i++) {
            Record record = filledRecords.get(i);
            dataPoints[i] = new DataPoint(i, record.getValue());
            if((i)%density==0 || i==0){
                hLabels[i] = Utils.dateToString(record.getDate(),Utils.DATE_FORMAT_DM);
                labelsCount++;
            }
            else
            {
                hLabels[i] = "";
            }

        }
        //if(filledRecords.size())


        graph.getViewport().setYAxisBoundsManual(true);

        if(min>0) graph.getViewport().setMinY(0);
        else graph.getViewport().setMinY(min - min/10);

        graph.getViewport().setMaxY(max + max/10);

        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.resetStyles();

        switch (labelsCount)
        {
            case 8:glr.setHorizontalLabelsAngle(1);glr.setLabelsSpace(5); break;
            case 9:glr.setHorizontalLabelsAngle(10);glr.setLabelsSpace(5);break;
            case 10:glr.setHorizontalLabelsAngle(20);glr.setLabelsSpace(5);break;
            case 11:glr.setHorizontalLabelsAngle(30);glr.setLabelsSpace(5);break;
            case 12:glr.setHorizontalLabelsAngle(40);glr.setLabelsSpace(6);break;
            case 13:glr.setHorizontalLabelsAngle(45);glr.setLabelsSpace(7);break;
            case 14:glr.setHorizontalLabelsAngle(50);glr.setLabelsSpace(7);break;
            case 15:glr.setHorizontalLabelsAngle(60);glr.setLabelsSpace(7);break;
            case 16:glr.setHorizontalLabelsAngle(65);glr.setLabelsSpace(7);break;
            case 17:glr.setHorizontalLabelsAngle(65);glr.setLabelsSpace(7);break;
            case 18:glr.setHorizontalLabelsAngle(70);glr.setLabelsSpace(10);break;
            case 19:glr.setHorizontalLabelsAngle(70);glr.setLabelsSpace(10);break;
            case 20:glr.setHorizontalLabelsAngle(70);glr.setLabelsSpace(10);break;
            default:  glr.setHorizontalLabelsAngle(0);glr.setLabelsSpace(5);
        }


        if(staticLabelsFormatter == null)
        {
            graph.getViewport().setMinX(0);
            staticLabelsFormatter = new StaticLabelsFormatter(graph);
            series = new BarGraphSeries<>(dataPoints);
            graph.addSeries(series);
            series.setSpacing(40);
            staticLabelsFormatter.setHorizontalLabels(hLabels);
            glr.setLabelFormatter(staticLabelsFormatter);
            series.setColor(getResources().getColor(R.color.primaryDarkColor));
            glr.setLabelHorizontalHeight((size>60)?300:40);

        }
        else
        {
            staticLabelsFormatter.setHorizontalLabels(hLabels);
            series.resetData(dataPoints);
        }
        showGraph();

    }

    private ArrayList<Record> getFilledRecords()
    {
        ArrayList<Record> filledRecords = new ArrayList<>();
        Date date = parent.mRecords.get(0).getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        int size = parent.mRecords.size();
        int id = 0;
        for(int i = 0; i < size; i++)
        {
            Record record = parent.mRecords.get(id);
            c.add(Calendar.DATE, -1);
            if (record.getDate().compareTo(c.getTime())==0)
            {
                Log.d("", "setGraph: "+record.getDate());
                filledRecords.add(record);
                id++;
            }
            else
            {
                filledRecords.add(new Record(c.getTime(),0));
                size++;
            }
        }
        Collections.reverse(filledRecords);
        return filledRecords;
    }


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
        if(count>=2)setGraph();
        else hideGraph();
        tv_count.setText(this.getString(R.string.count,count));
        if(count!=0){
            tv_min.setText(this.getString(R.string.min,parent.formatNoLastZero.format(min)));
            tv_max.setText(this.getString(R.string.max,parent.formatNoLastZero.format(max)));
            tv_sum.setText(this.getString(R.string.sum,parent.formatNoLastZero.format(sum)));
            tv_avg.setText(this.getString(R.string.avg,parent.formatNoLastZero.format(avg)));
        }
        else
        {
            tv_min.setText(this.getString(R.string.min,"-"));
            tv_max.setText(this.getString(R.string.max,"-"));
            tv_sum.setText(this.getString(R.string.sum,"-"));
            tv_avg.setText(this.getString(R.string.avg,"-"));
        }
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
