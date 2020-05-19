package de.domjos.geolib.services.caching.opencaching;

import com.google.gson.annotations.SerializedName;

import de.domjos.geolib.services.caching.interfaces.IUser;

public class User implements IUser {

    @SerializedName("uuid")
    private String id;

    @SerializedName("username")
    private String userName;

    @SerializedName("home_location")
    private Object home;

    private double longitude;
    private double latitude;

    private byte[] profilePic;

    public User(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public byte[] getProfilePic() {
        return this.profilePic;
    }

    @Override
    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public double getHomeLongitude() {
        return this.longitude;
    }

    @Override
    public void setHomeLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getHomeLatitude() {
        return this.latitude;
    }

    @Override
    public void setHomeLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Object getHome() {
        return this.home;
    }
}
