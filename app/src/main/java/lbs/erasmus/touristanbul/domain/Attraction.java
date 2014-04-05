package lbs.erasmus.touristanbul.domain;

/**
 * Created by sergiu on 24/03/14.
 */


import android.location.Location;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import lbs.erasmus.touristanbul.AssetProvider;

/**
 * Model object for attraction.
 */
public class Attraction implements Parcelable {

    /** Title of the attraction. */
    private final String mTitle;

    /** Short description of the attraction. */
    private final String mSubtitle;

    /** Description of the attraction. */
    //private final Location mLocation;

    /** Content URI of the image for the attraction. */
    private final Uri mImageUri;

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
        //mLocation = location;
        mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" +
                imageAssetFilePath);
    }

    private Attraction(Parcel parcel) {
        mTitle = parcel.readString();
        mSubtitle = parcel.readString();
        //mLocation = Location.CREATOR.createFromParcel(parcel);
        mImageUri = Uri.parse(parcel.readString());
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mSubtitle);
        //mLocation.writeToParcel(parcel, i);
        parcel.writeString(mImageUri.toString());
    }

    public static final Creator<Attraction> CREATOR = new Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel parcel) {
            return new Attraction(parcel);
        }

        @Override
        public Attraction[] newArray(int i) {
            return new Attraction[0];
        }
    };

}
