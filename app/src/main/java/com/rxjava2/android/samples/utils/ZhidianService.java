/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.rxjava2.android.samples.utils;

import com.rxjava2.android.samples.model.MessageCodeBean;
import com.rxjava2.android.samples.model.ZhiDianBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Edward on 2017/7/12 17:31.
 * 自动生成的模版
 */

public interface ZhidianService {
    /**
     * post 获取项目信息列表
     *
     * @param pi pageIndex
     * @param ps pageSize
     * @return
     */
    @POST("api/Project/listProj")
    Call<ResponseBody> listProj(@Query("pageIndex") int pi, @Query("pageSize") int ps);

    @POST("api/SMS/sendVertifyCode")
    Observable<ZhiDianBean> sendVertifyCode(@Query("mobile") String phone, @Query("messageType") int msgType);

    @POST("api/SMS/sendVertifyCode")
    Call<MessageCodeBean> sendCode(@Query("mobile") String phone, @Query("messageType") int msgType);

    /**
     * @param proId
     * @param title
     * @param img64
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("api/Project/SaveImg")
    Observable<ResponseBody> saveImg(@Field("projId") int proId, @Field("title") String title, @Field("img") String img64, @Field("token") String token);

    /**
     *
     * json type
     *
     * {
     * "token": "sample string 1",
     * "projId": 1,
     * "title": "sample string 2",
     * "img": [
     * "sample string 1",
     * "sample string 2"
     * ]
     * }
     *
     * @param bodyParam body中传递传输的类型 必须和 service api中一直
     * @return
     */
    @POST("api/Project/SaveImg")
    Observable<ResponseBody> saveImg(@Body Map<String, Object> bodyParam);


}
