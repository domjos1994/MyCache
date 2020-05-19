package de.domjos.geolib.services.caching.interfaces;

public interface IUser {

    long id();
    void setId(long id);

    String getUserName();
    void setUserName(String userName);

    byte[] getProfilePic();
    void setProfilePic(byte[] profilePic);

    double getHomeLongitude();
    void setHomeLongitude(double longitude);

    double getHomeLatitude();
    void setHomeLatitude(double latitude);
}
