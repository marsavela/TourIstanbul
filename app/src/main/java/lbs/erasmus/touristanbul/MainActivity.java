package lbs.erasmus.touristanbul;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.domain.User;
import lbs.erasmus.touristanbul.fragments.AttractionsFragment;
import lbs.erasmus.touristanbul.fragments.InformationFragment;
import lbs.erasmus.touristanbul.fragments.MapFragment;
import lbs.erasmus.touristanbul.fragments.ToolsFragment;
import lbs.erasmus.touristanbul.maps.Utils;

public class MainActivity extends BaseGameActivity implements View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        MapFragment.MapFragmentCommunication,
        SearchView.OnQueryTextListener{

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private static final int REQUEST_ACHIEVEMENTS = 1;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private boolean show_achievements=false;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private boolean mDbReady;
    private ConnectionResult mConnectionResult;
    private SignInButton mBtnSignIn;
    private ImageView mImgProfilePic;
    private String mPersonName;
    private TextView mViewPersonName;
    private User mUser;
    private Bitmap mUserProfilePhoto;
    private ImageView mImgSettings;
    private TextView mTxtSettings;
    private SearchView searchView;
    private Bundle extras;

    private DAOUsers daoUsers;
    private DAOAttractions daoAttractions;
    private ArrayList<Attraction> mAttractionsList;


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

    // JSON parser class
    private JSONParser jsonParser;
    private ArrayList<Attraction> attractionsList;
    private ArrayList<User> mUsersList;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private CharSequence mTitle;

    /**
     * Used to find others nearby users
     */
    private Location mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedClients(BaseGameActivity.CLIENT_GAMES |
                BaseGameActivity.CLIENT_PLUS);

        extras = new Bundle();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

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

        File mapFile = MapFragment.getMapFile();
        mDbReady = daoAttractions.checkDataBase();
        if (!mDbReady || !mapFile.exists())
            new TaskAttractions().execute();
        filterAttractions();

        daoUsers = new DAOUsers(this);

        jsonParser = new JSONParser();
        attractionsList = null;

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
        updateUI(mGoogleApiClient.isConnected() && mGoogleApiClient != null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (sharedPref.getBoolean("update", false)) {
            filterAttractions();
            editor.putBoolean("update", false);
            editor.commit();
        }
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
        Log.v("VERBOSE", "Enrtro een el navigation drawer " + position );
        switch (position + 1) {
            case 1:
                Log.v("VERBOSE", "Enrtro en map");
                if (mMapFragment == null)
                    mMapFragment = new MapFragment();
                replaceFragment(mMapFragment, getString(R.string.title_map));
                break;
            case 2:
                Log.v("VERBOSE", "Enrtro en attractions");
                extras.putParcelableArrayList("Attractions", mAttractionsList);
                mAttractionsFragment = new AttractionsFragment();
                mAttractionsFragment.setArguments(extras);
                replaceFragment(mAttractionsFragment, getString(R.string.title_attractions));
                mAttractionsFragment.setAttractions(mAttractionsList);
                break;
            case 3:
                Log.v("VERBOSE", "Enrtro en tools");
                if (mToolsFragment == null)
                    mToolsFragment = new ToolsFragment();
                replaceFragment(mToolsFragment, getString(R.string.title_tools));
                break;
            case 4:
                Log.v("VERBOSE", "Enrtro en information");
                if (mInformationFragment == null)
                    mInformationFragment = new InformationFragment();
                replaceFragment(mInformationFragment, getString(R.string.title_information));
                break;
            case 5:
                // Show user's achievements
                if (isSignedIn()) {
                    Games.Achievements.unlock(getApiClient(),  getResources().getString(R.string.achievement_login));
                    startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
                }
                else if (isConnectedToInternet()){
                    show_achievements=true;
                    reconnectClient();
                }
                else{
                    Toast.makeText(this, "You need an Internet connection to use this feature.", Toast.LENGTH_LONG).show();
                }
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
        Log.v("VERBOSE", "Entrando en el create menu");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            MenuItem m = null;
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
        //    Log.v("VERBOSE", "Creando manejador de settings");
            SettingsManager settingsManager = new SettingsManager(this);
        //    Log.v("VERBOSE", "creando view search");
            m = menu.findItem(R.id.action_search);
            searchView = (SearchView) m.getActionView();
            searchView.setQueryHint("Enter attraction");
            searchView.setOnQueryTextListener(this);

            if (mUser == null) {
                m = menu.findItem(R.id.action_nearby_people);
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
         Log.v("VERBOSE", "id menu"  + id + "id de busqueda " + R.id.action_search + "Personitas " + R.id.action_nearby_people);

        if (id == R.id.action_search) {
            Log.v("VERBOSE", "Entro dentro de la busqueda");
           /* searchView = (SearchView) findViewById(R.id.action_search);
            SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
            searchView.setQueryHint("Enter attraction");
            searchView.setOnQueryTextListener(this);*/
            // Get the intent, verify the action and get the query

            // if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            try {
                doMySearch(String.valueOf(searchView.getQuery()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else if (item.getItemId() == R.id.action_nearby_people) {
        //    new TaskNearbyPeople().execute();
            if(mUsersList!=null)
                showNearbyPeopleList(mUsersList);
            else{
                Toast.makeText(this, "Activate your current position first!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNearbyPeopleList(ArrayList<User> users) {

        ArrayList<String> names = new ArrayList<String>();

        for (User u : users) {
            names.add(u.getmName());
        }

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

    private void filterAttractions() {
        mAttractionsList = new ArrayList<Attraction>();
        SettingsManager settingsManager = new SettingsManager(this);
        for (Attraction a : daoAttractions.getAttractions()) {
            //if (interests.contains(a.getCategory()) || interests.contains(a.getInterest()))
            if (settingsManager.checkAttractionCategory(a) || settingsManager.checkAttractionInterest(a))
                mAttractionsList.add(a);
        }

        if (mAttractionsFragment != null)
            mAttractionsFragment.setAttractions(mAttractionsList);
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        try {
            doMySearch(query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    private class TaskNearbyPeople extends AsyncTask<Void, Void, ArrayList<User>> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Looking for someone nearby");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... params) {
            Log.v("VERBOSE users", "Nearby users background");
            ArrayList<User> users = new ArrayList<User>();
        //    users = daoUsers.nearUsersPosition(mUser);
            Log.v("VERBOSE users", users.toString());
            return users;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            showNearbyPeopleList(users);
            pDialog.dismiss();
            super.onPostExecute(users);
        }
    }

    private class TaskAttractions extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Magic ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!mDbReady) {
                daoAttractions.downloadAndSaveData();
                filterAttractions();
                mDbReady = daoAttractions.checkDataBase();
            }

            File mapFile = MapFragment.getMapFile();
            if (!mapFile.exists()) {
                File outputFile = null;
                AssetManager assetsManager = getAssets();
                try {
                    String localfolder = MapFragment.getLocalFolder() + "-gh";
                    // Check if the folder exists
                    File path = new File(localfolder);
                    if (!path.exists()) path.mkdirs();

                    // Delete zip file if exists
                    outputFile = new File(path, MapFragment.ZIP_FILENAME);
                    if (outputFile.exists()) outputFile.delete();

                    InputStream inputStream = assetsManager.open(MapFragment.ZIP_FILENAME);
                    Utils.createFileFromInputStream(inputStream, localfolder + "/" + MapFragment.ZIP_FILENAME);

                    // unzip files file
                    Utils.unzipFile(localfolder, MapFragment.ZIP_FILENAME);

                    if (outputFile.exists()) outputFile.delete();

                    if (mMapFragment != null) {
                        mMapFragment.initializeMapView();
                        mMapFragment.centerMap();
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error while copying maps to sd");
                }
            }

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
                if(isConnectedToInternet()) {
                    signInWithGplus();
                    beginUserInitiatedSignIn();
                }
                else{
                    Toast.makeText(this, "You need an Internet connection to log in", Toast.LENGTH_LONG).show();
                }
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
        i.putExtra("User", mUser);
        startActivity(i);
        mNavigationDrawerFragment.closeDrawer();
    }

    private void openUserProfile() {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("User", mUser);
            startActivity(i);
            mNavigationDrawerFragment.closeDrawer();

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
        //        resolveSignInError();
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

        // Update the UI after signing in with google+ and downloading the profile picture
        updateUI(true);

        if (isSignedIn())
            Games.Achievements.unlock(getApiClient(),  getResources().getString(R.string.achievement_login));

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
        //    resolveSignInError();
        }
    }

    /**
     * Method to resolve any sign in errors
     * */
/*    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }*/


    /**
     * Game Services Sign in
     */
    @Override
    public void onSignInSucceeded() {
        // show sign-out button, hide the sign-in button
        mBtnSignIn.setVisibility(View.GONE);
        mImgProfilePic.setVisibility(View.VISIBLE);
        mViewPersonName.setVisibility(View.VISIBLE);

        // Open achievements activity
        if(show_achievements){
            if (isSignedIn()) {
                Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_login));
                startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
            }
            show_achievements=false;
        }
    }

    @Override
    public void onSignInFailed() {
        mBtnSignIn.setVisibility(View.VISIBLE);
        mImgProfilePic.setVisibility(View.GONE);
        mViewPersonName.setVisibility(View.GONE);
    }


    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                mPersonName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

        //        Log.e(TAG, "Name: " + mPersonName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl);

                if(getImageBitmap(this, "profile_photo")!=null)
                    mImgProfilePic.setImageBitmap(getCroppedBitmap(getImageBitmap(this, "profile_photo")));
                else
                    new LoadProfileImage(mImgProfilePic, mUserProfilePhoto).execute(personPhotoUrl);

                if (mViewPersonName != null)
                    mViewPersonName.setText(mPersonName);

                mUser = new User(email, mPersonName, personPhotoUrl, personGooglePlusProfile, mUserLocation);
                mUser.setmPhoto(mImgProfilePic.getDrawingCache());

                if(isConnectedToInternet() && daoUsers.newUserRegistration(mUser)){
                    Toast.makeText(this, "User registered succesfully", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "User registered succesfully");
                }
                else{
    //                Toast.makeText(this, "You need an Internet connection to log in", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "User not registered");
                }
        //        mUsersList = daoUsers.nearUsersPosition(mUser);

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

    public Bitmap getImageBitmap(Context context,String name){
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Method to comunicate the map fragment with the list of attractions
     * @return
     */
    @Override
    public List<Attraction> getAttractionList() {
        //filterAttractions();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mViewPersonName.setText(mPersonName);
            bmImage.setImageBitmap(getCroppedBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.kebap)));
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
                if(daoUsers.updateProfilePicture(mUser)){
                    Log.e(TAG, "User profile picture updated succesfully");
                }
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


    private void doMySearch(String queryStr) throws JSONException {

        ArrayList<Attraction> attractions = new ArrayList<Attraction>();
        boolean isFound = false;
        if(isConnectedToInternet()){
            //AttemptAttractions attemptAttractions = new AttemptAttractions(queryStr, null, attractions);
            mAttractionsList = new ArrayList<Attraction>();
             new AttemptAttractions().execute(queryStr);
             attractions = mAttractionsList;
                if(attractions.size() == 0){
                    attractions = daoAttractions.getAttractionsByName(queryStr);}
            extras.putParcelableArrayList("Attractions", mAttractionsList);
        } else {
            attractions = daoAttractions.getAttractionsByName(queryStr);
        }
        Log.v("VERBOSE", "Tamaño del array: " + attractions.size());
        if (attractions.size() != 0 ){
            ArrayList<Attraction> attractionSearch = new ArrayList<Attraction>();
            for (int i = 0; i < attractions.size(); i++){
                Log.v("VERBOSE", "Agrego attraction: " + i);
                isFound = true;
                attractionSearch.add(attractions.get(i));
            }
            Bundle extras = new Bundle();
            extras.putParcelableArrayList("Attractions", attractionSearch);
            if (mAttractionsFragment == null) {
                mAttractionsFragment = new AttractionsFragment();
                mAttractionsFragment.setArguments(extras);
            }
            replaceFragment(mAttractionsFragment, getString(R.string.title_attractions));
            Log.v("VERBOSE", "VAlor de list " + attractionSearch.size());
            extras.putParcelableArrayList("Attractions", mAttractionsList);
            mAttractionsFragment.setAttractions(attractionSearch);
        }

        if (isFound == false){
            Toast.makeText(this, "Attraction introduced is not valid, please try again", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    class AttemptAttractions extends AsyncTask<String, Void, ArrayList<Attraction>> {
        List<NameValuePair> params =  new ArrayList<NameValuePair>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Attraction> doInBackground(String... args) {
            // TODO Auto-generated method stub
            HttpURLConnection con = null;
            JSONObject jsonAttraction = null;
            try {
                String url = "http://s459655320.mialojamiento.es/index.php/attraction/" + args[0];
                JSONObject json = jsonParser.makeHttpRequest(
                        url, "GET", null);
                if(json.getString("error").equals(false)){
                    mAttractionsList = new ArrayList<Attraction>();
                    for (int i = 0; i < json.getJSONArray("attractions").length(); i++) {
                        jsonAttraction = (JSONObject) json.getJSONArray("attractions").get(i);
                         Location l = new Location("");
                        l.setLatitude(jsonAttraction.getDouble("latitude"));
                        l.setLongitude(jsonAttraction.getDouble("longitude"));
                        Attraction attraction = new Attraction(jsonAttraction.getString("name"),jsonAttraction.getString("subtitle"), jsonAttraction.getString("description"),  jsonAttraction.getString("address"), l, jsonAttraction.getString("category"),jsonAttraction.getString("interest"), jsonAttraction.getDouble("rate"), jsonAttraction.getInt("numRates"), jsonAttraction.getString("openingTime"), jsonAttraction.getString("nameImage"));
                        mAttractionsList.add(attraction);
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Attraction introduced is not valid, please try again", Toast.LENGTH_SHORT);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mAttractionsList;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
        }
    }

    /**
     * When location is enabled the position will be updated
     */
    @Override
    public void setUserLocation(Location location) {
        mUserLocation = location;
        Log.e(TAG, Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
        mUser.setmLocation(location);
        daoUsers.updateUserLocation(mUser);

   /*     if(mUsersList==null) {
            mUsersList = daoUsers.nearUsersPosition(mUser);
        }*/
    }
}
