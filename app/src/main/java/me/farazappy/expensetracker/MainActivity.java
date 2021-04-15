package me.farazappy.expensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.farazappy.expensetracker.helpers.DatabaseHelper;
import me.farazappy.expensetracker.helpers.DayAdapter;
import me.farazappy.expensetracker.helpers.SessionManager;
import me.farazappy.expensetracker.models.Day;
import me.farazappy.expensetracker.models.Item;

public class MainActivity extends AppCompatActivity implements DayAdapter.OnClickListener {

    final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        setupView();
    }

    SessionManager sessionManager;
    DatabaseHelper databaseHelper;
    DayAdapter dayAdapter;

    List<Day> days = new ArrayList<Day>();

    LinearLayout mainLayout;
    TextView noTrackers, totalSpent;
    RecyclerView recyclerView;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mainLayout = findViewById(R.id.mainView);
        noTrackers = findViewById(R.id.noTrackers);
        totalSpent = findViewById(R.id.totalSpent);
        recyclerView = findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(this);
        dayAdapter = new DayAdapter(this, days, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dayAdapter);

        TextView helloText = findViewById(R.id.displayName);
        helloText.setText("Welcome " + sessionManager.getUser().getDisplayName());

        setupView();

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String MyFormat = "EEEE - dd MMMM, yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(MyFormat, Locale.getDefault());

                createDay(sdf.format(myCalendar.getTime()));
            }
        };


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        FloatingActionButton graphFabDaily = findViewById(R.id.fabGraph);
        graphFabDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG,"graph button clicked");
                startActivity(new Intent(MainActivity.this,DailyChartActivity.class));
            }
        });
    }

    private void setupView() {
        days.clear();
        days.addAll(databaseHelper.getAllDays());
        dayAdapter.notifyDataSetChanged();

        if(days.isEmpty()) {
            mainLayout.setVisibility(View.GONE);
            noTrackers.setVisibility(View.VISIBLE);
        } else {

            double totalCost = 0;

            for (Item item : databaseHelper.getAllItems()) {
                totalCost += item.getCost();
            }

            String firstDay = days.get(0).getCreatedAt();
            CharSequence str = "";

            long epochTimeStamp = 0;
            try {
                epochTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(firstDay).getTime();
                Date date = new Date(epochTimeStamp);
                SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM d, yyyy", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                str = sdf.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            totalSpent.setText("You've spent a total of ₹"+totalCost + " since " + str);

            noTrackers.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private void createDay(String dayName) {
        Day day = databaseHelper.createDay(new Day(dayName));
        onClick(day);
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Maybe later bro", Toast.LENGTH_LONG).show();
            sessionManager.logOut(false, null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Day day) {
        Intent i = new Intent(this, DayActivity.class);
        i.putExtra("day", day);
        startActivity(i);
    }
}
