package de.domjos.geolib.services.caching.opencaching;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenCaching {

    @GET("/okapi/services/users/by_username?fields=uuid,username,home_location")
    Call<List<User>> getUsers(@Query("consumer_key") String key, @Query("usernames") String usernames);
}
