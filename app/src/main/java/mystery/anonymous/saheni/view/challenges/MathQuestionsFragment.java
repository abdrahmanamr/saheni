package mystery.anonymous.saheni.view.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import missing.namespace.R;
import mystery.anonymous.saheni.viewmodel.MathQuestionsViewModel;

public class MathQuestionsFragment extends Fragment {
    private MathQuestionsViewModel viewModel;
    private EditText etAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_math_questions, container, false);

        TextView tvQuestionNumber = view.findViewById(R.id.tv_question_number);
        TextView tvQuestion = view.findViewById(R.id.tv_question);
        etAnswer = view.findViewById(R.id.et_answer);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

        viewModel = new ViewModelProvider(this).get(MathQuestionsViewModel.class);

        viewModel.getCurrentQuestionNumber().observe(getViewLifecycleOwner(), number -> {
            tvQuestionNumber.setText(getString(R.string.math_question_count, number));
        });

        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {
            tvQuestion.setText(question);
            etAnswer.setText("");
        });

        viewModel.getChallengeCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed) {
                ((ChallengeActivity)requireActivity()).onChallengeCompleted();
            }
        });

        btnSubmit.setOnClickListener(v -> checkAnswer());
        viewModel.generateNewQuestion();

        return view;
    }

    private void checkAnswer() {
        String answerText = etAnswer.getText().toString();
        if (answerText.isEmpty()) {
            showError("الرجاء إدخال إجابة");
            return;
        }

        try {
            int userAnswer = Integer.parseInt(answerText);
            // ... بقية الكود
        } catch (NumberFormatException e) {
            showError("الإجابة يجب أن تكون رقمية");
        }
    }

    private void showError(String message) {
        etAnswer.setError(message);
        etAnswer.requestFocus();
    }
}