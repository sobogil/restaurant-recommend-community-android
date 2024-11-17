package com.example.restaurant_community_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String TOKEN_KEY = "jwt_token";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply();
    }
}
