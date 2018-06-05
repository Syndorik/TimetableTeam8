package allani.alexandre.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by alexa on 30/05/2018.
 */

public class createfirst extends AppCompatActivity {

    CalendarView mCalendarView;
    int year;
    int month;
    int day;
    String uid;
    boolean ret = false;
    boolean priority;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createfirst);
        mCalendarView = (CalendarView) findViewById(R.id.calendarevent);
        uid = getIntent().getStringExtra("uid");
        priority = getIntent().getBooleanExtra("Priority",false);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                ret = false;
                year = i;
                month = i1;
                day = i2;
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                Intent myIntent = new Intent(createfirst.this,createsecondController.class);
                myIntent.putExtra("year",i);
                myIntent.putExtra("month",i1);
                myIntent.putExtra("day",i2);
                myIntent.putExtra("weekDay",dayOfWeek);
                myIntent.putExtra("uid",uid);
                myIntent.putExtra("Priority",priority);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(ret){
            Intent myIntent = new Intent(createfirst.this,calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",priority);
            startActivity(myIntent);
        }
        else{
            Toast.makeText(createfirst.this,"Press Back Button again to return to main menu", Toast.LENGTH_SHORT).show();
            ret = true;
        }
    }
}