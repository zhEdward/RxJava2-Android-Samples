package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 22/12/16.
 */

public class ThrottleLastExampleActivity extends AppCompatActivity {

    private static final String TAG = ThrottleLastExampleActivity.class.getSimpleName();
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
    * Using throttleLast() -> emit the most recent items emitted by an Observable within
    * periodic time intervals, so here it will emit 2, 6 and 7 as we have simulated it to be the
    * last the element in the interval of 500 millis
    */
    private void doSomeWork() {
        getObservable()
                //每500ms 一次 定时节流操作，并发送本次 节流中 最后一个数据
                .throttleLast(500, TimeUnit.MILLISECONDS)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Integer> getObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                textView.setText ("");

                // send events with simulated time wait
                //                Thread.sleep(0);
                //                emitter.onNext(1); // skip
                //                emitter.onNext(2); // deliver
                //                Thread.sleep(505);
                //                emitter.onNext(3); // skip
                //                Thread.sleep(99);
                //                emitter.onNext(4); // skip
                //                Thread.sleep(100);
                //                emitter.onNext(5); // skip
                //                emitter.onNext(6); // deliver
                //                Thread.sleep(305);
                //                emitter.onNext(7); // deliver
                //                Thread.sleep(510);


                emitter.onNext(1); // skip
                Thread.sleep (400);
                emitter.onNext (2);
                emitter.onNext (3); // deliver
                Thread.sleep(505);
                emitter.onNext(4); // skip
                Thread.sleep(100);
                emitter.onNext(5); // skip
                emitter.onNext (6); // skip
                emitter.onNext(7); // deliver
                Thread.sleep (605);
                emitter.onNext (8); // deliver
                Thread.sleep(510);


                emitter.onComplete();


            }
        });
    }

    private Observer<Integer> getObserver() {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                textView.append(" onNext : ");
                textView.append(AppConstant.LINE_SEPARATOR);
                textView.append(" value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onNext ");
                Log.d(TAG, " value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }

}