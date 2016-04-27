package com.shengtao.foundation;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 18:37
 * @package： com.foudation.framework.base
 * @classname： BaseWorkerFragmentActivity.java
 * @description： 对BaseWorkerFragmentActivity.java类的功能描述
 */
public abstract class BaseWorkerFragmentActivity extends BaseFragmentActivity {

    /**
     * HandlerThread 消息循环处理线程
     */
    private HandlerThread mHandlerThread;

    /**
     * 自定义后台更新UI的HandlerThread
     */
    protected BackgroundHandler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(this, mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (getActivityReference() != null && getActivityReference().get() != null) {
                    getActivityReference().get().handleBackgroundMessage(msg);
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
    }

    /**
     * 处理后台操作
     */
    protected void handleBackgroundMessage(Message msg) {
    }

    /**
     * 发送后台操作
     *
     * @param msg
     */
    protected void sendBackgroundMessage(Message msg) {
        mBackgroundHandler.sendMessage(msg);
    }

    /**
     * 发送后台操作
     *
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        mBackgroundHandler.sendEmptyMessage(what);
    }

    /**
     * 自定义后台更新UI的HandlerThread
     *
     * @author dane55
     *
     */
    private static class BackgroundHandler extends Handler {

        private final WeakReference<BaseWorkerFragmentActivity> mActivityReference;

        BackgroundHandler(BaseWorkerFragmentActivity activity, Looper looper) {
            super(looper);
            mActivityReference = new WeakReference<BaseWorkerFragmentActivity>(activity);
        }

        public WeakReference<BaseWorkerFragmentActivity> getActivityReference() {
            return mActivityReference;
        }
    }
}
