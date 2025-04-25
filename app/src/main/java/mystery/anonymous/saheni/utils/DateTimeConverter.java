package mystery.anonymous.saheni.utils;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * محول البيانات لقاعدة البيانات Room
 * يقوم بتحويل التواريخ وقوائم الأرقام إلى قيم يمكن تخزينها في قاعدة البيانات
 */
public class DateTimeConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    @TypeConverter
    public static String fromIntegerList(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @TypeConverter
    public static List<Integer> toIntegerList(String value) {
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        String[] array = value.split(",");
        List<Integer> result = new ArrayList<>();
        for (String s : array) {
            try {
                result.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // تجاهل القيم الغير صالحة
            }
        }
        return result;
    }
}