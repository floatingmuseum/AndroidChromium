package org.chromium.hat.net;

import org.chromium.hat.entity.RegistrationStatus;
import org.chromium.hat.entity.WhiteListInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Floatingmuseum on 2017/3/6.
 */

public interface NetService {

    //获取浏览器白名单
    @GET
    Observable<WhiteListInfo> getBrowserWhiteList(@Url String url, @Query("mac") String mac);

//    //检查过期时间
//    @POST("CRegistrationCodeServletGetLastUseDate.svl")
//    Observable<ExpireTime> getExpireTime(@Query("mac") String mac);

    //检查设备是否注册
    @GET("CNoRegisterGetRegisterStatusServlet.svl")
    Observable<RegistrationStatus> checkRegistration(@Query("mac") String mac);
}
