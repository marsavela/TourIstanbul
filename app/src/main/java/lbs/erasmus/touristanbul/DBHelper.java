package lbs.erasmus.touristanbul;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import lbs.erasmus.touristanbul.domain.Attraction;

/**
 * Created by SergiuDaniel on 15/04/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DBHelper.class.getName();
    // DB Name
    private static final String DB_NAME = "kebap.db";

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

    // Attractions table create statement
    private static final String CREATE_ATTRACTIONS = "create table " + TAG_ATTRACTIONS + "("
            + TAG_AID + " integer primary key autoincrement, "
            + TAG_NAME + " text, "
            + TAG_ADDRESS + " text, "
            + TAG_LATITUDE + " integer, "
            + TAG_LONGITUDE + " integer, "
            + TAG_CATEGORY + " text, "
            + TAG_RATE + " real, "
            + TAG_NUM_RATES + " integer, "
            + TAG_OPENING_TIME + " text, "
            + TAG_INTEREST + " text, "
            + TAG_DESCRIPTION + " text, "
            + TAG_SUBTITLE + " text, "
            + TAG_NAME_IMAGE + " text);";

    /**
     * Constructor
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.v("asdasd","CREADA");
        db.execSQL(CREATE_ATTRACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TAG_ATTRACTIONS);

        // create new tables
        onCreate(db);
    }

    /**
     * Select all attractions.
     *
     * @return a list of
     */
    public ArrayList<Attraction> getAttractions() {
        ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
        SQLiteDatabase db = this.getReadableDatabase();

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
                        Attraction.Category.UNKNOWN,
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
        SQLiteDatabase db = this.getReadableDatabase();

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
                        Attraction.Category.UNKNOWN,
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
        SQLiteDatabase db = this.getWritableDatabase();

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
        //TODO arreglar esto. Que guarde el nombre.
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

    // --------------- other methods ----------------//

    /**
     * Deleting DB
     */
    public void clearDB() {

        SQLiteDatabase db = this.getReadableDatabase();
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TAG_ATTRACTIONS);

        // create new tables
        onCreate(db);
    }

    /**
     * closing database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
