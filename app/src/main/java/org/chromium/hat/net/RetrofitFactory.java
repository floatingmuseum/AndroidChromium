package org.chromium.hat.net;

/**
 * Created by Floatingmuseum on 2017/3/6.
 */

public class RetrofitFactory {
    private static NetService service = new NetRetrofit().getService();

    public static NetService getInstance() {
        return service;
    }
}
