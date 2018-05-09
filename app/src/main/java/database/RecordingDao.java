package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecordingDao {

    @Insert
    void insert(RecordingEntity recording);

    @Update
    void update(RecordingEntity recording);

    @Delete
    void delete(RecordingEntity recording);

    @Query("DELETE FROM recording_table")
    void deleteAll();

    @Query("SELECT * from recording_table ORDER BY recordingId DESC")
    List<RecordingEntity> getAllRecordings();

    @Query("SELECT * FROM recording_table WHERE first_name LIKE :search "
            + "OR last_name LIKE :search")
    List<RecordingEntity> searchName(String search);

    @Query("SELECT * FROM recording_table WHERE recordingId = :search")
    List<RecordingEntity> searchId(int search);

    @Query("SELECT * FROM recording_table WHERE recordingId LIKE :search")
    List<RecordingEntity> searchTitle(String search);

}