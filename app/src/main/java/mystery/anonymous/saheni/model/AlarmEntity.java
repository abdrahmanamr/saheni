package mystery.anonymous.saheni.model;

import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

import mystery.anonymous.saheni.utils.DateTimeConverter;

@Entity(tableName = "alarms")
@TypeConverters(DateTimeConverter.class)
public class AlarmEntity {
    @PrimaryKey(autoGenerate = true) private int id;
    private int hour, minute;
    private boolean isActive, isRepeat;
    private boolean[] repeatDays = new boolean[7];
    private long exactDate; // للتنبيهات لمرة واحدة

    private String label, ringtonePath;

    public boolean isValidTime() {
        return hour>=0 && hour<=23 && minute>=0 && minute<=59;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isInPast() {
        if (isRepeat) return false;
        Calendar c = Calendar.getInstance();
        c.set(hour, minute, 0);
        return c.getTimeInMillis() < System.currentTimeMillis();
    }
}
