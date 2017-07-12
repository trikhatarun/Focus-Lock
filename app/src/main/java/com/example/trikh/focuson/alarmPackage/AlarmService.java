package com.example.trikh.focuson.alarmPackage;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.trikh.focuson.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by trikh on 07-07-2017.
 */

public class AlarmService extends IntentService {

    private static final String TAG = "AlarmService";

    public AlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int code = intent.getIntExtra("code", 0);

        Log.v("service started, Code: ", String.valueOf(code));

        final long currentTime, requiredTime;

        Timer timer = new Timer();

        currentTime = System.currentTimeMillis();

        Calendar requiredTimeCalendar = Calendar.getInstance();
        requiredTimeCalendar.setTimeInMillis(currentTime);
        if (code == Integer.parseInt(getString(R.string.request_code_for_morning))) {
            requiredTimeCalendar.add(Calendar.HOUR_OF_DAY, 1);
        } else if (code == Integer.parseInt(getString(R.string.request_code_for_night))) {
            requiredTimeCalendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        requiredTime = requiredTimeCalendar.getTimeInMillis();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentTime < requiredTime) {
                    Log.v("time log every second: ", String.valueOf(currentTime));
                }
            }
        }, 0, 1000);

        Log.v("Service status: ", "stopped");
    }
}
