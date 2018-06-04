package allani.alexandre.timetable.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Created by alexa on 31/05/2018.
 */

@Entity
public class PersonalEvent extends Event {

    @ColumnInfo(name = "START_TIME")
    private String start_time;

    @ColumnInfo(name = "END_TIME")
    private String end_time;

    @ColumnInfo(name = "LOCAITON")
    private String location;



    public PersonalEvent(String uid, String event_name, String perso_id, boolean priority, String description, boolean homework, boolean course, boolean personal_event,
                         String start_time, String end_time, String location,String date) {
        super(uid,event_name,perso_id,priority,description,homework,course,personal_event,date);
        this.end_time = end_time;
        this.start_time = start_time;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
