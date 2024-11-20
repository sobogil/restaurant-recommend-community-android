package com.example.restaurant_community_android.network;

import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


import java.util.List;

public interface ApiService {
    @GET("users/{userId}")
    void getUser(@Path("userId") String userId, @Header("Authorization") String token);

    @POST("users/register")
    void createUser(@Body User user);

//    @POST("users/login")
//    void loginUser(@Body User user);

    @POST("users/login")
    Call<ResponseBody> loginUser(@Body User user);

    @GET("posts")
    Call<List<Post>> getPosts(@Header("Authorization") String token);

    @GET("posts/{postId}")
    Call<Post> getPost(@Path("postId") String postId);

    @POST("posts")
    Call<Post> createPost(@Header("Authorization") String token, @Body Post post);

    @PUT("posts/{postId}")
    Call<Post> updatePost(@Header("Authorization") String token, @Path("postId") String postId, @Body Post post);

    @DELETE("posts/{postId}")
    Call<Void> deletePost(@Header("Authorization") String token, @Path("postId") String postId);
}
