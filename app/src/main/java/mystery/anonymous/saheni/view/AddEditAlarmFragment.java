package mystery.anonymous.saheni.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.text.SimpleDateFormat;
import java.util.*;

import missing.namespace.R;
import mystery.anonymous.saheni.model.AlarmEntity;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;

public class AddEditAlarmFragment extends Fragment {
    private Button btnTime, btnRingtone, btnDates, btnSave;
    private EditText etNote;
    private ChipGroup chipGroupDays;
    private AlarmViewModel vm;
    private AlarmEntity alarm = new AlarmEntity();
    private final List<String> selectedDates = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle b) {
        return inf.inflate(R.layout.fragment_add_edit_alarm, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        btnTime       = v.findViewById(R.id.btnTime);
        btnRingtone   = v.findViewById(R.id.btnRingtone);
        chipGroupDays = v.findViewById(R.id.chipGroupDays);
        btnDates      = v.findViewById(R.id.btnDates);
        etNote        = v.findViewById(R.id.etNote);
        btnSave       = v.findViewById(R.id.btnSave);

        vm = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);

        btnTime.setOnClickListener(x -> showTimePicker());
        btnDates.setOnClickListener(x -> showDatePicker());
        btnSave.setOnClickListener(x -> saveAlarm());
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(),
                (tp, h, m) -> {
                    c.set(Calendar.HOUR_OF_DAY, h);
                    c.set(Calendar.MINUTE, m);
                    c.set(Calendar.SECOND, 0);
                    alarm.setHour(h);
                    alarm.setMinute(m);
                    btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m));
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(),
                (dp, y, mo, d) -> {
                    c.set(y, mo, d);
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(c.getTime());
                    if (!selectedDates.contains(date)) {
                        selectedDates.add(date);
                        btnDates.setText("تواريخ (" + selectedDates.size() + ")");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void saveAlarm() {
        if (alarm.getHour() < 0) {
            Toast.makeText(getContext(), "الوقت مش مظبوط", Toast.LENGTH_SHORT).show();
            return;
        }
        alarm.setNote(etNote.getText().toString());
        alarm.setSpecificDates(selectedDates);
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < chipGroupDays.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupDays.getChildAt(i);
            if (chip.isChecked()) days.add(i);
        }
        alarm.setRepeatDays(days);
        vm.save(alarm);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
