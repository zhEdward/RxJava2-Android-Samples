package com.rxjava2.android.samples;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class DisposableExampleActivity extends AppCompatActivity {

    private static final String TAG = DisposableExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;
    private final CompositeDisposable disposables = new CompositeDisposable();

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


        //use with flowerbale
        Subscriber subscriber = new Subscriber () {

            Subscription sub;

            @Override
            public void onSubscribe(Subscription s) {
                sub = s;
                s.request (1);
            }

            @Override
            public void onNext(Object o) {
                //do some job and pull request new event
                sub.request (1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        //use with observables
        Observer observer = new Observer () {
            @Override
            public void onSubscribe(Disposable d) {
                d.isDisposed ();
                d.dispose ();
            }

            @Override
            public void onNext(Object value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // do not send event after activity has been destroyed
    }

    /*
     * Example to understand how to use disposables.
     * disposables is cleared in onDestroy of this activity.
     */
    void doSomeWork() {

        disposables.add (sampleObservable ()
                // Run on a background thread
                .subscribeOn (Schedulers.io ())
                // Be notified on the main thread
                .observeOn (AndroidSchedulers.mainThread ())
                //通过subscribeWith 操作符 把每个响应式对对象放入统一管理list中
                // TODO: 2016/12/30 把 disposeSubscribe 单独拆分 method 使用 lambda调用 的方式??? [this::getObserver]
                .subscribeWith (getObserver ()));


        //String s =String::valueOf;

        // Thread t =Thread::new;


    }


    public DisposableObserver<String> getObserver() {
        return new DisposableObserver<String> () {
            @Override
            public void onComplete() {
                textView.append (" onComplete");
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onComplete");
            }

            @Override
            public void onError(Throwable e) {
                textView.append (" onError : " + e.getMessage ());
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onError : " + e.getMessage ());
            }

            @Override
            public void onNext(String value) {
                textView.append (" onNext : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onNext value : " + value);
            }
        };
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(2000);
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }
}

