package com.example.restaurant_community_android;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_community_android.adapters.PostAdapter;
import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PostAdapter();
        recyclerView.setAdapter(adapter);
        
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);
        
        loadPosts();
    }

    private void loadPosts() {
        String token = tokenManager.getToken();
        apiService.getPosts(token).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostListActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 