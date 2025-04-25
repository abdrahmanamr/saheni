package mystery.anonymous.saheni.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mystery.anonymous.saheni.utils.DateTimeConverter;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import missing.namespace.R;
import mystery.anonymous.saheni.model.AlarmEntity;

public class AlarmAdapter extends ListAdapter<AlarmEntity, AlarmAdapter.AlarmViewHolder> {
    public AlarmAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmEntity alarm = getItem(position);
        holder.bind(alarm);
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvNote;
        private final SwitchCompat switchEnabled;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvNote = itemView.findViewById(R.id.tv_note);
            switchEnabled = itemView.findViewById(R.id.switch_enabled);
        }

        public void bind(AlarmEntity alarm) {
            tvTime.setText(DateTimeConverter.formatTime(alarm.getAlarmTime()));
            tvNote.setText(alarm.getAlarmNote());
            switchEnabled.setChecked(alarm.isEnabled());

            switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Handle enable/disable alarm
            });
        }
    }

    private static final DiffUtil.ItemCallback<AlarmEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<AlarmEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull AlarmEntity oldItem, @NonNull AlarmEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull AlarmEntity oldItem, @NonNull AlarmEntity newItem) {
                    return oldItem.getAlarmTime().equals(newItem.getAlarmTime()) &&
                            oldItem.isRepeat() == newItem.isRepeat() &&
                            oldItem.isEnabled() == newItem.isEnabled();
                }
            };
}