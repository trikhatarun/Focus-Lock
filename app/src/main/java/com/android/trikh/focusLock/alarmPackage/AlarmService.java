package com.android.trikh.focusLock.alarmPackage;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.trikh.focusLock.BackgroundService;
import com.android.trikh.focusLock.R;
import com.android.trikh.focusLock.ui.BlockedAppActivity;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.rvalerio.fgchecker.AppChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by trikh on 07-07-2017.
 */

public class AlarmService extends Service {

    private Set<String> blockedAppSet;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Focus Lock")
                .setContentText("Disturbances have been blocked. Let Focus be locked on right targets!!")
                .setPriority(Notification.PRIORITY_MIN)
                .setSmallIcon(R.drawable.focus_block_2_icon)
                .build();
        startForeground(1140, notification);
    }

    private void refreshBlockedAppSet() {
        blockedAppSet = PreferenceManager.getDefaultSharedPreferences(this).getStringSet(getString(R.string.block_app_set_key), new HashSet<String>());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int code = intent.getIntExtra("code", 0);
        long durationMillis;
        if (code == Integer.valueOf(getString(R.string.request_code_for_morning))) {
            durationMillis = TimeUnit.MINUTES.toMillis(180);
        } else if (code == Integer.valueOf(context.getString(R.string.request_code_for_night)))
            durationMillis = TimeUnit.MINUTES.toMillis(60);
        else {
            durationMillis = intent.getLongExtra("timeLeft", 0);
        }
        new CountDownTimer(durationMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                refreshBlockedAppSet();
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<AndroidAppProcess> runningAppProcessInfo = AndroidProcesses.getRunningAppProcesses();
                AppChecker appChecker = new AppChecker();

                for (int i = 0; i < runningAppProcessInfo.size(); i++) {
                    AndroidAppProcess currentApp = runningAppProcessInfo.get(i);
                    String currentAppName = currentApp.getPackageName();
                    if (blockedAppSet.contains(currentAppName)) {
                        Log.d("Foreground status: ", String.valueOf(currentAppName.equals(appChecker.getForegroundApp(context))));
                        if (currentAppName.equals(appChecker.getForegroundApp(context))) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            am.killBackgroundProcesses(currentAppName);
                            Intent intent = new Intent();
                            intent.setClass(context, BlockedAppActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(intent);
                        } else {
                            am.killBackgroundProcesses(currentAppName);
                        }
                    }
                }
            }
            public void onFinish() {
                stopSelf();
                startService(new Intent(AlarmService.this, BackgroundService.class));
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
