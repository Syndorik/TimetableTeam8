package allani.alexandre.timetable.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alexa on 31/05/2018.
 */

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "EID")
    private int eid;

    @ColumnInfo(name = "UID")
    private String uid;

    @ColumnInfo(name = "EVENT_NAME")
    private String event_name;

    @ColumnInfo(name = "PERSO_ID")
    private String perso_id;

    @ColumnInfo(name = "PRIORITY")
    private boolean priority;

    @ColumnInfo(name = "DESCRIPTION")
    private String description;

    @ColumnInfo(name = "HOMEWORK")
    private boolean homework;

    @ColumnInfo(name = "COURSE")
    private boolean course;

    @ColumnInfo(name = "PERSONAL_EVENT")
    private boolean personal_event;

    @ColumnInfo(name = "DATE")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Event(){

    }

    public Event(String uid, String event_name, String perso_id, boolean priority, String description, boolean homework, boolean course, boolean personal_event, String date) {
        this.uid = uid;
        this.event_name = event_name;
        this.perso_id = perso_id;
        this.priority = priority;
        this.description = description;
        this.homework = homework;
        this.course = course;
        this.personal_event = personal_event;
        this.date = date;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getPerso_id() {
        return perso_id;
    }

    public void setPerso_id(String perso_id) {
        this.perso_id = perso_id;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHomework() {
        return homework;
    }

    public void setHomework(boolean homework) {
        this.homework = homework;
    }

    public boolean isCourse() {
        return course;
    }

    public void setCourse(boolean course) {
        this.course = course;
    }

    public boolean isPersonal_event() {
        return personal_event;
    }

    public void setPersonal_event(boolean personal_event) {
        this.personal_event = personal_event;
    }
}
