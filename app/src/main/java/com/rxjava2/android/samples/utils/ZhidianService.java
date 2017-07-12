package com.rxjava2.android.samples.utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by Edward on 2017/7/12 17:31.
 * 自动生成的模版
 */

public interface ZhidianService {
    @POST("api/Project/listProj?pageIndex={pi}&pageSize={ps}")
    Call<String> listProj(@Field ("pageIndex") int pi, @Field ("pageSize")int ps);
}
