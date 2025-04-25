package mystery.anonymous.saheni.view;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import missing.namespace.R;

public class SplashActivity extends AppCompatActivity {
    private TextView tvHint;
    private String[] hints = {"Tip 1", "Tip 2", "Tip 3"};
    private int currentHint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvHint = findViewById(R.id.tv_hint);
        startHintCycle();
    }

    private void startHintCycle() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvHint.setText(hints[currentHint % hints.length]);
                currentHint++;
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}
