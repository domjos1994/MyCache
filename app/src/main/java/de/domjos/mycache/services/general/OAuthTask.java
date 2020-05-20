package de.domjos.mycache.services.general;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

import de.domjos.customwidgets.model.tasks.AbstractTask;
import de.domjos.customwidgets.utils.MessageHelper;
import de.domjos.mycache.R;
import de.domjos.mycache.activities.MainActivity;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class OAuthTask extends AbstractTask<Void, Void, OkHttpOAuthConsumer> {
    private WeakReference<WebView> wv;

    public OAuthTask(Activity activity, WebView webView) {
        super(activity, R.string.msg_load_data, R.string.msg_load_data, false, R.mipmap.ic_launcher_round);

        this.wv = new WeakReference<>(webView);
    }

    @Override
    protected OkHttpOAuthConsumer doInBackground(Void... voids) {
        try {
            OkHttpOAuthConsumer oAuthConsumer = new OkHttpOAuthConsumer(MainActivity.GLOBALS.getOcPublicKey(), MainActivity.GLOBALS.getOcSecretKey());
            OAuthProvider provider = new DefaultOAuthProvider(
                "https://www.opencaching.de/okapi/services/oauth/request_token",
                "https://www.opencaching.de/okapi/services/oauth/access_token",
                "https://www.opencaching.de/okapi/services/oauth/authorize"
            );
            String url = provider.retrieveRequestToken(oAuthConsumer, "https://domjos.de");
            String verifier = this.getVerifier(url);
            provider.retrieveAccessToken(oAuthConsumer, verifier);
            return oAuthConsumer;
        } catch (Exception ex) {
            super.printException(ex);
        }
        return null;
    }

    private String getVerifier(String url) {
        final String[] verifier = new String[]{""};
        ((Activity) super.getContext()).runOnUiThread(() -> {
            wv.get().setVisibility(View.VISIBLE);
            this.wv.get().setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    try {
                        if(url.contains("oauth_verifier")) {
                            wv.get().setVisibility(View.GONE);
                            Uri uri = Uri.parse(url);
                            verifier[0] = uri.getQueryParameter("oauth_verifier");


                            return true;
                        }
                    } catch (Exception ex) {
                        MessageHelper.printException(ex, R.mipmap.ic_launcher_round, getContext());
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            this.wv.get().loadUrl(url);
        });
        while(verifier[0].equals("")) {}
        return verifier[0];
    }
}
