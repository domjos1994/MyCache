package de.domjos.mycache.model.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;
import java.util.List;

import de.domjos.mycache.activities.MainActivity;
import de.domjos.mycache.settings.Settings;

public class LoginViewModel extends ViewModel {
    private static final String geoCaching = "geocaching.com";
    private static final String openCaching = "opencaching.com";

    private MutableLiveData<String> userName;
    private MutableLiveData<String> password;
    private MutableLiveData<String> address;
    private MutableLiveData<GeoPoint> geoPoint;
    private MutableLiveData<List<String>> services;
    private MutableLiveData<String> service;

    public LoginViewModel() {
        this.userName = new MutableLiveData<>();
        this.password = new MutableLiveData<>();
        this.address = new MutableLiveData<>();
        this.geoPoint = new MutableLiveData<>();
        this.services = new MutableLiveData<>(new LinkedList<>());
        this.service = new MutableLiveData<>();
        if(this.services.getValue() != null) {
            this.services.getValue().add(LoginViewModel.geoCaching);
            this.services.getValue().add(LoginViewModel.openCaching);
        }
    }

    public MutableLiveData<String> getUserName() {
        return this.userName;
    }

    public MutableLiveData<String> getPassword() {
        return this.password;
    }

    public MutableLiveData<String> getAddress() {
        return this.address;
    }

    public MutableLiveData<List<String>> getServices() {
        return this.services;
    }

    public MutableLiveData<String> getService() {
        return this.service;
    }

    public MutableLiveData<GeoPoint> getGeoPoint() {
        return this.geoPoint;
    }

    public void getSettings() {
        this.userName.setValue(MainActivity.GLOBALS.getSettings().getSetting("userName", ""));
        this.password.setValue(MainActivity.GLOBALS.getSettings().getSetting("password", ""));
        this.address.setValue(MainActivity.GLOBALS.getSettings().getSetting("address", ""));
        this.service.setValue(MainActivity.GLOBALS.getSettings().getSetting("service", ""));
        double longitude = MainActivity.GLOBALS.getSettings().getSetting("longitude", 0.0);
        double latitude = MainActivity.GLOBALS.getSettings().getSetting("latitude", 0.0);
        this.geoPoint.setValue(new GeoPoint(latitude, longitude));
    }

    public void saveSettings() {
        Settings settings = MainActivity.GLOBALS.getSettings();
        settings.saveSetting("userName", this.userName.getValue());
        settings.saveSetting("password", this.password.getValue());
        settings.saveSetting("address", this.address.getValue());
        settings.saveSetting("service", this.service.getValue());
        if(this.geoPoint.getValue() != null) {
            settings.saveSetting("longitude", this.geoPoint.getValue().getLongitude());
            settings.saveSetting("latitude", this.geoPoint.getValue().getLatitude());
        }
    }
}
