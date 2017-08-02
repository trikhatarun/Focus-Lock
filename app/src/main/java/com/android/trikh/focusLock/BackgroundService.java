package com.android.trikh.focusLock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by trikh on 31-07-2017.
 */

public class BackgroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                boolean alarmUp = false;
                List<AndroidAppProcess> runningAppProcessInfo = AndroidProcesses.getRunningAppProcesses();

                for (int i = 0; i < runningAppProcessInfo.size(); i++) {
                    String currentAppName = runningAppProcessInfo.get(i).getPackageName();
                    if (currentAppName.equals(getApplicationContext().getPackageName())) {
                        alarmUp = true;
                    }
                }

                if (alarmUp) {
                    Log.d("myTag", "Alarm is already active");
                } else {
                    Log.d("myTag", "Alarm is not active");
                }
                Intent service = new Intent(BackgroundService.this, BackgroundService.class);
                startService(service);
            }
        }, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("RestartAppPlease"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
