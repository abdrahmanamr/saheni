package mystery.anonymous.saheni.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import missing.namespace.R;
import mystery.anonymous.saheni.model.AlarmEntity;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;

public class AlarmListActivity extends AppCompatActivity {
    private AlarmViewModel viewModel;
    private AlarmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        RecyclerView recyclerView = findViewById(R.id.rv_alarms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        adapter = new AlarmAdapter(alarm -> {
            // Handle item click
        });

        recyclerView.setAdapter(adapter);

        viewModel.getAllAlarms().observe(this, alarms -> {
            adapter.submitList(alarms);
        });

        findViewById(R.id.fab_add_alarm).setOnClickListener(v -> {
            startActivity(new Intent(this, AlarmSetupActivity.class));
        });
    }
}