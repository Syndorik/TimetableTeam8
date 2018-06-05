package allani.alexandre.timetable;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.PersonalEvent;
import allani.alexandre.timetable.database.user;

/**
 * Created by alexa on 30/05/2018.
 */

public class createsecondController extends AppCompatActivity {

    int year;
    int month;
    int day;
    int weekday;
    String uid;
    boolean ret = false;
    boolean pprio;

    boolean parse = true;

    Boolean priority = false;
    Boolean homework = false;
    Boolean personalEvent = true;
    Boolean course = false;
    TextView mdateoftoday;
    TextView mstart;
    TextView mend;


    Button mCreate;
    AppDatabase db;

    EditText mname;
    EditText maddperso;
    EditText mdescription;
    EditText mLocation;


    Button mstart_hour;
    Button mend_hour;

    String name;
    String start_hour;
    String end_hour;
    String addperso ="";
    String description;
    String location;
    String[] perso_id;
    String date;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsecond);

        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();

        year = getIntent().getIntExtra("year",0);
        month = getIntent().getIntExtra("month",0);
        day = getIntent().getIntExtra("day",0);
        weekday = getIntent().getIntExtra("weekDay",0);
        mdateoftoday = (TextView) findViewById(R.id.txt_dot2);

        mname = (EditText) findViewById(R.id.et_event_name);
        mstart_hour = (Button) findViewById(R.id.bt_start_hour);
        mend_hour = (Button) findViewById(R.id.bt_ending_hour);
        maddperso = (EditText) findViewById(R.id.et_addperso);
        mdescription = (EditText) findViewById(R.id.et_description);
        mLocation = (EditText) findViewById(R.id.et_Location);
        mCreate = (Button) findViewById(R.id.b_create);
        mstart = (TextView) findViewById(R.id.txt_startH);
        mend = (TextView) findViewById(R.id.txt_endH);
        uid = getIntent().getStringExtra("uid");
        pprio = getIntent().getBooleanExtra("Priority",false);
        date = ""+day+"/"+month+"/"+year;






        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mname.getText().toString();
                start_hour = mstart.getText().toString();
                end_hour = mend.getText().toString();
                description = mdescription.getText().toString();
                location = mLocation.getText().toString();
                perso_id = maddperso.getText().toString().split(System.getProperty("line.separator"));
                new createsecondController.AsyncTaskRunner().execute("c","c","c");




            }
        });

        mstart_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        mend_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog2(view);
            }
        });

        update();

    }

    /*
        For timepicking
     */
    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }
    public void showTimePickerDialog2(View v) {
        TimePickerFragments newFragment = new TimePickerFragments();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    /*
        Update some views
     */

    public void update(){
        String t_month = new DateFormatSymbols().getMonths()[month];
        String t_date = new DateFormatSymbols().getShortWeekdays()[weekday];
        String toshow = t_date+" "+ day + " "+ t_month+" "+year;
        mdateoftoday.setText(toshow);
    }




    private class AsyncTaskRunner extends AsyncTask<String,String,String> {
        boolean go_on = true;
        boolean alone = true;
        boolean isname = true;
        boolean isloc = true;
        boolean isDesc = true;
        boolean isStartT = true;
        boolean isEndT = true;
        String nullman ="";
        user usr;
        String listev;

        @Override
        protected String doInBackground(String... strings) {
            convert(perso_id);
            check();
            if(go_on && isname && isloc && isDesc && isStartT && isEndT){
                PersonalEvent pe = new PersonalEvent(uid,
                        name,addperso,priority,description,
                        homework,course,personalEvent,start_hour,end_hour,location,date);
                db.mPersonalEventDao().insert_event(pe);

                List<PersonalEvent> lpe = db.mPersonalEventDao().getAll();
                pe = db.mPersonalEventDao().getEvent(uid,date,name,start_hour);


                if(!alone) {
                    for (int k = 0; k < perso_id.length; k++) {
                        user usr = db.mUserDao().getuserFromId(perso_id[k]);
                        listev = usr.getListevent() + pe.getEid() + "\n";
                        usr.setListevent(listev);
                        db.mUserDao().update(usr);
                    }
                }
                usr = db.mUserDao().getuserFromId(uid);
                listev = usr.getListevent()+pe.getEid()+"\n";
                usr.setListevent(listev);
                db.mUserDao().update(usr);

                printdb();
                Intent myIntent = new Intent(createsecondController.this, calendarController.class);
                myIntent.putExtra("uid",uid);
                myIntent.putExtra("Priority",pprio);
                startActivity(myIntent);





            }

            return null;
        }

        public void check(){
            if(name.isEmpty()){
                isname = false;
            }
            if(location.isEmpty()){
                isloc = false;
            }
            if(description.isEmpty()){
                isDesc = false;
            }
            if(start_hour.isEmpty()){
                isStartT = false;
            }
            if(end_hour.isEmpty()){
                isEndT = false;
            }
            publishProgress();
        }

        public void printdb(){
            List<user> lu = db.mUserDao().getAll();

            for(int k=0; k<lu.size(); k++){
                Log.d("Debug","usr : "+lu.get(k).getKaist_id());
                Log.d("Debug","EID : "+lu.get(k).getListevent());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(!isname){
                Toast.makeText(createsecondController.this, "Please Insert a Name", Toast.LENGTH_SHORT).show();

            }
            if(!isloc){
                Toast.makeText(createsecondController.this, "Please Insert a Location", Toast.LENGTH_SHORT).show();

            }
            if(!isDesc){
                Toast.makeText(createsecondController.this, "Please Insert a Description", Toast.LENGTH_SHORT).show();

            }
            if(!isStartT){
                Toast.makeText(createsecondController.this, "Please Insert a Start Hour", Toast.LENGTH_SHORT).show();

            }
            if(!isEndT){
                Toast.makeText(createsecondController.this, "Please Insert a End Hour", Toast.LENGTH_SHORT).show();

            }
            if(!go_on) {
                Toast.makeText(createsecondController.this, "The person with KAIST ID : "+nullman+", you're trying to add doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }

        public void convert(String[] persos){

            persos = new HashSet<String>(Arrays.asList(persos)).toArray(new String[0]);
            Log.d("Debug"," "+persos[0]);
            if((persos.length !=1) || (persos.length==1 && !persos[0].equals(""))) {
                alone = false;
                for (int k = 0; k < persos.length; k++) {
                    if (go_on) {
                        if ((usr = db.mUserDao().getuserFromId(persos[k])) != null) {
                            addperso += "" + usr.getKaist_id() + "\n";
                        } else {
                            go_on = false;
                            addperso = "";
                            nullman = persos[k];
                            publishProgress(persos[k]);
                        }
                    } else {
                        break;

                    }

                }
            }
        }
    }






    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        static String toprint;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @TargetApi(23)
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String cc = String.valueOf(view.getMinute());
            String cc2 = String.valueOf(view.getHour());
            if(view.getMinute()>=0 && view.getMinute()<=9){
                cc = "0"+ view.getMinute();
            }
            if(view.getHour()>=0 && view.getHour()<=9){
                cc2 = "0"+view.getHour();
            }
            toprint = cc2 +":" + cc;
            TextView tv1 = (TextView) getActivity().findViewById(R.id.txt_startH);
            tv1.setText(toprint);
        }
    }

    public static class TimePickerFragments extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        static String toprint;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @TargetApi(23)
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String cc = String.valueOf(view.getMinute());
            String cc2 = String.valueOf(view.getHour());
            if(view.getMinute()>=0 && view.getMinute()<=9){
                cc = "0"+ view.getMinute();
            }
            if(view.getHour()>=0 && view.getHour()<=9){
                cc2 = "0"+view.getHour();
            }
            toprint = cc2 +":" + cc;
            TextView tv1 = (TextView) getActivity().findViewById(R.id.txt_endH);
            tv1.setText(toprint);
        }
    }

    @Override
    public void onBackPressed(){
        if(ret){
            Intent myIntent = new Intent(createsecondController.this,calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",pprio);
            startActivity(myIntent);
        }
        else{
            Toast.makeText(createsecondController.this,"Press Back Button again to return to main menu", Toast.LENGTH_SHORT).show();
            ret = true;
        }
    }
}
