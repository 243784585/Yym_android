package com.shengtao.foundation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 18:14
 * @package： com.foudation.framework.base
 * @classname： BaseFragment.java
 * @description： 对BaseFragment.java类的功能描述
 */
public abstract class BaseFragment extends Fragment {

    private IToastBlock toastBlock = null;
    /**
     * 实例化一个UiHandler处理
     */
    protected Handler mUiHandler = new UiHandler(this) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getFragmentReference() != null && getFragmentReference().get() != null) {
                try {
                    getFragmentReference().get().handleUiMessage(msg);
                } catch (IllegalStateException e) {
                }
            }
        }

        ;
    };

    /**
     * 获取自定义的Toast类型
     *
     * @return
     */
    public abstract IToastBlock getToastBlock();

    /**
     * 自定义UiHandler,用于处理其他线程发送的消息
     *
     * @author dane55
     */
    private static class UiHandler extends Handler {
        // 定义BaseFragment的弱引用，以保证虚拟机随时可以回收此页面
        private final WeakReference<BaseFragment> mFragmentReference;

        /**
         * 构建自定义的UIHandler，传递当前页面引用(Activity或者BaseFragment)
         *
         * @param activity
         */
        public UiHandler(BaseFragment activity) {
            mFragmentReference = new WeakReference<BaseFragment>(activity);
        }

        /**
         * 获取当前页面(Activity或者BaseFragment)
         *
         * @return
         */
        public WeakReference<BaseFragment> getFragmentReference() {
            return mFragmentReference;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.toastBlock = getToastBlock();
    }

    /**
     * 处理更新UI任务(派生类需要重写此方法，用于处理自身业务)
     *
     * @param msg
     */
    protected void handleUiMessage(Message msg) {
    }

    /**
     * 发送UI更新操作
     *
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }


    /**
     * 发送UI更新操作
     *
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }


    /**
     * 显示一个Toast类型的消息
     *
     * @param msg 显示的消息
     */
    public void showToast(final String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    toastBlock.showText(getActivity(), msg, false);
                }
            });
        }
    }

//    /**
//     * 显示通知
//     *
//     * @param strResId 字符串资源id
//     */
//    public void showToast(final int strResId) {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    toastBlock.showText(getActivity(), getResources().getString(strResId), false);
//                }
//            });
//        }
//    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 接收来自BroadcastReceiver数据集
     *
     * @param context
     * @param intent
     */
    public void onReceive(Context context, Intent intent) {

    }
}