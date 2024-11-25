package com.example.restaurant_community_android;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_community_android.adapters.CommentAdapter;
import com.example.restaurant_community_android.models.Comment;
import com.example.restaurant_community_android.models.Like;
import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Button deleteButton;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private EditText commentEditText;
    private Button submitCommentButton;
    private MaterialButton likeButton;
    private TextView likeCountTextView;
    private boolean isLiked = false;
    private int likeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        ratingBar = findViewById(R.id.ratingBar);
        deleteButton = findViewById(R.id.deleteButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);
        likeButton = findViewById(R.id.likeButton);
        likeCountTextView = findViewById(R.id.likeCountTextView);

        postId = getIntent().getStringExtra("postId");
        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);

        deleteButton.setOnClickListener(v -> showDeleteConfirmDialog());

        commentAdapter = new CommentAdapter();
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);
        
        submitCommentButton.setOnClickListener(v -> submitComment());
        
        loadPostDetail();
        loadComments();

        likeButton.setOnClickListener(v -> toggleLike());
    }

    private void loadPostDetail() {
        Log.d("PostDetailActivity", "loadPostDetail: " + postId);
        String token = tokenManager.getToken();
        
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

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete", (dialog, which) -> deletePost())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePost() {
        String token = tokenManager.getToken();
        apiService.deletePost(token, postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostDetailActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Activity 종료하고 목록으로 돌아가기
                } else {
                    Toast.makeText(PostDetailActivity.this, "Failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "Error deleting post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        apiService.getComments(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentAdapter.setComments(response.body());
                } else {
                    Toast.makeText(PostDetailActivity.this, "댓글을 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "댓글을 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitComment() {
        String content = commentEditText.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "댓글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = new Comment();
        comment.setContent(content);

        String token = tokenManager.getToken();
        apiService.addComment(token, postId, comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    commentEditText.setText("");
                    loadComments();  // 댓글 목록 새로고침
                    Toast.makeText(PostDetailActivity.this, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostDetailActivity.this, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLikes() {
        apiService.getLikes(postId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> result = response.body();
                    likeCount = ((Double) result.get("count")).intValue();
                    updateLikeUI();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, "좋아요 정보 로딩 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLike() {
        String token = tokenManager.getToken();
        if (isLiked) {
            apiService.removeLike(token, postId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        isLiked = false;
                        likeCount--;
                        updateLikeUI();
                        Toast.makeText(PostDetailActivity.this, "좋아요를 취소했습니다", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PostDetailActivity.this, "좋��요 취소 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("postId", postId);
            apiService.addLike(token, body).enqueue(new Callback<Like>() {
                @Override
                public void onResponse(Call<Like> call, Response<Like> response) {
                    if (response.isSuccessful()) {
                        isLiked = true;
                        likeCount++;
                        updateLikeUI();
                        Toast.makeText(PostDetailActivity.this, "좋아요!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Like> call, Throwable t) {
                    Toast.makeText(PostDetailActivity.this, "좋아요 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateLikeUI() {
        if (isLiked) {
            likeButton.setIcon(getDrawable(R.drawable.ic_favorite));
            likeButton.setIconTint(ColorStateList.valueOf(getColor(R.color.accent_color)));
        } else {
            likeButton.setIcon(getDrawable(R.drawable.ic_favorite_border));
            likeButton.setIconTint(ColorStateList.valueOf(getColor(R.color.accent_color)));
        }
        likeCountTextView.setText(String.valueOf(likeCount));
    }
} 