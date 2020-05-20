package de.domjos.mycache.services.general;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import de.domjos.mycache.activities.MainActivity;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.QueryStringSigningStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        try {
            if(MainActivity.GLOBALS.getConsumer() != null) {
                MainActivity.GLOBALS.getConsumer().setSigningStrategy(new QueryStringSigningStrategy());
                HttpRequest httpRequest = MainActivity.GLOBALS.getConsumer().sign(request);
                return chain.proceed((Request) httpRequest.unwrap());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return chain.proceed(request);
    }
}
