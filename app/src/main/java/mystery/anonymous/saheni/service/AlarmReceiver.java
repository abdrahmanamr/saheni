package mystery.anonymous.saheni.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import mystery.anonymous.saheni.view.challenges.ChallengeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmId = intent.getIntExtra("ALARM_ID", -1);
        if (alarmId != -1) {
            Intent challengeIntent = new Intent(context, ChallengeActivity.class);
            challengeIntent.putExtra("ALARM_ID", alarmId);
            challengeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ContextCompat.startActivity(context, challengeIntent, null);
        }
    }

    private void playAlarmTone(Context context, String toneUri) {
        Uri uri;
        if (toneUri == null || toneUri.isEmpty()) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        } else {
            uri = Uri.parse(toneUri);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        if (ringtone != null) {
            ringtone.play();
        }
    }
}