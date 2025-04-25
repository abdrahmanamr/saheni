package mystery.anonymous.saheni.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mystery.anonymous.saheni.model.AlarmEntity;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAlarm(AlarmEntity alarm);

    @Update
    void updateAlarm(AlarmEntity alarm);

    @Delete
    void deleteAlarm(AlarmEntity alarm);

    @Query("SELECT * FROM alarms ORDER BY alarmTime ASC")
    LiveData<List<AlarmEntity>> getAllAlarms();

    @Query("SELECT * FROM alarms WHERE isActive=1") List<AlarmEntity> getActiveAlarms();
    @Query("SELECT * FROM alarms WHERE exactDate BETWEEN :start AND :end")
    List<AlarmEntity> getAlarmsInTimeRange(long start, long end);


    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    AlarmEntity getAlarmById(int alarmId);

    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY alarmTime ASC")
    List<AlarmEntity> getActiveAlarms();

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :alarmId")
    void setAlarmEnabled(int alarmId, boolean isEnabled);
}