package com.pnu.godofdatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        float write_snappy = intent.getFloatExtra("write_snappy",0);
        float read_snappy = intent.getFloatExtra("read_snappy",0);
        float write_sqlite = intent.getFloatExtra("write_sqlite",0);
        float read_sqlite = intent.getFloatExtra("read_sqlite",0);
        float write_god = intent.getFloatExtra("write_god",0);
        float read_god = intent.getFloatExtra("read_god",0);

        chart = (BarChart) findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(true);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        ArrayList<BarEntry> snappy = new ArrayList<>();
        snappy.add(new BarEntry(1,write_snappy));
        snappy.add(new BarEntry(2,read_snappy));

        ArrayList<BarEntry> god = new ArrayList<>();
        god.add(new BarEntry(1,write_god));
        god.add(new BarEntry(2,read_god));

        ArrayList<BarEntry> sqlite = new ArrayList<>();
        sqlite.add(new BarEntry(1,write_sqlite));
        sqlite.add(new BarEntry(2,read_sqlite));


        BarDataSet dataSet1, dataSet2, dataSet3;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            dataSet1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            dataSet2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            dataSet3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
            dataSet1.setValues(snappy);
            dataSet2.setValues(god);
            dataSet3.setValues(sqlite);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            dataSet1 = new BarDataSet(snappy, "SnappyDB");
            dataSet1.setColor(Color.rgb(104, 241, 175));
            dataSet2 = new BarDataSet(god, "GOD DB");
            dataSet2.setColor(Color.rgb(255, 136, 251));
            dataSet3 = new BarDataSet(sqlite, "SQlite DB");
            dataSet3.setColor(Color.rgb(164, 228, 251));

            BarData data = new BarData(dataSet1, dataSet2, dataSet3);
            data.setValueFormatter(new LargeValueFormatter());

            chart.setData(data);
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        ArrayList<String> xAxisValues = new ArrayList<String>();
        xAxisValues.add("put");
        xAxisValues.add("get");
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(10);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        chart.getBarData().setBarWidth(0.2f);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(0.3f, 0.02f) * 2);
        chart.groupBars(0, 0.3f, 0.02f);
        chart.animateY(3000);
        chart.invalidate();
    }
}
