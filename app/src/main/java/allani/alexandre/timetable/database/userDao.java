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
public interface userDao {
    @Query("SELECT * FROM user")
    List<user> getAll();

    @Query("SELECT * FROM user WHERE KAIST_ID LIKE :userid AND PASS LIKE :pwd AND Prof LIKE :prof")
    user authenticate(String userid, String pwd, Boolean prof);

    @Query("SELECT * FROM user WHERE KAIST_ID LIKE :userid")
    user getuserFromId(String userid);

    @Update
    void update(user usr);

    @Insert
    void insert(user usr);

    @Delete
    void delete(user usr);
}
