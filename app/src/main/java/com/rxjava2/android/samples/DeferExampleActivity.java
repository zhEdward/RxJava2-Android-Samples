package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.model.Car;
import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
                doSomeWork();
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


}