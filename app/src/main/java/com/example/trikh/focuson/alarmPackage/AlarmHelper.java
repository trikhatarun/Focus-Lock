package com.example.trikh.focuson.alarmPackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.trikh.focuson.R;

import java.util.Calendar;

/**
 * Created by trikh on 09-07-2017.
 */

public class AlarmHelper {

    private Context contextInstance;

    public AlarmHelper(Context context) {
        contextInstance = context;
    }

    public void setAlarm(String timeString, int code) {
        AlarmManager alarmManagerInstance = (AlarmManager) contextInstance.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(contextInstance, AlarmService.class);
        intent.putExtra("code", code);

        PendingIntent pendingIntent = PendingIntent.getService(contextInstance, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String[] time = timeString.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.MINUTE, minute);
        calendarTime.set(Calendar.SECOND, 1);
        if (code == Integer.parseInt(contextInstance.getString(R.string.request_code_for_morning))) {
            calendarTime.set(Calendar.HOUR_OF_DAY, hour);
        } else if (code == Integer.parseInt(contextInstance.getString(R.string.request_code_for_night))) {
            calendarTime.set(Calendar.HOUR_OF_DAY, hour - 1);
        }

        alarmManagerInstance.setRepeating(AlarmManager.RTC, calendarTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
