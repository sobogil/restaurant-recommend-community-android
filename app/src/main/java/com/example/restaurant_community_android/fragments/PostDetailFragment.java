package com.example.restaurant_community_android.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.restaurant_community_android.R;
import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailFragment extends Fragment {
    private TextView titleTextView;
    private TextView contentTextView;
    private TextView restaurantNameTextView;
    private RatingBar ratingBar;
    private ApiService apiService;
    private String postId;

    public static PostDetailFragment newInstance(String postId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString("postId", postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        titleTextView = view.findViewById(R.id.titleTextView);
        contentTextView = view.findViewById(R.id.contentTextView);
        restaurantNameTextView = view.findViewById(R.id.restaurantNameTextView);
        ratingBar = view.findViewById(R.id.ratingBar);

        postId = getArguments().getString("postId");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        loadPostDetail();

        return view;
    }

    private void loadPostDetail() {
        apiService.getPost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    titleTextView.setText(post.getTitle());
                    contentTextView.setText(post.getContent());
                    restaurantNameTextView.setText(post.getRestaurantName());
                    ratingBar.setRating(post.getRating());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load post detail", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 