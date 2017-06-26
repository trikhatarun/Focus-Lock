package com.example.trikh.focuson;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    private String wakeUpTimeString, sleepTimeString;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.activity_main, container, false);

        ButterKnife.bind(getActivity(), rootview);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rajdhani-Medium.ttf");

        wakeUpTime.setTypeface(typeface);
        sleepTime.setTypeface(typeface);

        loadPreferences();

        wakeUpTime.setText(wakeUpTimeString);
        sleepTime.setText(sleepTimeString);

        wakeUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootview;

    }

    private void loadPreferences() {
        wakeUpTimeString = preferences.getString(getString(R.string.wake_up_time_key), "05:00");
        sleepTimeString = preferences.getString(getString(R.string.sleep_time_key), "22:00");
    }
}
