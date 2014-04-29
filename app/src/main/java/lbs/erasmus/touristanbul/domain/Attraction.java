package lbs.erasmus.touristanbul.domain;

/**
 * Created by sergiu on 24/03/14.
 */


import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

import lbs.erasmus.touristanbul.AssetProvider;

/**
 * Model object for attraction.
 */
public class Attraction implements Parcelable {

    private static final String APP_FOLDER = "touristanbul";
    private static final String IMG_FOLDER = "images";

    /**
     * Public enum to control the attraction category
     */
    public enum Category {
        AIRPORT("Airports"), HOSPITAL("Hospitals"), HOTEL("Hotels"), MALL("Malls"), MOSQUES("Mosques"),
        MUSEUMS("Museums"), RESTAURANTS("Restaurants"), WIFI("Wifi Spots"),MONUMENTS("Monuments"), UNKNOWN("Unknown");

        private String mType;

        Category(String type) {
           mType = type;
        }

        @Override
        public String toString() {
            return mType;
        }
    }

    /** Title of the attraction. */
    private String mTitle;

    /** Short description of the attraction. */
    private String mSubtitle;

    /** Description of the attraction. */
    private String mDescription;

    /** Address of the attraction. */
    private String mAddress;

    /** Description of the attraction. */
    private Location mLocation;

    /** Category of the attraction. */
    //private Category mCategory;
    private String mCategory;

    /** Category res/drawable id **/
    private int mDrawableId;

    /** Interest of the attraction. */
    private String mInterest;

    /** Rate of the attraction. */
    private double mRate;

    /** Number of rates of the attraction. */
    private int mNumRates;

    /** Opening times of the attraction. */
    private String mOpeningTimes;

    /** Content URI of the image for the attraction. */
    private Uri mImageUri;

    /**
     * Constructs a new {@link Attraction}.
     *
     * @param titleString is the title
     * @param subtitleString is the description
     * @param imageAssetFilePath is the file path from the application's assets folder for
     *                           the image associated with this attraction
     */
    public Attraction(String titleString, String subtitleString, Location location, String imageAssetFilePath) {
        mTitle = titleString;
        mSubtitle = subtitleString;
        //mCategory = Category.UNKNOWN;
        mLocation = location;
        mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" +
                imageAssetFilePath);
    }

    public Attraction(String titleString, String subtitleString, double latitude, double longitude, String imageAssetFilePath) {
        mTitle = titleString;
        mSubtitle = subtitleString;
        //mCategory = Category.UNKNOWN;
        mLocation = new Location(titleString);
        mLocation.setLatitude(latitude);
        mLocation.setLongitude(longitude);
        mImageUri = Uri.fromFile(new File(getImagePath() + imageAssetFilePath));
    }

    /**
     * Constructs a new {@link Attraction}.
     *
     * @param titleString is the title
     * @param subtitleString is the short description
     * @param descriptionString is the description
     * @param addressString is the address
     * @param location is the location
     * @param category is the category
     * @param interestString is the interest
     * @param ratesDouble is the interest
     * @param numRatesInt is the interest
     * @param openingString is the interest
     * @param imageAssetFilePath is the file path from the application's assets folder for
     *                           the image associated with this attraction
     */

    public Attraction(String titleString, String subtitleString, String descriptionString,
                      String addressString, Location location, String category,
                      String interestString, double ratesDouble, int numRatesInt, String openingString,
                      String imageAssetFilePath) {
        mTitle = titleString;
        mSubtitle = subtitleString;
        mDescription = descriptionString;
        mAddress = addressString;
        mLocation = location;
        mCategory = category;
        mInterest = interestString;
        mRate = ratesDouble;
        mNumRates = numRatesInt;
        mOpeningTimes = openingString;
        //mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" +
          //      imageAssetFilePath);
        mImageUri = Uri.fromFile(new File(getImagePath() + imageAssetFilePath));
    }

    /*private Attraction(Parcel parcel) {
        mTitle = parcel.readString();
        mSubtitle = parcel.readString();
        mCategory = Category.UNKNOWN;
        mLocation = Location.CREATOR.createFromParcel(parcel);
        mImageUri = Uri.parse(parcel.readString());
    }
    */

    //* New Attraction(Parcel parcel)

    private Attraction(Parcel parcel) {
        mTitle = parcel.readString();
        mSubtitle = parcel.readString();
	    mDescription = parcel.readString();
        mAddress = parcel.readString();
        mLocation = Location.CREATOR.createFromParcel(parcel);
	    mCategory = parcel.readString();
	    mInterest = parcel.readString();
        mRate = parcel.readDouble();
	    mNumRates = parcel.readInt();
        mOpeningTimes = parcel.readString();
        mImageUri = Uri.parse(parcel.readString());
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getCategoryName() {
        return mCategory.toString();
    }

    public String getInterest() {
        return mInterest;
    }

    public double getRate() {
        return mRate;
    }

    public int getNumRates() {
        return mNumRates;
    }

    public String getOpeningTimes() {
        return mOpeningTimes;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int id) {
        mDrawableId = id;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public void setmDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
    }

    public void setmInterest(String mInterest) {
        this.mInterest = mInterest;
    }

    public void setmRate(double mRate) {
        this.mRate = mRate;
    }

    public void setmNumRates(int mNumRates) {
        this.mNumRates = mNumRates;
    }

    public void setmOpeningTimes(String mOpeningTimes) {
        this.mOpeningTimes = mOpeningTimes;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public void setmImageUri(Uri mImageUri) {
        this.mImageUri = mImageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*@Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mSubtitle);
        mLocation.writeToParcel(parcel, i);
        parcel.writeString(mImageUri.toString());
    }*/

    @Override
    public String toString() {
        return mTitle;
    }


    //New writeToParcel according new constructor
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mSubtitle);
        parcel.writeString(mDescription);
        parcel.writeString(mAddress);
        mLocation.writeToParcel(parcel, i);
        parcel.writeString(mCategory);
        parcel.writeString(mInterest);
        parcel.writeDouble(mRate);
        parcel.writeInt(mNumRates);
        parcel.writeString(mOpeningTimes);
        parcel.writeString(mImageUri.toString());
    }

    public static final Creator<Attraction> CREATOR = new Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel parcel) {
            return new Attraction(parcel);
        }

        @Override
        public Attraction[] newArray(int i) {
            return new Attraction[i];
        }
    };

    public String getImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER
                + File.separator + IMG_FOLDER + File.separator;
    }
}
