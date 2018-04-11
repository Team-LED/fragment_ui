package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecordingDao {

    @Insert
    void insert(RecordingEntity recording);

    @Query("DELETE FROM recording_table")
    void deleteAll();

    @Query("SELECT * from recording_table ORDER BY first_name ASC")
    List<RecordingEntity> getAllRecordings();

    @Query("SELECT * FROM recording_table WHERE first_name LIKE :search "
            + "OR last_name LIKE :search")
    List<RecordingEntity> searchName(String search);

}
