package mystery.anonymous.saheni.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class MathQuestionsViewModel extends ViewModel {
    private static final int TOTAL_QUESTIONS = 5;
    private final MutableLiveData<String> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentQuestionNumber = new MutableLiveData<>(1);
    private final MutableLiveData<Boolean> challengeCompleted = new MutableLiveData<>(false);
    private int correctAnswer;
    private int multiplyDivideCount = 0;

    public LiveData<String> getCurrentQuestion() { return currentQuestion; }
    public LiveData<Integer> getCurrentQuestionNumber() { return currentQuestionNumber; }
    public LiveData<Boolean> getChallengeCompleted() { return challengeCompleted; }

    public void generateNewQuestion() {
        if (currentQuestionNumber.getValue() != null && currentQuestionNumber.getValue() > TOTAL_QUESTIONS) {
            challengeCompleted.setValue(true);
            return;
        }

        Random random = new Random();
        int num1 = random.nextInt(6) + 2; // 2-6
        int num2 = random.nextInt(6) + 2;
        String operation;

        // تحديد العملية مع مراعاة الحد الأقصى لعمليات الضرب والقسمة
        int operationType = getOperationType();
        switch (operationType) {
            case 0:
                operation = "+";
                correctAnswer = num1 + num2;
                break;
            case 1:
                operation = "-";
                correctAnswer = num1 - num2;
                break;
            case 2:
                operation = "×";
                correctAnswer = num1 * num2;
                multiplyDivideCount++;
                break;
            case 3:
                operation = "÷";
                num1 = num1 * num2; // لتجنب الكسور
                correctAnswer = num1 / num2;
                multiplyDivideCount++;
                break;
            default:
                operation = "+";
                correctAnswer = num1 + num2;
        }

        currentQuestion.setValue(num1 + " " + operation + " " + num2 + " = ?");
    }

    private int getOperationType() {
        Random random = new Random();
        if (multiplyDivideCount >= 2) {
            return random.nextInt(2); // 0-1 (جمع أو طرح فقط)
        }
        return random.nextInt(4); // 0-3 (جميع العمليات)
    }

    public boolean checkAnswer(int userAnswer) {
        boolean isCorrect = userAnswer == correctAnswer;
        if (isCorrect) {
            currentQuestionNumber.setValue(currentQuestionNumber.getValue() + 1);
            if (currentQuestionNumber.getValue() > TOTAL_QUESTIONS) {
                challengeCompleted.setValue(true);
            } else {
                generateNewQuestion();
            }
        }
        return isCorrect;
    }

    public void resetChallenge() {
        currentQuestionNumber.setValue(1);
        challengeCompleted.setValue(false);
        multiplyDivideCount = 0;
        generateNewQuestion();
    }
}