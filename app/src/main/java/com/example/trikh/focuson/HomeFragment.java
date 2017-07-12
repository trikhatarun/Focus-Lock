package com.example.trikh.focuson;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 21-06-2017.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.wake_up_time)
    TextView wakeUpTime;
    @BindView(R.id.sleep_time)
    TextView sleepTime;

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
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                        wakeUpTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                        wakeUpTime.setText(wakeUpTimeString);
                        PreferenceHelper.setTimePreference(getContext(), wakeUpTimeString, Integer.parseInt(getString(R.string.request_code_for_morning)));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Wake Up time");
                timePickerDialog.show(getActivity().getFragmentManager(), "NOTAG");
            }
        });
        sleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] time = sleepTimeString.split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                        sleepTimeString = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                        sleepTime.setText(sleepTimeString);
                        PreferenceHelper.setTimePreference(getContext(), sleepTimeString, Integer.parseInt(getString(R.string.request_code_for_night)));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Sleeping Time");
                timePickerDialog.show(getActivity().getFragmentManager(), "NOTAG");
            }
        });

        return rootview;

    }

    private void loadPreferences() {
        Context context = getContext();
        wakeUpTimeString = PreferenceHelper.getMorningTimeString(context);
        sleepTimeString = PreferenceHelper.getSleepTimeString(context);
    }
}
