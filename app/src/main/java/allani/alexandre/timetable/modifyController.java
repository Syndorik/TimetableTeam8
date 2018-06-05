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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.PersonalEvent;
import allani.alexandre.timetable.database.user;

/**
 * Created by alexa on 31/05/2018.
 */

public class modifyController extends AppCompatActivity {

    int year;
    int month;
    int day;
    int weekday;
    String date;
    TextView mdateoftoday;
    Button mModify;
    Button mDelete;
    List<String> HeaderInfo;
    AppDatabase db;
    String uid;
    List<PersonalEvent> lpersonaleventoftoday;
    ExpandableListView expandableListView;
    Button mstart_hour;
    Button mend_hour;

    Button mnewb;

    EditText et_addpero;
    EditText et_location;
    EditText et_description;
    TextView startH;
    TextView endH;
    PersonalEvent theEvent;

    ScrollView sc;
    boolean priority;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        year = getIntent().getIntExtra("year",0);
        month = getIntent().getIntExtra("month",0);
        day = getIntent().getIntExtra("day",0);
        weekday = getIntent().getIntExtra("weekDay",0);
        mdateoftoday = (TextView) findViewById(R.id.txt_m_dot2);
        date = ""+day+"/"+month+"/"+year;
        uid = getIntent().getStringExtra("uid");
        priority = getIntent().getBooleanExtra("Priority",false);
        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();
        sc = (ScrollView) findViewById(R.id.scrollView2);




        expandableListView = (ExpandableListView)findViewById(R.id.lt_m_event);

        HeaderInfo = new ArrayList<String>();
        lpersonaleventoftoday = new ArrayList<PersonalEvent>();
        HeaderInfo.add("Event's Name");
        new modifyController.AsyncTaskGetEvent().execute("c","c","c");














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


    public void update(){
        String t_month = new DateFormatSymbols().getMonths()[month-1];
        String t_date = new DateFormatSymbols().getShortWeekdays()[weekday];
        String toshow = t_date+" "+ day + " "+ t_month+" "+year;
        mdateoftoday.setText(toshow);
    }

    public void initNewb(){
        mnewb = (Button) findViewById(R.id.bt_newb);

        mnewb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new modifyController.AsyncTaskDeleteNewb().execute("c","c","c");
            }
        });
    }

    public void initOwner(){

        et_addpero = (EditText) findViewById(R.id.et_m_addperso);
        et_description = (EditText) findViewById(R.id.et_m_description);
        et_location = (EditText) findViewById(R.id.et_m_Location);
        startH = (TextView) findViewById(R.id.txt_m_startH);
        endH = (TextView) findViewById(R.id.txt_m_endH);


        mstart_hour = (Button) findViewById(R.id.bt_m_start_hour);
        mend_hour = (Button) findViewById(R.id.bt_m_ending_hour);

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

        mModify = (Button) findViewById(R.id.b_m_modify);
        mModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new modifyController.AsyncTasModify().execute("c","c","c");

            }
        });

        mDelete = (Button) findViewById(R.id.bt_m_delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new modifyController.AsyncTaskDelete().execute("c","c","c");


            }
        });

    }




    private class AsyncTasModify extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String ll = theEvent.getPerso_id();
            String[] luser = theEvent.getPerso_id().split(System.getProperty("line.separator"));
            getInfo();
            String[] nluser = theEvent.getPerso_id().split(System.getProperty("line.separator"));
            String[] listusrev;
            boolean isInsidnluser =false;
            boolean isnew = true;
            int eidd = theEvent.getEid();
            List<String> persotoadd = new ArrayList<String>();


            //Add perso to Event
            if(!theEvent.getPerso_id().isEmpty()) {
                for (int u = 0; u < nluser.length; u++) {
                    for (int j = 0; j < luser.length; j++) {
                        if (nluser[u].equals(luser[j])) {
                            isnew = false;
                        }
                    }
                    if (isnew) {
                        persotoadd.add(nluser[u]);
                    }
                    isnew = true;
                }

                for (int j = 0; j < persotoadd.size(); j++) {
                    user ausr = db.mUserDao().getuserFromId(persotoadd.get(j));
                    ausr.setListevent(ausr.getListevent() + theEvent.getEid() + "\n");
                    db.mUserDao().update(ausr);
                }
            }

            printdb();
            Log.d("Debugd","lusr "+ll);
            Log.d("Debugd","nlusr "+theEvent.getPerso_id());


            if(!ll.isEmpty()) {
                for (int k = 0; k < luser.length; k++) {
                    isInsidnluser =false;
                    StringBuilder finaList = new StringBuilder("");
                    for (int l = 0; l < nluser.length; l++) {
                        if (luser[k].equals(nluser[l])) {
                            isInsidnluser = true;
                            break;
                        }
                    }
                    Log.d("Debuga","lusr"+luser[k]);
                    Log.d("Debuga"," "+isInsidnluser);
                    if (!isInsidnluser) {
                        Log.d("Debuggg","Here : "+ luser[k]);
                        user usr = db.mUserDao().getuserFromId(luser[k]);
                        listusrev = usr.getListevent().split(System.getProperty("line.separator"));
                        for (int i = 0; i < listusrev.length; i++) {
                            if (Integer.parseInt(listusrev[i]) != eidd) {
                                finaList.append(listusrev[i] + "\n");
                            } else {
                                Log.d("Debug", "lusr : " + listusrev[i] + " eid : " + eidd);
                            }
                        }
                        Log.d("Debug", "striing :" + finaList);
                        usr.setListevent(finaList.toString());
                        db.mUserDao().update(usr);
                    }
                }
            }




            db.mPersonalEventDao().UpdatePersonalEvent(theEvent);
            Log.d("Debug","Event updated");

            Intent myIntent = new Intent(modifyController.this, calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",priority);
            startActivity(myIntent);
            return null;
        }

        private void getInfo(){
            theEvent.setStart_time(startH.getText().toString());
            theEvent.setEnd_time(endH.getText().toString());
            theEvent.setPerso_id(et_addpero.getText().toString());
            theEvent.setLocation(et_location.getText().toString());
            theEvent.setDescription(et_description.getText().toString());
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
        }
    }





    private class AsyncTaskDeleteNewb extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Debug","Myevent : "+theEvent.getEvent_name());
            int eidd =theEvent.getEid();
            String[] listusrev;
            StringBuilder finaList = new StringBuilder("");


            user theusr = db.mUserDao().getuserFromId(uid);
            listusrev = theusr.getListevent().split(System.getProperty("line.separator"));
            for(int i =0; i<listusrev.length; i++){
                if(Integer.parseInt(listusrev[i]) != eidd){
                    finaList.append(listusrev[i]+"\n");
                }else{
                    Log.d("Debug","lusrev : "+ listusrev[i] + " eid : "+ eidd);
                }
            }
            theusr.setListevent(finaList.toString());
            Log.d("Debug","striing :"+finaList);



            String[] luser = theEvent.getPerso_id().split(System.getProperty("line.separator"));
            finaList = new StringBuilder("");
            for(int k=0; k<luser.length;k++){
                if(!luser[k].equals(uid)){
                    finaList.append(luser[k]+"\n");
                }else {
                    Log.d("Debug","lusr : "+ luser[k] + " uid : "+ uid);
                }
            }


            Log.d("Debug","striing :"+finaList);
            theEvent.setPerso_id(finaList.toString());

            db.mPersonalEventDao().UpdatePersonalEvent(theEvent);
            db.mUserDao().update(theusr);


            Intent myIntent = new Intent(modifyController.this, calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",priority);
            startActivity(myIntent);

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
        }
    }



    private class AsyncTaskDelete extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Debug","Myevent : "+theEvent.getEvent_name());
            db.mPersonalEventDao().delete_event(theEvent);
            int eidd =theEvent.getEid();
            String[] luser = theEvent.getPerso_id().split(System.getProperty("line.separator"));
            String[] listusrev;

            if(!theEvent.getPerso_id().isEmpty()) {
                for (int k = 0; k < luser.length; k++) {
                    StringBuilder finaList = new StringBuilder("");
                    user usr = db.mUserDao().getuserFromId(luser[k]);
                    listusrev = usr.getListevent().split(System.getProperty("line.separator"));
                    for (int i = 0; i < listusrev.length; i++) {
                        if (Integer.parseInt(listusrev[i]) != eidd) {
                            finaList.append(listusrev[i] + "\n");
                        } else {
                            Log.d("Debug", "lusr : " + listusrev[i] + " eid : " + eidd);
                        }
                    }
                    Log.d("Debug", "striing :" + finaList);
                    usr.setListevent(finaList.toString());
                    db.mUserDao().update(usr);
                }
            }

            StringBuilder finaList = new StringBuilder("");
            user theusr = db.mUserDao().getuserFromId(uid);
            listusrev = theusr.getListevent().split(System.getProperty("line.separator"));
            for(int i =0; i<listusrev.length; i++){
                if(Integer.parseInt(listusrev[i]) != eidd){
                    finaList.append(listusrev[i]+"\n");
                }else{
                    Log.d("Debug","lusr : "+ listusrev[i] + " eid : "+ eidd);
                }
            }
            Log.d("Debug","striing :"+finaList);
            theusr.setListevent(finaList.toString());
            db.mUserDao().update(theusr);


            Intent myIntent = new Intent(modifyController.this, calendarController.class);
            myIntent.putExtra("uid",uid);
            myIntent.putExtra("Priority",priority);
            startActivity(myIntent);

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
        }
    }




    private class AsyncTaskGetEvent extends AsyncTask<String,String,String> {
        String[] peusr;
        user usr;
        PersonalEvent anev;
        HashMap<String, List<String>> childContent = new HashMap<String, List<String>>();


        @Override
        protected String doInBackground(String... strings) {

            getlistEvent();

            HashMap<String, List<String>> allChildItems = returnGroupedChildItems();
            ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getApplicationContext(), HeaderInfo, allChildItems);
            expandableListView.setAdapter(expandableListViewAdapter);

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    TextView tv = view.findViewById(R.id.child_layout);
                    String text = tv.getText().toString();
                    Log.d("Debug"," "+text);
                    theEvent = getEvent(text);
                    if(uid.equals(theEvent.getUid())){
                        sc.removeAllViews();
                        ViewGroup parent = (ViewGroup) findViewById(R.id.scrollView2);
                        View c = getLayoutInflater().inflate(R.layout.owneroption,parent,false);
                        parent.addView(c);
                        initOwner();
                        upEv(theEvent);
                    }else {
                        sc.removeAllViews();
                        ViewGroup parent = (ViewGroup) findViewById(R.id.scrollView2);
                        View c = getLayoutInflater().inflate(R.layout.newbieoption,parent,false);
                        parent.addView(c);
                        initNewb();

                    }
                    HeaderInfo.set(0,theEvent.getEvent_name());
                    HashMap<String, List<String>> allChildItems = returnGroupedChildItems();
                    ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getApplicationContext(), HeaderInfo, allChildItems);
                    expandableListView.setAdapter(expandableListViewAdapter);
                    expandableListView.collapseGroup(0);



                    return true;

                }
            });


            return null;
        }

        private void upEv(PersonalEvent ev){
            et_addpero.setText(ev.getPerso_id());
            et_description.setText(ev.getDescription());
            et_location.setText(ev.getLocation());
            startH.setText(ev.getStart_time());
            endH.setText(ev.getEnd_time());
        }

        private PersonalEvent getEvent(String text){
            PersonalEvent eventToRet = null;
            Log.d("Debug",""+text.split(":")[0]);
            int anid = Integer.parseInt(text.split(":")[0]);
            for(int k =0; k<lpersonaleventoftoday.size();k++){
                if(lpersonaleventoftoday.get(k).getEid() == anid){
                    eventToRet = lpersonaleventoftoday.get(k);
                    break;
                }
            }
            if(eventToRet == null){
                Log.d("Debug","null eventToRet");
            }
            return eventToRet;
        }

        private HashMap<String, List<String>> returnGroupedChildItems(){
            List<String> event = new ArrayList<String>();
            for(int i =0; i< lpersonaleventoftoday.size(); i++){
                event.add( lpersonaleventoftoday.get(i).getEid()+":"+lpersonaleventoftoday.get(i).getEvent_name());
            }
            childContent.put(HeaderInfo.get(0), event);
            return childContent;
        }


        private void getlistEvent(){
            usr = db.mUserDao().getuserFromId(uid);
            peusr = usr.getListevent().split(System.getProperty("line.separator"));
            for(int k =0; k< peusr.length; k++){
                anev = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(peusr[k]));
                if(anev.getDate().equals(date) && anev.isPersonal_event()){
                    lpersonaleventoftoday.add(anev);
                }
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
            TextView tv1 = (TextView) getActivity().findViewById(R.id.txt_m_startH);
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
            TextView tv1 = (TextView) getActivity().findViewById(R.id.txt_m_endH);
            tv1.setText(toprint);
        }
    }


    public void printdb(){
        List<user> lu = db.mUserDao().getAll();

        for(int k=0; k<lu.size(); k++){
            Log.d("Debug","usr : "+lu.get(k).getKaist_id());
            Log.d("Debug","EID : "+lu.get(k).getListevent());
        }
        Log.d("Debug","###############");

        List<PersonalEvent> le = db.mPersonalEventDao().getAll();

        for(int j=0; j<le.size(); j++){
            Log.d("Debug", "EID : "+ le.get(j).getEid());
            Log.d("Debug","Perso ID: "+ le.get(j).getPerso_id());
        }
    }


    @Override
    public void onBackPressed(){

        Intent myIntent = new Intent(modifyController.this,calendarController.class);
        myIntent.putExtra("uid",uid);
        myIntent.putExtra("Priority",priority);
        startActivity(myIntent);

    }
}
