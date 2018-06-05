package allani.alexandre.timetable;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import allani.alexandre.timetable.Calendarevie.EventDecorator;
import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.PersonalEvent;
import allani.alexandre.timetable.database.user;

/**
 * Created by alexa on 30/05/2018.
 */

public class calendarController extends AppCompatActivity{

    boolean quit=false;
    int year;
    int month;
    int dday;
    boolean priority;
    String dofthed;
    String uid;
    AppDatabase db;
    MaterialCalendarView calendarView;
    EditText pop_name;
    EditText pop_pass;
    EditText pop_pass_conf;
    CheckBox cb_prof;
    Button bt_cancel;
    Button bt_add;
    PopupWindow pw;

    EditText et_pop_del_name;
    Button bt_pop_del_cancel;
    Button bt_pop_del_del;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_calendar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();

        uid = getIntent().getStringExtra("uid");
        priority = getIntent().getBooleanExtra("Priority",false);
        Log.d("Debug",""+uid);

        CalendarDay Curday = CalendarDay.today();
        dofthed = Curday.getDay()+"/"+Curday.getMonth()+"/"+Curday.getYear();




        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                quit = false;
                year = date.getYear();
                month = date.getMonth();
                dday = date.getDay();
                Calendar c = Calendar.getInstance();
                c.set(year,month,dday);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                Intent myIntent = new Intent(calendarController.this,eventController.class);
                myIntent.putExtra("year",year);
                myIntent.putExtra("month",month);
                myIntent.putExtra("day",dday);
                myIntent.putExtra("weekDay",dayOfWeek);
                myIntent.putExtra("uid",uid);
                myIntent.putExtra("Priority",priority);
                startActivity(myIntent);
            }
        });

        new calendarController.AsyncTaskRunnerUpdateUI().execute("c","c","c");


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(priority){
            inflater.inflate(R.menu.menu_prof,menu);
        }else {
            inflater.inflate(R.menu.menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        quit = false;
        if(priority) {
            switch (item.getItemId()) {
                case R.id.menu_setting:
                    return true;
                case R.id.menu_create:
                    Intent myIntent = new Intent(this, createfirst.class);
                    myIntent.putExtra("uid", uid);
                    myIntent.putExtra("Priority",priority);
                    startActivity(myIntent);
                    return true;
                case R.id.menu_modify:
                    Intent myInttent = new Intent(this, modifyfirstController.class);
                    myInttent.putExtra("uid", uid);
                    myInttent.putExtra("Priority",priority);
                    startActivity(myInttent);
                    return true;
                case R.id.menu_addID:
                    //We need to get the instance of the LayoutInflater, use the context of this activity
                    LayoutInflater inflater = (LayoutInflater) calendarController.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
                    View layout = inflater.inflate(R.layout.popupadd_layout,null);

                    pop_name = (EditText) layout.findViewById(R.id.et_pop_name);
                    pop_pass = (EditText) layout.findViewById(R.id.et_pop_pwd1);
                    pop_pass_conf = (EditText) layout.findViewById(R.id.et_pop_pwd2);
                    cb_prof = (CheckBox) layout.findViewById(R.id.cb_pop_prof);
                    bt_add = (Button) layout.findViewById(R.id.bt_pop_add);
                    bt_cancel =(Button) layout.findViewById(R.id.bt_pop_cancel);
                    Log.d("Debug","GERE");

                    float density=calendarController.this.getResources().getDisplayMetrics().density;
                    // create a focusable PopupWindow with the given layout and correct size
                    pw = new PopupWindow(layout,(int)density*300, (int)density*600, true);

                    pw.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    bt_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new calendarController.AsyncTaskAddPerso().execute("c","c","c");

                        }
                    });

                    bt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pw.dismiss();
                        }
                    });
                    pw.setOutsideTouchable(true);
                    pw.showAtLocation(layout, Gravity.CENTER, 0,0);

                    return true;
                case R.id.menu_notification:
                    new calendarController.AsyncTaskGetNotif().execute("c", "c", "c");
                    return true;
                case R.id.menu_delete:
                    LayoutInflater inflater2 = (LayoutInflater) calendarController.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
                    View layout2 = inflater2.inflate(R.layout.popupdel_layout,null);

                    et_pop_del_name = (EditText) layout2.findViewById(R.id.et_pop_del_name);
                    bt_pop_del_del = (Button) layout2.findViewById(R.id.bt_pop_del_del);
                    bt_pop_del_cancel = (Button) layout2.findViewById(R.id.bt_pop_del_cancel);
                    float density2 =calendarController.this.getResources().getDisplayMetrics().density;
                    pw = new PopupWindow(layout2,(int)density2*300, (int)density2*600, true);

                    pw.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    bt_pop_del_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pw.dismiss();

                        }
                    });

                    bt_pop_del_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new calendarController.AsyncTaskDel().execute("c","c","c");

                        }
                    });
                    pw.setOutsideTouchable(true);
                    pw.showAtLocation(layout2, Gravity.CENTER, 0,0);

                    return true;
                case R.id.menu_logout:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;

                default:
                    return true;
            }
        }
        else {
            switch (item.getItemId()) {
                case R.id.menu_setting:
                    return true;
                case R.id.menu_create:
                    Intent myIntent = new Intent(this, createfirst.class);
                    myIntent.putExtra("uid", uid);
                    myIntent.putExtra("Priority",priority);
                    startActivity(myIntent);
                    return true;
                case R.id.menu_modify:
                    Intent myInttent = new Intent(this, modifyfirstController.class);
                    myInttent.putExtra("uid", uid);
                    myInttent.putExtra("Priority",priority);
                    startActivity(myInttent);
                    return true;
                case R.id.menu_notification:
                    new calendarController.AsyncTaskGetNotif().execute("c", "c", "c");
                    return true;
                case R.id.menu_logout:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;

                default:
                    return true;
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(quit){
            moveTaskToBack(true);
        }
        else{
            Toast.makeText(calendarController.this,"Press Back Button again to exit", Toast.LENGTH_SHORT).show();
            quit = true;
        }
    }



    private class AsyncTaskDel extends AsyncTask<String,String,String> {
        List<user> lu = new ArrayList<>();
        boolean donedel = false;
        PersonalEvent pe;
        String[] slpe;
        String[] lue;
        List<String> lpe;
        StringBuilder bb;
        String[] nlu;
        List<String> nlpe;

        @Override
        protected String doInBackground(String... strings) {
            donedel = false;
            lu = db.mUserDao().getAll();
            String nametodel = et_pop_del_name.getText().toString();
            user usr = db.mUserDao().getuserFromId(nametodel);

            slpe = usr.getListevent().split(System.getProperty("line.separator"));
            if(!usr.getListevent().isEmpty()) {
                for (int k = 0; k < slpe.length; k++) {
                    pe =db.mPersonalEventDao().getEventFromEID(Integer.parseInt(slpe[k]));

                    if(pe.getUid().equals(usr.getKaist_id())){
                        db.mPersonalEventDao().delete_event(pe);
                        nlu = pe.getPerso_id().split(System.getProperty("line.separator"));
                        for(int l =0; l< nlu.length; l++){
                            user nusr = db.mUserDao().getuserFromId(nlu[l]);
                            nlpe = new ArrayList<>(Arrays.asList(nusr.getListevent().split(System.getProperty("line.separator"))));
                            nlpe.remove(String.valueOf(pe.getEid()));
                            bb= new StringBuilder("");
                            for(int j =0; j< nlpe.size(); j++){
                                bb.append(nlpe.get(j)+"\n");
                            }
                            nusr.setListevent(bb.toString());
                            db.mUserDao().update(nusr);
                        }

                    }else{
                        if(!pe.getPerso_id().isEmpty()){
                            lue= pe.getPerso_id().split(System.getProperty("line.separator"));
                            lpe = new ArrayList<>(Arrays.asList(lue));
                            lpe.remove(nametodel);

                            bb= new StringBuilder("");
                            for(int j =0; j< lpe.size(); j++){
                                bb.append(lpe.get(j)+"\n");
                            }

                            pe.setPerso_id(bb.toString());
                            db.mPersonalEventDao().UpdatePersonalEvent(pe);
                        }
                        else{
                            Log.d("Debug","THIS IS IMPOSSIBLEEEEEEEE");
                        }
                    }

                }
            }
            if(usr != null){
                db.mUserDao().delete(usr);
                donedel = true;
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
            if(donedel){
                pw.dismiss();
            }else{
                Toast.makeText(calendarController.this,"Name entered is not in database", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    private class AsyncTaskAddPerso extends AsyncTask<String,String,String> {
        boolean notsamepass = true;
        boolean usrexist = true;
        boolean usrempty = true;
        boolean done= false;
        List<user> lu = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            done = false;
            usrexist = false;
            String name = pop_name.getText().toString();
            String pass1 = pop_pass.getText().toString();
            String pass2 = pop_pass_conf.getText().toString();
            lu = db.mUserDao().getAll();


            user usr = new user(name,pass1,cb_prof.isChecked());

            usrempty = name.isEmpty();
            for(int k=0; k<lu.size();k++){
                if(lu.get(k).getKaist_id().equals(name)){
                    usrexist =true;
                }
            }
            notsamepass = !pass1.equals(pass2);


            if(!usrempty && !usrexist && !notsamepass) {
                db.mUserDao().insert(usr);
                done = true;
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
            if(notsamepass){
                Toast.makeText(calendarController.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
            }
            if(usrempty){
                Toast.makeText(calendarController.this,"Please provide a name",Toast.LENGTH_SHORT).show();
            }
            if(usrexist){
                Toast.makeText(calendarController.this,"User already exist in database",Toast.LENGTH_SHORT).show();
            }
            if(done){
                pw.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }



    private class AsyncTaskGetNotif extends AsyncTask<String,String,String> {
        List<PersonalEvent> lpeofToday= new ArrayList<PersonalEvent>();
        StringBuilder message = new StringBuilder("########\n");
        @Override
        protected String doInBackground(String... strings) {
            getlisteventofToday();

            for(int k =0; k<lpeofToday.size(); k++){
                String[] date= lpeofToday.get(k).getDate().split("/");
                message.append("PERSONAL EVENT" + "\n"
                        + "Event name : " + lpeofToday.get(k).getEvent_name() + "\n"
                        + "Event ID (EID): " + lpeofToday.get(k).getEid() + "\n"
                        + "Owner: " + lpeofToday.get(k).getUid() + "\n"
                        + "People in this event:\n " + lpeofToday.get(k).getPerso_id() + "\n"
                        + "Date " + date[0]+"/"+(Integer.parseInt(date[1])+1)+"/"+date[2] + "\n"
                        + "Starting hour: " + lpeofToday.get(k).getStart_time() + "\n"
                        + "Ending hour: " + lpeofToday.get(k).getEnd_time() + "\n\n"
                        + "Description: \n" + lpeofToday.get(k).getDescription() + "\n"
                        + "#######\n");
            }

            return null;
        }

        private void getlisteventofToday(){
            user usr = db.mUserDao().getuserFromId(uid);
            PersonalEvent pe;
            String lpe = usr.getListevent();
            String[] listPersonalEvent = lpe.split(System.getProperty("line.separator"));
            if(!lpe.isEmpty()) {
                for (int k = 0; k < listPersonalEvent.length; k++) {
                    pe = db.mPersonalEventDao().getEventFromEID(Integer.parseInt(listPersonalEvent[k]));
                    if(pe.getDate().equals(dofthed)){
                        lpeofToday.add(pe);
                    }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(calendarController.this);
            builder.setMessage(message.toString())
                    .setTitle("Notifications\n")
                    .setPositiveButton("Return to main menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
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
                calendarView.addDecorator(new EventDecorator(Color.RED, dates));
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
