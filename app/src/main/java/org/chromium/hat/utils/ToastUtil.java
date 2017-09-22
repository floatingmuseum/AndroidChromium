package org.chromium.hat.utils;

import android.widget.Toast;

import org.chromium.base.ContextUtils;

/**
 * 单例Toast
 */
public class ToastUtil {
    private static Toast toast = null;

    private ToastUtil() {
    }

    /**
     * 传字符串
     *
     * @param content
     */
    public static void show(String content) {
        if (toast == null) {
            // TODO: 2017/9/20  这里不报空指针是因为Toast的makeText是静态方法,不过不稳妥,计划改一下
            toast = Toast.makeText(ContextUtils.getApplicationContext(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 传String资源id
     *
     * @param resId
     */
    public static void show(int resId) {
        show(ContextUtils.getApplicationContext().getText(resId).toString());
    }
}
