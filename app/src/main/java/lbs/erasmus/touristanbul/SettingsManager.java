package lbs.erasmus.touristanbul;

import android.content.Context;
import android.content.SharedPreferences;

import lbs.erasmus.touristanbul.domain.Attraction;

/**
 * Created by nacho on 12/04/14.
 */
public class SettingsManager {

    public final static String PREFERENCES_FILE = "lbs.erasmus.touristanbul_preferences";

    private final static String DISTANCE_UNITS = "distance_units";
    private final static String TEMPERATURE_SCALE = "temperature_scale";
    private final static String INTERESTS_PARENTS = "interest1";
    private final static String INTERESTS_BACKPACKERS = "interest2";
    private final static String INTERESTS_BUSINESS = "interest3";
    private final static String ATTRACTIONS_AIRPORTS = "interest4";
    private final static String ATTRACTIONS_HOSPITALS = "interest5";
    private final static String ATTRACTIONS_HOTELS = "interest6";
    private final static String ATTRACTIONS_MALLS = "interest7";
    private final static String ATTRACTIONS_MOSQUES = "interest8";
    private final static String ATTRACTIONS_MUSEUMS = "interest9";
    private final static String ATTRACTIONS_RESTAURANTS = "interest10";
    private final static String ATTRACTIONS_WIFI = "interest11";
    private final static String LOCATION = "share_location";

    private SharedPreferences mPrefs;
    private Context mContext;


    public SettingsManager(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public int getDistanceUnits() {
       return Integer.parseInt(mPrefs.getString(DISTANCE_UNITS, "0"));
    }

    public int getTemperatureScale() {
        return Integer.parseInt(mPrefs.getString(TEMPERATURE_SCALE, "1"));
    }

    public boolean getParentsInterest() {
        return mPrefs.getBoolean(INTERESTS_PARENTS, true);
    }

    public boolean getBackpackersInterest() {
        return mPrefs.getBoolean(INTERESTS_BACKPACKERS, true);
    }

    public boolean getBusinessInterest() {
        return mPrefs.getBoolean(INTERESTS_BUSINESS, true);
    }

    public boolean getAirportAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_AIRPORTS, true);
    }

    public boolean getHospitalAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_HOSPITALS, true);
    }

    public boolean getHotelsAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_HOTELS, true);
    }

    public boolean getMallsAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_MALLS, true);
    }

    public boolean getMosquesAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_MOSQUES, true);
    }

    public boolean getMuseumsAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_MUSEUMS, true);
    }

    public boolean getRestaurantsAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_RESTAURANTS, true);
    }

    public boolean getWifiAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_WIFI, true);
    }

    public boolean getShareLocation() {
        return mPrefs.getBoolean(LOCATION, true);
    }

    /**
     * Return true if the attraction category is enabled
     * @param attraction to check
     * @return boolean
     */
    public boolean checkAttractionCategory(Attraction attraction) {
        /*switch (attraction.getCategory()) {
            case AIRPORT:
                return getAirportAttractions();
            case HOSPITAL:
                return getHospitalAttractions();
            case HOTEL:
                return getHotelsAttractions();
            case MALL:
                return getMallsAttractions();
            case MOSQUES:
                return getMosquesAttractions();
            case MUSEUMS:
                return getMuseumsAttractions();
            case RESTAURANTS:
                return getRestaurantsAttractions();
            case WIFI:
                return getWifiAttractions();
            default:
                return true;
        }*/
        if (attraction.getCategory().equals("Airport"))
            return getAirportAttractions();
        else if (attraction.getCategory().equals("Hospital"))
            return getHospitalAttractions();
        else if (attraction.getCategory().equals("Hotel"))
            return getHotelsAttractions();
        else if (attraction.getCategory().equals("Mall"))
            return getMallsAttractions();
        else if (attraction.getCategory().equals("Mosques"))
            return getMosquesAttractions();
        else if (attraction.getCategory().equals("Museums"))
            return getMuseumsAttractions();
        else if (attraction.getCategory().equals("Restaurant"))
            return getRestaurantsAttractions();
        else if (attraction.getCategory().equals("Wifi"))
            return getWifiAttractions();
        else return false;
    }

    /**
     * Return true if the attraction category is enabled
     * @param attraction to check
     * @return boolean
     */
    public boolean checkAttractionInterest(Attraction attraction) {
        if (attraction.getInterest().equals("Parents"))
            return getParentsInterest();
        else if (attraction.getInterest().equals("Backpackers"))
            return getBackpackersInterest();
        else if (attraction.getInterest().equals("Business Travelers"))
            return getBusinessInterest();
        else return false;
    }
}
