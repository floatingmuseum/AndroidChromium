package org.chromium.hat.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Floatingmuseum on 2017/3/6.
 */

public class NetRetrofit {
    private final NetService service;

    final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").serializeNulls().create();

    NetRetrofit() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
//                .addInterceptor(new HeaderIntercept())
//                .addInterceptor(new AuthInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                //baseUrl方法指定了请求地址的前半部分，即服务器地址
                .baseUrl(HttpUrl.getHeadBasePath())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(NetService.class);
    }

    public NetService getService() {
        return service;
    }
}
