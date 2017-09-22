package org.chromium.hat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by Floatingmuseum on 2016/7/29.
 */
public class SPUtil {
    private static Context mContext;
    private static SharedPreferences sp;

    /**
     * Init method, always by invoked in Application
     *
     * @param context
     */
    public static void init(Context context) {
        if (null == context) {
            throw new IllegalArgumentException("context cannot be null.");
        }
        mContext = context.getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static boolean containsKey(String name, String key) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE).contains(key);
    }

    public static boolean containsKey(String key) {
        return sp.contains(key);
    }

    public static String getString(String name, String key, String defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getString(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void putString(String name, String key, String value) {
        mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void putString(String key, String value) {
        sp.edit().putString(key, value)
                .apply();
    }

    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static int getInt(String name, String key, int defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getInt(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        sp.edit().putInt(key, value)
                .apply();
    }

    public static void putInt(String name, String key, int value) {
        mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static boolean getBoolean(String name, String key, boolean defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String name, String key, boolean value) {
        mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value)
                .apply();
    }

    public static long getLong(String name, String key, long defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getLong(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static void putLong(String spName, String key, long defaultValue) {
        mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, defaultValue)
                .apply();
    }

    public static void putLong(String key, long defaultValue) {
        sp.edit()
                .putLong(key, defaultValue)
                .apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public static float getFloat(String name, String key, long defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getFloat(key, defaultValue);
    }

    public static Set<String> getStringSet(String name, String key, Set<String> defaultValue) {
        return mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .getStringSet(key, defaultValue);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public static void putStringSet(String name, String key, Set<String> set) {
        mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(key, set)
                .apply();
    }

    public static void putStringSet(String key, Set<String> set) {
        sp.edit()
                .putStringSet(key, set)
                .apply();
    }

    public static void remove(String name, String key) {
        mContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .remove(key)
                .apply();
    }

    public static void remove(String key) {
        sp.edit().remove(key).apply();
    }
}
