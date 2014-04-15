package lbs.erasmus.touristanbul;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.domain.Attraction;

/**
 * Created by SergiuDaniel on 15/04/2014.
 */
public class DAOAttractions {

    private static final String APP_FOLDER = "touristanbul";
    private static final String IMG_FOLDER = "images";

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
    private static final String DB_NAME = "kebap.db";
    private final Context context;
    //private final AttractionsListener listener;
    private ArrayList<Attraction> mAttractionsList;
    private DBHelper dbHelper;

    public DAOAttractions(Context context) {
        this.context = context;
        generateDirectories();
        dbHelper = new DBHelper(context);
        mAttractionsList = new ArrayList<Attraction>();
    }

    private void generateDirectories() {
        File f = new File(getLocalFolder());
        if (!f.isDirectory()) {
            f = new File(getLocalFolder() + File.separator + ".nomedia");
            if (!f.mkdirs()) {
                Log.e("DAOAttractions", "Creating directories");
            }
            f = new File(getLocalFolder() + File.separator + IMG_FOLDER);
            if (!f.mkdirs()) {
                Log.e("DAOAttractions", "Creating images directory");
            }
        } else {
            f = new File(getLocalFolder() + File.separator + ".nomedia");
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    Log.e("DAOAttractions", "Creating .nomedia");
                }
            }

        }
    }

    public ArrayList<Attraction> retrieveData() {

        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
        JSONArray jsonArray = null;

        String url_attractions = "http://s459655320.mialojamiento.es/index.php/attractions";
        String url_images = "http://s459655320.mialojamiento.es/images/";

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair(TAG_TID, Integer.toString(team
        //      .getTeamId())));

        // getting JSON Object
        // Note that check player url accepts POST method
        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.makeHttpRequest(url_attractions, "GET",
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

            if (jsonArray != null)
                // looping through All attractions
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = null;
                    c = jsonArray.getJSONObject(i);

                    Location location = new Location("Nose");
                    location.setLatitude(c.getDouble("latitude"));
                    location.setLongitude(c.getDouble("longitude"));

                    // Storing each json item in variable
                    attractionArrayList.add(new Attraction(
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

                    //Saving file to SD
                    File f = new File(getLocalFolder() + File.separator +
                            c.getString("nameImage"));
                    if (!f.exists()) {
                        FileOutputStream fos = null;
                        URL url = new URL(url_images +
                                c.getString("nameImage"));
                        HttpURLConnection connection = (HttpURLConnection) url
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap image = BitmapFactory.decodeStream(input);

                        /*--- this method will save your downloaded image to SD card ---*/

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        /*--- you can select your preferred CompressFormat and quality.
     * I'm going to use JPEG and 100% quality ---*/
                        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    /*--- create a new file on SD card ---*/
                        File file = new File(getLocalImageFolder()
                                + File.separator + c.getString("nameImage"));

                        file.createNewFile();

    /*--- create a new FileOutputStream and write bytes to file ---*/
                        fos = new FileOutputStream(file);
                        if (fos != null) {
                            fos.write(bytes.toByteArray());
                            fos.close();
                        }

                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("getBmpFromUrl error: ", e.getMessage().toString());
        }

        dbHelper.insertAttractions(attractionArrayList);

        return attractionArrayList;
    }

    private String getLocalFolder() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER;
    }

    private String getLocalImageFolder() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER + File.separator + IMG_FOLDER;
    }
}
