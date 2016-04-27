package com.shengtao.foundation;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 17:52
 * @package： com.foudation.framework.base
 * @classname： BaseApplication.java
 * @description： Android应用程序基类；封装常用的Application UI线程回调处理
 * 常量定义规范：类型_功能_描述
 */
public class BaseApplication extends Application {
    //定义静态的Application
    private static BaseApplication mApplication;

    //用于存储应用全局变量
    private static Hashtable<String, Object> mAppParamsHolder;

    //消息提示Flag，
    private static final int MSG_SHOW_MSG = 0X01;

    //Application处理消息回调，接受当前定义mHandler的回调请求
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_MSG:
                    Toast.makeText(mApplication, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    /**
     * 重写Application的onCreate方法，初始化BaseApplication和Android应用全局缓存
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mAppParamsHolder = new Hashtable<String, Object>();
    }

    /**
     * 获取Adnroid应用程序实例
     * @return
     */
    public  static  BaseApplication getInstance(){
        if (mApplication == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mApplication;
    }
    /**
     * 存储全局数据
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, Object value) {
        mAppParamsHolder.put(key, value);
    }
    /**
     * 获取当前全局数据Key的Value值
     *
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        return mAppParamsHolder.get(key);
    }
    /**
     * 检测是否包含值
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return mAppParamsHolder.containsKey(key);
    }

    /**
     * 获取自身App安装包信息
     *
     * @return
     */
    public PackageInfo getLocalPackageInfo() {
        return getPackageInfo(getPackageName());
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(String packageName) {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public static void showToast(String msg) {
        //创建HandlerMessage并发送至mHandler
        mHandler.obtainMessage(MSG_SHOW_MSG, msg).sendToTarget();
    }

}
