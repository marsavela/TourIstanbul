package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.location.LocationClient;
import com.google.example.games.basegameutils.BaseGameActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.fadingactionbar.FadingActionBarHelper;

/**
 * Created by sergiu on 24/03/14.
 */
public class AttractionActivity extends BaseGameActivity implements OnInitListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    // Global constants
    private Attraction mAttraction;
    //TextView mTitleView;
    private TextView mDescription, rateText, openingTimes;
    private ImageView mAttractionImageView;
    private Button mPlayButton;
    private Button mRateButton;
    private TextToSpeech mTTS;
    private boolean isPaying;
    private int ratingStars;
    private DAOAttractions daoAttractions;
    private RatingBar ratingBar;

    private final static int MY_DATA_CHECK_CODE = 0;
    private static final String APP_FOLDER = "touristanbul";
    private static final String IMG_FOLDER = "images";


    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.activity_atraction_header)
                .contentLayout(R.layout.activity_attraction_scrollview);
        setContentView(helper.createView(this));
        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        ratingStars = 0;
        rateText = (TextView)findViewById(R.id.rateText);
        openingTimes = (TextView)findViewById(R.id.openingTimes);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        mAttraction = getIntent().getExtras().getParcelable("Attraction");
        /*mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(mAttraction.getTitle());*/
        mDescription = (TextView) findViewById(R.id.description);
        rateText.setText("" + mAttraction.getRate());
        mDescription.setText(mAttraction.getDescription());
        openingTimes.setText( mAttraction.getOpeningTimes());
        mAttractionImageView = (ImageView) findViewById(R.id.image_header);
        mAttractionImageView.setImageURI(mAttraction.getImageUri());
        mPlayButton = (Button) findViewById(R.id.button2);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDescription();
            }
        });
        mRateButton = (Button) findViewById(R.id.button);
        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkOnlineState()) {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(AttractionActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                } else if (!isUserInRange()) {
                    // Internet Connection is Present
                    // make HTTP requests
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttractionActivity.this);
                    builder.setTitle("Out of range")
                            .setMessage("Seems like you are a bit far from this attraction. Are you sure you want to rate it?")
                            .setCancelable(false)
                            .setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    rateDialog();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {

                }
            }
        });

        getActionBar().setTitle(mAttraction.getTitle());

        helper.initActionBar(this);
    }

    public void addListenerOnRatingBar(View view) {

        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.v("VERBOSE", "Valor de las estrellitas " + rating);
                ratingStars = Integer.parseInt(String.valueOf(rating));
            }
        });
    }

    private void rateDialog() {
        AlertDialog.Builder builderRateDialog = new AlertDialog.Builder(this);
        // Get the layout inflater
        final LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View viewRateDialog = inflater.inflate(R.layout.activity_attraction_rate_dialog, null);
        builderRateDialog.setView(viewRateDialog)
                .setTitle("try")
                        // Add action buttons
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // send the users rate ...
                        if (isConnectingToInternet()) {
                            Log.v("VERBOSE", "rate dialog, tengo conexion");
                            daoAttractions = new DAOAttractions(getApplicationContext());
                            Log.v("VERBOSE", "rate dialog, creo el objeto DAO");
                            addListenerOnRatingBar(viewRateDialog);
                            Log.v("VERBOSE", "rate dialog,creo el listener");
                            new AttemptRate().execute(""+ratingBar.getRating());
                            Log.v("VERBOSE", "rate dialog, he lanzado el attempt con un valor " + ratingBar.getRating());
                            //daoAttractions.updateRatesAttractions(mAttraction.getTitle(),Integer.parseInt(String.valueOf(ratingBar.getRating())));
                        }else{
                            Toast.makeText(getApplicationContext(),"Attraction not rated, you need internet connection", Toast.LENGTH_SHORT).show();
                        }
                        if (isSignedIn()) {
                            Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_attraction_rated));
                            Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_5attraction_rated), 1);
                            Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_25attraction_rated), 1);
                        }
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertRateDialog = builderRateDialog.create();
        alertRateDialog.show();
    }

    private boolean isUserInRange() {
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        //TODO No hay forma de parar el text-to-speech
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void readDescription() {
        //speak straight away
        mTTS.speak(mAttraction.getDescription(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attraction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                shareAttraction();
                return true;
            case R.id.menu_item_point:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(MainActivity.LOCATION_KEY, mAttraction.getLocation());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRateText(int rate){
        rateText = (TextView)findViewById(R.id.rateText);
        rateText.setText(""+rate);
    }

    private void shareAttraction() {
        // Create share intent
        Intent shareIntent = null;
        try {
            shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setText("Hey, I'm at " + mAttraction.getTitle() + "!!")
                    .setType("image/jpeg")
                    .setStream(copy(mAttraction.getImageUri()))
                    .setChooserTitle("Where do you want to share?")
                    .createChooserIntent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivity(shareIntent);
        if (isSignedIn()) {
            Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_attraction_shared));
            Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_5attraction_shared), 1);
            Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_25attraction_shared), 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        break;
                }
                break;
            case MY_DATA_CHECK_CODE :
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    mTTS = new TextToSpeech(this, this);
                }
                else {
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
        }
    }

    @Override
    public void onInit(int i) {
        //check for successful instantiation
        if (i == TextToSpeech.SUCCESS) {
            if(mTTS.isLanguageAvailable(Locale.getDefault())==TextToSpeech.LANG_AVAILABLE)
                mTTS.setLanguage(Locale.US);
        }
        else if (i == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkOnlineState() {

        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setIcon((status) ? R.drawable.btn_check_buttonless_on : android.R.drawable.ic_delete);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
    //    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showAlertDialog(AttractionActivity.this, "No Location Connection",
                    Integer.toString(connectionResult.getErrorCode()), false);
        }
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
    public boolean isConnectingToInternet(){
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

    class AttemptRate extends AsyncTask<String, String, String> {
        List<NameValuePair> params =  new ArrayList<NameValuePair>();
        // Progress Dialog
        //private ProgressDialog pDialog;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
       /*     pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Rating attraction...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
     */   }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            int rateActual = 0;

            String url_vote = "http://s459655320.mialojamiento.es/index.php/rateAttraction";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("name", mAttraction.getTitle()));
            params.add(new BasicNameValuePair("rate", args[0]));
            // getting JSON Object
            // Note that check player url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_vote, "POST",
                    params);

            try {
                String error = json.getString("error");

                if ("false".equals(error)) {
                    rateActual = json.getInt("message");
                    Log.v("VERBOSE", "mensaje del json " + rateActual);
                    Log.v("VERBOSE", "titulo de la attraction " + mAttraction.getTitle());

                    if (rateActual != 0) {
                        daoAttractions.updateRatesAttractions(mAttraction.getTitle(),rateActual);

                    //updateRateText(rateActual);

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            //  pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(getApplicationContext(), "Rated successful", Toast.LENGTH_LONG).show();
            }

        }

    }

    public Uri copy(Uri src) throws IOException {

        File dst = new File(getImagePath() + "share_" + mAttraction.getTitle());

        InputStream in = new FileInputStream(new File(src.getPath()));
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        return Uri.fromFile(dst);
    }

    public String getImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER
                + File.separator + IMG_FOLDER + File.separator;
    }
}