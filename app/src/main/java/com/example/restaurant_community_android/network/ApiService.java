package com.example.restaurant_community_android.network;

import com.example.restaurant_community_android.models.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("user/{userId}")
    void getUser(@Path("userId") String userId, @Header("Authorization") String token);

    @POST("user")
    void createUser(@Body User user);

    @POST("user/login")
    void loginUser(@Body User user);

    @POST("user/logout")
    void logoutUser(@Header("Authorization") String token);
}
