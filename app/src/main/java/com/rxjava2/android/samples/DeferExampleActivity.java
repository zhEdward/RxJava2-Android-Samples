package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.model.Car;
import com.rxjava2.android.samples.utils.AppConstant;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 30/08/16.
 */
public class DeferExampleActivity extends AppCompatActivity {

    private static final String TAG = DeferExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //doSomeWork();

                deferDemo ();
            }
        });
    }

    /*
     * Defer used for Deferring Observable code until subscription in RxJava
     */
    private void doSomeWork() {

        Car car = new Car();

        Observable<String> brandDeferObservable = car.brandDeferObservable();

        car.setBrand("BMW");  // Even if we are setting the brand after creating Observable
                              // we will get the brand as BMW.
                              // If we had not used defer, we would have got null as the brand.

        //直到有观察者订阅时（或者每次订阅）才创建Observable，并且为每个（新的observer）观察者创建一个新的Observable
        brandDeferObservable.subscribe (getObserver (1));

        car.setBrand ("one 77");

        brandDeferObservable.subscribe (getObserver (2));//car brand has change to 'one 77'
    }

    private Observer<String> getObserver(int index) {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, index + "、 onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(String value) {
                textView.append (index + "？ onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " : value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (index + " onError : " + e.getMessage ());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append (index + "、 onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }



    private void deferDemo(){
        Observable.defer (new Callable<ObservableSource<? extends String>> () {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                Log.i (TAG, "call: call()");
                return Observable.just (getItem (1),getItem (2),getItem (3));
            }
        }).timeout(3, TimeUnit.SECONDS)//5s 检测有效网络
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (new Observer<String> () {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i (TAG,"onNext:"+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e (TAG,e.toString ());
                    }

                    @Override
                    public void onComplete() {
                        Log.i (TAG,"onComplete=====");
                    }
                });
    }

    private String getItem(int index){

       long delay= index==1? 2000:(index==2? 7000:100);
        try {
            Log.i (TAG,"getItem:"+index);
            Thread.sleep (delay);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }finally {

        }

        return "current no:"+index;
    }


}