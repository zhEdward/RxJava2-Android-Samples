package com.rxjava2.android.samples.utils;

import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.model.UserOrganationBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Edward on 2017/7/12 17:03.
 * 自动生成的模版
 */

public interface GitHubService {

    //获取某个用户 所加入的 组织
    @GET("users/{name}/orgs")
    public  Call<List<UserOrganationBean>> listOrgs(@Path("name") String name);

    //获取某个用户的个人信息 且直接映射为java对象
    @GET("users/{name}")
    public  Call<User> getProfile(@Path("name") String name);

    //在没有使用 第三方转换器，默认只能返回 responseBody！！！ 切记
    @GET("users/{name}")
    public  Call<ResponseBody> getProfileRaw(@Path("name") String name);


}
