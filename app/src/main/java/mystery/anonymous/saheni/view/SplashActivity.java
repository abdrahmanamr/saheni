package mystery.anonymous.saheni.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import missing.namespace.R;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION = 5000;
    private TextView hintView;
    private String[] hints;
    private int hintIndex = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_splash);

        hintView = findViewById(R.id.tv_hint);
        hints = getResources().getStringArray(R.array.splash_hints);

        rotateHints();
        handler.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }

    private void rotateHints() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hintView.setText(hints[hintIndex]);
                hintIndex = (hintIndex + 1) % hints.length;
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
