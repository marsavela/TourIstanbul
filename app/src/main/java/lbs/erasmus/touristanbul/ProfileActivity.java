package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import lbs.erasmus.touristanbul.domain.User;

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

        User mUser = getIntent().getExtras().getParcelable("User");

        mTxtName.setText(mUser.getmName());
        mTxtEmail.setText(mUser.getmEmail());
        mImgProfilePic.setImageBitmap(getImageBitmap(this, "profile_photo"));

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
