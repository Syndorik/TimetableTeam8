package allani.alexandre.timetable.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by alexa on 31/05/2018.
 */


@Dao
public interface EventDao {
    @Query("SELECT * FROM Event")
    List<Event> getAll();

    @Query("SELECT * FROM Event WHERE UID LIKE :uid AND DATE LIKE :date AND EVENT_NAME LIKE:evname ")
    Event getEvent(int uid, String date, String evname);

    @Insert
    void insert_event(Event event);

    @Delete
    void delete_event(Event event);
}
