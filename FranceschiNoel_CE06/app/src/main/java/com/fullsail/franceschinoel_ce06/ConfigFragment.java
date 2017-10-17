package com.fullsail.franceschinoel_ce06;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

// Noel Franceschi
// MDF3 1610
// ConfigFragment.java

public class ConfigFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public static final String THEME_PREFERENCE = "com.fullsail.franceschinoel_ce06.THEME_CHOOSER";
    public static final String LOCATION_PREFERENCE = "com.fullsail.franceschinoel_ce06.LOCATION_CHOOSER";

    public ConfigFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.w_config);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Preference themes = findPreference(THEME_PREFERENCE);
        themes.setOnPreferenceChangeListener(this);

        Preference locations = findPreference(LOCATION_PREFERENCE);
        locations.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String prefKey = preference.getKey();

        switch (prefKey) {

            case THEME_PREFERENCE:
                setThemePreference(getActivity(), (String) newValue);
                return true;

            case LOCATION_PREFERENCE:
                setLocationPreference(getActivity(), (String) newValue);
                return true;
        }

        return false;
    }

    public static void setThemePreference(Context context, String theme) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(THEME_PREFERENCE, theme).apply();
    }

    public static void setLocationPreference(Context context, String location) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(LOCATION_PREFERENCE, location).apply();
    }

}

