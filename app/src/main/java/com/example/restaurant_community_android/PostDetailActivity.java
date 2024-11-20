package com.example.restaurant_community_android;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private ApiService apiService;
    private TokenManager tokenManager;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postId = getIntent().getStringExtra("post_id");
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);

        loadPostDetail();
    }

    private void loadPostDetail() {
        apiService.getPost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    updateUI(post);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Failed to load post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Post post) {
        TextView titleView = findViewById(R.id.titleTextView);
        TextView contentView = findViewById(R.id.contentTextView);
        TextView restaurantView = findViewById(R.id.restaurantTextView);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        titleView.setText(post.getTitle());
        contentView.setText(post.getContent());
        restaurantView.setText(post.getRestaurantName());
        ratingBar.setRating(post.getRating());
    }
} 