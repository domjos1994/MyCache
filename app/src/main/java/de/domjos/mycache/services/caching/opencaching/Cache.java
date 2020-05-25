package de.domjos.mycache.services.caching.opencaching;

import org.osmdroid.util.GeoPoint;

import de.domjos.mycache.services.caching.interfaces.ICache;

public class Cache implements ICache {
    private String code, name, location, type, status;

    public Cache(String code, String name, String location, String type, String status) {
        this.code = code;
        this.name = name;
        this.location = location;
        this.type = type;
        this.status = status;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public GeoPoint getGeoPoint() {
        if(this.location  != null) {
            double lat = Double.parseDouble(this.location.split("\\|")[0]);
            double lng = Double.parseDouble(this.location.split("\\|")[1]);

            return new GeoPoint(lat, lng);
        }
        return null;
    }
}
