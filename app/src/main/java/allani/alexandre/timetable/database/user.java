package allani.alexandre.timetable.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by alexa on 31/05/2018.
 */

@Entity(indices = {@Index(value = {"KAIST_ID"},unique = true)})
public class user {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "Prof")
    private Boolean prof;

    @ColumnInfo(name = "KAIST_ID")
    private String kaist_id;

    @ColumnInfo(name = "PASS")
    private String pass;

    @ColumnInfo(name = "LISTEVENT")
    private String listevent;

    public user(String kaist_id, String pass, Boolean prof) {
        this.kaist_id = kaist_id;
        this.pass = pass;
        this.prof = prof;
        this.listevent ="";
    }

    public String getListevent() {
        return listevent;
    }

    public void setListevent(String listevent) {
        this.listevent = listevent;
    }

    public Boolean getProf() {
        return prof;
    }

    public void setProf(Boolean prof) {
        this.prof = prof;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getKaist_id() {
        return kaist_id;
    }

    public void setKaist_id(String kaist_id) {
        this.kaist_id = kaist_id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
