package mystery.anonymous.saheni.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * أداة إدارة الأذونات للتطبيق
 */
public class PermissionHandler {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    // معرّفات الأذونات المختلفة في التطبيق
    public static final int OVERLAY_PERMISSION = 1;
    public static final int DND_PERMISSION = 2;
    public static final int ACTIVITY_RECOGNITION = 3;

    private final Activity activity;
    private final Map<String, Integer> permissionResults;
    private PermissionCallback callback;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
        this.permissionResults = new HashMap<>();
    }

    /**
     * يتحقق من وجود كافة الأذونات المطلوبة للتطبيق
     * @return true إذا كانت كافة الأذونات متاحة، false خلاف ذلك
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasAllPermissions() {
        return hasOverlayPermission() && hasDndPermission() && hasActivityRecognitionPermission();
    }

    /**
     * يتحقق من إذن عرض النوافذ فوق التطبيقات الأخرى
     * @return true إذا كان الإذن متاح، false خلاف ذلك
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasOverlayPermission() {
        return Settings.canDrawOverlays(activity);
    }

    /**
     * يتحقق من إذن تجاوز وضع عدم الإزعاج
     * @return true إذا كان الإذن متاح، false خلاف ذلك
     */
    public boolean hasDndPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(activity);
        }
        return true;
    }

    /**
     * يتحقق من إذن الوصول إلى مستشعرات الحركة
     * @return true إذا كان الإذن متاح، false خلاف ذلك
     */
    public boolean hasActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACTIVITY_RECOGNITION) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * يطلب كافة الأذونات المطلوبة للتطبيق
     * @param callback رد الفعل على نتيجة طلب الأذونات
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestAllPermissions(PermissionCallback callback) {
        this.callback = callback;

        List<Integer> missingPermissions = new ArrayList<>();

        if (!hasOverlayPermission()) {
            missingPermissions.add(OVERLAY_PERMISSION);
        }

        if (!hasDndPermission()) {
            missingPermissions.add(DND_PERMISSION);
        }

        if (!hasActivityRecognitionPermission()) {
            missingPermissions.add(ACTIVITY_RECOGNITION);
        }

        if (missingPermissions.isEmpty()) {
            if (callback != null) {
                callback.onPermissionsGranted();
            }
            return;
        }

        // طلب الأذونات واحداً تلو الآخر
        requestNextPermission(missingPermissions, 0);
    }

    /**
     * يطلب الإذن التالي في القائمة
     * @param permissions قائمة الأذونات المفقودة
     * @param index موقع الإذن الحالي
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestNextPermission(List<Integer> permissions, int index) {
        if (index >= permissions.size()) {
            // انتهت عملية طلب الأذونات
            if (callback != null) {
                if (hasAllPermissions()) {
                    callback.onPermissionsGranted();
                } else {
                    callback.onPermissionsDenied();
                }
            }
            return;
        }

        int permission = permissions.get(index);
        switch (permission) {
            case OVERLAY_PERMISSION:
                requestOverlayPermission();
                break;
            case DND_PERMISSION:
                requestDndPermission();
                break;
            case ACTIVITY_RECOGNITION:
                requestActivityRecognitionPermission();
                break;
        }
    }

    /**
     * يطلب إذن عرض النوافذ فوق التطبيقات الأخرى
     */
    public void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, OVERLAY_PERMISSION);
        }
    }

    /**
     * يطلب إذن تجاوز وضع عدم الإزعاج
     */
    public void requestDndPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, DND_PERMISSION);
        }
    }

    /**
     * يطلب إذن الوصول إلى مستشعرات الحركة
     */
    public void requestActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION},
                    ACTIVITY_RECOGNITION);
        }
    }

    /**
     * يعالج نتائج طلب الأذونات
     * @param requestCode كود الطلب
     * @param resultCode نتيجة الطلب
     * @param data البيانات المرتبطة بالطلب
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OVERLAY_PERMISSION:
                boolean granted = Settings.canDrawOverlays(activity);
                handlePermissionResult(OVERLAY_PERMISSION, granted);
                break;
            case DND_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean dndGranted = Settings.System.canWrite(activity);
                    handlePermissionResult(DND_PERMISSION, dndGranted);
                }
                break;
        }
    }
    /**
     * يعالج نتائج طلب الأذونات من خلال requestPermissions
     * @param requestCode كود الطلب
     * @param permissions قائمة الأذونات
     * @param grantResults نتائج الطلب
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.onPermissionGranted(ACTIVITY_RECOGNITION);
                }
            } else {
                if (callback != null) {
                    callback.onPermissionDenied(ACTIVITY_RECOGNITION);
                }
            }
        }
    }

    /**
     * واجهة للاستجابة لنتائج طلب الأذونات
     */
    public interface PermissionCallback {
        void onPermissionsGranted();
        void onPermissionsDenied();
        void onPermissionGranted(int permission);
        void onPermissionDenied(int permission);
    }

    private void handlePermissionResult(int permission, boolean granted) {
        if (granted) {
            callback.onPermissionGranted(permission);
        } else {
            callback.onPermissionDenied(permission);
        }
    }
}