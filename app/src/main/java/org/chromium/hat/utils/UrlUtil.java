package org.chromium.hat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Floatingmuseum on 2017/9/20.
 */

public class UrlUtil {

    public static String isStartWithHttp(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
            if (!url.endsWith("/")) {
                url = url + "/";
            }
            return url;
        }
        return url;
    }

    /**
     * ip地址(带端口号)
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIpAddressWithPort(String ipAddress) {
        if (ipAddress != null && ipAddress.length() > 0) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\:\\d{1,5}$";
            Pattern ipAndPortPattern = Pattern.compile(regex);
            Matcher matcher = ipAndPortPattern.matcher(ipAddress);
            matcher.reset();
            if (!matcher.matches()) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * ip地址(不带端口号)
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIpAddress(String ipAddress) {
        if ("".equals(ipAddress) || ipAddress.length() < 7 || ipAddress.length() > 15) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(regex);

        Matcher mat = pat.matcher(ipAddress);

        return mat.find();
    }
}
