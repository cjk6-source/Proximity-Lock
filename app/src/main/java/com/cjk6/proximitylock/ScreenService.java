package com.cjk6.proximitylock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.cjk6.proximitylock.App.CHANNEL_ID;

public class ScreenService extends Service implements SensorEventListener {

    private static final float THRESHOLD = 7;
    private static final int DELAY = 1000000;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private PowerManager.WakeLock wl;
    private PowerManager pm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL, DELAY);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "myApp:ProximityLock");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Proximity Lock")
                .setContentText("Active")
                .setSmallIcon(R.drawable.lock_portrait)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float proximityValue = event.values[0];
            if (proximityValue < THRESHOLD)
            {
                //TURN OFF SCREEN
                if (!wl.isHeld()) {
                    wl.acquire();
                }
            }
            else
            {
                //TURN ON SCREEN
                if (wl.isHeld()) {
                    wl.release();
                }
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null && wl.isHeld()) {
            wl.release();
        }
        sensorManager.unregisterListener(this);
    }

}
