package de.domjos.mycache.services.general;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(String url) {
        if(retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new OAuthInterceptor()).build();

            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        }
        return retrofit;
    }
}
