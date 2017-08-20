package com.android.trikh.focusLock.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.trikh.focusLock.PreferenceHelper;
import com.android.trikh.focusLock.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.wake_up_time)
    TextView wakeUpTime;
    @BindView(R.id.sleep_time)
    TextView sleepTime;
    @BindView(R.id.wake_up_icon)
    ImageView wakeUpIcon;
    @BindView(R.id.sleep_icon)
    ImageView sleepIcon;
    @BindView(R.id.days_left_text_view)
    TextView daysLeft;
    @BindView(R.id.days_left_string)
    TextView daysLeftString;
    @BindView(R.id.disclaimer)
    TextView disclaimer;

    private int hour, minute;
    private DecimalFormat formatter;
    private Context contextInstance;

    private String wakeUpTimeString, sleepTimeString;

    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }

    private void updateUI() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contextInstance);
        int daysLeftCount = sharedPreferences.getInt(contextInstance.getString(R.string.life_days_left_key), 0);
        if (daysLeftCount != 0) {
            daysLeft.setText(String.valueOf(daysLeftCount));
            disclaimer.setText(contextInstance.getString(R.string.disclaimer, PreferenceHelper.getCountryName(contextInstance)));
            daysLeftString.setVisibility(View.VISIBLE);
            daysLeft.setVisibility(View.VISIBLE);
            disclaimer.setVisibility(View.VISIBLE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.home, container, false);

        ButterKnife.bind(this, rootview);

        contextInstance = getContext();

        YoYo.with(Techniques.Pulse)
                .duration(800)
                .delay(15)
                .playOn(wakeUpTime);

        YoYo.with(Techniques.Pulse)
                .duration(800)
                .delay(15)
                .playOn(sleepTime);

        formatter = new DecimalFormat("00");

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rajdhani-Medium.ttf");

        wakeUpTime.setTypeface(typeface);
        sleepTime.setTypeface(typeface);
        daysLeft.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rajdhani-Bold.ttf"));

        loadPreferences();

        wakeUpTime.setText(wakeUpTimeString);
        sleepTime.setText(sleepTimeString);

        wakeUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morningTimeOnClick();
            }
        });
        wakeUpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morningTimeOnClick();
            }
        });


        sleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepTimeOnClick();
            }
        });
        sleepIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepTimeOnClick();
            }
        });

        int daysLeftCount = PreferenceHelper.getDaysLeft(contextInstance);
        if (daysLeftCount != 0) {
            daysLeft.setText(String.valueOf(daysLeftCount));
            disclaimer.setText(contextInstance.getString(R.string.disclaimer, PreferenceHelper.getCountryName(contextInstance)));
            daysLeftString.setVisibility(View.VISIBLE);
            daysLeft.setVisibility(View.VISIBLE);
        }

        return rootview;

    }

    private void loadPreferences() {
        Context context = contextInstance;
        wakeUpTimeString = PreferenceHelper.getMorningTimeString(context);
        sleepTimeString = PreferenceHelper.getSleepTimeString(context);
    }

    private void morningTimeOnClick() {
        String[] time = wakeUpTimeString.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                wakeUpTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                wakeUpTime.setText(wakeUpTimeString);
                PreferenceHelper.setTimePreference(contextInstance, wakeUpTimeString, Integer.parseInt(getString(R.string.request_code_for_morning)));
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Wake Up time");
        timePickerDialog.show(getActivity().getFragmentManager(), "NOTAG");
    }

    private void sleepTimeOnClick() {
        String[] time = sleepTimeString.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                sleepTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                sleepTime.setText(sleepTimeString);
                PreferenceHelper.setTimePreference(contextInstance, sleepTimeString, Integer.parseInt(getString(R.string.request_code_for_night)));
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Sleeping Time");
        timePickerDialog.setAccentColor(getResources().getColor(R.color.sleep_clock_accent));
        timePickerDialog.show(getActivity().getFragmentManager(), "NOTAG");
    }
}


