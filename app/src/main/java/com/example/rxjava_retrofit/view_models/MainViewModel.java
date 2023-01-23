package com.example.rxjava_retrofit.view_models;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.rxjava_retrofit.models.Product;
import com.example.rxjava_retrofit.retrofit.ApiRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Product>> products = new MutableLiveData<ArrayList<Product>>();
    public LiveData getProducts = products;

//    private MutableLiveData<HashMap<String, Product>> searchedProducts = new MutableLiveData<HashMap<String, Product>>();
//    public LiveData getSearchedProducts = searchedProducts;

    private MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData getError = error;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel() {
        System.out.println("ViewModel init called no msg in constructor" );
        startFetchingProducts();
    }

    public MainViewModel(String searchQuery) {
        System.out.println("ViewModel init called" + searchQuery);
        Log.e("INIT:", "MainViewModel");
        startFetchingProducts(searchQuery);
    }

    /// If we have to create the viewmodel with some params, then we create it like this
    static public final ViewModelInitializer<MainViewModel> initializer(String searchQuery) {
       return new ViewModelInitializer<>(
                MainViewModel.class,
                creationExtras -> {
                    return new MainViewModel(searchQuery);
                }
        );
    };

    /// We use method overloading to create different versions of same function
    /// with and without some params, as Java doesn't have optional params
    /// more solutions for optional params: https://stackoverflow.com/questions/965690/how-do-i-use-optional-parameters-in-java
    private void startFetchingProducts() {
        startFetchingProducts(null);
    }

    private void startFetchingProducts(@Nullable String searchQuery) {
        Observable<ArrayList<Product>> observable = ApiRepository.getApiEndPoints().getProducts();
        DisposableObserver<ArrayList<Product>> newObserver = getProductObserver(searchQuery);

        observable.map(
                new Function<ArrayList<Product>, ArrayList<Product>>() {
                    @Override
                    public ArrayList<Product> apply(ArrayList<Product> newProducts) throws Exception {
                        if(searchQuery != null ){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                ArrayList<Product> filteredList = new ArrayList<>();
                                newProducts.forEach(
                                        (_product -> {
                                            if(_product.title.toLowerCase().contains(searchQuery)){
                                                filteredList.add(_product);
                                            }
                                        })
                                );
                                return filteredList;
                            }

                            /// return blank array if outdated android version
                            return new ArrayList<>();
                        } else {
                            return newProducts;
                        }
                    }
                }
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(newObserver);

        compositeDisposable.add(newObserver);

        ///// In order for map to work we need to write everything together

//        observable.subscribeOn(Schedulers.io());
//        observable.observeOn(AndroidSchedulers.mainThread());
//        observable.subscribeWith(newObserver);
//
//        if(searchQuery != null){
//            observable.map(
//                    new Function<ArrayList<Product>, ArrayList<Product>>() {
//                        @Override
//                        public ArrayList<Product> apply(ArrayList<Product> newProducts) throws Exception {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                ArrayList<Product> filteredList = new ArrayList<>();
//                                newProducts.forEach(
//                                        (_product -> {
//                                            if(_product.title.toLowerCase().contains(searchQuery)){
//                                                filteredList.add(_product);
//                                            }
//                                        })
//                                );
//
//                                System.out.println("filteredList length" + String.valueOf(filteredList.size()));
//
//                                return filteredList;
//                            }
//
//                            /// return blank array if outdated android version
//                            return new ArrayList<>();
//                        }
//                    }
//            );
//        }
//
//
    }

    private DisposableObserver<ArrayList<Product>> getProductObserver(String searchQuery) {
        return new DisposableObserver<ArrayList<Product>>() {
            @Override
            public void onNext(@NonNull ArrayList<Product> newProducts) {
//                ArrayList<Product> filteredList = new ArrayList<>();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && searchQuery != null) {
//                    newProducts.forEach(
//                            (_product -> {
//                                if(_product.title.toLowerCase().contains(searchQuery.toLowerCase())){
//                                    filteredList.add(_product);
//                                }
//                            })
//                    );
//                    products.postValue(filteredList);
//                    Log.e("getProductObserver():", String.valueOf(newProducts.size()));
//                } else {
                    products.postValue(newProducts);
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("getProduct Error:", e.toString());
                error.postValue(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
