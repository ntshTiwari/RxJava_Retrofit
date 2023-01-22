package com.example.rxjava_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.rxjava_retrofit.adapters.ProductViewAdapter;
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
        viewModel.getError.observe(this, getErrorObserver());

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
                setProductList(products);
            }
        };
    }

    Observer<String> getErrorObserver() {
        return new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setProductList(ArrayList<Product> product) {
        RecyclerView recyclerView = findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProductViewAdapter productViewAdapter = new ProductViewAdapter(product);

        recyclerView.setAdapter(productViewAdapter);
    }
}