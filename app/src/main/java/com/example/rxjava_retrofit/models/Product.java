package com.example.rxjava_retrofit.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("price")
    public double price;
    @SerializedName("description")
    public String description;
    @SerializedName("category")
    public String category;
    @SerializedName("image")
    public String image;

    @SerializedName("rating")
    public Rating rating;

    public class Rating {
        @SerializedName("rate")
        public double rate;
        @SerializedName("count")
        public int count;
    }
}
