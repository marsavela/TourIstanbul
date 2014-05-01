package lbs.erasmus.touristanbul;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

import lbs.erasmus.touristanbul.domain.User;
import lbs.erasmus.touristanbul.fragments.MapFragment;
import lbs.erasmus.touristanbul.maps.Utils;

/**
 * Created by patmonsi on 20/03/14.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static User mUser;
    public final static String SHARE_LOCATION = "share_location";
    public final static String SYNC_APP = "sync_app";

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        mUser = getIntent().getExtras().getParcelable("User");

    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {

        private DAOAttractions daoAttractions;
        private boolean mDbReady;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            if (mUser == null)
                findPreference(SHARE_LOCATION).setEnabled(false);

            Preference myPref = findPreference(SYNC_APP);
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    update();
                    return true;
                }
            });
        }

        private void update() {

            daoAttractions = new DAOAttractions(getActivity());

            daoAttractions.clearDB();
            new TaskAttractions().execute();
        }


        private class TaskAttractions extends AsyncTask<Void, Void, Void> {

            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = new ProgressDialog(getActivity());
                pDialog.setTitle(getString(R.string.contact_servers));
                pDialog.setMessage(getString(R.string.magic));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (!mDbReady) {
                    daoAttractions.downloadAndSaveData();
                    mDbReady = daoAttractions.checkDataBase();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                super.onPostExecute(aVoid);
            }
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

        String INTERESTS_PARENTS = "Parents";
        String INTERESTS_BACKPACKERS = "Backpackers";
        String INTERESTS_BUSINESS = "Business Travelers";
        String ATTRACTIONS_AIRPORTS = "Airports";
        String ATTRACTIONS_HOSPITALS = "Hospitals";
        String ATTRACTIONS_HOTELS = "Hotels";
        String ATTRACTIONS_MALLS = "Malls";
        String ATTRACTIONS_MOSQUES = "Mosques";
        String ATTRACTIONS_MUSEUMS = "Museums";
        String ATTRACTIONS_RESTAURANTS = "Restaurants";
        String ATTRACTIONS_WIFI = "Wifi Spots";
        String ATTRACTIONS_MONUMENTS = "Monuments";


        if (key.equals(INTERESTS_PARENTS) || key.equals(INTERESTS_BACKPACKERS) ||
                key.equals(INTERESTS_BUSINESS) || key.equals(ATTRACTIONS_AIRPORTS) ||
                key.equals(ATTRACTIONS_HOSPITALS) || key.equals(ATTRACTIONS_HOTELS) ||
                key.equals(ATTRACTIONS_MALLS) || key.equals(ATTRACTIONS_MOSQUES) ||
                key.equals(ATTRACTIONS_MUSEUMS) ||
                key.equals(ATTRACTIONS_RESTAURANTS) || key.equals(ATTRACTIONS_WIFI)
                || key.equals(ATTRACTIONS_MONUMENTS)) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("update", true);
            editor.commit();

        } else if (key.equals(SHARE_LOCATION)) {
            new UpdateShareBit().execute();
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON response.
     **/
    private class UpdateShareBit extends AsyncTask<User, Void, Boolean> {

        @Override
        protected Boolean doInBackground(User... user) {
            return sendBit();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                showToast(getString(R.string.settings_updated));
            } else {
                showToast(getString(R.string.settings_updated_error));
            }
        }
    }

    private boolean sendBit() {
        SettingsManager settingsManager = new SettingsManager(this);
        DAOUsers daoUsers = new DAOUsers(this);
        return daoUsers.updateShareBit(mUser, settingsManager.getShareLocation() ? 1 : 0);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
