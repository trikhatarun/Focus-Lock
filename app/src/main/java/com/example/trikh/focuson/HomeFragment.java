package com.example.trikh.focuson;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 21-06-2017.
 */

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.wake_up_time)
    TextView wakeUpTime;
    @BindView(R.id.sleep_time)
    TextView sleepTime;

    private SharedPreferences preferences;
    private int hour, minute;
    private DecimalFormat formatter;

    private String wakeUpTimeString, sleepTimeString;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.home, container, false);

        ButterKnife.bind(this, rootview);

        YoYo.with(Techniques.Pulse)
                .duration(800)
                .delay(15)
                .playOn(wakeUpTime);

        YoYo.with(Techniques.Pulse)
                .duration(800)
                .delay(15)
                .playOn(sleepTime);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        formatter = new DecimalFormat("00");

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rajdhani-Medium.ttf");

        wakeUpTime.setTypeface(typeface);
        sleepTime.setTypeface(typeface);

        loadPreferences();

        wakeUpTime.setText(wakeUpTimeString);
        sleepTime.setText(sleepTimeString);

        wakeUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] time = wakeUpTimeString.split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                TimePickerDialog timepicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SharedPreferences.Editor editor = preferences.edit();
                        wakeUpTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                        wakeUpTime.setText(wakeUpTimeString);
                        editor.putString(getString(R.string.wake_up_time_key), wakeUpTimeString);
                        editor.apply();
                    }
                }, hour, minute, true);
                timepicker.setTitle("Wake Up time");
                timepicker.show();
            }
        });
        sleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] time = sleepTimeString.split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                TimePickerDialog timepicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SharedPreferences.Editor editor = preferences.edit();
                        sleepTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                        sleepTime.setText(sleepTimeString);
                        editor.putString(getString(R.string.sleep_time_key), sleepTimeString);
                        editor.apply();
                    }
                }, hour, minute, true);
                timepicker.setTitle("Sleeping time");
                timepicker.show();
            }
        });

        return rootview;

    }

    private void loadPreferences() {
        wakeUpTimeString = preferences.getString(getString(R.string.wake_up_time_key), "05:00");
        sleepTimeString = preferences.getString(getString(R.string.sleep_time_key), "22:00");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadPreferences();
    }

    private void alarm() {
        //Code to initiate alarm manager
        AlarmManager alarmManagerInstance = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        PendingIntent intent = PendingIntent.getService(getContext(), 0, new Intent(getContext(), AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String[] time = preferences.getString(getString(R.string.wake_up_time_key), "05:00").split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);

        Calendar wakeUpTimeCalendar = Calendar.getInstance();
        wakeUpTimeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        wakeUpTimeCalendar.set(Calendar.MINUTE, minute);

        alarmManagerInstance.setInexactRepeating(AlarmManager.RTC, wakeUpTimeCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, intent);
    }
}
