package de.domjos.mycache.services.caching.opencaching;

import com.google.gson.annotations.SerializedName;

import de.domjos.mycache.services.caching.interfaces.IUser;

public class User implements IUser {

    @SerializedName("uuid")
    private String id;

    @SerializedName("username")
    private String userName;

    @SerializedName("home_location")
    private Object home;

    private byte[] profilePic;

    public User(String id, String userName, String home) {
        this.id = id;
        this.userName = userName;
        this.home = home;
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
        if(this.home != null) {
            if(this.home.toString().contains("|")) {
                return Double.parseDouble(this.home.toString().split("\\|")[1]);
            }
        }
        return 0;
    }

    @Override
    public void setHomeLongitude(double longitude) {
        // nothing to do here
    }

    @Override
    public double getHomeLatitude() {
        if(this.home != null) {
            if(this.home.toString().contains("|")) {
                return Double.parseDouble(this.home.toString().split("\\|")[0]);
            }
        }
        return 0;
    }

    @Override
    public void setHomeLatitude(double latitude) {
        // nothing to do here
    }

    public Object getHome() {
        return this.home;
    }
}
