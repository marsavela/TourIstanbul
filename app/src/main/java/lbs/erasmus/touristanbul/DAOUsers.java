package lbs.erasmus.touristanbul;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lbs.erasmus.touristanbul.domain.User;

/**
 * Created by patmonsi on 16/04/14.
 */
public class DAOUsers {

    // Users Table - column names
    private static final String TAG_USERS = "users";
    private static final String TAG_AID = "ID";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String LOG = "DAOUsers";
    private static final String TAG_SUCCESS = "success";

    private final Context context;
    private DBHelper dbHelper;

    public DAOUsers(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    /**
     * Register new user
     *
     * @return a boolean
     */
    public boolean newUserRegistration(User mUser) {

        // getting JSON Object
        // Note that add user url accepts POST method
        try {
            JSONObject json = new AddUser().execute(mUser).get();

            // check log cat from response
            Log.d("Create Response", json.toString());

            // check for success tag

            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return false;

    }


    /**
     * Async Task to get and send data to My Sql database through JSON response.
     **/
    private class AddUser extends AsyncTask<User, Void, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // activity.finish();
            pDialog = new ProgressDialog(context);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(User... user) {

            // url to create new product
            String url_check_user = "http://s459655320.mialojamiento.es/index.php/register";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", user[0].getmName()));
            params.add(new BasicNameValuePair("email", user[0].getmEmail()));
            params.add(new BasicNameValuePair("password", "1234"));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_check_user,
                    "POST", params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
        }
    }

    // Update profile picture
    public boolean updateProfilePicture(User mUser){

        try {
            JSONObject json = new UpdatePicture().execute(mUser).get();

            // check log cat from response
            Log.d("Create Response", json.toString());

            // check for success tag

            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return false;
    }

    /**
     * Async Task to get and send data to My Sql database through JSON response.
     **/
    private class UpdatePicture extends AsyncTask<User, Void, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // activity.finish();
            pDialog = new ProgressDialog(context);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(User... user) {

            // url to create new product
            String url_check_user = "http://s459655320.mialojamiento.es/index.php/updatePicture";

/*            Bitmap bitmap = getImageBitmap(context, "profile_photo");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] byte_arr=stream.toByteArray();
            String image_str=Base64.encodeToString(byte_arr, Base64.NO_WRAP|Base64.URL_SAFE);*/

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", user[0].getmEmail()));
            params.add(new BasicNameValuePair("picture", user[0].getmPhotoUrl()));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_check_user,
                    "POST", params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
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
}

