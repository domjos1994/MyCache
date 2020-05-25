package de.domjos.mycache.services.Tasks;

import android.app.Activity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.domjos.customwidgets.model.tasks.AbstractTask;
import de.domjos.customwidgets.utils.MessageHelper;
import de.domjos.mycache.R;
import de.domjos.mycache.activities.MainActivity;
import de.domjos.mycache.model.viewModel.LoginViewModel;
import de.domjos.mycache.services.caching.opencaching.Cache;
import de.domjos.mycache.services.caching.opencaching.CacheList;
import de.domjos.mycache.services.caching.opencaching.OpenCaching;
import de.domjos.mycache.services.general.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Response;

public class CacheTask extends AbstractTask<String, Integer, List<Cache>> {
    private OpenCaching openCaching;

    public CacheTask(Activity activity, int icon) {
        super(activity, R.string.task_cache_load, R.string.task_cache_load_content, true, icon);

        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.getSettings();
        String service = loginViewModel.getService().getValue();
        String locale = loginViewModel.getLocale().getValue();
        this.openCaching = RetrofitInstance.getRetrofitInstance("https://" + service + locale).create(OpenCaching.class);
    }

    @Override
    protected List<Cache> doInBackground(String... strings) {
        List<Cache> caches = new LinkedList<>();
        try {
            Call<CacheList> cacheListCall = this.openCaching.getNearestCaches(strings[0], MainActivity.GLOBALS.getOcPublicKey());
            int index = 1;
            for(String code : Objects.requireNonNull(cacheListCall.execute().body()).getResults()) {
                Call<Cache> cacheCall = this.openCaching.getGeoCache(code, MainActivity.GLOBALS.getOcPublicKey());
                Response<Cache> response = cacheCall.execute();
                if(response.isSuccessful()) {
                    caches.add(response.body());
                    publishProgress(index);
                    index++;
                }
            }
        } catch (IOException e) {
            super.printException(e);
        }
        return caches;
    }
}
