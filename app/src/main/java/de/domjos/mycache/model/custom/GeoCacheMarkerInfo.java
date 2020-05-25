package de.domjos.mycache.model.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.Objects;

import de.domjos.mycache.R;
import de.domjos.mycache.services.caching.opencaching.Cache;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GeoCacheMarkerInfo extends InfoWindow {
    private Activity activity;
    private Cache cache;

    public GeoCacheMarkerInfo(MapView mapView, Activity activity, Cache cache) {
        super(R.layout.geocache_info, mapView);

        this.activity = activity;
        this.cache = cache;
    }

    @Override
    public void onOpen(Object object) {
        LayoutInflater l = (LayoutInflater) this.activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout myRoot = new LinearLayout(activity);
        View view = Objects.requireNonNull(l).inflate(R.layout.geocache_info, myRoot, false);
        ((TextView) view.findViewById(R.id.lblInfoName)).setText(this.cache.getName());
    }

    @Override
    public void onClose() {

    }
}
