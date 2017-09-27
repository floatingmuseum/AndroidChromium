package org.chromium.hat;

import java.net.URI;
import java.net.URISyntaxException;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.chromium.hat.entity.RegistrationStatus;
import org.chromium.hat.entity.WhiteListInfo;
import org.chromium.hat.utils.SPUtil;
import org.chromium.hat.utils.UrlUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Floatingmuseum on 2017/9/20.
 */

public class WhiteListManager {
    private static final String TAG = "HAT测试";
    private static final String PREDEFINED_WHITE_DOMAIN1 = "edu505.com";
    private static final String PREDEFINED_WHITE_DOMAIN2 = "91yunxiao.com";
    public static final String SP_KEY_BROWSER_SETTING = "browser_setting";
    public static final String SP_KEY_DEVICE_REGISTRATION = "is_registered";
    public static final String SP_KEY_REGION_SERVER_HOST = "region_server_host";

    private static final int OPENING_HOURS = 1;
    private static final int CLOSING_HOURS = 0;

    private WhiteListInfo listInfo;

    private static WhiteListManager whiteListManager;
    private boolean isDeviceRegistered = false;

    private WhiteListManager() {
        isDeviceRegistered = SPUtil.getBoolean(SP_KEY_DEVICE_REGISTRATION, false);
    }

    public static WhiteListManager getInstance() {
        if (whiteListManager == null) {
            synchronized (WhiteListManager.class) {
                if (whiteListManager == null) {
                    whiteListManager = new WhiteListManager();
                }
            }
        }
        return whiteListManager;
    }

    /**
     * @param url 被检测的网址
     * @return true为需要拦截, false为不需要拦截
     */
    public boolean checkUrl(String url) {
        /**
         * 这里和管控中的判断是反过来的,
         * 管控中是判断url是否合法,
         * 这里判断url是否非法,
         * 所有这里的true和false和管控中是相反的.
         */
        Log.d("HAT测试", "当前网址:" + url + "..." + url.startsWith("chrome"));

        //如果未注册则不拦截
        if (!isDeviceRegistered) {
            return false;
        }

        //Chrome的内置标签页均以chrome开头,不拦截
        if (!TextUtils.isEmpty(url) && url.startsWith("chrome")) {
            return false;
        }

        String domain = null;
        try {
            domain = UrlUtil.getDomain(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("HAT测试", "当前网址Domain:" + domain + "...原网址:" + url);
        //默认主页
        if (PREDEFINED_WHITE_DOMAIN1.equalsIgnoreCase(domain) || PREDEFINED_WHITE_DOMAIN2.equals(domain)) {
            Log.d(TAG, "白名单判断:预置Domain:" + domain);
            return false;
        }

        if (listInfo == null) {
            //如果为空说明应用启动后第一次联网就失败了，这时取出之前保存的浏览器设置信息
            String localBrowserSetting = SPUtil.getString(SP_KEY_BROWSER_SETTING, null);
            if (localBrowserSetting == null) {
                Log.d(TAG, "WhiteListManager:本地存储的浏览器设置localBrowserSetting为空");
                return false;
            }

            Gson gson = new GsonBuilder().create();
            WhiteListInfo localSetting = gson.fromJson(localBrowserSetting, WhiteListInfo.class);
            if (localSetting == null) {
                Log.d(TAG, "WhiteListManager:本地存储的浏览器设置Gson转换localSetting出错");
                return false;
            }
            listInfo = localSetting;
        }


        int isUse = listInfo.getIsUse();
        //如果使用开放时段，则检查是否处于开放时段内
        Log.d(TAG, "WhiteListManager:是否使用开放时段..." + (isUse == OPENING_HOURS));
        if (isUse == OPENING_HOURS) {
            isUse = isInsideOpeningHours();
        }
        Log.d(TAG, "WhiteListManager:是否处于开放时段..." + (isUse == OPENING_HOURS));
        /**
         * 开放时段可以访问黑名单以外的网站
         * 非开放时段只可访问白名单内网站
         */
        if (isUse == OPENING_HOURS) {
            if (isContainDomain(listInfo.getSbl(), domain) || isContainDomain(listInfo.getAbl(), domain)) {
                Log.d(TAG, "WhiteListManager:开放上网时段,黑名单中网站禁止访问:" + domain);
                return true;
            }
            Log.d(TAG, "WhiteListManager:开放上网时段" + domain);
            return false;
        } else {
            if (isContainDomain(listInfo.getSwl(), domain) || isContainDomain(listInfo.getAwl(), domain)) {
                Log.d(TAG, "WhiteListManager:关闭上网时段，白名单中网站可以访问:" + domain);
                return false;
            }
            Log.d(TAG, "WhiteListManager:关闭上网时段" + domain);
            return true;
        }
    }

    private int isInsideOpeningHours() {
        int isUse = listInfo.getIsUse();
        Calendar now = Calendar.getInstance();
        int w = now.get(Calendar.DAY_OF_WEEK);
        // w取得是Calendar的周天，也就是周日是1，周一是2，周六是7
        // if (w == 7) //Calendar的7是SATURDAY
        if (w == 1) {// Calendar的1是周日
            w = 7; // 7是服务端的周日
        } else {
            w--;
        }

        // 到这里时，w已经表示了与服务端等同的值，也就是1代表周一，7代表周日
        WhiteListInfo.NetTime[] ts = listInfo.getNetTime().get(String.valueOf(w));
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);
        String nowSt = (h < 10 ? "0" : "") + h + (h < 10 ? ":0" : ":") + m;
        if (ts != null) {
            for (WhiteListInfo.NetTime t : ts) {
                if (nowSt.compareTo(t.sTime) == -1) {
                    isUse = CLOSING_HOURS;
                    continue;
                }

                if (nowSt.compareTo(t.eTime) == 1) {
                    isUse = CLOSING_HOURS;
                    continue;
                }
                isUse = OPENING_HOURS;
                break;
            }
        } else {
            isUse = CLOSING_HOURS;
        }
        return isUse;
    }

    private boolean isContainDomain(List<String> list, String domain) {
        if (list == null || domain == null) {
            return false;
        }
        for (String url : list) {
            if (url == null) {
                return false;
            }
            if (url.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeviceRegistered() {
        return isDeviceRegistered;
    }

    public void handleResult(WhiteListInfo result) {
        if (result != null) {
            Log.d("HAT测试", "黑白名单:" + result.toString());
            listInfo = result;
            Gson gson = new Gson();
            String jsonResult = gson.toJson(result);
            SPUtil.putString(SP_KEY_BROWSER_SETTING, jsonResult);
        }
    }

    public void handleRegistrationResult(RegistrationStatus result) {
        Log.d("HAT测试", "检查设备是否注册onNext:" + result.getResult() + "..." + result.getRegion_server_host());
        boolean registered = result.getResult() == 0;
        SPUtil.putBoolean(SP_KEY_DEVICE_REGISTRATION, registered);

        if (registered) {
            SPUtil.putString(SP_KEY_REGION_SERVER_HOST, result.getRegion_server_host());
            if (!isDeviceRegistered) {
                isDeviceRegistered = true;
                SyncManager.getInstance().syncBrowserWhiteList();
                SyncManager.getInstance().stopSync(SyncManager.SYNC_STOP_TYPE_REGISTRATION);
                SyncManager.getInstance().setRegisteredTime(true);
                SyncManager.getInstance().syncIsRegistration();
            }
        } else {
            isDeviceRegistered = false;
        }
    }
}
