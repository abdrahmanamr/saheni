package mystery.anonymous.saheni.view.challenges;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import mystery.anonymous.saheni.viewmodel.StepCounterViewModel;

import missing.namespace.R;

public class StepCounterFragment extends Fragment {
    private StepCounterViewModel viewModel;
    private SensorManager sensorManager;
    private TextView tvSteps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);
        tvSteps = view.findViewById(R.id.tv_steps);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        viewModel = new ViewModelProvider(this).get(StepCounterViewModel.class);

        viewModel.getCurrentSteps().observe(getViewLifecycleOwner(), steps -> {
            String stepsText = getString(R.string.steps_progress, steps, viewModel.getTargetSteps());
            tvSteps.setText(stepsText);
        });

        viewModel.getChallengeCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed) {
                ((ChallengeActivity)requireActivity()).onChallengeCompleted();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.startStepCounter(sensorManager);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stopStepCounter(sensorManager);
    }
}