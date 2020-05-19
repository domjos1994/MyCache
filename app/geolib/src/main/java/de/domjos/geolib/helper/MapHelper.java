package de.domjos.geolib.helper;

import android.app.Activity;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK;

public class MapHelper {

    public static void configureMap(MapView mv, Activity activity) {
        Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity));

        mv.setTileSource(MAPNIK);

        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(activity), mv);
        myLocationNewOverlay.enableMyLocation();
        mv.getOverlays().add(myLocationNewOverlay);
    }

    public static GeoPoint setStartLocation(MapView mv, GeoPoint geoPoint, Activity activity) {
        IMapController mapController = mv.getController();
        mapController.setZoom(9.5);
        if(geoPoint != null) {
            mapController.setCenter(geoPoint);
            return geoPoint;
        } else {
            GeoHelper helper = new GeoHelper(activity);
            if(helper.canGetLocation()) {
                GeoPoint home = new GeoPoint(helper.getLocation().getLatitude(), helper.getLocation().getLongitude());
                mapController.setCenter(home);
                return home;
            }
        }
        return null;
    }
}
