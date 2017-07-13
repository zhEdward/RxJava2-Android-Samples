package com.rxjava2.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjava2.android.samples.model.GithubBean;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.model.UserOrganationBean;
import com.rxjava2.android.samples.utils.AppConstant;
import com.rxjava2.android.samples.utils.GitHubService;
import com.rxjava2.android.samples.utils.ZhidianService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.id.list;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class TimerExample extends AppCompatActivity {

    private static final String TAG = "TimerExample";
    Button btn;
    TextView textView;
    ListView lv;
    GitHubService service;
    ZhidianService zhidianService;
    Retrofit zhidianRetrofit;

    ArrayAdapter<GithubBean> arrayAdapter;//UserOrganationBean


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_example);
        ButterKnife.bind (this);
        btn = (Button) findViewById (R.id.btn);
        textView = (TextView) findViewById (R.id.textView);
        lv = (ListView) findViewById (R.id.lv);
        arrayAdapter = new ArrayAdapter<GithubBean> (TimerExample.this, android.R.layout
                .simple_list_item_1);
        lv.setAdapter (arrayAdapter);

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                doSomeWork ();
            }
        });


        //        Retrofit retrofit = new Retrofit.Builder()
        //                .baseUrl("https://www.jurengongchuang.com/")
        //                //.addConverterFactory(GsonConverterFactory.create())
        //                .build();

        Retrofit retrofit = new Retrofit.Builder ().baseUrl ("https://api.github.com/")
                //.addConverterFactory (GsonConverterFactory.create ())
                .build ();

        zhidianRetrofit = new Retrofit.Builder ().baseUrl ("https://https://www.jurengongchuang.com/Help/")
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();
        zhidianService =zhidianRetrofit.create (ZhidianService.class);

        service = retrofit.create (GitHubService.class);


        Toolbar myToolbar = (Toolbar) findViewById (R.id.my_toolbar);
        Log.i (TAG, "=====" + myToolbar);
        setSupportActionBar (myToolbar);

        //        ActionBar ab = getSupportActionBar ();
        //        ab.setHomeAsUpIndicator (R.mipmap.logo);
        //        ab.setHomeButtonEnabled (true);


    }

    @OnClick(R.id.retrofitTest)
    public void testRetrofit() {


        new Thread (new Runnable () {
            @Override
            public void run() {

                try {
//                    Call<List<UserOrganationBean>> list = service.listOrgs ("JakeWharton");
//                    retrofit2.Response<List<UserOrganationBean>> response=list.execute ();
//                    Log.i (TAG,"-----"+response);
      //              List<UserOrganationBean> l=list.execute ().body ();//会阻塞主线程

                    

                   User l =null;//=service.getProfile("zhEdward").execute().body();
                    String str =service.getProfileRaw("daimajia").execute().body().string();
                    Log.i(TAG, "run1: "+str);

                    runOnUiThread (new Runnable () {
                        @Override
                        public void run() {
                            if(l!=null){
                                arrayAdapter.clear ();
                               // arrayAdapter.addAll (l);
                                arrayAdapter.add(l);
                                arrayAdapter.notifyDataSetChanged ();
                            }else Log.i(TAG, "run: what the fuck");

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }).start ();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.miCompose:
                Log.i (TAG, "miCompose");
                break;
            case R.id.miProfile:
                Log.i (TAG, "miProfile");
                break;
        }
        return true;
    }

    /*
             * simple example using timer to do something after 2 second
             */
    private void doSomeWork() {
        getObservable ()
                // Run on a background thread
                .subscribeOn (Schedulers.io ())
                // Be notified on the main thread
                .observeOn (AndroidSchedulers.mainThread ()).subscribe (getObserver ());
    }

    private Observable<? extends Long> getObservable() {
        return Observable.timer (2, TimeUnit.SECONDS);
    }

    private Observer<Long> getObserver() {
        return new Observer<Long> () {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d (TAG, " onSubscribe : " + d.isDisposed ());
            }

            @Override
            public void onNext(Long value) {
                textView.append (" onNext : value : " + value);
                textView.append (AppConstant.LINE_SEPARATOR);
                Log.d (TAG, " onNext : value : " + value);
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