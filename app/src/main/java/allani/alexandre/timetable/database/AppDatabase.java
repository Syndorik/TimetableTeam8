package allani.alexandre.timetable.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by alexa on 31/05/2018.
 */
@Database(entities = {user.class,Event.class,PersonalEvent.class}, version = 39)
public abstract class AppDatabase extends RoomDatabase {
    public abstract userDao mUserDao();
    public abstract EventDao mEventDao();
    public abstract PersonalEventDao mPersonalEventDao();

}
