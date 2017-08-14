package com.android.trikh.focusLock.alarmPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.trikh.focusLock.BackgroundService;
import com.android.trikh.focusLock.PreferenceHelper;

import java.util.Calendar;

/**
 * Created by trikh on 08-07-2017.
 */

public class RebootedDevice extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTime = System.currentTimeMillis();

        Calendar expectedTimeCalendarNightStart = Calendar.getInstance();
        Calendar expectedTimeCalendarMorningStart = Calendar.getInstance();
        Calendar expectedTimeCalendarNightEnd = Calendar.getInstance();
        Calendar expectedTimeCalendarMorningEnd = Calendar.getInstance();

        String sleepTimeString = PreferenceHelper.getSleepTimeString(context);
        String wakeUpTimeString = PreferenceHelper.getMorningTimeString(context);

        String[] sleepTime = sleepTimeString.split(":");
        int hourNight = Integer.parseInt(sleepTime[0]);
        int minuteNight = Integer.parseInt(sleepTime[1]);

        expectedTimeCalendarNightStart.set(Calendar.HOUR_OF_DAY, hourNight - 1);
        expectedTimeCalendarNightStart.set(Calendar.MINUTE, minuteNight);

        expectedTimeCalendarNightEnd.set(Calendar.HOUR_OF_DAY, hourNight);
        expectedTimeCalendarNightEnd.set(Calendar.MINUTE, minuteNight);

        String[] morningTime = wakeUpTimeString.split(":");
        int hourMorning = Integer.parseInt(morningTime[0]);
        int minuteMorning = Integer.parseInt(morningTime[1]);

        expectedTimeCalendarMorningStart.set(Calendar.HOUR_OF_DAY, hourMorning);
        expectedTimeCalendarMorningStart.set(Calendar.MINUTE, minuteMorning);

        expectedTimeCalendarMorningEnd.set(Calendar.HOUR_OF_DAY, hourMorning + 3);
        expectedTimeCalendarMorningEnd.set(Calendar.MINUTE, minuteMorning);

        long expectedTimeMillisMorningStart, expectedTimeMillisMorningEnd, expectedTimeMillisNightStart, expectedTimeMillisNightEnd;
        expectedTimeMillisMorningStart = expectedTimeCalendarMorningStart.getTimeInMillis();
        expectedTimeMillisNightStart = expectedTimeCalendarNightStart.getTimeInMillis();
        expectedTimeMillisMorningEnd = expectedTimeCalendarMorningEnd.getTimeInMillis();
        expectedTimeMillisNightEnd = expectedTimeCalendarNightEnd.getTimeInMillis();

        Intent intentToSend;

        if (currentTime >= expectedTimeMillisMorningStart && currentTime < expectedTimeMillisMorningEnd) {
            intentToSend = new Intent(context, AlarmService.class);
            intentToSend.putExtra("timeLeft", expectedTimeMillisMorningEnd - currentTime);
        } else if (currentTime >= expectedTimeMillisNightStart && currentTime < expectedTimeMillisNightEnd) {
            intentToSend = new Intent(context, AlarmService.class);
            intentToSend.putExtra("timeLeft", expectedTimeMillisNightEnd - currentTime);
        } else {
            intentToSend = new Intent(context, BackgroundService.class);
        }

        context.startService(intentToSend);

    }
}
