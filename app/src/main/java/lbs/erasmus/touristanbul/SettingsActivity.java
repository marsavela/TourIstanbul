package lbs.erasmus.touristanbul;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by patmonsi on 20/03/14.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        //TODO No refresca las attractions

        String INTERESTS_PARENTS = "interest1";
        String INTERESTS_BACKPACKERS = "interest2";
        String INTERESTS_BUSINESS = "interest3";
        String ATTRACTIONS_AIRPORTS = "interest4";
        String ATTRACTIONS_HOSPITALS = "interest5";
        String ATTRACTIONS_HOTELS = "interest6";
        String ATTRACTIONS_MALLS = "interest7";
        String ATTRACTIONS_MOSQUES = "interest8";
        String ATTRACTIONS_MUSEUMS = "interest9";
        String ATTRACTIONS_RESTAURANTS = "interest10";
        String ATTRACTIONS_WIFI = "interest11";

        if (key.equals(INTERESTS_PARENTS) || key.equals(INTERESTS_BACKPACKERS) ||
                key.equals(INTERESTS_BUSINESS) || key.equals(ATTRACTIONS_AIRPORTS) ||
                key.equals(ATTRACTIONS_HOSPITALS) || key.equals(ATTRACTIONS_HOTELS) ||
                key.equals(ATTRACTIONS_MALLS) || key.equals(ATTRACTIONS_MOSQUES) ||
                key.equals(ATTRACTIONS_MUSEUMS) ||
                key.equals(ATTRACTIONS_RESTAURANTS) || key.equals(ATTRACTIONS_WIFI)) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("update", true);
            editor.commit();
        }

    }
}
