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
 * @date： 15/9/15 18:39
 * @package： com.foudation.framework.base
 * @classname： BaseWorkerFragment.java
 * @description： 对BaseWorkerFragment.java类的功能描述
 */
public abstract class BaseWorkerFragment extends BaseFragment {

    protected HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread("fragment worker:"
                + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(this,
                mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (getFragmentReference() != null
                        && getFragmentReference().get() != null) {
                    getFragmentReference().get().handleBackgroundMessage(msg);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null
                && mBackgroundHandler.getLooper() != null) {
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
    public void sendBackgroundMessage(Message msg) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendMessage(msg);
        }
    }

    public void sendBackgroundMessageDelayed(Message msg, long delayMillis) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    /**
     * 发送后台操作
     *
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendEmptyMessage(what);
        }
    }

    // 后台Handler
    private static class BackgroundHandler extends Handler {

        private final WeakReference<BaseWorkerFragment> mFragmentReference;

        BackgroundHandler(BaseWorkerFragment fragment, Looper looper) {
            super(looper);
            mFragmentReference = new WeakReference<BaseWorkerFragment>(fragment);
        }

        public WeakReference<BaseWorkerFragment> getFragmentReference() {
            return mFragmentReference;
        }

    }
}
