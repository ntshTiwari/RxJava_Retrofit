package com.example.rxjava_retrofit.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.rxjava_retrofit.models.Product;
import com.example.rxjava_retrofit.retrofit.ApiRepository;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Product>> products = new MutableLiveData<ArrayList<Product>>();
    public LiveData getProducts = products;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel() {
        System.out.println("ViewModel init called no msg in constructor" );
        startFetchingProducts();
    }

    public MainViewModel(String msg) {
        System.out.println("ViewModel init called" + msg);
        Log.e("INIT:", "MainViewModel");
    }

    /// If we have to create the viewmodel with some params, then we create it like this
    static public final ViewModelInitializer<MainViewModel> initializer(String msg) {
       return new ViewModelInitializer<>(
                MainViewModel.class,
                creationExtras -> {
                    return new MainViewModel(msg);
                }
        );
    };

    private void startFetchingProducts() {
        Observable<ArrayList<Product>> observable = ApiRepository.getApiEndPoints().getProducts();

        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());

        DisposableObserver<ArrayList<Product>> newObserver = getProductObserver();

        observable.subscribeWith(newObserver);
        compositeDisposable.add(newObserver);
    }

    private DisposableObserver<ArrayList<Product>> getProductObserver() {
        return new DisposableObserver<ArrayList<Product>>() {
            @Override
            public void onNext(@NonNull ArrayList<Product> newProducts) {
                products.postValue(newProducts);
                Log.e("getProductObserver():", String.valueOf(newProducts.size()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("getProduct Error:", e.toString());
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
