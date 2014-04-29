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
    private final static String INTERESTS_PARENTS = "Parents";
    private final static String INTERESTS_BACKPACKERS = "Backpackers";
    private final static String INTERESTS_BUSINESS = "Business Travelers";
    private final static String ATTRACTIONS_AIRPORTS = "Airports";
    private final static String ATTRACTIONS_HOSPITALS = "Hospitals";
    private final static String ATTRACTIONS_HOTELS = "Hotels";
    private final static String ATTRACTIONS_MALLS = "Malls";
    private final static String ATTRACTIONS_MOSQUES = "Mosques";
    private final static String ATTRACTIONS_MUSEUMS = "Museums";
    private final static String ATTRACTIONS_RESTAURANTS = "Restaurants";
    private final static String ATTRACTIONS_WIFI = "Wifi Spots";
    private final static String ATTRACTIONS_MONUMENTS = "Monuments";
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

    public boolean getMonumentsAttractions() {
        return mPrefs.getBoolean(ATTRACTIONS_MONUMENTS, true);
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
        if (attraction.getCategory().equals("Airports"))
            return getAirportAttractions();
        else if (attraction.getCategory().equals("Hospitals"))
            return getHospitalAttractions();
        else if (attraction.getCategory().equals("Hotels"))
            return getHotelsAttractions();
        else if (attraction.getCategory().equals("Malls"))
            return getMallsAttractions();
        else if (attraction.getCategory().equals("Mosques"))
            return getMosquesAttractions();
        else if (attraction.getCategory().equals("Museums"))
            return getMuseumsAttractions();
        else if (attraction.getCategory().equals("Restaurants"))
            return getRestaurantsAttractions();
        else if (attraction.getCategory().equals("Wifi Spots"))
            return getWifiAttractions();
        else if (attraction.getCategory().equals("Monuments"))
            return getMonumentsAttractions();
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
