package mystery.anonymous.saheni.viewmodel;

import android.app.Application;
import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import mystery.anonymous.saheni.model.AlarmEntity;
import mystery.anonymous.saheni.repository.AlarmRepository;
import mystery.anonymous.saheni.service.AlarmService;
import mystery.anonymous.saheni.service.NotificationHelper;
import mystery.anonymous.saheni.utils.AlarmValidator;

public class AlarmViewModel extends AndroidViewModel {

    private final AlarmRepository repository;
    private final LiveData<List<AlarmEntity>> allAlarms;
    private final MutableLiveData<AlarmEntity> currentAlarm = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        allAlarms = repository.getAllAlarms();
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() { return repository.getAllAlarms(); }

    public void insertOrUpdate(AlarmEntity alarm) {
        repository.scheduleAlarm(alarm);
    }
    public void delete(AlarmEntity alarm) {
        repository.cancelAlarm(alarm);
        repository.deleteAlarm(alarm);
    }


    public LiveData<AlarmEntity> getCurrentAlarm() {
        return currentAlarm;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    private void scheduleAlarm(AlarmEntity alarm) {
        AlarmService alarmService = new AlarmService(getApplication());
        alarmService.scheduleAlarm(alarm);

        // إرسال إشعار
        String timeUntil = getFormattedTimeUntilAlarm(alarm.getAlarmTime());
        if (timeUntil != null) {
            NotificationHelper notificationHelper = new NotificationHelper(getApplication());
            Notification notification = notificationHelper.createAlarmNotification(
                    "تم جدولة المنبه",
                    "المنبه سيعمل بعد " + timeUntil
            );
            notificationHelper.showNotification((int) alarm.getId(), notification);
        }
    }

    public void setCurrentAlarm(AlarmEntity alarm) {
        currentAlarm.setValue(alarm);
    }

    public void saveAlarm(AlarmEntity alarm) {
        // التحقق من صحة إعدادات المنبه
        if (!AlarmValidator.isAlarmValid(alarm)) {
            errorMessage.setValue("إعدادات المنبه غير صالحة، تأكد من تعبئة جميع الحقول المطلوبة");
            return;
        }

        // التحقق من كون وقت المنبه في المستقبل
        AlarmEntity validatedAlarm = AlarmValidator.validateAlarmTime(alarm);

        // حفظ المنبه في قاعدة البيانات
        if (validatedAlarm.getId() == 0) {
            // منبه جديد
            repository.insertAlarm(validatedAlarm, savedAlarm -> {
                currentAlarm.postValue(savedAlarm);
                successMessage.postValue("تم حفظ المنبه بنجاح");
                scheduleAlarm(savedAlarm);
            });
        } else {
            // تحديث منبه موجود
            repository.updateAlarm(validatedAlarm);
            currentAlarm.setValue(validatedAlarm);
            successMessage.setValue("تم تحديث المنبه بنجاح");
            scheduleAlarm(validatedAlarm);
        }
    }

    public void deleteAlarm(AlarmEntity alarm) {
        repository.deleteAlarm(alarm);
        cancelAlarm(alarm);
        successMessage.setValue("تم حذف المنبه بنجاح");
    }

    public void loadAlarm(int alarmId) {
        repository.getAlarmById(alarmId, alarm -> {
            if (alarm != null) {
                currentAlarm.postValue(alarm);
            } else {
                errorMessage.postValue("لم يتم العثور على المنبه");
            }
        });
    }

    public void toggleAlarmEnabled(AlarmEntity alarm, boolean isEnabled) {
        alarm.setEnabled(isEnabled);
        repository.updateAlarm(alarm);

        if (isEnabled) {
            scheduleAlarm(alarm);
            successMessage.setValue("تم تفعيل المنبه");
        } else {
            cancelAlarm(alarm);
            successMessage.setValue("تم إلغاء تفعيل المنبه");
        }
    }

    /**
     * جدولة المنبه باستخدام AlarmManager
     * @param alarm المنبه المراد جدولته
     */
    private void scheduleAlarm(AlarmEntity alarm) {
        // تنفيذ في كلاس AlarmService
    }

    /**
     * إلغاء جدولة المنبه
     * @param alarm المنبه المراد إلغاؤه
     */
    private void cancelAlarm(AlarmEntity alarm) {
        // تنفيذ في كلاس AlarmService
    }

    /**
     * حساب الوقت المتبقي للمنبه وتنسيقه كنص
     * @param alarmTime وقت المنبه
     * @return نص يمثل الوقت المتبقي
     */
    public String getFormattedTimeUntilAlarm(Date alarmTime) {
        long secondsUntilAlarm = AlarmValidator.getTimeUntilAlarmInSeconds(alarmTime);

        // إذا كان الوقت المتبقي أكثر من 24 ساعة أو في الماضي، نرجع null
        if (secondsUntilAlarm < 0 || secondsUntilAlarm > 86400) {
            return null;
        }

        return AlarmValidator.formatTimeUntilAlarm(secondsUntilAlarm);
    }
}