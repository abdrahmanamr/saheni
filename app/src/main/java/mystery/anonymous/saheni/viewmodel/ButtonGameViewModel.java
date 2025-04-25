package mystery.anonymous.saheni.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import mystery.anonymous.saheni.utils.RandomChallengeSelector;

public class ButtonGameViewModel extends ViewModel {

    private static final int TARGET_POINTS = 50;
    private static final int BUTTON_HIGH_POINTS = 5;
    private static final int BUTTON_NORMAL_POINTS = 1;
    private static final int BUTTON_NEGATIVE_POINTS = -1;

    private final MutableLiveData<Integer> points = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> challengeCompleted = new MutableLiveData<>(false);
    private final MutableLiveData<ButtonType> currentButtonType = new MutableLiveData<>();

    public LiveData<Integer> getPoints() {
        return points;
    }

    public LiveData<Boolean> getChallengeCompleted() {
        return challengeCompleted;
    }

    public LiveData<ButtonType> getCurrentButtonType() {
        return currentButtonType;
    }

    /**
     * إعادة تعيين التحدي
     */
    public void resetChallenge() {
        points.setValue(0);
        challengeCompleted.setValue(false);
        generateRandomButtonType();
    }

    /**
     * معالجة الضغط على الزر
     */
    public void onButtonPressed() {
        ButtonType type = currentButtonType.getValue();
        if (type == null) {
            type = generateRandomButtonType();
        }

        // إضافة النقاط حسب نوع الزر
        int currentPoints = points.getValue() != null ? points.getValue() : 0;
        int pointsToAdd = 0;

        switch (type) {
            case HIGH:
                pointsToAdd = BUTTON_HIGH_POINTS;
                break;
            case NORMAL:
                pointsToAdd = BUTTON_NORMAL_POINTS;
                break;
            case NEGATIVE:
                pointsToAdd = BUTTON_NEGATIVE_POINTS;
                break;
        }

        int newPoints = currentPoints + pointsToAdd;
        points.setValue(newPoints);

        // التحقق من اكتمال التحدي
        if (newPoints >= TARGET_POINTS) {
            challengeCompleted.setValue(true);
        } else {
            // توليد نوع جديد للزر التالي
            generateRandomButtonType();
        }
    }

    /**
     * توليد نوع عشوائي للزر
     * @return نوع الزر الجديد
     */
    private ButtonType generateRandomButtonType() {
        int random = RandomChallengeSelector.getRandomNumberInRange(1, 10);
        ButtonType type;

        if (random <= 2) {
            // 20% فرصة للحصول على زر بنقاط عالية
            type = ButtonType.HIGH;
        } else if (random <= 7) {
            // 50% فرصة للحصول على زر بنقاط عادية
            type = ButtonType.NORMAL;
        } else {
            // 30% فرصة للحصول على زر بنقاط سالبة
            type = ButtonType.NEGATIVE;
        }

        currentButtonType.setValue(type);
        return type;
    }

    /**
     * أنواع الأزرار في اللعبة
     */
    public enum ButtonType {
        HIGH,    // زر بنقاط عالية (5 نقاط)
        NORMAL,  // زر بنقاط عادية (1 نقطة)
        NEGATIVE // زر بنقاط سالبة (-1 نقطة)
    }
}