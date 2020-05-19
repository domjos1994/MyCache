package de.domjos.mycache.fragments;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;

import java.util.List;
import java.util.Objects;

import de.domjos.customwidgets.model.tasks.AbstractTask;
import de.domjos.customwidgets.utils.MessageHelper;
import de.domjos.geolib.helper.MapHelper;
import de.domjos.geolib.services.caching.RetrofitInstance;
import de.domjos.geolib.services.caching.opencaching.OpenCaching;
import de.domjos.geolib.services.caching.opencaching.User;
import de.domjos.mycache.R;
import de.domjos.mycache.model.custom.AbstractFragment;
import de.domjos.mycache.model.viewModel.LoginViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LoginFragment extends AbstractFragment<LoginViewModel> {
    private ImageButton cmdLoginHome, cmdLoginSave, cmdLoginAddress;
    private Button cmdLoginTest;

    private Spinner spLoginService;
    private ArrayAdapter<String> services;
    private EditText txtLoginUserName, txtLoginPassword, txtLoginAddress, txtLoginLongitude, txtLoginLatitude;
    private MapView mvLoginHome;
    private Activity activity;

    public LoginFragment() {
        super(R.layout.fragment_main_login);
    }

    @Override
    protected void initControls() {
        this.activity = this.requireActivity();

        this.cmdLoginHome = super.root.findViewById(R.id.cmdLoginPosition);
        this.cmdLoginAddress = super.root.findViewById(R.id.cmdLoginAddress);
        this.cmdLoginSave = super.root.findViewById(R.id.cmdLoginSave);
        this.cmdLoginAddress = super.root.findViewById(R.id.cmdLoginAddress);
        this.cmdLoginTest = super.root.findViewById(R.id.cmdLoginTest);

        this.spLoginService = super.root.findViewById(R.id.spLoginService);
        this.services = new ArrayAdapter<>(this.activity, android.R.layout.simple_spinner_item);
        this.spLoginService.setAdapter(this.services);
        this.services.notifyDataSetChanged();

        this.spLoginService.setAdapter(this.services);
        this.services.notifyDataSetChanged();

        this.txtLoginUserName = super.root.findViewById(R.id.txtLoginUserName);
        this.txtLoginPassword = super.root.findViewById(R.id.txtLoginPassword);
        this.txtLoginAddress = super.root.findViewById(R.id.txtLoginAddress);
        this.txtLoginLongitude = super.root.findViewById(R.id.txtLoginLongitude);
        this.txtLoginLatitude = super.root.findViewById(R.id.txtLoginLatitude);

        this.mvLoginHome = super.root.findViewById(R.id.mvLoginHome);
        MapHelper.configureMap(this.mvLoginHome, this.activity);
    }

    @Override
    protected void initActions() {
        this.cmdLoginHome.setOnClickListener(event -> super.model.getGeoPoint().setValue(MapHelper.setStartLocation(this.mvLoginHome, null, this.activity)));

        MapEventsReceiver receiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                LoginFragment.super.model.getGeoPoint().setValue(p);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                LoginFragment.super.model.getGeoPoint().setValue(p);
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(receiver);
        this.mvLoginHome.getOverlays().add(mapEventsOverlay);

        this.cmdLoginSave.setOnClickListener(event -> {
            super.model.getUserName().setValue(this.txtLoginUserName.getText().toString());
            super.model.getPassword().setValue(this.txtLoginPassword.getText().toString());
            super.model.getService().setValue(this.spLoginService.getSelectedItem().toString());
            super.model.getAddress().setValue(this.txtLoginAddress.getText().toString());
            super.model.getGeoPoint().setValue((GeoPoint) this.mvLoginHome.getMapCenter());
            super.model.saveSettings();

            MessageHelper.printMessage(this.getString(R.string.msg_save_success), R.mipmap.ic_launcher_round, this.activity);
        });

        this.cmdLoginAddress.setOnClickListener(event -> {
            AddressToLocation addressToLocation = new AddressToLocation(this.activity);
            addressToLocation.after((AbstractTask.PostExecuteListener<GeoPoint>) o -> this.setLocationToFields(o.getLongitude(), o.getLatitude()));
            addressToLocation.execute(this.txtLoginAddress.getText().toString());
        });

        this.cmdLoginTest.setOnClickListener(event -> {
            if(this.spLoginService.getSelectedItem().toString().equals(Objects.requireNonNull(super.model.getServices().getValue()).get(1))) {
                OpenCaching openCaching = RetrofitInstance.getRetrofitInstance(this.getString(R.string.open_caching_url)).create(OpenCaching.class);
                Call<List<User>> call = openCaching.getUsers(this.getString(R.string.open_caching_key), this.txtLoginUserName.getText().toString());
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse( Call<List<User>> call, Response<List<User>> response) {
                        MessageHelper.printMessage(Objects.requireNonNull(response.body()).toString(), R.mipmap.ic_launcher_round, LoginFragment.this.activity);
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<List<User>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void initViewModel() {
        super.model.getSettings();

        this.services.addAll(Objects.requireNonNull(super.model.getServices().getValue()));
        super.model.getUserName().observe(getViewLifecycleOwner(), s -> this.txtLoginUserName.setText(s));
        super.model.getPassword().observe(getViewLifecycleOwner(), s -> this.txtLoginPassword.setText(s));
        super.model.getService().observe(getViewLifecycleOwner(), s -> this.spLoginService.setSelection(this.services.getPosition(s)));
        super.model.getAddress().observe(getViewLifecycleOwner(), s -> this.txtLoginAddress.setText(s));
        super.model.getGeoPoint().observe(getViewLifecycleOwner(), s -> {
            if(s != null) {
                this.setLocationToFields(s.getLongitude(), s.getLatitude());
                MapHelper.setStartLocation(this.mvLoginHome, s, this.activity);
            }
        });

        GeoPoint geoPoint = MapHelper.setStartLocation(this.mvLoginHome, null, this.activity);
        if(geoPoint != null) {
            GeoPoint tmp = super.model.getGeoPoint().getValue();
            if(tmp != null) {
                if(Double.compare(tmp.getLongitude(), 0.0) == 0 && Double.compare(tmp.getLatitude(), 0.0) == 0) {
                    super.model.getGeoPoint().setValue(geoPoint);
                }
            } else {
                super.model.getGeoPoint().setValue(geoPoint);
            }
        } else {
            this.setLocationToFields(48.0, 10.0);
        }
    }

    private void setLocationToFields(double longitude, double latitude) {
        String strLongitude = Location.convert(longitude, Location.FORMAT_DEGREES);
        String strLatitude = Location.convert(latitude, Location.FORMAT_DEGREES);

        this.txtLoginLongitude.setText(strLongitude);
        this.txtLoginLatitude.setText(strLatitude);
        LocationToAddress locationToAddress = new LocationToAddress(this.activity);
        locationToAddress.after((AbstractTask.PostExecuteListener<String>) o -> this.txtLoginAddress.setText(o));
        locationToAddress.execute(latitude, longitude);

        this.mvLoginHome.getController().setCenter(new GeoPoint(latitude, longitude));
    }

    public static class LocationToAddress extends AbstractTask<Double, Void, String> {

        LocationToAddress(Activity activity) {
            super(activity, R.string.msg_load_data, R.string.msg_load_data, true, R.mipmap.ic_launcher_round);
        }

        @Override
        protected String doInBackground(Double... doubles) {
            try {
                String userAgent = Configuration.getInstance().getUserAgentValue();
                GeocoderNominatim geocoderNominatim = new GeocoderNominatim(userAgent);
                List<Address> addresses = geocoderNominatim.getFromLocation(doubles[0], doubles[1], 1);
                for(Address address : addresses) {
                    return String.format("%s, %s %s, %s", address.getThoroughfare(), address.getPostalCode(), address.getLocality(), address.getCountryName());
                }
            } catch (Exception ex) {
                super.printException(ex);
            }
            return null;
        }
    }

    public static class AddressToLocation extends AbstractTask<String, Void, GeoPoint> {

        AddressToLocation(Activity activity) {
            super(activity, R.string.msg_load_data, R.string.msg_load_data, true, R.mipmap.ic_launcher_round);
        }

        @Override
        protected GeoPoint doInBackground(String... addresses) {
            try {
                String userAgent = Configuration.getInstance().getUserAgentValue();
                GeocoderNominatim geocoderNominatim = new GeocoderNominatim(userAgent);
                List<Address> locations = geocoderNominatim.getFromLocationName(addresses[0], 1);
                for(Address address : locations) {
                    return new GeoPoint(address.getLatitude(), address.getLongitude());
                }
            } catch (Exception ex) {
                super.printException(ex);
            }
            return null;
        }
    }
}
