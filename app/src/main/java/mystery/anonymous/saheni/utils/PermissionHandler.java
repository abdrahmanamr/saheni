package mystery.anonymous.saheni.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    public static boolean checkOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(activity)) {
                Toast.makeText(activity, "Overlay permission required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static void requestStepCounterPermission(Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.ACTIVITY_RECOGNITION") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.ACTIVITY_RECOGNITION"}, requestCode);
        }
    }
}