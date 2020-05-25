package de.domjos.mycache.fragments;

import android.webkit.WebView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.List;
import java.util.Objects;

import de.domjos.customwidgets.model.tasks.AbstractTask;
import de.domjos.customwidgets.model.tasks.AbstractTask.PostExecuteListener;
import de.domjos.mycache.R;
import de.domjos.mycache.activities.MainActivity;
import de.domjos.mycache.helper.MapHelper;
import de.domjos.mycache.model.custom.AbstractFragment;
import de.domjos.mycache.model.viewModel.HomeViewModel;
import de.domjos.mycache.model.viewModel.LoginViewModel;
import de.domjos.mycache.services.Tasks.CacheTask;
import de.domjos.mycache.services.caching.opencaching.Cache;
import de.domjos.mycache.services.general.OAuthTask;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class HomeFragment extends AbstractFragment<HomeViewModel> {
    private MapView mvHomeMap;
    private LoginViewModel loginViewModel;

    public HomeFragment() {
        super(R.layout.fragment_main_home);
    }

    @Override
    protected void initControls() {
        this.mvHomeMap = super.root.findViewById(R.id.mvHomeMap);
    }

    @Override
    protected void initActions() {

    }

    @Override
    protected void initViewModel() {
        MapHelper.configureMap(this.mvHomeMap, super.activity);
        this.loginViewModel = new LoginViewModel();
        this.loginViewModel.getSettings();
        MapHelper.setStartLocation(this.mvHomeMap, this.loginViewModel.getGeoPoint().getValue(), super.activity);

        this.initWebService();
    }

    private void initAccessToken(WebView webView) {
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getSettings();
        if(Objects.requireNonNull(loginViewModel.getService().getValue()).equals(Objects.requireNonNull(loginViewModel.getServices().getValue()).get(1))) {
            OAuthTask helper = new OAuthTask(this.activity, webView);
            helper.after((AbstractTask.PostExecuteListener<OkHttpOAuthConsumer>) MainActivity.GLOBALS::setConsumer);
            helper.execute();
        }
    }

    private void initWebService() {
        this.initAccessToken(super.root.findViewById(R.id.wvMainAuth));
        if(Objects.requireNonNull(this.loginViewModel.getService().getValue()).contains(Objects.requireNonNull(this.loginViewModel.getServices().getValue()).get(1))) {
            if(this.loginViewModel.getGeoPoint().getValue() != null) {
                String lat = String.valueOf(this.loginViewModel.getGeoPoint().getValue().getLatitude());
                String lng = String.valueOf(this.loginViewModel.getGeoPoint().getValue().getLongitude());

                CacheTask cacheTask = new CacheTask(this.activity, R.mipmap.ic_launcher_round);
                cacheTask.after((PostExecuteListener<List<Cache>>) o -> {
                    for(Cache cache : o) {
                        if(cache != null) {
                            if(cache.getGeoPoint() != null) {
                                Marker marker = new Marker(mvHomeMap);
                                marker.setIcon(activity.getDrawable(R.mipmap.ic_launcher_round));
                                marker.setPosition(cache.getGeoPoint());
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
                                marker.setTitle(cache.getName());
                                marker.setSubDescription(cache.getCode() + "\n" + cache.getType() + "\n" + cache.getStatus());
                                marker.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mvHomeMap));
                                mvHomeMap.getOverlays().add(marker);
                            }
                        }
                    }
                });
                cacheTask.execute(String.format("%s|%s", lat, lng));
            }
        }
    }
}
