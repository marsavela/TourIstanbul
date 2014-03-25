package lbs.erasmus.touristanbul.domain;

/**
 * Created by sergiu on 24/03/14.
 */


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

    /** Description of the attraction. */
    private final String mSubtitle;

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
    public Attraction(String titleString, String subtitleString, String imageAssetFilePath) {
        mTitle = titleString;
        mSubtitle = subtitleString;
        mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" +
                imageAssetFilePath);
    }

    private Attraction(Parcel parcel) {
        mTitle = parcel.readString();
        mSubtitle = parcel.readString();
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
