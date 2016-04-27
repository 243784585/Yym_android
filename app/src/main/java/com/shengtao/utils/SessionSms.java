package com.shengtao.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.shengtao.application.UIApplication;


/**
 * 将数据存到本地,专属短信验证码
 */
public class SessionSms {
    private static final SharedPreferences preferences = UIApplication.getContext().getSharedPreferences("sms",
            Activity.MODE_PRIVATE);

    public static void SetString(String key, Object value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value.toString());
        editor.apply();
    }

    public static void SetBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void SetInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Boolean GetBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public static Boolean GetBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static String GetString(String key) {
        return preferences.getString(key, "");
    }

    public static String GetString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static int GetInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static int GetInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static void ClearSession() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void remove(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
}