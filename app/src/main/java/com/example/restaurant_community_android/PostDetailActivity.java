package com.example.restaurant_community_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView contentTextView;
    private TextView restaurantNameTextView;
    private RatingBar ratingBar;
    private ApiService apiService;
    private TokenManager tokenManager;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        ratingBar = findViewById(R.id.ratingBar);

        postId = getIntent().getStringExtra("postId");
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);

        loadPostDetail();
    }

    private void loadPostDetail() {
        Log.d("PostDetailActivity", "loadPostDetail: " + postId);
        String token = "Bearer " + tokenManager.getToken();
        
        apiService.getPost(token, postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    Log.d("PostDetailActivity", "Post: " + post.toString());
                    titleTextView.setText(post.getTitle());
                    contentTextView.setText(post.getContent());
                    restaurantNameTextView.setText(post.getRestaurantName());
                    ratingBar.setRating(post.getRating());
                } else {
                    Log.e("PostDetailActivity", "Error: " + response.code());
                    Toast.makeText(PostDetailActivity.this, "Failed to load post detail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("PostDetailActivity", "Error: " + t.getMessage());
                Toast.makeText(PostDetailActivity.this, "Failed to load post detail", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 