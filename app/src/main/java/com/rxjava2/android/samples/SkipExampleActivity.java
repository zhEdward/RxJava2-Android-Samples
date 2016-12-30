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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by amitshekhar on 27/08/16.
 */
public class SkipExampleActivity extends AppCompatActivity {

    private static final String TAG = SkipExampleActivity.class.getSimpleName ();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.skip_activity_example);
        btn = (Button) findViewById (R.id.btn);
        textView = (TextView) findViewById (R.id.textView);
        //  ButterKnife.bind (this);

        //        btn.setOnClickListener (new View.OnClickListener () {
        //            @Override
        //            public void onClick(View view) {
        //                doSomeWork ();
        //            }
        //        });
    }

    CompositeDisposable disposables = new CompositeDisposable ();

    /* Using skip operator, it only not emit
    * the first 2 values.
    */
    //  @OnClick({R.id.btn, R.id.btn1, R.id.btn2})
    public void doSomeWork(View v) {
        //   Log.i (TAG, "doSomeWork: " + v.getId ());
        disposables.clear ();
        if (v.getId () == R.id.btn) {
            getObservable ()
                    // Run on a background thread
                    .subscribeOn (Schedulers.io ()).map (i -> {
                Thread.sleep (250);
                return i.intValue ();
            })
                    // Be notified on the main thread
                    .observeOn (AndroidSchedulers.mainThread ()).skipLast (2).subscribe (getDisposableObserver ());

        } else if (v.getId () == R.id.btn1) {
            // TODO: 2016/12/30  具体实现例子??
            Observable.interval (500, TimeUnit.MILLISECONDS).subscribeOn (Schedulers.io ())
                    //丢弃原始Observable发射的数据，直到第二个Observable发射了一个
                    //数据，然后发射原始Observable的剩余数据
                    .skipUntil (srcObser -> Observable.just (100).delay (500, TimeUnit.MILLISECONDS)).observeOn (AndroidSchedulers.mainThread ()).subscribe (new Observer<Long> () {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i (TAG, "onSubscribe: ");
                }

                @Override
                public void onNext(Long value) {
                    Log.i (TAG, "onNext: " + value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e (TAG, "onError: " + e.getMessage ());
                }

                @Override
                public void onComplete() {
                    Log.i (TAG, "onComplete: ");

                }
            });
            //getDisposableObserver ()
        } else if (v.getId () == R.id.btn2) {
            disposables.add (getObservable ()

                    .skipWhile (num -> num >= 1)//#只判断一次# 首个发送的item 当 num>=1 就丢弃 本次datas emit，否则按部就班的发射items
                    // TODO: 2016/12/30 subscribeWith api 只能接收匿名内部类，使用方法返回无法套用
                    .subscribeWith (new DisposableObserver<Integer> () {

                        //            @Override
                        //            public void onSubscribe(Disposable d) {
                        //                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
                        //            }

                        @Override
                        public void onNext(Integer value) {
                            textView.append (" onNext : value : " + value);
                            textView.append (AppConstant.LINE_SEPARATOR);
                            Log.d (TAG, " onNext value : " + value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            textView.append (" onError : " + e.getMessage ());
                            textView.append (AppConstant.LINE_SEPARATOR);
                            Log.d (TAG, " onError : " + e.getMessage ());
                        }

                        @Override
                        public void onComplete() {
                            textView.append (" onComplete");
                            textView.append (AppConstant.LINE_SEPARATOR);
                            Log.d (TAG, " onComplete");
                        }
                    }));
        }


    }


    private Observable<Integer> getObservable() {
        return Observable.just (1, 2, 3, 4, 5);
    }


    private Observer<Integer> getDisposableObserver() {
        return new DisposableObserver<Integer> () {
            @Override
            protected void onStart() {
                //clear display view content
                textView.setText ("");
            }

            //            @Override
            //            public void onSubscribe(Disposable d) {
            //                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
            //            }

            @Override
            public void onNext(Integer value) {
                textView.append (" onNext : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (" onError : " + e.getMessage ());
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onError : " + e.getMessage ());
            }

            @Override
            public void onComplete() {
                textView.append (" onComplete");
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onComplete");
            }
        };
    }


}