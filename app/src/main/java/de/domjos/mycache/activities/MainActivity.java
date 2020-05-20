package de.domjos.mycache.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.domjos.customwidgets.model.AbstractActivity;
import de.domjos.customwidgets.model.tasks.AbstractTask;
import de.domjos.customwidgets.utils.MessageHelper;
import de.domjos.mycache.R;
import de.domjos.mycache.model.viewModel.LoginViewModel;
import de.domjos.mycache.services.general.OAuthTask;
import de.domjos.mycache.settings.Globals;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class MainActivity extends AbstractActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public static final Globals GLOBALS = new Globals();

    private TextView lblTitle;
    private ImageView iv;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void initActions() {
        this.initPermissions();
    }

    @Override
    protected void initControls() {
        try {
            MainActivity.GLOBALS.setSettings(this.getApplicationContext());
            MainActivity.GLOBALS.setOcPublicKey(this.getString(R.string.open_caching_key));
            MainActivity.GLOBALS.setOcSecretKey(this.getString(R.string.open_caching_key_secret));
            this.initAccessToken(this.findViewById(R.id.wvMainAuth));
        } catch (Exception ex) {
            MessageHelper.printException(ex, R.mipmap.ic_launcher_round, this);
        }

        // init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        // init drawer-layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        this.lblTitle = navigationView.getHeaderView(0).findViewById(R.id.lblUserName);
        this.iv = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        this.lblTitle.setText(MainActivity.GLOBALS.getSettings().getSetting("userName",  ""));

        this.mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_login).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            this.lblTitle.setText(MainActivity.GLOBALS.getSettings().getSetting("userName",  ""));
            this.iv.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_perm_identity_black_24dp));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        this.initPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void initPermissions() {
        Activity act = MainActivity.this;
        try {
            int grant = PackageManager.PERMISSION_GRANTED;

            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            if(info.requestedPermissions != null) {

                List<String> permissions = new LinkedList<>();
                boolean grantState = true;
                for(String perm : info.requestedPermissions) {
                    permissions.add(perm);
                    if(ContextCompat.checkSelfPermission(act, perm) != grant) {
                        grantState = false;
                    }
                }
                if(!grantState) {
                    ActivityCompat.requestPermissions(act, permissions.toArray(new String[]{}), 99);
                }
            }
        } catch (Exception ex) {
            MessageHelper.printException(ex, R.mipmap.ic_launcher_round, act);
        }
    }

    private void initAccessToken(WebView webView) {
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getSettings();
        if(Objects.requireNonNull(loginViewModel.getService().getValue()).equals(Objects.requireNonNull(loginViewModel.getServices().getValue()).get(1))) {
            OAuthTask helper = new OAuthTask(this, webView);
            helper.after((AbstractTask.PostExecuteListener<OkHttpOAuthConsumer>) MainActivity.GLOBALS::setConsumer);
            helper.execute();
        }
    }
}
