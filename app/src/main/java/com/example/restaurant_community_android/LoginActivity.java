package com.example.restaurant_community_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurant_community_android.models.User;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.restaurant_community_android.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LoginActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("LoginActivity", "Email: " + email + ", Password: " + password);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // 사용자 객체 생성
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Log.d("LoginActivity", "User: " + user.toString());
        Call<ResponseBody> call = apiService.loginUser(user);
        Log.d("LoginActivity", "Call: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("LoginActivity", "Response: " + responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String token = jsonObject.getString("token");

                        // TokenManager를 사용하여 토큰 저장
                        TokenManager tokenManager = new TokenManager(LoginActivity.this);
                        tokenManager.saveToken(token);

                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // PostListActivity로 이동
                        Intent intent = new Intent(LoginActivity.this, PostListActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Check your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LoginActivity", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
