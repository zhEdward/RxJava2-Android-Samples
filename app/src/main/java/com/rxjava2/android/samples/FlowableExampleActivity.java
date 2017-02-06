package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 * <p>
 * 支持 backpressure 操作的 flowable
 */
public class FlowableExampleActivity extends AppCompatActivity {

    private static final String TAG = FlowableExampleActivity.class.getSimpleName ();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_example);
        btn = (Button) findViewById (R.id.btn);
        textView = (TextView) findViewById (R.id.textView);

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                doSomeWork ();
                // doSomeWorkTest ();
            }
        });
    }

    boolean parallaxCompleted;

    private void doSomeWorkTest() {
        Log.i (TAG, "doSomeWorkTest: ");

        doParallax ().subscribe ();

        //在一个computation 线程类 顺序计算 1-10 每个 item 自乘操作
        //        Flowable.range(1, 1000*1000)
        //                .observeOn(Schedulers.computation())
        //                .map(v -> v * v)
        //                .takeWhile (v->parallaxCompleted)
        //                .doOnComplete (()-> Log.i (TAG, "map-op"+parallaxCompleted))
        //                .doOnCancel (()-> Log.i (TAG, "onCancel"))
        //                .blockingSubscribe ();

        //如果
        // doParallax ().publish (selector->Flowable.merge (selector,doOrder ().takeUntil (selector))).subscribe ();


        //只能发射一个 item
        //        Single.just (111)
        //                .map (n->{
        //                    "123".substring (10);//throw exception
        //                    return n;
        //                })
        //                .subscribe (integer -> Log.i (TAG, "Single: "),Throwable::printStackTrace);


    }


    public Flowable<Integer> doOrder() {
        return Flowable.range (1, 100 * 100).observeOn (Schedulers.computation ()).map (v -> v * v).doOnNext (i -> Log.i (TAG, "doOnNext: " + i)).doOnComplete (() -> Log.i (TAG, "map-op: "));
    }


    public Flowable<Integer> doParallax() {
        //在 10 个computation 并行计算 item 自乘 之后 在合并到 源observable 中进行发射
        //在数据量大的时候  性能优势明显?
        // TODO: 2017/1/21  flatMap op产生的效果并不是提高性能? 实测效果 耗时比map还更多
        return Flowable.range (1, 100 * 100).flatMap (x -> Flowable.just (x).subscribeOn (Schedulers.computation ()).map (xx -> x * x)).doOnNext (i -> Log.i (TAG, "doOnNext(flatmap): " + i)).doOnComplete (() -> {
            parallaxCompleted = true;
            Log.i (TAG, "flatmap-op");
        });
    }


    /*
     * simple example using Flowable
     */
    private void doSomeWork() {
        //Flowable<T> ---> reduce( R,BiFunction<R,T,R>)

//        Flowable.just (1, 2, 3, 4,5,6,7,8).filter (num -> num.intValue () % 2 == 1)//只对筛选出奇数进行 reduce "累加器"函数变换
//                //                .reduce ("50", new BiFunction<String, Integer, String> () {//50 的数值类型作为 用过apply 方法变换后 的新类型的初始值
//                //            @Override
//                //            public String apply(String t1, Integer t2) {
//                //                //t1 发射的item已经累加过后的（新类型）结果 ： 当前发射的item
//                //                Log.i ("FlowableExampleActivity", "apply: " + t1 + "-str:" + t2);
//                //                return t1 + (t2 + "");
//                //            }
//                //        })
//                .reduce ("666", (sInt, s) -> sInt + (s + "")).subscribe (getSingleObserver ());


        getCustomFlowable ().subscribeOn (AndroidSchedulers.mainThread ()).subscribe (new Subscriber<Integer> () {
           Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                Log.i (TAG, "cus-onSubscribe: ");

                this.s =s;
                s.request (4);

            }

            @Override
            public void onNext(Integer integer) {
                Log.i (TAG, "cus-onNext: "+integer);
                //本次发射 回调结束 手动再请求2个 被观察者数据进行发射
                s.request (2);
            }

            @Override
            public void onError(Throwable t) {
                Log.i (TAG, "cus-onError: ");
            }

            @Override
            public void onComplete() {
                Log.i (TAG, "cus-onComplete: ");
            }
        });


    }

    public Flowable<Integer> getCustomFlowable() {
        Flowable<Integer> tmp = Flowable.create (new FlowableOnSubscribe<Integer> () {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                //自定义 observable 的发送策略
                e.onNext (1);
                e.onNext (2);
                e.onNext (3);
                Thread.sleep (1000);
                e.onNext (4);
                e.onNext (5);
                e.onNext (6);
                Thread.sleep (2000);
               // e.onError (new IllegalFormatFlagsException ("err flags"));
                e.onNext (7);
                e.onNext (8);
                Thread.sleep (2000);
                e.onNext (9);
                e.onComplete ();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn (Schedulers.io ());
        //BackpresureStrategy.BUFFER 设置 flowable 支持背压



        return tmp;
    }


    private SingleObserver<String> getSingleObserver() {

        return new SingleObserver<String> () {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onSuccess(String value) {
                textView.append (" onSuccess : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onSuccess : value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (" onError : " + e.getMessage ());
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onError : " + e.getMessage ());
            }
        };
    }
}