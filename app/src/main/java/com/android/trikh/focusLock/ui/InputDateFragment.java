package com.android.trikh.focusLock.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.trikh.focusLock.R;
import com.android.trikh.focusLock.data.JsonHelper;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputDateFragment extends SlideFragment {

    @BindView(R.id.date_input)
    EditText dateInput;

    private Boolean gotDate = false;

    public InputDateFragment() {
    }

    public static InputDateFragment newInstance() {
        return new InputDateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dob_input_layout, container, false);

        final Context context = getContext();

        ButterKnife.bind(this, view);

        final Calendar calendar = Calendar.getInstance();
        final int yearToday = calendar.get(Calendar.YEAR);
        final int monthToday = calendar.get(Calendar.MONTH);
        final int dayToday = calendar.get(Calendar.DATE);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        String countryCode = tm.getSimCountryIso();
                        Locale locale = new Locale("", countryCode);
                        String country = locale.getDisplayCountry().toLowerCase();
                        Log.v("country: ", country);
                        JSONArray data = null;
                        try {
                            data = new JSONArray(new JsonHelper(context).loadJSONFromAsset());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {
                            for (int i = 0; i < data.length(); i++) {
                                try {
                                    JSONObject jsonobject = data.getJSONObject(i);
                                    String countryJson = jsonobject.getString("country");
                                    Log.v("countryJson: ", countryJson);
                                    if (countryJson.toLowerCase().equals(country)) {
                                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                                        int daysLeftlog = (int) (Float.valueOf(jsonobject.getString("2015")) * 365 - getDifference(dayOfMonth, monthOfYear, year, yearToday, monthToday, dayToday));
                                        editor.putInt(getString(R.string.life_days_left_key), daysLeftlog);
                                        Log.v("daysLeftIf: ", String.valueOf(getDifference(dayOfMonth, monthOfYear, year, yearToday, monthToday, dayToday)));
                                        editor.putString(getString(R.string.country_key), countryJson);
                                        editor.commit();
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        dateInput.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
                        gotDate = true;
                    }
                }, yearToday, monthToday, dayToday - 1);
                dpd.showYearPickerFirst(true);
                dpd.show(getActivity().getFragmentManager(), "NOTAG");
            }
        });

        return view;
    }

    private Integer getDifference(int dayOfMonth, int monthOfYear, int year, int yearToday, int monthToday, int dateToday) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(yearToday, monthToday, dateToday);
        date2.clear();
        date2.set(year, monthOfYear, dayOfMonth);

        long diff = date1.getTimeInMillis() - date2.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        Log.v("days lelo: ", String.valueOf(diff));

        return (int) dayCount;
    }

    @Override
    public boolean canGoForward() {
        return gotDate;
    }
}
