package com.shengtao.foundation;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 18:29
 * @package： com.foudation.framework.base
 * @classname： BaseFragmentActivity.java
 * @description： 对BaseFragmentActivity.java类的功能描述
 */
public abstract class BaseFragmentActivity extends SwipeBackActivity implements View.OnClickListener {
    private boolean isShowing;
    private IToastBlock mToastBlack = null;
    private boolean background = false;

    String mPackageName;

    /**
     * ActivityManager 管理Android应用程序所有的Activity
     */
    ActivityManager mActivityManager;

    PowerManager powerManager;

    protected Handler mUiHandler = new UiHandler(this) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getActivityReference() != null && getActivityReference().get() != null) {
                getActivityReference().get().handleUiMessage(msg);
            }
        }

        ;
    };

    private static class UiHandler extends Handler {
        private final WeakReference<BaseFragmentActivity> mActivityReference;

        public UiHandler(BaseFragmentActivity activity) {
            mActivityReference = new WeakReference<BaseFragmentActivity>(activity);
        }

        public WeakReference<BaseFragmentActivity> getActivityReference() {
            return mActivityReference;
        }
    }

    /**
     * 获取自定义的Toast类型
     *
     * @return
     */
    public abstract IToastBlock getmToastBlack();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivityManager.getActivityManager().pushActivity(this);
        this.mToastBlack = getmToastBlack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentActivityManager.getActivityManager().popActivity(this);
    }

    /**
     * 处理更新UI任务
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
    public void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    public void sendUiMessageDelayed(Message msg, long delayMillis) {
        mUiHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * 发送UI更新操作
     *
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }

    protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
        mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * 显示一个Toast类型的消息
     *
     * @param msg 显示的消息
     */
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToastBlack.showText(BaseFragmentActivity.this, msg, false);
            }
        });
    }

    /**
     * 显示通知
     *
     * @param strResId 字符串资源id
     */
    public void showToast(final int strResId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToastBlack.showText(BaseFragmentActivity.this, getResources().getString(strResId), false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowing = true;
        if (background) {
            background = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShowing = false;
        mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        mPackageName = getPackageName();
        // 获取电源管理
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);

    }

    /**
     * 判断Android应用是否在前台
     *
     * @return
     */
    public boolean isAppOnForeground() {

        List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);

        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 判断Android是否锁屏
     *
     * @param c
     * @return
     */
    public final static boolean isScreenLocked(Context c) {
        boolean ScreenLocked = false;
        KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
        ScreenLocked = !mKeyguardManager.inKeyguardRestrictedInputMode();
        return ScreenLocked;

    }

    /**
     * 获取Android手机内安装的所有桌面
     *
     * @param context
     * @return
     */
    private static List<String> getAllTheLauncher(Context context) {
        List<String> names = null;
        PackageManager pkgMgt = context.getPackageManager();
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
        if (ra.size() != 0) {
            names = new ArrayList<String>();
        }
        for (int i = 0; i < ra.size(); i++) {
            String packageName = ra.get(i).activityInfo.packageName;
            names.add(packageName);
        }
        return names;
    }

    /**
     * Android 判断程序前后台状态
     *
     * @param context
     * @return
     */
    public static boolean isLauncherRunnig(Context context) {
        boolean result = false;
        List<String> names = getAllTheLauncher(context);
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo running : appList) {
            if (running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (int i = 0; i < names.size(); i++) {
                    if (names.get(i).equals(running.processName)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param it
     * @return void
     * @Title: startAnimationActivity
     * @Description: 开始动画跳转
     */
    public void startAnimationActivity(Intent it) {
        startActivity(it);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * @param it
     * @param requestCode
     * @return void
     * @Title: startAnimationActivityForResult
     * @Description: 开始动画跳转
     */
    public void startAnimationActivityForResult(Intent it, int requestCode) {
        startActivityForResult(it, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * @return void
     * @Title: finishAnimationActivity
     * @Description: 动画方式结束页面
     */
    public void finishAnimationActivity() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
