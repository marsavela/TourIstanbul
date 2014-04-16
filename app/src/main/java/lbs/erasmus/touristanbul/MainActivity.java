package lbs.erasmus.touristanbul;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.domain.User;
import lbs.erasmus.touristanbul.fragments.AttractionsFragment;
import lbs.erasmus.touristanbul.fragments.InformationFragment;
import lbs.erasmus.touristanbul.fragments.MapFragment;
import lbs.erasmus.touristanbul.fragments.ToolsFragment;

public class MainActivity extends BaseGameActivity implements View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        MapFragment.MapFragmentCommunication, OnPreferencesChanged {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton mBtnSignIn;
    private ImageView mImgProfilePic;
    private String mPersonName;
    private TextView mViewPersonName;
    private User mUser;
    private Bitmap mUserProfilePhoto;
    private ImageView mImgSettings;
    private TextView mTxtSettings;

    private DAOAttractions daoAttractions;
    private ArrayList<Attraction> mAttractionsList;
    SettingsActivity settings;

    /**
     * Fragments for each section of the application.
     */
    private MapFragment mMapFragment;
    private AttractionsFragment mAttractionsFragment;
    private ToolsFragment mToolsFragment;
    private InformationFragment mInformationFragment;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedClients(BaseGameActivity.CLIENT_GAMES |
                BaseGameActivity.CLIENT_PLUS);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        settings = new SettingsActivity();
        settings.setOnPreferencesChanged(this);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.setRetainInstance(true);

        // Set up the Google+ buttons
        mBtnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        // Set up the profile
        mImgProfilePic = (ImageView) findViewById(R.id.user_profile_photo);

        // Set up the profile name
        mViewPersonName = (TextView) findViewById(R.id.user_profile_name);

        // Set up the settings buttons
        mImgSettings = (ImageView) findViewById(R.id.action_settings_image);
        mTxtSettings = (TextView) findViewById(R.id.action_settings_text);

        // Button click listeners
        mBtnSignIn.setOnClickListener(this);
        mImgProfilePic.setOnClickListener(this);
        mViewPersonName.setOnClickListener(this);
        mImgSettings.setOnClickListener(this);
        mTxtSettings.setOnClickListener(this);

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        /**
         * Generate content for testing
         */
        daoAttractions = new DAOAttractions(this);
        mAttractionsList = new ArrayList<Attraction>();

        if (!daoAttractions.checkDataBase())
            new TaskAttractions().execute();
        //mAttractionsList = daoAttractions.getAttractions();
        filterAttractions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mGoogleApiClient.isConnected())
        mGoogleApiClient.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI(mGoogleApiClient.isConnected() && mGoogleApiClient!=null);
    //    mImgProfilePic.setImageBitmap(getImageBitmap(this, "profile_photo"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Update the main content by replacing fragments

        switch (position + 1) {
            case 1:
                if (mMapFragment == null)
                    mMapFragment = new MapFragment();
                replaceFragment(mMapFragment, getString(R.string.title_map));
                break;
            case 2:
                Bundle extras = new Bundle();
                extras.putParcelableArrayList("Attractions",mAttractionsList);
                if (mAttractionsFragment == null) {
                    mAttractionsFragment = new AttractionsFragment();
                    mAttractionsFragment.setArguments(extras);
                }
                replaceFragment(mAttractionsFragment, getString(R.string.title_attractions));
                break;
            case 3:
                if (mToolsFragment == null)
                    mToolsFragment = new ToolsFragment();
                replaceFragment(mToolsFragment, getString(R.string.title_tools));
                break;
            case 4:
                if (mInformationFragment == null)
                    mInformationFragment = new InformationFragment();
                replaceFragment(mInformationFragment, getString(R.string.title_information));
                break;
        }
    }

    private void replaceFragment(Fragment newFragment, CharSequence mTitle) {
        this.mTitle = mTitle;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            SettingsManager settingsManager = new SettingsManager(this);
            if (!settingsManager.getShareLocation()) {
                MenuItem m = menu.findItem(R.id.action_nearby_people);
                m.setVisible(false);
            }

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        } else if (item.getItemId() == R.id.action_nearby_people) {
            showNearbyPeopleList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNearbyPeopleList() {

        String names[] ={"Antonio","Bianca","Carlos","Domingo"};

        AlertDialog.Builder builderRateDialog = new AlertDialog.Builder(this);
        // Get the layout inflater
        View lista = getLayoutInflater().inflate(R.layout.fragment_map_dialog_list_people, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builderRateDialog.setView(lista)
                .setTitle("Meet with");

        AlertDialog alertRateDialog = builderRateDialog.create();
        ListView lv = (ListView) lista.findViewById(R.id.list_nearby_people);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Example action number " + Integer.toString(i),
                        Toast.LENGTH_SHORT).show();
            }
        });
        alertRateDialog.show();
    }

    /*@Override
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
            filterAttractions();
        }

    }*/

    private void filterAttractions() {
        mAttractionsList = new ArrayList<Attraction>();
        SettingsManager settingsManager = new SettingsManager(this);
        ArrayList<String> interests = getUserInterests();
        for (Attraction a : daoAttractions.getAttractions()) {
            //if (interests.contains(a.getCategory()) || interests.contains(a.getInterest()))
            if (settingsManager.checkAttractionCategory(a) || settingsManager.checkAttractionInterest(a))
                mAttractionsList.add(a);
        }
    }

    private ArrayList<String> getUserInterests() {
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

        SettingsManager settingsManager = new SettingsManager(this);
        ArrayList<String> interests = new ArrayList<String>();

        if(settingsManager.getParentsInterest())
            interests.add(INTERESTS_PARENTS);
        if(settingsManager.getBackpackersInterest())
            interests.add(INTERESTS_BACKPACKERS);
        if(settingsManager.getBusinessInterest())
            interests.add(INTERESTS_BUSINESS);
        if(settingsManager.getAirportAttractions())
            interests.add(ATTRACTIONS_AIRPORTS);
        if(settingsManager.getHospitalAttractions())
            interests.add(ATTRACTIONS_HOSPITALS);
        if(settingsManager.getHotelsAttractions())
            interests.add(ATTRACTIONS_HOTELS);
        if(settingsManager.getMallsAttractions())
            interests.add(ATTRACTIONS_MALLS);
        if(settingsManager.getMosquesAttractions())
            interests.add(ATTRACTIONS_MOSQUES);
        if(settingsManager.getMuseumsAttractions())
            interests.add(ATTRACTIONS_MUSEUMS);
        if(settingsManager.getRestaurantsAttractions())
            interests.add(ATTRACTIONS_RESTAURANTS);
        if(settingsManager.getWifiAttractions())
            interests.add(ATTRACTIONS_WIFI);

        return interests;
    }

    @Override
    public void onPreferencesUpdated() {
        filterAttractions();
    }

    private class TaskAttractions extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            daoAttractions.downloadAndSaveData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAttractionsList = daoAttractions.getAttractions();
            /*Attraction attraction = new Attraction("Airport","", 40.983934, 28.820443, null);
            mAttractionsList.add(attraction);
            attraction = new Attraction("Yeditepe University","", 40.973210, 29.151750, null);
            mAttractionsList.add(attraction);*/
            pDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Sign in button clicked
                signInWithGplus();
                beginUserInitiatedSignIn();
                break;
            case R.id.user_profile_photo:
                // Open Profile
                openUserProfile();
                break;
            case R.id.user_profile_name:
                // Open Profile
                openUserProfile();
                break;
            case R.id.action_settings_image:
                // Open user settings
                openUserSettings();
                break;
            case R.id.action_settings_text:
                // Open user settings
                openUserSettings();
                break;

        }
    }

    private void openUserSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        mNavigationDrawerFragment.closeDrawer();
    }

    private void openUserProfile() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("User", mUser);
        startActivity(i);
        mNavigationDrawerFragment.closeDrawer();

        if (isSignedIn())
            Games.Achievements.unlock(getApiClient(),  getResources().getString(R.string.achievement_login));
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;

        // Get user's information

        getProfileInformation();

        // Update the UI after signing in
        updateUI(true);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mBtnSignIn.setVisibility(View.GONE);
            mImgProfilePic.setVisibility(View.VISIBLE);
            mViewPersonName.setVisibility(View.VISIBLE);
        } else {
            mBtnSignIn.setVisibility(View.VISIBLE);
            mImgProfilePic.setVisibility(View.GONE);
            mViewPersonName.setVisibility(View.GONE);
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                mPersonName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + mPersonName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                mUser = new User(email, mPersonName, personPhotoUrl, personGooglePlusProfile);

                new LoadProfileImage(mImgProfilePic, mUserProfilePhoto).execute(personPhotoUrl);
                if (mViewPersonName != null)
                    mViewPersonName.setText(mPersonName);
                mUser.setmPhoto(mImgProfilePic.getDrawingCache());


            } else {
                Toast.makeText(this,
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crops a circle out of the thumbnail photo.
     */
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        int halfWidth = bitmap.getWidth()/2;
        int halfHeight = bitmap.getHeight()/2;

        canvas.drawCircle(halfWidth, halfHeight, Math.max(halfWidth, halfHeight), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public void onSignInSucceeded() {
        // show sign-out button, hide the sign-in button
        mBtnSignIn.setVisibility(View.GONE);
        mImgProfilePic.setVisibility(View.VISIBLE);
        mViewPersonName.setVisibility(View.VISIBLE);
    //    getProfileInformation();
    }

    @Override
    public void onSignInFailed() {
        mBtnSignIn.setVisibility(View.VISIBLE);
        mImgProfilePic.setVisibility(View.GONE);
        mViewPersonName.setVisibility(View.GONE);
    }

    /**
     * Method to comunicate the map fragment with the list of attractions
     * @return
     */
    @Override
    public List<Attraction> getAttractionList() {
        return mAttractionsList;
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Bitmap mIcon11 = null;

        public LoadProfileImage(ImageView bmImage, Bitmap mIcon11) {
            this.bmImage = bmImage;
            this.mIcon11 = mIcon11;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //mImgProfilePic.setImageBitmap(getCroppedBitmap(mUserProfilePhoto));
            if (result != null) {
                bmImage.setImageBitmap(getCroppedBitmap(result));
                saveImage(getBaseContext(), result, "profile_photo");
            } else
                bmImage.setImageBitmap(getCroppedBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.kebap)));
        }
    }

    public void saveImage(Context context, Bitmap b,String name){

        FileOutputStream out;
        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImageBitmap(Context context,String name){
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }

}
