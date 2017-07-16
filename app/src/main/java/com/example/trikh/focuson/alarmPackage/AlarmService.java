package com.example.trikh.focuson.alarmPackage;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.trikh.focuson.R;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by trikh on 07-07-2017.
 */

public class AlarmService extends Service {

    private int code;
    private Set<String> blockedAppSet;

    @Override
    public void onCreate() {
        super.onCreate();
        long durationMillis;
        if (code == Integer.valueOf(getString(R.string.request_code_for_morning))) {
            durationMillis = TimeUnit.MINUTES.toMillis(180);
        } else
            durationMillis = TimeUnit.MINUTES.toMillis(60);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                refreshBlockedAppSet();
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<AndroidAppProcess> runningAppProcessInfo = AndroidProcesses.getRunningAppProcesses();

                for (int i = 0; i < runningAppProcessInfo.size(); i++) {
                    Log.i("Location: ", "AppLaunched " + runningAppProcessInfo.get(i).getPackageName());
                    if (blockedAppSet.contains(runningAppProcessInfo.get(i).getPackageName())) {
                        am.killBackgroundProcesses(runningAppProcessInfo.get(i).getPackageName());
                        Log.i("Location: ", "Killed app process: " + runningAppProcessInfo.get(i).getPackageName());
                    }
                }
            }

            public void onFinish() {
                stopSelf();
            }
        }.start();
    }

    private void refreshBlockedAppSet() {
        blockedAppSet = PreferenceManager.getDefaultSharedPreferences(this).getStringSet(getString(R.string.block_app_set_key), new HashSet<String>());
    }

    @Override
    public void onDestroy() {
        Log.i("Service: ", "Ended and destroyed");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        code = intent.getIntExtra("code", 0);
        Log.i("Service: ", "started by onStartCommand with code: " + code);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
