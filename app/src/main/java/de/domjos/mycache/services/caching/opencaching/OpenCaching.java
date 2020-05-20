package de.domjos.mycache.services.caching.opencaching;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenCaching {

    @GET("/okapi/services/users/by_username")
    Call<User> getUsers(@Query("fields") String fields, @Query("consumer_key") String key, @Query("username") String username);
}
