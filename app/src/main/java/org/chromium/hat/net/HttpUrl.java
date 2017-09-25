package org.chromium.hat.net;

import org.chromium.chrome.browser.BuildConfig;
import org.chromium.hat.WhiteListManager;
import org.chromium.hat.utils.SPUtil;

/****
 * 数据请求地址
 *
 * @author seconds
 */
public class HttpUrl {

    public static final int URL_BROWSER_LIST = 1;
    public static final int URL_APP_LIST = 2;
    public static final int URL_APP_DOWNLOAD = 3;
    public static final int URL_UPLOAD_INSTALLED_APP = 4;
    public static final int URL_USER_INFO = 5;
    public static final int URL_UPLOAD_DEVICE_INFO = 6;
    public static final int URL_BACKGROUND_IMAGE = 7;
    public static final int URL_SETTINGS_CONFIG = 8;
    public static final int URL_UPLOAD_DEVICE_APP = 9;
    public static final int URL_GET_DEVICE_STRATEGY = 10;
    //static 类加载时加载 ，获取新值时得动态赋值覆盖旧值

    //总服务器地址
    /**
     * 正式地址
     **/
    public static String HEAD_BASE_PATH = "http://center.hangzhisoft.cn:8080/educationCloud/";
    /**
     * 测试地址
     **/
//    public static String HEAD_BASE_PATH = "http://192.168.1.123:8080/educationCloud/";

    private static final boolean APPLY_TEST_SERVER = false;

    public static String getHeadBasePath() {
        if (BuildConfig.DEBUG) {
            if (APPLY_TEST_SERVER) {
                return "http://192.168.1.142:8080/educationCloud/";
            } else {
                return HEAD_BASE_PATH;
            }
        } else {
            return HEAD_BASE_PATH;
        }
    }

    public static String getRegionServerPath() {
        if (BuildConfig.DEBUG) {
            if (APPLY_TEST_SERVER) {
                return "http://192.168.1.142:8080/regionalServer/";
            } else {
                String region = SPUtil.getString(WhiteListManager.SP_KEY_REGION_SERVER_HOST, "www.floatingmuseum.com");
                return "http://" + region + ":8080/regionalServer/";
            }
        } else {
            String region = SPUtil.getString(WhiteListManager.SP_KEY_REGION_SERVER_HOST, "www.floatingmuseum.com");
            return "http://" + region + ":8080/regionalServer/";
        }
    }

    public static String getUrl(int type) {
        String REGION_SERVER_PATH = getRegionServerPath();


        switch (type) {
            case URL_BROWSER_LIST://浏览器黑白名单
                return REGION_SERVER_PATH + "whitelist";
            case URL_APP_LIST://应用列表
                return REGION_SERVER_PATH + "CAppServletGetApps.svl";
            case URL_APP_DOWNLOAD:
                return REGION_SERVER_PATH + "appdownload.action";
            case URL_UPLOAD_INSTALLED_APP://上传已安装的推荐应用
                return REGION_SERVER_PATH + "CAppUpdateInstalledApps.svl";
            case URL_USER_INFO://用户信息
                return REGION_SERVER_PATH + "CDeviceGetServlet.svl";
            case URL_UPLOAD_DEVICE_INFO://上传平板设备信息
                return REGION_SERVER_PATH + "CDeviceServletUpdateStatus.svl";
            case URL_BACKGROUND_IMAGE://桌面壁纸
                return REGION_SERVER_PATH + "CBackgroundImageGetServlet.svl";
            case URL_SETTINGS_CONFIG://获取设置页面可显示选项
                return REGION_SERVER_PATH + "CAndroidSettingsServletGetSettings.svl";
            case URL_UPLOAD_DEVICE_APP://上传终端应用
                return REGION_SERVER_PATH + "CDeviceUpdateAppTerminals.svl";
            case URL_GET_DEVICE_STRATEGY://终端应用使用策略
                return REGION_SERVER_PATH + "CAppServletGetAppTerminals.svl";
            default:
                return REGION_SERVER_PATH;
        }
    }
}
