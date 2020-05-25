package de.domjos.mycache.services.caching.interfaces;

import org.osmdroid.util.GeoPoint;

public interface ICache {
    void setCode(String code);
    String getCode();

    void setName(String name);
    String getName();

    void setLocation(String location);
    String getLocation();

    void setType(String type);
    String getType();

    void setStatus(String status);
    String getStatus();

    GeoPoint getGeoPoint();
}
