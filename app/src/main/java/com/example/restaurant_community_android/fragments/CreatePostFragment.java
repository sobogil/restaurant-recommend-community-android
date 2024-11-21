package com.example.restaurant_community_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.restaurant_community_android.R;
import com.example.restaurant_community_android.models.Post;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostFragment extends Fragment {
    private EditText titleEditText;
    private EditText contentEditText;
    private EditText restaurantNameEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        restaurantNameEditText = view.findViewById(R.id.restaurantNameEditText);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submitButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(requireContext());

        submitButton.setOnClickListener(v -> createPost());

        return view;
    }

    private void createPost() {
        Post post = new Post();
        post.setTitle(titleEditText.getText().toString());
        post.setContent(contentEditText.getText().toString());
        post.setRestaurantName(restaurantNameEditText.getText().toString());
        post.setRating(ratingBar.getRating());

        String token = tokenManager.getToken();
        apiService.createPost(token, post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create post", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 