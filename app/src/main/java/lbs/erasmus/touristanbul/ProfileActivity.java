package lbs.erasmus.touristanbul;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.io.FileInputStream;

import lbs.erasmus.touristanbul.domain.User;

/**
 * Created by patmonsi on 11/03/14.
 */
public class ProfileActivity extends BaseGameActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private Button mBtnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView mTxtName = (TextView) findViewById(R.id.txtName);
        TextView mTxtEmail = (TextView) findViewById(R.id.txtEmail);
        ImageView mImgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);

        User mUser = getIntent().getExtras().getParcelable("User");

        if (mUser != null) {
            mTxtName.setText(mUser.getmName());
            mTxtEmail.setText(mUser.getmEmail());
        }

        mImgProfilePic.setImageBitmap(getImageBitmap(this, "profile_photo"));

        // Set up the Google+ buttons
        mBtnSignOut = (Button) findViewById(R.id.btn_sign_out);

        // Button click listeners
        mBtnSignOut.setOnClickListener(this);

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_out:
                // Sign out button clicked
                signOutFromGplus();
                break;
        }
    }

    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            mBtnSignOut.setVisibility(View.GONE);
            signOut();
            this.finish();
        }
    }


    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
}
