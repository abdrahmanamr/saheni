package mystery.anonymous.saheni.view.challenges;


import missing.namespace.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import mystery.anonymous.saheni.viewmodel.ButtonGameViewModel;

public class ButtonGameFragment extends Fragment {
    private ButtonGameViewModel viewModel;
    private TextView tvPoints;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_game, container, false);
        tvPoints = view.findViewById(R.id.tv_points);
        Button btnChallenge = view.findViewById(R.id.btn_challenge);

        viewModel = new ViewModelProvider(this).get(ButtonGameViewModel.class);
        viewModel.getPoints().observe(getViewLifecycleOwner(), points -> {
            tvPoints.setText(getString(R.string.button_game_points, points));
            if (points >= 50) {
                ((ChallengeActivity)requireActivity()).onChallengeCompleted();
            }
        });

        btnChallenge.setOnClickListener(v -> viewModel.onButtonPressed());
        viewModel.resetChallenge();
        return view;
    }
}