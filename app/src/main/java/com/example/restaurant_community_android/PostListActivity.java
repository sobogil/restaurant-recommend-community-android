package com.example.restaurant_community_android;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_community_android.adapters.PostAdapter;
import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private ApiService apiService;
    private TokenManager tokenManager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 
            R.string.navigation_drawer_open, 
            R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_posts) {
                // 현재 화면이므로 아무것도 하지 않음
            } else if (id == R.id.nav_create_post) {
                startActivity(new Intent(this, CreatePostActivity.class));
            } else if (id == R.id.nav_logout) {
                tokenManager.clearToken();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (id == R.id.nav_restaurant_search) {
                startActivity(new Intent(this, RestaurantSearchActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PostAdapter();
        adapter.setOnPostClickListener(post -> {
            Intent intent = new Intent(PostListActivity.this, PostDetailActivity.class);
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);
        
        loadPosts();

        FloatingActionButton fab = findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(PostListActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();  // 화면이 다시 보일 때마다 게시글 목록 새로고침
    }

    private void loadPosts() {
        String token = tokenManager.getToken();
        apiService.getPosts(token).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    for (Post post : posts) {
                        Log.d("PostListActivity", "Post ID: " + post.getId() + ", Title: " + post.getTitle());
                    }
                    adapter.setPosts(posts);
                } else {
                    Log.e("PostListActivity", "Error: " + response.code());
                    Toast.makeText(PostListActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostListActivity", "Error: " + t.getMessage());
                Toast.makeText(PostListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
} 