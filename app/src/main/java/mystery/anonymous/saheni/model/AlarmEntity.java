package mystery.anonymous.saheni.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class AlarmEntity {
    @PrimaryKey(autoGenerate = true)
    private int alarmId;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "repeat_days")
    private String repeatDays;

    @ColumnInfo(name = "tone_uri")
    private String toneUri;

    @ColumnInfo(name = "note")
    private String note;

    // Getters and Setters
    public int getAlarmId() { return alarmId; }
    public void setAlarmId(int alarmId) { this.alarmId = alarmId; }
    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public String getRepeatDays() { return repeatDays; }
    public void setRepeatDays(String repeatDays) { this.repeatDays = repeatDays; }
    public String getToneUri() { return toneUri; }
    public void setToneUri(String toneUri) { this.toneUri = toneUri; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
