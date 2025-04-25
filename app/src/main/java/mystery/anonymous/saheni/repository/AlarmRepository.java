package mystery.anonymous.saheni.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import mystery.anonymous.saheni.model.AlarmEntity;

import java.util.List;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<AlarmEntity>> allAlarms;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
    }

    public void insert(AlarmEntity alarm) {
        new InsertAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void update(AlarmEntity alarm) {
        new UpdateAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void delete(AlarmEntity alarm) {
        new DeleteAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return allAlarms;
    }

    private static class InsertAlarmAsyncTask extends AsyncTask<AlarmEntity, Void, Void> {
        private AlarmDao alarmDao;

        private InsertAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmEntity... alarms) {
            alarmDao.insert(alarms[0]);
            return null;
        }
    }

    // Similar AsyncTasks for update and delete
}