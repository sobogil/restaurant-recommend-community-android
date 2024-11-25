package com.example.restaurant_community_android.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RestaurantResponse {
    @SerializedName("items")
    private List<Restaurant> items;
    
    @SerializedName("total")
    private int total;
    
    @SerializedName("display")
    private int display;

    public List<Restaurant> getItems() {
        return items;
    }

    public void setItems(List<Restaurant> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
} 