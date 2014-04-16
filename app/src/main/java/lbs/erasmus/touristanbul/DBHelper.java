package lbs.erasmus.touristanbul;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_ATTRACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TAG_ATTRACTIONS);

        // create new tables
        onCreate(db);
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
