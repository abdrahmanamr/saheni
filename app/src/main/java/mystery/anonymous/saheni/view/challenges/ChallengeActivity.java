package mystery.anonymous.saheni.view.challenges;

import android.os.Bundle;
import android.view.WindowManager;

import missing.namespace.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import mystery.anonymous.saheni.model.ChallengeType;
import mystery.anonymous.saheni.utils.RandomChallengeSelector;

public class ChallengeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ChallengeType challengeType = RandomChallengeSelector.selectRandomChallenge();
        loadChallengeFragment(challengeType);
    }

    private void loadChallengeFragment(ChallengeType type) {
        Fragment fragment = null;
        switch (type) {
            case BUTTON_GAME:
                fragment = new ButtonGameFragment();
                break;
            case MATH_QUESTIONS:
                fragment = new MathQuestionsFragment();
                break;
            case STEP_COUNTER:
                fragment = new StepCounterFragment();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void onChallengeCompleted() {
        // إيقاف الصوت وإغلاق النشاط
        finish();
    }
}