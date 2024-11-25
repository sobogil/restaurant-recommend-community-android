package com.example.restaurant_community_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_community_android.adapters.RestaurantAdapter;
import com.example.restaurant_community_android.models.RestaurantResponse;
import com.example.restaurant_community_android.network.ApiService;
import com.example.restaurant_community_android.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("맛집 검색");
        }

        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);
        
        adapter = new RestaurantAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // 검색 버튼 클릭 리스너 수정
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchRestaurants(query);
                    // 키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        adapter.setOnRestaurantClickListener(restaurant -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getLink()));
            startActivity(intent);
        });

        // 초기 검색어로 검색 실행
        searchRestaurants("맛집");
    }

    private void searchRestaurants(String query) {
        apiService.searchRestaurants(query).enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setRestaurants(response.body().getItems());
                } else {
                    Toast.makeText(RestaurantSearchActivity.this, 
                        "검색 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Toast.makeText(RestaurantSearchActivity.this, 
                    "검색 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 