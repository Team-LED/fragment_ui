package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {RecordingEntity.class}, version = 1,exportSchema = false)
public abstract class RecordingDatabase extends RoomDatabase {
    private static RecordingDatabase db;
    public abstract RecordingDao RecordingDao();

    public static RecordingDatabase getRecordingDatabase(Context context){
        if(db == null){
            db = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RecordingDatabase.class,
                    "recording-db").allowMainThreadQueries().build();
        }
        return db;
    }

    public static void destroyInstance(){
        db = null;
    }


}
