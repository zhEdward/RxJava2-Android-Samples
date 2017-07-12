package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;
import com.rxjava2.android.samples.utils.Utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.observers.ResourceMaybeObserver;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import io.reactivex.subscribers.TestSubscriber;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class SingleObserverExampleActivity extends AppCompatActivity {

    private static final String TAG = SingleObserverExampleActivity.class.getSimpleName ();
    Button btn;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.single_activity_example);
        ButterKnife.bind (this);
        btn = (Button) findViewById (R.id.btn1);
        textView = (TextView) findViewById (R.id.textView);

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                // doSomeWork ();

                Observable.just (Utils.getApiUserList ()).subscribeOn (Schedulers.newThread ())
                        .observeOn (AndroidSchedulers.mainThread ()).subscribe (user -> Log.i
                        (TAG, "doSomeWork: " + user.size ()), err -> {
                    Log.e (TAG, "maybe onError: ", err);
                });
            }
        });
    }

    CompositeDisposable cd = new CompositeDisposable ();


    @Override
    protected void onDestroy() {
        super.onDestroy ();

        cd.clear ();
    }

    /*
         * simple example using SingleObserver
         */
    private void doSomeWork() {

        Flowable.range (1, 3).subscribe (new Subscriber<Integer> () {

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println ("OnSubscribe start");
                s.request (Long.MAX_VALUE);
                System.out.println ("OnSubscribe end");
            }

            @Override
            public void onNext(Integer v) {
                System.out.println (v);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace ();
            }

            @Override
            public void onComplete() {
                System.out.println ("Done");
            }
        });


        Single.just ("Amit").subscribeOn (Schedulers.trampoline ()).subscribe (getSingleObserver
                ());
        //subscribeWith 用于 替代 xx.subscribe() return void

        CompositeDisposable disposable = new CompositeDisposable ();
        disposable.add (Flowable.just ("Amit", "lala", "eva", "peter", "edward").subscribeWith
                (new ResourceSubscriber<String> () {
            /* * print order is:
             * onStart: 1
             * onNext: Amit
             * onNext: lala
             * onNext: eva
             * onNext: peter
             * onNext: edward
             * onComplete:
             * onStart: 2
             * */
            @Override
            protected void onStart() {
                Log.i (TAG, "onStart: 1");
                super.onStart ();/// request (Long.MAX_VALUE);//正数(不包括0)
                Log.i (TAG, "onStart: 2");

            }

            @Override
            public void onNext(String value) {
                Log.i (TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i (TAG, "onComplete: ");
            }
        }));

        //alse for ResourceCompletableObserver,ResourceSingleObserver,ResourceMaybeObserver
        disposable.add (Observable.range (1, 2).subscribeWith (new ResourceObserver<Integer> () {

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        }));


        Flowable.just (1, 2).subscribe (new Subscriber<Integer> () {
            @Override
            public void onSubscribe(Subscription s) {
                //通过回调方法进行 flow 注销
                s.cancel ();
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        Observable.just (1, 2).subscribe (new Observer<Integer> () {
            @Override
            public void onSubscribe(Disposable d) {
                //通过回调方法进行 flow 注销
                d.dispose ();
            }

            @Override
            public void onNext(Integer value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        ConnectableFlowable connectableFlowable = Flowable.fromArray (1, 2, 3, 4).publish ();
        connectableFlowable.subscribe (new Subscriber () {
            @Override
            public void onSubscribe(Subscription s) {
                s.cancel ();//取消订阅
                //或者使用 CompositeDisposable 统一管理订阅是
            }

            @Override
            public void onNext(Object o) {
                Log.i (TAG, "onNext: " + ((Integer) o).intValue ());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        cd.add (connectableFlowable.connect ());
    }

    @OnClick({R.id.btn2, R.id.btn3})
    void buttonClick(View v) {
        if (v.getId () == R.id.btn2) {
            List<Integer> list = Flowable.range (1, 100).map (integer -> {
                Thread.sleep (50);
                return integer;
            }).subscribeOn (Schedulers.newThread ()).doOnComplete (() -> Log.i (TAG, "blockingGet" +
                    " SUCCESS")).toList ().blockingGet (); // toList() returns Single


            //默认在  main thread 运行
            Integer i = Flowable.range (1, 50).blockingLast ();//return last item

            Log.i (TAG, "buttonClick: " + list.size () + "," + i.intValue ());
        } else {

            Flowable.just (1, 2, 3).doOnCancel (() -> System.out.println ("Cancelled11!")).take
                    (2)//will cancel
                    .subscribe (System.out::println);

            Flowable.just (1, 2, 3).doOnCancel (() -> System.out.println ("Cancelled22!"))
                    .subscribe (System.out::println);
        }


    }

    @OnClick(R.id.btn4)
    void btn4Click() {
        Flowable.range (1, 10).filter (integer -> (integer % 2 == 0)).test ().assertResult (2, 4,
                6, 8, 10);

        TestObserver to;
        TestSubscriber ts;

        //        Observable.intervalRange (1, 50, 1000, 500, TimeUnit.MICROSECONDS, Schedulers
        // .io ())
        //                .map (num -> String.valueOf (num)).subscribe (sNum -> Log.i (TAG,
        // "btn4Click: " + sNum));


        //prefetch 10
        Observable.range (1, 100).concatMap (integer -> Observable.just ("rank" + integer), 10)
                .subscribe (new Observer<String> () {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i (TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                Log.i (TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i (TAG, "onComplete: ");
            }
        });

    }

    private SingleObserver<String> getSingleObserver() {

        return new SingleObserver<String> () {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onSuccess(String value) {
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
        };
    }

    private Observer xObserves() {
        new BiConsumer<String, String> () {

            @Override
            public void accept(String s, String s2) throws Exception {

            }
        };

        return new Observer<String> () {

            @Override
            public void onSubscribe(Disposable d) {
                d.dispose ();
            }

            @Override
            public void onNext(String value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


    }


    private ResourceSubscriber<String> getResourceObserver() {

        Subscriber s;
        Observer o;
        SingleObserver so;

        ResourceObserver ro;
        ResourceMaybeObserver rm;
        ResourceSingleObserver rs;
        ResourceCompletableObserver rc;


        new ResourceObserver<String> () {

            @Override
            public void onNext(String value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        return new ResourceSubscriber<String> () {
            @Override
            public void onNext(String value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

}