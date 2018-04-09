package database;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

public interface RecordingDao {

    @Insert
    void insert(RecordingEntity recording);

    @Query("DELETE FROM recording_table")
    void deleteAll();

    @Query("SELECT * from recording_table ORDER BY first_name ASC")
    List<RecordingEntity> getAllWords();
}
