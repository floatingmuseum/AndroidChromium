package org.chromium.hat.net;

/**
 * Created by Floatingmuseum on 2017/9/25.
 */

public interface DataCallback<T> {
    void onSuccess(T t);
    void onError(Throwable e);
}
