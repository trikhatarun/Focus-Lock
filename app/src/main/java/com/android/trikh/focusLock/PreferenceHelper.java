package com.android.trikh.focusLock;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.trikh.focusLock.alarmPackage.AlarmHelper;

import java.util.Calendar;

public class PreferenceHelper {

    public static String getMorningTimeString(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.wake_up_time_key), "5:00");
    }

    public static String getSleepTimeString(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.sleep_time_key), "22:00");
    }

    public static void setTimePreference(Context context, String time, int requestCode) {
        if (String.valueOf(requestCode).equals(context.getString(R.string.request_code_for_morning))) {
            if (!getMorningTimeString(context).equals(time)) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString(context.getString(R.string.wake_up_time_key), time);
                editor.apply();
                AlarmHelper ah = new AlarmHelper(context);
                ah.setAlarm(time, requestCode);
            }
        } else if (String.valueOf(requestCode).equals(context.getString(R.string.request_code_for_night))) {
            if (!getSleepTimeString(context).equals(time)) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString(context.getString(R.string.sleep_time_key), time);
                editor.apply();
                AlarmHelper ah = new AlarmHelper(context);
                ah.setAlarm(time, requestCode);
            }
        }
    }

    public static int getFirstDay(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int days = preferences.getInt(context.getString(R.string.day_installed_key), 0);
        if (days == 0) {
            days = firstDay();
            setFirstDay(context, days);
        }
        return days;
    }

    private static void setFirstDay(Context context, int days) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(context.getString(R.string.day_installed_key), days);
        editor.apply();
    }

    private static int firstDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDaysLeft(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.life_days_left_key), 0);
    }

    public static String getCountryName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.country_key), null);
    }

    static int getDayToday(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int dayToday = preferences.getInt(context.getString(R.string.day_today_key), 0);
        if (dayToday == 0) {
            updateDayToday(context);
            dayToday = preferences.getInt(context.getString(R.string.day_today_key), 0);
        }
        return dayToday;
    }

    static void updateDayToday(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Calendar calendar = Calendar.getInstance();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.day_today_key), calendar.get(Calendar.DAY_OF_YEAR));
        editor.commit();
    }

    static void reduceDaysLeft(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(context.getString(R.string.life_days_left_key), getDaysLeft(context) - 1).apply();
    }
}
