package com.example.restaurant_community_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurant_community_android.models.User;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;
import com.example.restaurant_community_android.utils.TokenManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private Button updateButton;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.usernameEditText);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        updateButton = findViewById(R.id.updateButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);

        loadUserProfile();
        updateButton.setOnClickListener(v -> updateProfile());
    }

    private void loadUserProfile() {
        String token = tokenManager.getToken();
        apiService.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    usernameEditText.setText(user.getUsername());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "프로필 로딩 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        String username = usernameEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        Map<String, String> updateData = new HashMap<>();
        if (!username.isEmpty()) {
            updateData.put("username", username);
        }
        if (!currentPassword.isEmpty()) {
            updateData.put("password", currentPassword);
        }
        if (!newPassword.isEmpty()) {
            updateData.put("newPassword", newPassword);
        }

        String token = tokenManager.getToken();
        apiService.updateUserProfile(token, updateData).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "프로필이 업데이트되었습니다", Toast.LENGTH_SHORT).show();
                    currentPasswordEditText.setText("");
                    newPasswordEditText.setText("");
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "프로필 업데이트 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "프로필 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 