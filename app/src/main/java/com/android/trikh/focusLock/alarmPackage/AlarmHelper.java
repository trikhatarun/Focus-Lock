package com.android.trikh.focusLock.alarmPackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android.trikh.focusLock.R;

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

        PendingIntent pendingIntent = PendingIntent.getService(contextInstance, code, intent, PendingIntent.FLAG_CANCEL_CURRENT);

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

        if (calendarTime.before(Calendar.getInstance())) {
            calendarTime.add(Calendar.DATE, 1);
        }

        alarmManagerInstance.setExact(AlarmManager.RTC, calendarTime.getTimeInMillis(), pendingIntent);
    }
}
