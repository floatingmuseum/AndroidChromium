package org.chromium.hat.utils;


import com.google.common.net.InternetDomainName;

import org.chromium.chrome.browser.util.UrlUtilities;
import org.chromium.hat.WhiteListManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public static String getDomain(String url) throws Exception {
        url = UrlUtil.isStartWithHttp(url);
        URL uri;
        String host;
        try {
            uri = new URL(url);
            host = uri.getHost();
        } catch (MalformedURLException e) {
            host = null;
            e.printStackTrace();
        }

        if (host == null) {
            return null;
        }

        if (isIpAddress(host)) {
            return host;
        }
//        Logger.d("GUAVA...host:"+ host+"..."+url);
//        return UrlUtilities.getDomainAndRegistry(host, false);
        return InternetDomainName.from(host).topPrivateDomain().toString();
    }

    public static List<String> getRootDomains(List<String> urls) {
        List<String> rootDomains = new ArrayList<>();
        if (!ListUtil.hasData(urls)) {
            return rootDomains;
        }
        for (String url : urls) {
            //根域名获取失败后不添加到集合中，并且继续获取后面的根域名
            String domain;
            try {
                domain = UrlUtil.getDomain(url);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            rootDomains.add(domain);
        }
        return rootDomains;
    }
}
