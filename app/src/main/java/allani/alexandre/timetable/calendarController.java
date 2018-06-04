package allani.alexandre.timetable;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

import allani.alexandre.timetable.database.PersonalEvent;

/**
 * Created by alexa on 30/05/2018.
 */

public class calendarController extends AppCompatActivity{

    boolean quit=false;
    int year;
    int month;
    int day;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_calendar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        uid = getIntent().getStringExtra("uid");
        Log.d("Debug",""+uid);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                quit = false;
                year = i;
                month = i1;
                day = i2;
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                Intent myIntent = new Intent(calendarController.this,eventController.class);
                myIntent.putExtra("year",i);
                myIntent.putExtra("month",i1);
                myIntent.putExtra("day",i2);
                myIntent.putExtra("weekDay",dayOfWeek);
                myIntent.putExtra("uid",uid);
                startActivity(myIntent);

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        quit = false;
        switch (item.getItemId()){
            case R.id.menu_setting:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_create:
                Intent myIntent = new Intent(this, createfirst.class);
                myIntent.putExtra("uid",uid);
                startActivity(myIntent);
                return true;
            case R.id.menu_modify:
                Intent myInttent = new Intent(this, modifyfirstController.class);
                myInttent.putExtra("uid",uid);
                startActivity(myInttent);
                return true;
            case R.id.menu_notification:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_logout:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onBackPressed(){
        if(quit){
            finish();
            moveTaskToBack(true);
        }
        else{
            Toast.makeText(calendarController.this,"Press Back Button again to exit", Toast.LENGTH_SHORT).show();
            quit = true;
        }
    }
}
