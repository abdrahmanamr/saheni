package mystery.anonymous.saheni.repository;

import android.app.Application;
import android.content.Context;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mystery.anonymous.saheni.model.AlarmEntity;

public class AlarmRepository {
    private final AlarmDao alarmDao;
    private final LiveData<List<AlarmEntity>> allAlarms;
    private final ExecutorService executorService;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return allAlarms;
    }

    public void scheduleAlarm(AlarmEntity alarm) {
        if (alarm.isInPast()) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            c.set(Calendar.MINUTE, alarm.getMinute());
            c.add(Calendar.DAY_OF_MONTH, 1);
            alarm.setExactDate(c.getTimeInMillis());
        }
        scheduleAlarmWithAlarmManager(alarm);
        saveAlarm(alarm);
    }
    // إضافة:
    public String getDefaultRingtone(Context ctx) {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    }


    public void insertAlarm(AlarmEntity alarm, OnAlarmSavedListener listener) {
        executorService.execute(() -> {
            try {
                long id = alarmDao.insertAlarm(alarm);
                alarm.setId(id);
                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onAlarmSaved(alarm));
                }
            } catch (Exception e) {
                Log.e("AlarmRepository", "Error inserting alarm", e);
            }
        });
    }

    public void updateAlarm(AlarmEntity alarm) {
        executorService.execute(() -> alarmDao.updateAlarm(alarm));
    }

    public void deleteAlarm(AlarmEntity alarm) {
        executorService.execute(() -> alarmDao.deleteAlarm(alarm));
    }

    public void getAlarmById(int alarmId, OnAlarmLoadedListener listener) {
        executorService.execute(() -> {
            AlarmEntity alarm = alarmDao.getAlarmById(alarmId);
            if (listener != null) {
                listener.onAlarmLoaded(alarm);
            }
        });
    }

    public void getActiveAlarms(OnActiveAlarmsLoadedListener listener) {
        executorService.execute(() -> {
            List<AlarmEntity> activeAlarms = alarmDao.getActiveAlarms();
            if (listener != null) {
                listener.onActiveAlarmsLoaded(activeAlarms);
            }
        });
    }

    public void setAlarmEnabled(int alarmId, boolean isEnabled) {
        executorService.execute(() -> alarmDao.setAlarmEnabled(alarmId, isEnabled));
    }

    public interface OnAlarmSavedListener {
        void onAlarmSaved(AlarmEntity alarm);
    }

    public interface OnAlarmLoadedListener {
        void onAlarmLoaded(AlarmEntity alarm);
    }

    public interface OnActiveAlarmsLoadedListener {
        void onActiveAlarmsLoaded(List<AlarmEntity> alarms);
    }
}