package com.android.trikh.focusLock;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.trikh.focusLock.alarmPackage.AlarmService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by trikh on 31-07-2017.
 */

public class BackgroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("myTag1: ", "Service started");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //Intent alarm = new Intent(MainActivity.class,AlarmService.class);
                boolean alarmUp = (PendingIntent.getService(getApplicationContext(), 110,
                        new Intent(AlarmService.class.toString()),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (alarmUp) {
                    Log.d("myTag", "Alarm is already active");
                } else {
                    Log.d("myTag", "Alarm is not active");
                }
                Intent service = new Intent(BackgroundService.this, BackgroundService.class);
                startService(service);
            }
        }, 10000);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
