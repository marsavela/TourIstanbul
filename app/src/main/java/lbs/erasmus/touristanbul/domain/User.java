package lbs.erasmus.touristanbul.domain;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by patmonsi on 13/03/14.
 */
public class User implements Parcelable {

    private String mEmail;
    private String mName;
    private String mPhotoUrl;
    private String mPlusProfile;
    private Bitmap mPhoto;
    private Location mLocation;

    public User(String mEmail, String mName, String mPhotoUrl, Location mLocation) {
        super();
        this.mEmail = mEmail;
        this.mName = mName;
        this.mPhotoUrl = mPhotoUrl;
        this.mLocation = mLocation;
    }

    public User(String mEmail, String mName, String mPhoto, String mPlusProfile) {
        super();
        this.mEmail = mEmail;
        this.mName = mName;
        this.mPhotoUrl = mPhoto;
        this.mPlusProfile = mPlusProfile;
    }

    public User(String mEmail, String mName, String mPhoto, String mPlusProfile, Location mLocation) {
        super();
        this.mEmail = mEmail;
        this.mName = mName;
        this.mPhotoUrl = mPhoto;
        this.mPlusProfile = mPlusProfile;
        this.mLocation = mLocation;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public Bitmap getmPhoto() {
        return mPhoto;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public void setmPhoto(Bitmap mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmPlusProfile() {
        return mPlusProfile;
    }

    public void setmPlusProfile(String mPlusProfile) {
        this.mPlusProfile = mPlusProfile;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        mEmail = in.readString();
        mName = in.readString();
        mPhoto = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
        dest.writeString(mName);
        dest.writeParcelable(mPhoto, flags);
    }

    public int describeContents() {
        return 0;
    }
}
