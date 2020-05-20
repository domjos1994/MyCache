package de.domjos.mycache.services.caching.interfaces;

public interface IUser {

    String getId();
    void setId(String id);

    String getUserName();
    void setUserName(String userName);

    byte[] getProfilePic();
    void setProfilePic(byte[] profilePic);

    double getHomeLongitude();
    void setHomeLongitude(double longitude);

    double getHomeLatitude();
    void setHomeLatitude(double latitude);
}
