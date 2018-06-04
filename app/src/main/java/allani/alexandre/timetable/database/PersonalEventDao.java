package allani.alexandre.timetable.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by alexa on 31/05/2018.
 */


@Dao
public interface PersonalEventDao {

    @Query("SELECT * FROM PersonalEvent")
    List<PersonalEvent> getAll();

    @Query("SELECT * FROM PersonalEvent WHERE UID LIKE :kid AND DATE LIKE :date AND EVENT_NAME LIKE:evname AND START_TIME LIKE :start_time")
    PersonalEvent getEvent(String kid, String date, String evname, String start_time);

    @Query("SELECT *FROM PersonalEvent WHERE EID LIKE :eid")
    PersonalEvent getEventFromEID(int eid);

    @Update
    void UpdatePersonalEvent(PersonalEvent personalEvent);

    @Insert
    void insert_event(PersonalEvent event);

    @Delete
    void delete_event(PersonalEvent event);
}
