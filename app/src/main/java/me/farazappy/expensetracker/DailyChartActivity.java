package me.farazappy.expensetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.farazappy.expensetracker.helpers.DatabaseHelper;
import me.farazappy.expensetracker.models.Day;
import me.farazappy.expensetracker.models.Item;

public class DailyChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_chart);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        HashMap<Integer,Double> costMonthHashMap = new HashMap<Integer,Double>();
        for(int i=0;i<databaseHelper.getAllItems().size();i++)
        {
            long dayId = databaseHelper.getAllItems().get(i).getDayId();
            Day day = databaseHelper.getDay(dayId);
            int month = day.getMonth();
            if (costMonthHashMap.containsKey(month))
            {
                costMonthHashMap.put(month,costMonthHashMap.get(month)+databaseHelper.getAllItems().get(i).getCost());
            }
            else {
                costMonthHashMap.put(month,databaseHelper.getAllItems().get(i).getCost());
            }
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] dataPoints = new DataPoint[costMonthHashMap.size()];
        int i=0;
        for(int month:costMonthHashMap.keySet())
        {
            dataPoints[i] = new DataPoint(month,costMonthHashMap.get(month));
            i++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        graph.addSeries(series);
    }
}