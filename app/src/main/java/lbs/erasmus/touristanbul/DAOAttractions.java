package lbs.erasmus.touristanbul;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.domain.Attraction;

/**
 * Created by SergiuDaniel on 15/04/2014.
 */
public class DAOAttractions {

    // Attractions Table - column names
    private static final String TAG_ATTRACTIONS = "attractions";
    private static final String TAG_AID = "ID";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_RATE = "rate";
    private static final String TAG_NUM_RATES = "numRates";
    private static final String TAG_OPENING_TIME = "openingTime";
    private static final String TAG_INTEREST = "interest";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_SUBTITLE = "subtitle";
    private static final String TAG_NAME_IMAGE = "nameImage";

    private ArrayList<Attraction> mAttractionsList;
    private DBHelper dao;
    private Context context;

    private static final String DB_NAME = "kebap.db";

    public DAOAttractions(Context context) {
        this.context = context;
        dao = new DBHelper(context);
        mAttractionsList = new ArrayList<Attraction>();
    }

    public ArrayList<Attraction> getmAttractionsList() {
        return mAttractionsList;
    }

    /*private void getAttractions() {
        File db = context.getDatabasePath(DB_NAME);
        if (db.exists())
            mAttractionsList = dao.getAttractions();
        else
            new TaskAttractions().execute();
    }*/

    public ArrayList<Attraction> getAttractions() {

        JSONArray jsonArray = null;
        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();

        String url_check_user = "http://s459655320.mialojamiento.es/index.php/attractions";

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair(TAG_TID, Integer.toString(team
        //      .getTeamId())));

        // getting JSON Object
        // Note that check player url accepts POST method
        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.makeHttpRequest(url_check_user, "GET",
                params);

        try {
            // Check your log cat for JSON reponse
            //Log.d("All String: ", json.toString());

            // Checking for SUCCESS TAG
            boolean error = json.getBoolean("error");

            if (!error) {
                // products found
                // Getting Array of attractions
                jsonArray = json.getJSONArray("attractions");
            }

            if(jsonArray!=null)
                // looping through All attractions
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = null;
                    c = jsonArray.getJSONObject(i);

                    Location location = new Location("Nose");
                    location.setLatitude(c.getDouble("latitude"));
                    location.setLongitude(c.getDouble("longitude"));

                    // Storing each json item in variable
                    mAttractionsList.add(new Attraction(
                            c.getString("name"),
                            c.getString("subtitle"),
                            c.getString("description"),
                            c.getString("address"),
                            location,
                            Attraction.Category.UNKNOWN,
                            //Attraction.Category.valueOf(c.getString("category")),
                            c.getString("interest"),
                            c.getDouble("rate"),
                            c.getInt("numRates"),
                            c.getString("openingTime"),
                            c.getString("nameImage")
                    ));
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dao.insertAttractions(mAttractionsList);

        return attractionArrayList;
    }

    private class TaskAttractions extends AsyncTask<Void, Void, Void> {

        JSONArray a;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params2) {
            // TODO Auto-generated method stub
            //a = createAllAttractions();
            String url_check_user = "http://s459655320.mialojamiento.es/index.php/attractions";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair(TAG_TID, Integer.toString(team
            //      .getTeamId())));

            // getting JSON Object
            // Note that check player url accepts POST method
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_check_user, "GET",
                    params);

            try {
                // Check your log cat for JSON reponse
                //Log.d("All String: ", json.toString());

                // Checking for SUCCESS TAG
                boolean error = json.getBoolean("error");

                if (!error) {
                    // products found
                    // Getting Array of attractions
                    a = json.getJSONArray("attractions");


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mAttractionsList = new ArrayList<Attraction>();

            if(a!=null)
                // looping through All attractions
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = null;
                    try {
                        c = a.getJSONObject(i);

                        Location location = new Location("Nose");
                        location.setLatitude(c.getDouble("latitude"));
                        location.setLongitude(c.getDouble("longitude"));

                        // Storing each json item in variable
                        mAttractionsList.add(new Attraction(
                                c.getString("name"),
                                c.getString("subtitle"),
                                c.getString("description"),
                                c.getString("address"),
                                location,
                                Attraction.Category.UNKNOWN,
                                //Attraction.Category.valueOf(c.getString("category")),
                                c.getString("interest"),
                                c.getDouble("rate"),
                                c.getInt("numRates"),
                                c.getString("openingTime"),
                                c.getString("nameImage")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            dao.insertAttractions(mAttractionsList);
            pDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
