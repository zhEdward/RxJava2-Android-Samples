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
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by amitshekhar on 17/12/16.
 *
 * 定义一个 relayObservables  多个 observer在不同时间段进行订阅 都会收到完整的 发送items
 *
 */

public class ReplaySubjectExample extends AppCompatActivity {

    private static final String TAG = ReplaySubjectExample.class.getSimpleName();
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

    /* ReplaySubject emits to any observer all of the items that were emitted
     * by the source Observable, regardless of when the observer subscribes.
     */
    private void doSomeWork() {

        ReplaySubject<Integer> source = ReplaySubject.create();

        source.subscribe (getObserver ("First")); // it will get 1, 2, 3, 4

        source.onNext(1);
        source.onNext(2);



        /*
         * 当订阅后 立刻收到 之前已经发送过的1，2
         * 后续还会收到 3，4
         */
        source.subscribe (getObserver ("Second"));

        source.onNext (3);
        source.onNext (4);

        source.onComplete ();

        //再次订阅的observer 由于 onCompleted已经完成 所以也将一次性收到 1，2，3，4
        source.subscribe (getObserver ("Third"));




    }


    private Observer<Integer> getObserver(String index) {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, index + "  onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(Integer value) {
                textView.append (index + " onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "  onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (index + " onError : " + e.getMessage ());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "onError : " + e.getMessage ());
            }

            @Override
            public void onComplete() {
                textView.append (index + " onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "onComplete");
            }
        };
    }


}