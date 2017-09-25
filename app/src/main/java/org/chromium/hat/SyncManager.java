package org.chromium.hat;

import android.util.Log;

import org.chromium.hat.net.Repository;
import org.chromium.hat.utils.SPUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Floatingmuseum on 2017/9/25.
 */

public class SyncManager {
    public static final String SP_KEY_BROWSER_WHITE_LIST_LAST_TIME = "browserWhiteListLastSyncTime";
    public static final String SP_KEY_EXPIRE_TIME_LAST_TIME = "expireTimeLastSyncTime";
    public static final String SP_KEY_IS_REGISTRATION_LAST_TIME = "isRegistrationLastSyncTime";

    private static final long SYNC_OVERTIME = 60 * 1000;//1分钟 超时时间
    private static final long SYNC_BROWSER_WHITE_LIST_TIME = 3 * 60 * 1000;//3分钟  浏览器黑白名单
    private static final long SYNC_IS_REGISTRATION = 30 * 60 * 1000;//30分钟 设备是否已注册
    private static final long SYNC_EXPIRE_TIME = 60 * 60 * 1000;//60分钟 过期时间

    private static SyncManager manager;
    private long lastCheckTime = -1;
    private Disposable syncBrowserWhiteListDisposable;
    private Disposable syncIsRegistrationDisposable;

    private SyncManager() {
    }

    public static SyncManager getInstance() {
        if (manager == null) {
            synchronized (SyncManager.class) {
                if (manager == null) {
                    manager = new SyncManager();
                }
            }
        }
        return manager;
    }

    public void checkLastSyncTime() {
        if (isCheckTooOften()) {
            //避免几秒内频繁检测
            Log.d("HAT测试", "SyncManager...checkLastSyncTime()...无效请求...间隔过短");
            return;
        }
        Log.d("HAT测试", "SyncManager...checkLastSyncTime()...有效请求");
        if (isSyncOverTime(syncBrowserWhiteListDisposable, SP_KEY_BROWSER_WHITE_LIST_LAST_TIME)) {
            syncBrowserWhiteList();
        }
        if (isSyncOverTime(syncIsRegistrationDisposable, SP_KEY_IS_REGISTRATION_LAST_TIME)) {
            syncIsRegistration();
        }
    }

    /**
     * 两次检测间隔不小于30秒
     */
    private boolean isCheckTooOften() {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - lastCheckTime;
        if (lastCheckTime == -1 || interval > 30000) {
            lastCheckTime = currentTime;
            return false;
        } else {
            Log.d("HAT测试", "SyncManager...checkLastSyncTime()...无效请求...间隔过短..." + interval);
            return true;
        }
    }

    /**
     * 当前时间-上次同步时间>时间间隔+限定超时时间
     */
    private boolean isSyncOverTime(Disposable disposable, String spKey) {
        long lastSyncTime = SPUtil.getLong(spKey, -1);
        if (lastSyncTime == -1) {
            Log.d("HAT测试", "SyncManager:isSyncOverTime:...spKey:" + spKey + "...lastSyncTime:" + lastSyncTime + "...OverTime:" + true);
            return true;
        } else {
            long currentTime = System.currentTimeMillis();
            long syncTime = getSyncTime(spKey);
            if (isStopped(disposable) || currentTime - lastSyncTime > (syncTime + SYNC_OVERTIME)) {//如果间隔任务已结束或者超时,则重新开始
                Log.d("HAT测试", "SyncManager:isSyncOverTime:...spKey:" + spKey + "...lastSyncTime:" + lastSyncTime + "...OverTime:" + true);
                return true;
            } else {
                Log.d("HAT测试", "SyncManager:isSyncOverTime:...spKey:" + spKey + "...lastSyncTime:" + lastSyncTime + "...OverTime:" + false);
                return false;
            }
        }
    }

    private long getSyncTime(String spKey) {
        if (SP_KEY_BROWSER_WHITE_LIST_LAST_TIME.equals(spKey)) {
            return SYNC_BROWSER_WHITE_LIST_TIME;
        } else if (SP_KEY_EXPIRE_TIME_LAST_TIME.equals(spKey)) {
            return SYNC_EXPIRE_TIME;
        }
        return -1;
    }

    private boolean isStopped(Disposable disposable) {
        return disposable == null || disposable.isDisposed();
    }

    /**
     * 同步浏览器黑白名单
     */
    public void syncBrowserWhiteList() {
        stopDisposable(syncBrowserWhiteListDisposable);
        if (WhiteListManager.getInstance().isDeviceRegistered()) {
            syncBrowserWhiteListDisposable = Flowable.interval(0, SYNC_BROWSER_WHITE_LIST_TIME, TimeUnit.MILLISECONDS)
                    .onBackpressureDrop()
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            Repository.getInstance().getBrowserWhiteList();
                        }
                    });
        }
    }

    /**
     * 同步过期时间
     */
    public void syncIsRegistration() {
        stopDisposable(syncIsRegistrationDisposable);
        syncIsRegistrationDisposable = Flowable.interval(0, SYNC_IS_REGISTRATION, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Repository.getInstance().checkRegistration();
                    }
                });
    }

    public void stopSync() {
        stopDisposable(syncBrowserWhiteListDisposable);
        stopDisposable(syncIsRegistrationDisposable);
    }

    private void stopDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
