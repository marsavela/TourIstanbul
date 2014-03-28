package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.util.Locale;

import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.fadingactionbar.FadingActionBarHelper;

/**
 * Created by sergiu on 24/03/14.
 */
public class AttractionActivity extends Activity implements OnInitListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    // Global constants
    private Attraction mAttraction;
    //TextView mTitleView;
    private TextView mDescription;
    private ImageView mAttractionImageView;
    private Button mListenButton;
    private Button mRateButton;

    private TextToSpeech mTTS;
    private final static int MY_DATA_CHECK_CODE = 0;

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

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        mAttraction = getIntent().getExtras().getParcelable("Attraction");
        /*mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(mAttraction.getTitle());*/
        mDescription = (TextView) findViewById(R.id.description);
        mDescription.setText(mAttraction.getSubtitle());
        mAttractionImageView = (ImageView) findViewById(R.id.image_header);
        mAttractionImageView.setImageURI(mAttraction.getImageUri());
        mListenButton = (Button) findViewById(R.id.button2);
        mListenButton.setOnClickListener(new View.OnClickListener() {
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
                                    dialog.cancel();
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

    private void rateDialog() {
        AlertDialog.Builder builderRateDialog = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builderRateDialog.setView(inflater.inflate(R.layout.activity_attraction_rate_dialog, null))
                .setTitle("try")
                        // Add action buttons
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // send the users rate ...
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
        mTTS.speak(mAttraction.getSubtitle(), TextToSpeech.QUEUE_FLUSH, null);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareAttraction() {
        // Create share intent
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText("Hey, I'm at " + mAttraction.getTitle() + "!!")
                .setType("image/jpeg")
                .setStream(mAttraction.getImageUri())
                .setChooserTitle("Where do you want to share?")
                .createChooserIntent();
        startActivity(shareIntent);
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

    /*private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog

            }
            return false;
        }
    }*/

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
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

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
}