package lbs.erasmus.touristanbul;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

    public DAOUsers(Context context) {
        this.context = context;
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

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
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


    // User Login to get the api_key
    public String userLogin(User mUser){

        String api_key=null;

        // url to log in
        String url_user_login = "http://s459655320.mialojamiento.es/index.php/login";

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", mUser.getmEmail()));
        params.add(new BasicNameValuePair("password", "1234"));

        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.makeHttpRequest(url_user_login, "POST", params);

        try {
            api_key = json.getString("apiKey");
            Log.d("apiKey: ", api_key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return api_key;
    }


    // Update user location
    public boolean updateUserLocation(User mUser){

        try {
            JSONObject json = new updateUserLocation().execute(mUser).get();

            // check log cat from response
            Log.d("Update User's Location: ", json.toString());

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
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
    private class updateUserLocation extends AsyncTask<User, Void, JSONObject> {

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
        protected JSONObject doInBackground(User... mUser) {

            // url to update user's location
            String url_update_location = "http://s459655320.mialojamiento.es/index.php/updateLocation";

            Log.e("Update User's Location: ", mUser[0].getmEmail() + ", " + mUser[0].getmLocation().getLatitude() + ", " + mUser[0].getmLocation().getLongitude());

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", mUser[0].getmEmail()));
            params.add(new BasicNameValuePair("locationx", Double.toString(mUser[0].getmLocation().getLatitude())));
            params.add(new BasicNameValuePair("locationy", Double.toString(mUser[0].getmLocation().getLatitude())));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_update_location, "POST", params);

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

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
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
            String url_update_photo = "http://s459655320.mialojamiento.es/index.php/updatePicture";

                Bitmap bitmap = getImageBitmap(context, "profile_photo");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                String image_str = Base64.encodeToString(byte_arr, Base64.NO_WRAP | Base64.URL_SAFE);

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", user[0].getmEmail()));
                params.add(new BasicNameValuePair("picture", image_str));
                params.add(new BasicNameValuePair("From", userLogin(user[0])));


                // getting JSON Object
                // Note that create product url accepts POST method
                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(url_update_photo,
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


    // Get near user's location
    public ArrayList<User> nearUsersPosition(User mUser){
        ArrayList<User> mNearUsersList = new ArrayList<User>();

        JSONArray jsonArray = null;
        Log.d("Near users location", "near users");
        try {
            JSONObject json = new nearUsersPosition().execute(mUser).get();

            // check log cat from response
            Log.d("Near users location", json.toString());

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
                // Getting Array of Users
                jsonArray = json.getJSONArray("users");
            }
            if (jsonArray != null)
                // looping through All users
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = null;
                    c = jsonArray.getJSONObject(i);

                    Location mLocation = new Location("Nose");
                    mLocation.setLatitude(c.getDouble("latitude"));
                    mLocation.setLongitude(c.getDouble("longitude"));

                    // Storing each json item in variable
                    mNearUsersList.add(new User(
                            c.getString("email"),
                            c.getString("name"),
                            c.getString("picture"),
                            mLocation
                    ));
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

        return mNearUsersList;

    }

    /**
     * Async Task to get and send data to My Sql database through JSON response.
     **/
    private class nearUsersPosition extends AsyncTask<User, Void, JSONObject> {

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
        protected JSONObject doInBackground(User... mUser) {

            // url to get near user's location
            String url_near_users_location = "http://s459655320.mialojamiento.es/index.php/nearUserLocation";
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", mUser[0].getmEmail()));
            params.add(new BasicNameValuePair("locationx", Double.toString(mUser[0].getmLocation().getLatitude())));
            params.add(new BasicNameValuePair("locationy", Double.toString(mUser[0].getmLocation().getLatitude())));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_near_users_location, "POST", params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
        }
    }


    // Update user's share bit, receives a user a a share bit (0|1)
    public boolean updateShareBit(User mUser, Integer share){

        // url to get near user's location
        String url_share_bit = "http://s459655320.mialojamiento.es/index.php/updateShareLocation";

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", mUser.getmEmail()));
        params.add(new BasicNameValuePair("shareLocation", Integer.toString(share)));

        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.makeHttpRequest(url_share_bit, "POST", params);

        try {
            // check log cat from response
            Log.d("Share Bit: ", json.toString());

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

}

