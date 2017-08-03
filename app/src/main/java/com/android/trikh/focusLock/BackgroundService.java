package com.android.trikh.focusLock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.trikh.focusLock.alarmPackage.AlarmHelper;
import com.android.trikh.focusLock.alarmPackage.AlarmService;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.valueOf;

/**
 * Created by trikh on 31-07-2017.
 */

public class BackgroundService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Trial Title")
                .setContentText("Trial message")
                .setSmallIcon(R.drawable.alarm_clock)
                .setTicker("Trial ticker text")
                .build();

        startForeground(1337, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                boolean alarmUp = (PendingIntent.getService(BackgroundService.this, 110,
                        new Intent(BackgroundService.this, AlarmService.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (alarmUp) {
                    Log.d("myTag", "Alarm is already active");
                } else {
                    setAlarmsNow(BackgroundService.this);
                    Log.d("myTag", "Alarm is not active");
                }
                sendBroadcast(new Intent("RestartAppPlease"));
            }
        }, 90000);
        return START_STICKY;
    }

    private void setAlarmsNow(Context context) {
        AlarmHelper ah = new AlarmHelper(context);
        Log.v("myTag: ", "alarm tried to set");
        ah.setAlarm(PreferenceHelper.getMorningTimeString(context), valueOf(context.getString(R.string.request_code_for_morning)));
        ah.setAlarm(PreferenceHelper.getSleepTimeString(context), valueOf(context.getString(R.string.request_code_for_night)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
