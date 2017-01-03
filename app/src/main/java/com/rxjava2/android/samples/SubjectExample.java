package com.rxjava2.android.samples;
/**
 * Created by Edward on 2017/1/3 9:44.
 * 自动生成的模版
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.rxjava2.android.samples.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author Edward
 * @revised by
 * Ddddddddd
 * <p>
 * Ower PDD
 */
public class SubjectExample extends AppCompatActivity {

    final static public String TAG = "SubjectExample";
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.subject_activity_example);
        //enable debug mode
        ButterKnife.setDebug (BuildConfig.DEBUG);
        ButterKnife.bind (this);
    }

    /*
        [RelaySubject]  多个 observer在不同时间点进行订阅 都会收到源 完整发送的items
    ReplaySubject emits to any observer all of the items that were emitted
     * by the source Observable, regardless of when the observer subscribes.
     */
    @OnClick(R.id.btn)
    void relaySubClick() {
        Log.i (TAG, "btn onClick: ");

        textView.setText ("");
        ReplaySubject<Integer> source = ReplaySubject.create ();

        source.subscribe (getObserver ("First")); // it will get 1, 2, 3, 4

        source.onNext (1);
        source.onNext (2);
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


    /* PublishSubject emits to an observer only those items that are emitted
     * by the source Observable, subsequent to the time of the subscription.
     */
    @OnClick(R.id.btn1)
    public void publishSubClick() {

        Log.i (TAG, "publishSubClick: ");

        textView.setText ("");

        PublishSubject<Integer> source = PublishSubject.create ();

        source.subscribe (getObserver ("First")); // it will get 1, 2, 3, 4 and onComplete

        source.onNext (1);
        source.onNext (2);
        source.onNext (3);

        /*
        PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
         * it will emit 4 and onComplete for second observer also.
         */
        source.subscribe (getObserver ("Second"));

        source.onNext (4);
        source.onComplete ();

    }


    /*
     [BehaivorSubject ] 当被订阅时 将接受到最近一次的item下 和 接下来所有从该时间点发送出来的item数据
     When an observer subscribes to a BehaviorSubject, it begins by emitting the item most
     * recently emitted by the source Observable (or a seed/default value if none has yet been
     * emitted) and then continues to emit any other items emitted later by the source Observable(s).
     */
    @OnClick(R.id.btn2)
    protected void behaivorSubClick() {
        textView.setText ("");
        BehaviorSubject<Integer> source = BehaviorSubject.create ();

        source.subscribe (getObserver ("First")); // it will get 1, 2, 3, 4 and onComplete

        source.onNext (1);
        source.onNext (2);
        source.onNext (3);

        /*
         * it will emit 3(last emitted), 4 and onComplete for second observer also.
         * 当观察者订阅BehaviorSubject时，它开始发射原始Observable最近发射的数据（ 如果此时还
         * 没有收到任何数据，它会发射一个默认值） ，然后继续发射其它任何来自原始Observable的
         * 数据
         *
         */
        source.subscribe (getObserver ("Second"));

        source.onNext (4);
        source.onComplete ();
    }

 /* An AsyncSubject emits the last value (and only the last value) emitted by the source
     * Observable, and only after that source Observable completes. (If the source Observable
     * does not emit any values, the AsyncSubject also completes without emitting any values.)
     */

    @OnClick(R.id.btn3)
        //注解的方法不能是 private
    void asyncSubClick() {
        textView.setText ("");

        AsyncSubject<Integer> source = AsyncSubject.create ();

        source.subscribe (getObserver ("First")); // it will emit only 4 and onComplete

        source.onNext (1);
        source.onNext (2);
        source.onNext (3);

        /*
         * it will emit 4 and onComplete for second observer also.
         *
         * 一个AsyncSubject只在原始Observable完成(onCompleted)后，发射来自原始Observable的最后一个值。
（ 如果原始Observable没有发射任何值，AsyncObject也不发射任何值） 它会把这最后一个值
发射给任何后续的观察者
         */
        source.subscribe (getObserver ("Second"));
        source.onNext (4);
        source.onComplete ();
    }


    private Observer<Integer> getObserver(String index) {
        return new Observer<Integer> () {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, index + "  onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(Integer value) {
                textView.append (index + " onNext : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "  onNext value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append (index + " onError : " + e.getMessage ());
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "onError : " + e.getMessage ());
            }

            @Override
            public void onComplete() {
                textView.append (index + " onComplete");
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, index + "onComplete");
            }
        };
    }

}
