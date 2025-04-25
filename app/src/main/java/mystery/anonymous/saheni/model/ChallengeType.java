package mystery.anonymous.saheni.model;

/**
 * تعريف أنواع التحديات المتاحة في التطبيق
 */
public enum ChallengeType {
    BUTTON_GAME(0),
    MATH_QUESTIONS(1),
    STEP_COUNTER(2);

    private final int value;

    ChallengeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChallengeType fromValue(int value) {
        for (ChallengeType type : ChallengeType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        // في حالة عدم العثور على القيمة، يتم إرجاع النوع الافتراضي
        return BUTTON_GAME;
    }
}