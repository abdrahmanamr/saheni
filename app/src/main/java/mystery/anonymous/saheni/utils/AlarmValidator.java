package mystery.anonymous.saheni.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mystery.anonymous.saheni.model.AlarmEntity;

/**
 * أداة للتحقق من صحة إعدادات المنبه والتعديل عند الحاجة
 */
public class AlarmValidator {

    /**
     * يتحقق من كون وقت المنبه في المستقبل، وإذا كان في الماضي وبدون تكرار، يضبطه لليوم التالي
     * @param alarm المنبه الذي يجب التحقق منه
     * @return المنبه بعد التعديل إذا كان ضرورياً
     */
    public static AlarmEntity validateAlarmTime(AlarmEntity alarm) {
        if (alarm == null || alarm.getAlarmTime() == null) {
            throw new IllegalArgumentException("المنبه أو وقت المنبه غير محدد");
        }

        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.setTime(alarm.getAlarmTime());

        Calendar calendarNow = Calendar.getInstance();

        // إذا كان وقت المنبه في الماضي وليس هناك تكرار، نضبطه لليوم التالي
        if (calendarAlarm.before(calendarNow) && !alarm.isRepeat()) {
            calendarAlarm.add(Calendar.DAY_OF_MONTH, 1);
            alarm.setAlarmTime(calendarAlarm.getTime());
        }

        return alarm;
    }

    /**
     * يتحقق من كون المنبه يحتوي على الإعدادات الأساسية
     * @param alarm المنبه الذي يجب التحقق منه
     * @return true إذا كان المنبه صالحاً، false خلاف ذلك
     */
    public static boolean isAlarmValid(AlarmEntity alarm) {
        if (alarm == null || alarm.getAlarmTime() == null) {
            return false;
        }

        // إذا كان المنبه مكرراً، يجب أن يكون هناك أيام تكرار محددة
        if (alarm.isRepeat()) {
            List<Integer> repeatDays = alarm.getRepeatDays();
            return repeatDays != null && !repeatDays.isEmpty();
        }

        return true;
    }

    /**
     * يحسب الوقت المتبقي حتى المنبه بالثواني
     * @param alarmTime وقت المنبه
     * @return عدد الثواني المتبقية
     */
    public static long getTimeUntilAlarmInSeconds(Date alarmTime) {
        if (alarmTime == null) {
            return -1;
        }

        long alarmTimeMillis = alarmTime.getTime();
        long nowMillis = System.currentTimeMillis();
        long diffMillis = alarmTimeMillis - nowMillis;

        return diffMillis / 1000;
    }

    /**
     * يحول عدد الثواني إلى نص يمثل الوقت بصيغة "س ساعة و د دقيقة"
     * @param seconds عدد الثواني
     * @return نص يمثل الوقت
     */
    public static String formatTimeUntilAlarm(long seconds) {
        if (seconds < 0) {
            return "المنبه في الماضي";
        }

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append(" ساعة");
            if (minutes > 0) {
                result.append(" و ");
            }
        }
        if (minutes > 0 || hours == 0) {
            result.append(minutes).append(" دقيقة");
        }

        return result.toString();
    }

    public static boolean isDateValid(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year >= 2023 && year <= 2030; // نطاق تواريخ معقول
    }
}