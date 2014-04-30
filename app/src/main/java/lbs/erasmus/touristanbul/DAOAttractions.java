package lbs.erasmus.touristanbul;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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
    private static final String LOG = "DAOAttractions";
    private final Context context;
    private DBHelper dbHelper;

    public DAOAttractions(Context context) {
        this.context = context;
        generateDirectories();
        dbHelper = new DBHelper(context);
    }

    /**
     * Select all attractions.
     *
     * @return a list of
     */
    public ArrayList<Attraction> getAttractions() {
        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TAG_ATTRACTIONS;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Location location = new Location("Nose");
                location.setLatitude(c.getDouble(c.getColumnIndex(TAG_LATITUDE)));
                location.setLongitude(c.getDouble(c.getColumnIndex(TAG_LONGITUDE)));

                Attraction attraction = new Attraction(
                        c.getString(c.getColumnIndex(TAG_NAME)),
                        c.getString(c.getColumnIndex(TAG_SUBTITLE)),
                        c.getString(c.getColumnIndex(TAG_DESCRIPTION)),
                        c.getString(c.getColumnIndex(TAG_ADDRESS)),
                        location,
                        c.getString(c.getColumnIndex(TAG_CATEGORY)),
                        //Attraction.Category.valueOf(c.getString("category")),
                        c.getString(c.getColumnIndex(TAG_INTEREST)),
                        c.getDouble(c.getColumnIndex(TAG_RATE)),
                        c.getInt(c.getColumnIndex(TAG_NUM_RATES)),
                        c.getString(c.getColumnIndex(TAG_OPENING_TIME)),
                        c.getString(c.getColumnIndex(TAG_NAME_IMAGE))
                );

                attractionArrayList.add(attraction);
            } while (c.moveToNext());
        }

        return attractionArrayList;
    }

    /**
     * Select all attractions of a category.
     *
     * @param category
     * @return a list of
     */
    public ArrayList<Attraction> getAttractionsByCategory(String category) {
        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TAG_ATTRACTIONS + " WHERE " + TAG_CATEGORY + " = "
                + category;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Location location = new Location("Nose");
                location.setLatitude(c.getDouble(c.getColumnIndex(TAG_LATITUDE)));
                location.setLongitude(c.getDouble(c.getColumnIndex(TAG_LONGITUDE)));

                Attraction attraction = new Attraction(
                        c.getString(c.getColumnIndex(TAG_NAME)),
                        c.getString(c.getColumnIndex(TAG_SUBTITLE)),
                        c.getString(c.getColumnIndex(TAG_DESCRIPTION)),
                        c.getString(c.getColumnIndex(TAG_ADDRESS)),
                        location,
                        c.getString(c.getColumnIndex(TAG_CATEGORY)),
                        //Attraction.Category.valueOf(c.getString("category")),
                        c.getString(c.getColumnIndex(TAG_INTEREST)),
                        c.getDouble(c.getColumnIndex(TAG_RATE)),
                        c.getInt(c.getColumnIndex(TAG_NUM_RATES)),
                        c.getString(c.getColumnIndex(TAG_OPENING_TIME)),
                        c.getString(c.getColumnIndex(TAG_NAME_IMAGE))
                );

                attractionArrayList.add(attraction);
            } while (c.moveToNext());
        }

        return attractionArrayList;
    }

    /**
     * Select all attractions of a name.
     *
     * @param name
     * @return a list of
     */
    public ArrayList<Attraction> getAttractionsByName(String name) {
        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TAG_ATTRACTIONS + " WHERE " + TAG_NAME + " like '%"
                + name + "%' OR " + TAG_DESCRIPTION + " like '%" + name + "%'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Location location = new Location("Nose");
                location.setLatitude(c.getDouble(c.getColumnIndex(TAG_LATITUDE)));
                location.setLongitude(c.getDouble(c.getColumnIndex(TAG_LONGITUDE)));

                Attraction attraction = new Attraction(
                        c.getString(c.getColumnIndex(TAG_NAME)),
                        c.getString(c.getColumnIndex(TAG_SUBTITLE)),
                        c.getString(c.getColumnIndex(TAG_DESCRIPTION)),
                        c.getString(c.getColumnIndex(TAG_ADDRESS)),
                        location,
                        c.getString(c.getColumnIndex(TAG_CATEGORY)),
                        //Attraction.Category.valueOf(c.getString("category")),
                        c.getString(c.getColumnIndex(TAG_INTEREST)),
                        c.getDouble(c.getColumnIndex(TAG_RATE)),
                        c.getInt(c.getColumnIndex(TAG_NUM_RATES)),
                        c.getString(c.getColumnIndex(TAG_OPENING_TIME)),
                        c.getString(c.getColumnIndex(TAG_NAME_IMAGE))
                );

                attractionArrayList.add(attraction);
            } while (c.moveToNext());
        }

        return attractionArrayList;
    }

    /**
     * Inser a given Act in the local DB.
     *
     * @param attraction one attraction
     * @return id of the inserted attraction
     */
    public long insertAttraction(Attraction attraction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(TAG_AID, attraction.getId()); This one is not necessary, as the
        // own DB increments the local ID.
        values.put(TAG_NAME, attraction.getTitle());
        values.put(TAG_SUBTITLE, attraction.getSubtitle());
        values.put(TAG_DESCRIPTION, attraction.getDescription());
        values.put(TAG_ADDRESS, attraction.getAddress());
        Location location = attraction.getLocation();
        values.put(TAG_LATITUDE, location.getLatitude());
        values.put(TAG_LONGITUDE, location.getLongitude());
        values.put(TAG_CATEGORY, attraction.getCategoryName());
        values.put(TAG_INTEREST, attraction.getInterest());
        values.put(TAG_RATE, attraction.getRate());
        values.put(TAG_NUM_RATES, attraction.getNumRates());
        values.put(TAG_OPENING_TIME, attraction.getOpeningTimes());
        values.put(TAG_NAME_IMAGE, attraction.getImageUri().getPathSegments().get(
                attraction.getImageUri().getPathSegments().size()-1));

        // insert row
        long attraction_id = db.insertWithOnConflict(TAG_ATTRACTIONS, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);

        // Return the id of the inserted Act
        return attraction_id;
    }

    /**
     * Insert a list of attractions in the local DB.
     *
     * @param attractionArrayList list of attractions
     * @return id of the inserted attraction
     */
    public void insertAttractions(ArrayList<Attraction> attractionArrayList) {
        for (Attraction attraction : attractionArrayList) {
            insertAttraction(attraction);
        }
    }

    public void updateRatesAttractions(String name, int vote) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int rates = 0;
        Log.v("VERBOSE", "entro en updateRatesAttractions con name  " + name + " y voto " + vote +" y eso");

        try {
            String selectQuery = "SELECT numRates FROM " + TAG_ATTRACTIONS + " WHERE name = '" + name + "'";
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                rates = c.getInt(0);
                Log.v("VERBOSE", "la atraccion tiene  " + rates + " votos ");
                rates++;
            }

            String selectQuery2 = "UPDATE " + TAG_ATTRACTIONS + " SET rate = " + vote + ", numRates = " + rates + " WHERE name = '" + name + "'";
            db.execSQL(selectQuery2);
            Log.v("VERBOSE", "update hecho");

            Log.v("VERBOSE", "UPDATE " + TAG_ATTRACTIONS + " SET rate = " + vote + ", numRates = " + rates + " WHERE name = '" + name + "'");

        }catch (SQLiteException e){
            Toast.makeText(context,"Your rate not saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(context,"Attraction not rated, you need internet connection", Toast.LENGTH_SHORT).show();

        }
    }

    public void downloadAndSaveData() {

        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();

            Log.v(LOG,"No Existe");
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
                                c.getString("category"),
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

            insertAttractions(attractionArrayList);
    }

    private void downloadImages() {

    }

    private void generateDirectories() {
        File f = new File(getLocalFolder());
        if (!f.isDirectory()) {
            f = new File(getLocalFolder() + File.separator + ".nomedia");
            if (!f.mkdirs()) {
                Log.e(LOG, "Creating directories");
            }
            f = new File(getLocalFolder() + File.separator + IMG_FOLDER);
            if (!f.mkdirs()) {
                Log.e(LOG, "Creating images directory");
            }
        } else {
            f = new File(getLocalFolder() + File.separator + ".nomedia");
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    Log.e(LOG, "Creating .nomedia");
                }
            }
            f = new File(getLocalFolder() + File.separator + IMG_FOLDER);
            if (!f.isDirectory()) {
                if (!f.mkdirs()) {
                    Log.e(LOG, "Creating images directory");
                }
            }
        }
    }

    /**
     * Check if the database exist
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(context.getApplicationInfo().dataDir + "/databases/" + DB_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null ? true : false;
    }

    private String getLocalFolder() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER;
    }

    private String getLocalImageFolder() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER + File.separator + IMG_FOLDER;
    }

    public void closeDB() {
        dbHelper.closeDB();
    }

    public void clearDB() {
        dbHelper.clearDB();
    }
}
