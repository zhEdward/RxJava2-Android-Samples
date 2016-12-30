package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by techteam on 13/09/16.
 */
public class DistinctExampleActivity extends AppCompatActivity {

    private static final String TAG = DistinctExampleActivity.class.getSimpleName ();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_example);
        btn = (Button) findViewById (R.id.btn);
        textView = (TextView) findViewById (R.id.textView);

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                doSomeWork ();
            }
        });
    }


    private void doSomeWork() {
        // TODO: 2016/12/30  对发射的observables 筛选 是否等于4 来去重 第一项 not equal 4 发射，第二项not equal 4 
        // TODO: 2016/12/30  （之前和 第一项重复 发射）到第七项 equal 4 （因为之前的项目 都是 not equal 所以 发射）
        // TODO: 2016/12/30  继续剩下的 item项 由于 去重条件 equal or not equal 都找到唯一项 被发射，后续的都不发射
        getObservable ().distinct (num -> num == 4).subscribe (getObserver ());
    }

    private Observable<Integer> getObservable() {
        return Observable.just (1, 2, 1, 1, 2, 3, 4, 6, 4);
    }


    private Observer<Integer> getObserver() {
        return new Observer<Integer> () {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(Integer value) {
                textView.append (" onNext : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d (TAG, " onError : " + e.getMessage ());
            }

            @Override
            public void onComplete() {
                Log.d (TAG, " onComplete");
            }
        };
    }
}
