package org.chromium.hat.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import org.chromium.hat.WhiteListManager;
import org.chromium.hat.entity.RegistrationStatus;
import org.chromium.hat.entity.WhiteListInfo;
import org.chromium.hat.utils.MacUtil;
import org.chromium.hat.utils.RxUtil;
import org.chromium.hat.utils.SPUtil;
import org.chromium.hat.utils.UrlUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by Floatingmuseum on 2017/3/6.
 * <p>
 * 发起所有网络请求的类
 */

public class Repository {
    private static Repository repository;
    protected NetService service;

    public Repository() {
        service = RetrofitFactory.getInstance();
    }

    public static Repository getInstance() {
        if (repository == null) {
            synchronized (Repository.class) {
                if (repository == null) {
                    repository = new Repository();
                }
            }
        }
        return repository;
    }

    /**
     * 获取浏览器黑白名单
     */
    public void getBrowserWhiteList() {
        String mac = MacUtil.getMacAddress();
        Log.d("HAT测试", "获取浏览器黑白名单url:" + HttpUrl.getUrl(HttpUrl.URL_BROWSER_LIST) + "......ServerHost:" + SPUtil.getString("reftionserver", "www.floatingmuseum.com"));
        service.getBrowserWhiteList(HttpUrl.getUrl(HttpUrl.URL_BROWSER_LIST), mac)
                .map(new Function<WhiteListInfo, WhiteListInfo>() {
                    @Override
                    public WhiteListInfo apply(@NonNull WhiteListInfo whiteListInfo) throws Exception {
                        // 这里把域名处理一下，比如www.hexun.com要取得hexun.com的根域
                        whiteListInfo.setAbl(UrlUtil.getRootDomains(whiteListInfo.getAbl()));
                        whiteListInfo.setAwl(UrlUtil.getRootDomains(whiteListInfo.getAwl()));
                        whiteListInfo.setSbl(UrlUtil.getRootDomains(whiteListInfo.getSbl()));
                        whiteListInfo.setSwl(UrlUtil.getRootDomains(whiteListInfo.getSwl()));
                        return whiteListInfo;
                    }
                })
                .compose(RxUtil.<WhiteListInfo>threadSwitch())
                .subscribe(new Consumer<WhiteListInfo>() {
                    @Override
                    public void accept(@NonNull WhiteListInfo whiteListInfo) throws Exception {
                        Log.d("HAT测试", "获取浏览器黑白名单onNext:" + whiteListInfo);
//                        SyncManager.getInstance().updateSyncTime(SyncManager.SP_KEY_BROWSER_WHITE_LIST_LAST_TIME, System.currentTimeMillis());
                        WhiteListManager.getInstance().handleResult(whiteListInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        Log.d("HAT测试", "获取浏览器黑白名单onError");
                        e.printStackTrace();
                    }
                });
    }

//    /**
//     * 检查过期时间
//     */
//    public void getExpireTime() {
//        String mac = MacUtil.getMacAddress();
//        service.getExpireTime(mac)
//                .compose(RxUtil.<ExpireTime>threadSwitch())
//                .subscribe(new Consumer<ExpireTime>() {
//                    @Override
//                    public void accept(@NonNull ExpireTime expireTime) throws Exception {
//                        Logger.d("检查过期时间onNext:" + expireTime);
//                        if (expireTime != null) {
//                            Logger.d("检查过期时间onNext:" + expireTime.getLast_use_date());
//                            ProductExpireManager.getInstance().handleResult(expireTime);
//                            SyncManager.getInstance().updateSyncTime(SyncManager.SP_KEY_EXPIRE_TIME_LAST_TIME, System.currentTimeMillis());
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable e) throws Exception {
//                        Logger.d("检查过期时间onError");
//                        e.printStackTrace();
//                    }
//                });
//    }

    /**
     * 检查设备是否注册
     */
    public void checkRegistration() {
        String mac = MacUtil.getMacAddress();
        Log.d("HAT测试", "检查设备是否注册mac:" + mac);
        service.checkRegistration(mac)
                .compose(RxUtil.<RegistrationStatus>threadSwitch())
                .subscribe(new Consumer<RegistrationStatus>() {
                    @Override
                    public void accept(@NonNull RegistrationStatus result) throws Exception {
                        Log.d("HAT测试", "检查设备是否注册onNext:" + result);
                        WhiteListManager.getInstance().handleRegistrationResult(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        Log.d("HAT测试", "检查设备是否注册onError");
                        e.printStackTrace();
                    }
                });
    }
}
