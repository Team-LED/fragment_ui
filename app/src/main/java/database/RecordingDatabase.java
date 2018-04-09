package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RecordingEntity.class}, version = 1)
public abstract class RecordingDatabase extends RoomDatabase {
    public abstract RecordingDao RecordingDao();
}
