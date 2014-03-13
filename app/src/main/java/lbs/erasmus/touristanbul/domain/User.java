package lbs.erasmus.touristanbul.domain;

/**
 * Created by patmonsi on 13/03/14.
 */
public class User {

    private String mEmail;
    private String mName;
    private String mPhoto;
    private String mPlusProfile;


    public User(String mEmail, String mName, String mPhoto, String mPlusProfile) {
        super();
        this.mEmail = mEmail;
        this.mName = mName;
        this.mPhoto = mPhoto;
        this.mPlusProfile = mPlusProfile;
    }

    public User(String mName, String mPhoto, String mPlusProfile) {
        super();
        this.mEmail = mEmail;
        this.mName = mName;
        this.mPhoto = mPhoto;
        this.mPlusProfile = mPlusProfile;
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

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }
    /*    public String getProfileUrl() {
        return this.googlePublicProfilePhotoUrl.split("\\?sz=")[0] + "?sz=100";
    }*/

}
