package com.rxjava2.android.samples.model;

import io.reactivex.Observable;

/**
 * Created by amitshekhar on 30/08/16.
 */
public class Car {

    final private String TAG = "Car";

    private String brand = "unknow";

    public void setBrand(String brand) {
        this.brand = brand;
    }

    //创建一个 车子品牌的 observables
    public Observable<String> brandDeferObservable() {
        //        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
        //            @Override
        //            public ObservableSource<? extends String> call() throws Exception {
        //                Log.i (TAG, "call: call()");
        //                return Observable.just(brand);
        //            }
        //        });

        return Observable.defer (() -> Observable.just (brand));
    }

}
