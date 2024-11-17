package com.example.restaurant_community_android.network;

import com.example.restaurant_community_android.models.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("users/{userId}")
    void getUser(@Path("userId") String userId, @Header("Authorization") String token);

    @POST("users/register")
    void createUser(@Body User user);

    @POST("users/login")
    void loginUser(@Body User user);
}
