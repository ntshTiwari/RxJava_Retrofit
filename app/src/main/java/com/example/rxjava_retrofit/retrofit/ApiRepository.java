package com.example.rxjava_retrofit.retrofit;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepository {
    private static String BASE_URL = "https://fakestoreapi.com/";

    private static Retrofit retrofitBuilder;

    private  static Retrofit getRetrofitBuilder() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        /// without RxJava Adapter
//        retrofitBuilder = new Retrofit.Builder()
//                .baseUrl(ApiRepository.BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
//                .build();

        /// this is the change we need to do to return Observable from our Retorfit methods
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ApiRepository.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        return retrofitBuilder;
    }

    public static ApiInterface getApiEndPoints() {
        return getRetrofitBuilder().create(ApiInterface.class);
    }

}
