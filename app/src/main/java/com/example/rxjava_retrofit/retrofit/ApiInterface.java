package com.example.rxjava_retrofit.retrofit;

import com.example.rxjava_retrofit.models.Product;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("products")
    Observable<ArrayList<Product>> getProducts();
}
