package com.example.restaurant_community_android.network;

import com.example.restaurant_community_android.models.Comment;
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
import java.util.Map;

public interface ApiService {
    @GET("users/{userId}")
    void getUser(@Path("userId") String userId, @Header("Authorization") String token);



//    @POST("users/login")
//    void loginUser(@Body User user);

    @POST("users/login")
    Call<ResponseBody> loginUser(@Body User user);

    @GET("posts")
    Call<List<Post>> getPosts(@Header("Authorization") String token);

    @GET("posts/{id}")
    Call<Post> getPost(@Header("Authorization") String token, @Path("id") String postId);

    @POST("posts")
    Call<Post> createPost(@Header("Authorization") String token, @Body Post post);

    @PUT("posts/{postId}")
    Call<Post> updatePost(@Header("Authorization") String token, @Path("postId") String postId, @Body Post post);

    @DELETE("posts/{postId}")
    Call<Void> deletePost(@Header("Authorization") String token, @Path("postId") String postId);

    @POST("users/register")
    Call<ResponseBody> register(@Body User user);

    @POST("comments/{postId}")
    Call<Comment> addComment(@Header("Authorization") String token, @Path("postId") String postId, @Body Comment comment);

    @GET("comments/{postId}")
    Call<List<Comment>> getComments(@Path("postId") String postId);

    @DELETE("comments/{commentId}")
    Call<Void> deleteComment(@Header("Authorization") String token, @Path("commentId") String commentId);

    @GET("users/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @PUT("users/profile")
    Call<User> updateUserProfile(@Header("Authorization") String token, @Body Map<String, String> updateData);
}
