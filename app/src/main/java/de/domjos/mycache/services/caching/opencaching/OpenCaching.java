package de.domjos.mycache.services.caching.opencaching;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenCaching {

    @GET("/okapi/services/users/by_username")
    Call<User> getUsers(@Query("fields") String fields, @Query("consumer_key") String key, @Query("username") String username);

    @GET("/okapi/services/caches/search/nearest?auth=1")
    Call<CacheList> getNearestCaches(@Query(value = "center", encoded = true) String center, @Query("consumer_key") String key);

    @GET("/okapi/services/caches/geocache?auth=1")
    Call<Cache> getGeoCache(@Query(value = "cache_code") String code, @Query("consumer_key") String key);
}
