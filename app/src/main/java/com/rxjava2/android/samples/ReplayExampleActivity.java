package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class ReplayExampleActivity extends AppCompatActivity {

    private static final String TAG = ReplayExampleActivity.class.getSimpleName();
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

    /* Using replay operator, replay ensure that all observers see the same sequence
     * of emitted items, even if they subscribe after the Observable has begun emitting items
     */
    private void doSomeWork() {

        PublishSubject<Integer> source = PublishSubject.create();
        ConnectableObservable<Integer> connectableObservable = source.replay(3); // bufferSize = 3 to retain 3 values to replay
        connectableObservable.connect(); // connecting the connectableObservable

        connectableObservable.subscribe (getObserver ("First"));

        textView.setText ("");
        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.onNext(4);


        /*
         * it will emit 2, 3, 4 as (count = 3), retains the 3 values for replay
         */
        connectableObservable.subscribe (getObserver ("Second"));

        source.onNext (5);
        source.onComplete ();

        //当 源observables 发送完毕(mean onCompleted be call ） 后注册的observer 将被只能收到 relay buffer 长度的 数据
        connectableObservable.subscribe (getObserver ("Third"));

    }


    private Observer<Integer> getObserver(String tag) {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, tag + " onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(Integer value) {
                textView.append (tag + "  onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, tag + "  onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (tag + "  onError : " + e.getMessage ());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, tag + "  onError : " + e.getMessage ());
            }

            @Override
            public void onComplete() {
                textView.append (tag + " onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, tag + " onComplete");
            }
        };
    }

    @Deprecated
    private Observer<Integer> getSecondObserver() {
        //replay 的buffer=3 只缓存 当前最新的三个item data
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                textView.append(" Second onSubscribe : isDisposed :" + d.isDisposed());
                Log.d(TAG, " Second onSubscribe : " + d.isDisposed());
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onNext(Integer value) {
                textView.append(" Second onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " Second onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" Second onError : " + e.getMessage());
                Log.d(TAG, " Second onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" Second onComplete");
                Log.d(TAG, " Second onComplete");
            }
        };
    }


}