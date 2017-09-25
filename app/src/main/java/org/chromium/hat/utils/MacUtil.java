package org.chromium.hat.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import org.chromium.base.ContextUtils;
import org.chromium.chrome.browser.ChromeApplication;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Floatingmuseum on 2017/9/25.
 */

public class MacUtil {

    public static String getMacAddress() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                return getMacAddressOnAndroid6Above();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        WifiManager wifiManager = (WifiManager) ContextUtils.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 获取本机mac物理地址
//		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        String macInfo = wifiManager.getConnectionInfo().getMacAddress();
//
//		// 将获取到的mac存放于SharedPreferenceData中
//		SharedPreferenceData.setMacAddress(macInfo);
        System.out.println("本机的mac物理地址为：" + macInfo);
        return macInfo;
    }

    private static String getMacAddressOnAndroid6Above() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iF = interfaces.nextElement();
            byte[] addr = iF.getHardwareAddress();
            if (addr == null || addr.length == 0) {
                continue;
            }
            StringBuilder buf = new StringBuilder();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            String mac = buf.toString();
            if (iF.getName().equals("wlan0")){
                return mac;
            }
        }
        return "";
    }
}
