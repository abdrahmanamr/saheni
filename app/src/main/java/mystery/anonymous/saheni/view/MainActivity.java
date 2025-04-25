package mystery.anonymous.saheni.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import missing.namespace.R;
import mystery.anonymous.saheni.model.AlarmEntity;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_REQ_CODE = 100;
    private AlarmViewModel viewModel;

    // Launcher لطلبات الأذونات المتعددة
    private final ActivityResultLauncher<String[]> permsLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> {
                        List<String> denied = new ArrayList<>();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            result.forEach((perm, granted) -> {
                                if (!granted) denied.add(perm);
                            });
                        }
                        if (!denied.isEmpty()) {
                            showPermissionDeniedDialog(denied);
                        }
                    }
            );

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة RecyclerView ونمط MVVM
        RecyclerView rv = findViewById(R.id.alarms_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));  // ضروري لتجنب NPE
        AlarmAdapter adapter = new AlarmAdapter(new AlarmAdapter.Listener() {
            @Override public void onToggle(AlarmEntity alarm) { viewModel.toggle(alarm); }
            @Override public void onDelete(AlarmEntity alarm) { viewModel.delete(alarm); }
            @Override public void onEdit(AlarmEntity alarm) {
                Intent i = new Intent(MainActivity.this, AddAlarmActivity.class)
                        .putExtra("alarm_id", alarm.id);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        viewModel.getAllAlarms().observe(this, adapter::submitList);  // استخدام LiveData :contentReference[oaicite:4]{index=4}

        // زر الإضافة
        FloatingActionButton fab = findViewById(R.id.add_alarm_fab);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditAlarmFragment.class))
        );

        // التحقق وطلب الأذونات المطلوبة
        checkAndRequestAllPermissions();
    }

    /** يتحقق من الأذونات ويطلبها إذا لم تكن ممنوحة */
    private void checkAndRequestAllPermissions() {
        List<String> permsToRequest = new ArrayList<>();

        // أذونات رنّ الوقت الفعلي
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        // إذن الجدولة الدقيقة
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permsToRequest.add(Manifest.permission.SCHEDULE_EXACT_ALARM);
        }

        if (!permsToRequest.isEmpty()) {
            permsLauncher.launch(permsToRequest.toArray(new String[0]));  // طلب متعدد :contentReference[oaicite:5]{index=5}
        }

        // طلب إذن الظهور فوق التطبيقات
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, OVERLAY_REQ_CODE);  // خاص بإذن SYSTEM_ALERT_WINDOW :contentReference[oaicite:6]{index=6}
        }

        // إذن تجاوز وضع عدم الإزعاج
        android.app.NotificationManager nm =
                (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !nm.isNotificationPolicyAccessGranted()) {
            startActivity(
                    new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            );  // لتفعيل Access Notification Policy :contentReference[oaicite:7]{index=7}
        }
    }

    /** عرض حوار عند رفض الأذونات يشرح تأثير الرفض */
    private void showPermissionDeniedDialog(List<String> deniedPerms) {
        new AlertDialog.Builder(this)
                .setTitle("الأذونات مطلوبة")
                .setMessage("هذه الأذونات ضرورية لعمل التطبيق بشكل صحيح:\n"
                        + deniedPerms.toString())
                .setPositiveButton("إعدادات", (d, w) -> {
                    // فتح إعدادات التطبيق لتمكين الأذونات يدويًا
                    Intent intent = new Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName())
                    );
                    startActivity(intent);
                })
                .setNegativeButton("إلغاء", null)
                .show();  // rationale قبل طلب الأذونات مجددًا :contentReference[oaicite:8]{index=8}
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == OVERLAY_REQ_CODE) {
            // يمكن التحقق مجددًا بعد عودة المستخدم
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showPermissionDeniedDialog(
                            List.of(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    );
                }
            }
        }
    }
}
