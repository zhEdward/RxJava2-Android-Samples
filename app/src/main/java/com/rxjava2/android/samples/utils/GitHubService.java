package com.rxjava2.android.samples.utils;

import com.rxjava2.android.samples.model.UserOrganationBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Edward on 2017/7/12 17:03.
 * 自动生成的模版
 */

public interface GitHubService {
    @GET("users/{name}/orgs")
    public  Call<List<UserOrganationBean>> listOrgs(@Path("name") String name);


}
