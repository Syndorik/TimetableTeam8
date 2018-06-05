package allani.alexandre.timetable;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.PersonalEvent;
import allani.alexandre.timetable.database.user;


public class eventController extends AppCompatActivity{
    int year;
    int month;
    int day;
    int weekday;
    String uid;
    TextView mdateoftoday;
    AppDatabase db;
    String[] lev;
    List<PersonalEvent> listev;
    String[] dateofev;
    PersonalEvent anev;

    TextView txt_ev1;
    TextView txt_loc1;

    TextView txt_ev2;
    TextView txt_loc2;

    TextView txt_ev3;
    TextView txt_loc3;

    TextView txt_ev4;
    TextView txt_loc4;

    TextView txt_ev5;
    TextView txt_loc5;

    TextView txt_ev6;
    TextView txt_loc6;

    TextView txt_ev7;
    TextView txt_loc7;

    TextView txt_ev8;
    TextView txt_loc8;

    TextView txt_ev9;
    TextView txt_loc9;

    TextView txt_ev10;
    TextView txt_loc10;

    TextView txt_ev11;
    TextView txt_loc11;

    TextView txt_ev12;
    TextView txt_loc12;

    TextView txt_ev13;
    TextView txt_loc13;

    TextView txt_ev14;
    TextView txt_loc14;

    TextView txt_ev15;
    TextView txt_loc15;

    TextView txt_ev16;
    TextView txt_loc16;

    TextView txt_ev17;
    TextView txt_loc17;

    Button button_event;
    boolean priority;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        txt_ev1 = findViewById(R.id.txt_ev1);
        txt_loc1 = findViewById(R.id.txt_loc1);

        txt_ev2 = findViewById(R.id.txt_ev2);
        txt_loc2 = findViewById(R.id.txt_loc2);

        txt_ev3 = findViewById(R.id.txt_ev3);
        txt_loc3 = findViewById(R.id.txt_loc3);

        txt_ev4 = findViewById(R.id.txt_ev4);
        txt_loc4 = findViewById(R.id.txt_loc4);

        txt_ev5 = findViewById(R.id.txt_ev5);
        txt_loc5 = findViewById(R.id.txt_loc5);

        txt_ev6 = findViewById(R.id.txt_ev6);
        txt_loc6 = findViewById(R.id.txt_loc6);

        txt_ev7 = findViewById(R.id.txt_ev7);
        txt_loc7 = findViewById(R.id.txt_loc7);

        txt_ev8 = findViewById(R.id.txt_ev8);
        txt_loc8 = findViewById(R.id.txt_loc8);

        txt_ev9 = findViewById(R.id.txt_ev9);
        txt_loc9 = findViewById(R.id.txt_loc9);

        txt_ev10 = findViewById(R.id.txt_ev10);
        txt_loc10 = findViewById(R.id.txt_loc10);

        txt_ev11 = findViewById(R.id.txt_ev11);
        txt_loc11 = findViewById(R.id.txt_loc11);

        txt_ev12 = findViewById(R.id.txt_ev12);
        txt_loc12 = findViewById(R.id.txt_loc12);

        txt_ev13 = findViewById(R.id.txt_ev13);
        txt_loc13 = findViewById(R.id.txt_loc13);

        txt_ev14 = findViewById(R.id.txt_ev14);
        txt_loc14 = findViewById(R.id.txt_loc14);

        txt_ev15 = findViewById(R.id.txt_ev15);
        txt_loc15 = findViewById(R.id.txt_loc15);

        txt_ev16 = findViewById(R.id.txt_ev16);
        txt_loc16 = findViewById(R.id.txt_loc16);

        txt_ev17 = findViewById(R.id.txt_ev17);
        txt_loc17 = findViewById(R.id.txt_loc17);

        button_event = findViewById(R.id.button_event);





        year = getIntent().getIntExtra("year",0);
        month = getIntent().getIntExtra("month",0);
        day = getIntent().getIntExtra("day",0);
        weekday = getIntent().getIntExtra("weekDay",0);
        uid = getIntent().getStringExtra("uid");
        priority = getIntent().getBooleanExtra("Priority",false);
        mdateoftoday = (TextView) findViewById(R.id.txt_dot);
        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();

        new eventController.AsyncTaskRunner().execute("c","c","c");

        button_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new eventController.AsyncTaskoth().execute("c","c","c");

            }
        });





    }

    public void update(){
        String t_month = new DateFormatSymbols().getMonths()[month];
        String t_date = new DateFormatSymbols().getShortWeekdays()[weekday];
        String toshow = t_date+" "+ day + " "+ t_month+" "+year;
        mdateoftoday.setText(toshow);
    }

    public void updateTable(){
        String[] rstart;
        String[] rend;
        String name;
        String location;
        int start_hour;
        int end_hour;
        PersonalEvent theev;

        for(int k=0; k<listev.size(); k++){
            theev = listev.get(k);
            rstart = theev.getStart_time().split(":");
            rend = theev.getEnd_time().split(":");
            name = theev.getEvent_name();
            location = theev.getLocation();


            start_hour = Integer.parseInt(rstart[0]);
            end_hour = Integer.parseInt(rend[0]);

            Log.d("Debug"," start time : "+start_hour);
            Log.d("Debug", " end time : "+end_hour);

            convert(start_hour,name,location);
            convert(end_hour,name,location);

        }

    }

    public void convert(int H, String name, String loc){
        if((H>=0) && (H<=7) ){
            txt_ev1.setText(name);
            txt_loc1.setText(loc);
        }
        else if(H== 8){
            txt_ev2.setText(name);
            txt_loc2.setText(loc);
        }
        else if(H== 9){
            txt_ev3.setText(name);
            txt_loc3.setText(loc);
        }
        else if(H== 10){
            txt_ev4.setText(name);
            txt_loc4.setText(loc);
        }
        else if(H== 11){
            txt_ev5.setText(name);
            txt_loc5.setText(loc);
        }
        else if(H== 12){
            txt_ev6.setText(name);
            txt_loc6.setText(loc);
        }
        else if(H== 13){
            txt_ev7.setText(name);
            txt_loc7.setText(loc);
        }
        else if(H== 14){
            txt_ev8.setText(name);
            txt_loc8.setText(loc);
        }
        else if(H== 15){
            txt_ev9.setText(name);
            txt_loc9.setText(loc);
        }
        else if(H== 16){
            txt_ev10.setText(name);
            txt_loc10.setText(loc);
        }
        else if(H== 17){
            txt_ev11.setText(name);
            txt_loc11.setText(loc);
        }
        else if(H== 18){
            txt_ev12.setText(name);
            txt_loc12.setText(loc);
        }
        else if(H== 19){
            txt_ev13.setText(name);
            txt_loc13.setText(loc);
        }
        else if(H== 20){
            txt_ev14.setText(name);
            txt_loc14.setText(loc);
        }
        else if(H== 20){
            txt_ev14.setText(name);
            txt_loc14.setText(loc);
        }
        else if(H== 21){
            txt_ev15.setText(name);
            txt_loc15.setText(loc);
        }
        else if(H== 22){
            txt_ev16.setText(name);
            txt_loc16.setText(loc);
        }
        else if(H== 23){
            txt_ev17.setText(name);
            txt_loc17.setText(loc);
        }
    }

    private class AsyncTaskoth extends android.os.AsyncTask<String,String,String>{
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
            Log.d("Debug", ""+values[0]);
            AlertDialog.Builder builder = new AlertDialog.Builder(eventController.this);
            builder.setMessage(values[0])
                    .setTitle("Events\n")
                    .setPositiveButton("OK(no choice sorry)", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CONFIRM
                        }
                    })
                    .setNegativeButton("Ok ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CANCEL
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {
            listev = new ArrayList<PersonalEvent>();
            user usr = db.mUserDao().getuserFromId(uid);
            StringBuilder message = new StringBuilder("########\n");
            String the_message;

            lev = usr.getListevent().split(System.getProperty("line.separator"));
            if(lev.length !=1 ||(lev.length == 1 && !lev[0].equals(""))) {
                for (int k = 0; k < lev.length; k++) {
                    anev = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(lev[k]));
                    dateofev = anev.getDate().split("/");
                    if (anev.isPersonal_event() && (Integer.parseInt(dateofev[0]) == day) && (Integer.parseInt(dateofev[1]) == month) && (Integer.parseInt(dateofev[2]) == year)) {
                        message.append("PERSONAL EVENT" + "\n"
                                + "Event name : " + anev.getEvent_name() + "\n"
                                + "Event ID (EID): " + anev.getEid() + "\n"
                                + "Owner: " + anev.getUid() + "\n\n"
                                + "People in this event:\n" + anev.getPerso_id() + "\n"
                                + "Date " + day+"/"+(month+1)+"/"+year + "\n"
                                + "Starting hour: " + anev.getStart_time() + "\n"
                                + "Ending hour: " + anev.getEnd_time() + "\n\n"
                                + "Description: \n" + anev.getDescription() + "\n"
                                + "#######\n");
                    }
                }
            }
            the_message = message.toString();
            publishProgress(the_message);

            return null;
        }
    }

    private class AsyncTaskRunner extends android.os.AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            listev = new ArrayList<PersonalEvent>();

            user usr = db.mUserDao().getuserFromId(uid);

            update();

            lev = usr.getListevent().split(System.getProperty("line.separator"));
            Log.d("Debug","list event : "+usr.getListevent());
            if(!usr.getListevent().equals("")) {
                for (int k = 0; k < lev.length; k++) {
                    anev = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(lev[k]));
                    dateofev = anev.getDate().split("/");
                    if ((Integer.parseInt(dateofev[0]) == day) && (Integer.parseInt(dateofev[1]) == month) && (Integer.parseInt(dateofev[2]) == year)) {
                        listev.add(anev);
                    }
                }
            }

            if(!listev.isEmpty()){
                updateTable();
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
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(eventController.this,"Wrong ID/Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent myIntent = new Intent(eventController.this, calendarController.class);
        myIntent.putExtra("uid",uid);
        myIntent.putExtra("Priority",priority);
        startActivity(myIntent);
    }


}
