package mystery.anonymous.saheni.utils;

import java.util.Random;

import mystery.anonymous.saheni.model.ChallengeType;

/**
 * أداة لاختيار نوع التحدي عشوائياً عند رنين المنبه
 */
public class RandomChallengeSelector {

    private static final Random random = new Random();

    /**
     * يختار نوع التحدي عشوائياً من بين الأنواع المتاحة
     * @return نوع التحدي المختار
     */
    public static ChallengeType selectRandomChallenge() {
        // عدد أنواع التحديات
        int challengeCount = ChallengeType.values().length;
        // اختيار رقم عشوائي بين 0 و (عدد التحديات - 1)
        int selectedIndex = random.nextInt(challengeCount);
        // تحويل الرقم إلى نوع التحدي
        return ChallengeType.fromValue(selectedIndex);
    }

    /**
     * يختار رقماً عشوائياً ضمن نطاق محدد
     * @param min الحد الأدنى (مشمول)
     * @param max الحد الأعلى (مشمول)
     * @return رقم عشوائي ضمن النطاق المحدد
     */
    public static int getRandomNumberInRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * يختار عملية رياضية عشوائية
     * @param maxMultiplyDivideCount الحد الأقصى لعدد عمليات الضرب والقسمة
     * @return عملية عشوائية (0: جمع، 1: طرح، 2: ضرب، 3: قسمة)
     */
    public static int getRandomOperation(int maxMultiplyDivideCount) {
        // إذا وصلنا للحد الأقصى من عمليات الضرب والقسمة، نرجع فقط الجمع أو الطرح
        if (maxMultiplyDivideCount <= 0) {
            return random.nextInt(2); // 0: جمع، 1: طرح
        } else {
            return random.nextInt(4); // 0: جمع، 1: طرح، 2: ضرب، 3: قسمة
        }
    }

    /**
     * يولد عدداً عشوائياً من الخطوات المطلوبة لتحدي المشي
     * @return عدد الخطوات المطلوبة (بين 5 و 10)
     */
    public static int getRandomStepsRequired() {
        return getRandomNumberInRange(5, 10);
    }
}