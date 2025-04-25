package mystery.anonymous.saheni.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import mystery.anonymous.saheni.model.AlarmEntity;

@Database(entities = {AlarmEntity.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "alarm_database";
    private static AlarmDatabase instance;

    public abstract AlarmDao alarmDao();

    @TypeConverters({Converters.class})
    public abstract class AppDatabase extends RoomDatabase { … }


    public static synchronized AlarmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AlarmDatabase.class,
                            DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}