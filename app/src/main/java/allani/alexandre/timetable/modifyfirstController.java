package allani.alexandre.timetable;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import allani.alexandre.timetable.Calendarevie.EventDecorator;
import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.PersonalEvent;
import allani.alexandre.timetable.database.user;

/**
 * Created by alexa on 30/05/2018.
 */

public class modifyfirstController extends AppCompatActivity {

    MaterialCalendarView mCalendarView;
    int year;
    int month;
    int day;
    boolean ret = false;
    String datee;
    String uid;
    AppDatabase db;
    int dayOfWeek;
    boolean priority;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyfirst);
        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendarevent2);
        uid = getIntent().getStringExtra("uid");
        priority = getIntent().getBooleanExtra("Priority",false);
        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();


        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                ret = false;
                year = date.getYear();
                month = date.getMonth();
                day = date.getDay();
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                datee = day+"/"+month+"/"+year;
                new modifyfirstController.AsyncTaskRunner().execute("c","c","c");
            }
        });

        new modifyfirstController.AsyncTaskRunnerUpdateUI().execute("c","c","c");

        /*
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                ret = false;
                year = i;
                month = i1;
                day = i2;
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                date = day+"/"+month+"/"+year;
                new modifyfirstController.AsyncTaskRunner().execute("c","c","c");


            }
        });*/
    }

    @Override
    public void onBackPressed(){
        if(ret){
            Intent myIntent = new Intent(modifyfirstController.this,calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",priority);
            startActivity(myIntent);
        }
        else{
            Toast.makeText(modifyfirstController.this,"Press Back Button again to return to main menu", Toast.LENGTH_SHORT).show();
            ret = true;
        }
    }




    private class AsyncTaskRunner extends AsyncTask<String,String,String> {
        String ok;

        @Override
        protected String doInBackground(String... strings) {
            if(isEmpty()){
                ok ="1";
            }
            else {
                ok="0";
            }

            return null;
        }

        private boolean isEmpty(){
            user usr = db.mUserDao().getuserFromId(uid);
            boolean cond = true;
            String[] anev = usr.getListevent().split(System.getProperty("line.separator"));
            if(usr.getListevent().isEmpty()){
                cond = true;
            }
            else {
                for (int k = 0; k < anev.length; k++) {
                    PersonalEvent ev = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(anev[k]));
                    if (ev.getDate().equals(datee)) {
                        cond = false;
                        break;
                    }
                }
            }
            return cond;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(ok .equals("1")){
                AlertDialog.Builder builder = new AlertDialog.Builder(modifyfirstController.this);
                builder.setMessage("There are no event to modify on this date. Dates marked by a red dot contains event that can be modified")
                        .setTitle("NO EVENT ON THIS DATE\n")
                        .setPositiveButton("Return to main menu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(modifyfirstController.this,calendarController.class);
                                myIntent.putExtra("uid",uid);
                                myIntent.putExtra("Priority",priority);
                                startActivity(myIntent);
                            }
                        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
            else{
                Intent myIntent = new Intent(modifyfirstController.this,modifyController.class);
                myIntent.putExtra("year",year);
                myIntent.putExtra("month",month);
                myIntent.putExtra("day",day);
                myIntent.putExtra("weekDay",dayOfWeek);
                myIntent.putExtra("uid",uid);
                myIntent.putExtra("Priority",priority);
                startActivity(myIntent);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    private class AsyncTaskRunnerUpdateUI extends AsyncTask<String,String,String> {
        List<PersonalEvent> mylpe = new ArrayList<>();
        ArrayList<CalendarDay> dates = new ArrayList<>();
        boolean listevempty = true;
        PersonalEvent pe;

        @Override
        protected String doInBackground(String... strings) {
            int day;
            int month;
            int year;
            String[] theD;
            user usr = db.mUserDao().getuserFromId(uid);
            String spe[] = usr.getListevent().split(System.getProperty("line.separator"));
            if(!usr.getListevent().isEmpty()) {
                listevempty = false;
                for (int k = 0; k < spe.length; k++) {
                    pe = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(spe[k]));
                    mylpe.add(pe);
                }
                //We have all events of the guy
                for (int k = 0; k < mylpe.size(); k++) {
                    theD = mylpe.get(k).getDate().split("/");
                    day = Integer.parseInt(theD[0]);
                    month = Integer.parseInt(theD[1]);
                    year = Integer.parseInt(theD[2]);

                    dates.add(CalendarDay.from(year, month, day));
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!listevempty) {
                mCalendarView.addDecorator(new EventDecorator(Color.RED, dates));
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
