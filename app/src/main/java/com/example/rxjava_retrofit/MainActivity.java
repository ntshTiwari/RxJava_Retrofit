package com.example.rxjava_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.rxjava_retrofit.models.Product;
import com.example.rxjava_retrofit.view_models.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getProducts.observe(this, getProductObserver());

        /// code to create Viewmodel with some params
//        MainViewModel viewModel2 = new ViewModelProvider(this, ViewModelProvider.Factory.from(MainViewModel.initializer(
//                "viewModel2"
//        ))).get(MainViewModel.class);

    }

    Observer<ArrayList<Product>> getProductObserver() {
        return new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                for(Product product: products){
                    System.out.println(product.id);
                }
            }
        };
    }
}