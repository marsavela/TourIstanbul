package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by patmonsi on 11/03/14.
 */
public class ProfileActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView mTxtName = (TextView) findViewById(R.id.txtName);
        TextView mTxtEmail = (TextView) findViewById(R.id.txtEmail);
        ImageView mImgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        String personPhotoUrl="";

        if(b!=null)
        {
            String j =(String) b.get("name");
            mTxtName.setText(j);
            j=(String) b.get("email");
            mTxtEmail.setText(j);
            personPhotoUrl =(String) b.get("photo");

        }

        new LoadProfileImage(mImgProfilePic).execute(personPhotoUrl);
    }


    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
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
            bmImage.setImageBitmap(result);
        }
    }
}
