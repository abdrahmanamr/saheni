package mystery.anonymous.saheni.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.Settings;
import android.net.Uri;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import missing.namespace.R;
import mystery.anonymous.saheni.model.AlarmEntity;
import mystery.anonymous.saheni.view.AlarmAdapter;
import mystery.anonymous.saheni.utils.PermissionHandler;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_OVERLAY = 100;
    private AlarmViewModel viewModel;
    private PermissionHandler permHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permHandler = new PermissionHandler(this);
        permHandler.checkAndRequestPermissions(); // طلب الأذونات :contentReference[oaicite:4]{index=4}

        RecyclerView rv = findViewById(R.id.alarms_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));  // ضروري لتجنب NullPointerException

        AlarmAdapter adapter = new AlarmAdapter(new AlarmAdapter.Listener() {
            @Override public void onToggle(AlarmEntity alarm) { viewModel.toggle(alarm); }
            @Override public void onDelete(AlarmEntity alarm) { viewModel.delete(alarm); }
            @Override public void onEdit(AlarmEntity alarm) {
                Intent i = new Intent(MainActivity.this, SetAlarmActivity.class);
                i.putExtra("alarm_id", alarm.getId());
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        viewModel.getAllAlarms().observe(this, adapter::submitList); // ربط LiveData :contentReference[oaicite:5]{index=5}

        FloatingActionButton fab = findViewById(R.id.add_alarm_fab);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, SetAlarmActivity.class))
        );
    }

    // مثال لطلب إذن OVERLAY الخاص بالرسم أعلى التطبيقات الأخرى
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permHandler.handlePermissionsResult(requestCode, permissions, grantResults);
    }
}
