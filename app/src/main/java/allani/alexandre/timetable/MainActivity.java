package allani.alexandre.timetable;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import allani.alexandre.timetable.database.AppDatabase;
import allani.alexandre.timetable.database.user;
import allani.alexandre.timetable.database.userDao;

/*
            user usr1 = new user("20186024","alexall",false);
            user usr2 = new user("20186025","roxall",false);
            user usr3 = new user("admin","admin",true);

            db.mUserDao().insert(usr1);
            db.mUserDao().insert(usr2);
            db.mUserDao().insert(usr3);

 */

public class MainActivity extends AppCompatActivity {
    Button mCoButton;
    EditText mName;
    EditText mPass;
    CheckBox mCheckBox;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoButton = (Button) findViewById(R.id.button_co);
        db =  Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"mydb").fallbackToDestructiveMigration().build();



        mCoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = (EditText) findViewById(R.id.et_name);
                mPass = (EditText) findViewById(R.id.et_pass);
                mCheckBox = (CheckBox) findViewById(R.id.checkBox);
                new AsyncTaskRunner().execute("c","c","c");
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            List<user> lu = db.mUserDao().getAll();
            if(lu.isEmpty()) {
                user usr1 = new user("20186024", "alexall", false);
                user usr2 = new user("20186025", "roxall", false);
                user usr3 = new user("admin", "admin", true);
                user usr4 = new user("aaa", "aaa", true);

                db.mUserDao().insert(usr1);
                db.mUserDao().insert(usr2);
                db.mUserDao().insert(usr3);
                db.mUserDao().insert(usr4);
            }
            /**/

            user usr = new user(mName.getText().toString(), mPass.getText().toString(),mCheckBox.isChecked());
            if((usr = db.mUserDao().authenticate(usr.getKaist_id(),usr.getPass(),usr.getProf()) ) != null){
                Intent myIntent = new Intent(MainActivity.this, calendarController.class);
                printdb();
                myIntent.putExtra("uid",usr.getKaist_id());
                myIntent.putExtra("Priority",usr.getProf());
                startActivity(myIntent);

            }
            else{
                publishProgress();
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
            Toast.makeText(MainActivity.this,"Wrong ID/Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void printdb(){
        List<user> lu = db.mUserDao().getAll();

        for(int k=0; k<lu.size(); k++){
            Log.d("Debug","usr : "+lu.get(k).getKaist_id());
            Log.d("Debug","EID : "+lu.get(k).getListevent());
            Log.d("Debug","Pwd : "+lu.get(k).getPass());
            Log.d("Debug","Prof : "+lu.get(k).getProf());
            Log.d("Debug","------------------------------");
        }
    }


}
