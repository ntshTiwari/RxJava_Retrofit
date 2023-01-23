package com.example.rxjava_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.rxjava_retrofit.adapters.ProductViewAdapter;
import com.example.rxjava_retrofit.models.Product;
import com.example.rxjava_retrofit.view_models.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleActionBarCode();

        /// handle if it is a search intent
        handleIntent(getIntent());

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getProducts.observe(this, getProductObserver());
        viewModel.getError.observe(this, getErrorObserver());

        /// code to create Viewmodel with some params
//        MainViewModel viewModel2 = new ViewModelProvider(this, ViewModelProvider.Factory.from(MainViewModel.initializer(
//                "viewModel2"
//        ))).get(MainViewModel.class);

    }

    private void handleActionBarCode() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void handleIntent(Intent intent){
        /// if this is a search intent
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.e("Search Intent", searchQuery);
        }
    }

    Observer<ArrayList<Product>> getProductObserver() {
        return new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                for(Product product: products){
                    System.out.println(product.id);
                }
                removeLoadingView();
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

    /// we remove the loader when items are loaded
    private void removeLoadingView() {
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.product_recycler_view).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
}