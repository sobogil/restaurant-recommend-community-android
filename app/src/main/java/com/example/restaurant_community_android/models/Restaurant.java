package com.example.restaurant_community_android.models;

import com.google.gson.annotations.SerializedName;

public class Restaurant {
    @SerializedName("title")
    private String title;
    
    @SerializedName("address")
    private String address;
    
    @SerializedName("roadAddress")
    private String roadAddress;
    
    @SerializedName("category")
    private String category;
    
    @SerializedName("telephone")
    private String telephone;
    
    @SerializedName("link")
    private String link;
    
    @SerializedName("description")
    private String description;

    // Getters and Setters
    public String getTitle() {
        return title.replaceAll("<[^>]*>", "");  // HTML 태그 제거
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 