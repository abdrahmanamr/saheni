package mystery.anonymous.saheni.viewmodel;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StepCounterViewModel extends ViewModel implements SensorEventListener {
    private final MutableLiveData<Integer> currentSteps = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> challengeCompleted = new MutableLiveData<>(false);
    private int targetSteps;
    private long lastStepTime = 0;

    public LiveData<Integer> getCurrentSteps() { return currentSteps; }
    public LiveData<Boolean> getChallengeCompleted() { return challengeCompleted; }

    public void setTargetSteps(int steps) {
        targetSteps = steps;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastStepTime > 1000) { // تحديث كل ثانية
                int newSteps = (int) event.values[0];
                currentSteps.postValue(newSteps);

                if (newSteps >= targetSteps) {
                    challengeCompleted.postValue(true);
                }
                lastStepTime = currentTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // لا حاجة للتنفيذ حالياً
    }

    public void resetChallenge() {
        currentSteps.setValue(0);
        challengeCompleted.setValue(false);
    }
    public void startStepCounter(SensorManager sensorManager) {
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopStepCounter(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
    }
}