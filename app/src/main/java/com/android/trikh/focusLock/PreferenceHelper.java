package com.android.trikh.focusLock;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.trikh.focusLock.alarmPackage.AlarmHelper;

/**
 * Created by trikh on 09-07-2017.
 */

class PreferenceHelper {

    static String getMorningTimeString(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.wake_up_time_key), "5:00");
    }

    static String getSleepTimeString(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.sleep_time_key), "22:00");
    }

    static void setTimePreference(Context context, String time, int requestCode) {
        Log.v("Context: ", context.toString());
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
                editor.putString(context.getString(R.string.wake_up_time_key), time);
                editor.apply();
                AlarmHelper ah = new AlarmHelper(context);
                ah.setAlarm(time, requestCode);
            }
        }
    }


}
