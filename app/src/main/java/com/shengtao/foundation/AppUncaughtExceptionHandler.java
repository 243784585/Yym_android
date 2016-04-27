package com.shengtao.foundation;

import android.content.pm.PackageInfo;
import android.os.Environment;
import android.text.TextUtils;

import com.shengtao.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/16 10:25
 * @package： com.motu.base
 * @classname： AppUncaughtExceptionHandler.java
 * @description： 对AppUncaughtExceptionHandler.java类的功能描述
 */
public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {

    /**
     * 日期格式器
     */
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 单例
     */
    private static AppUncaughtExceptionHandler sAppUncaughtExceptionHandler;

    public static synchronized AppUncaughtExceptionHandler getInstance() {
        if (sAppUncaughtExceptionHandler == null) {
            sAppUncaughtExceptionHandler = new AppUncaughtExceptionHandler();
        }
        return sAppUncaughtExceptionHandler;
    }

    private AppUncaughtExceptionHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
    }

    // *************************************************************************
    /**
     * 【】(处理异常)
     *
     * @param ex
     * @return
     */
    // *************************************************************************
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();

        final String crashReport = getCrashReport(ex);
        saveExceptionToSdcard(crashReport);

        // long time = VariableDataCache.getInstance().getReportTime();
        // long curTime = System.currentTimeMillis();
        // if (curTime - time > 2000) {
        // // 低于2秒的不报告
        // saveThrowToSdcard(crashReport);
        // }
        // VariableDataCache.getInstance().setReportTime(curTime);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LogUtil.e("AppUncaughtExceptionHandler", "error:" + e.getMessage());
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        return true;
    }

    // *************************************************************************
    /**
     * 【】(获取崩溃报告)
     *
     * @param ex
     * @return
     */
    // *************************************************************************
    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo = BaseApplication.getInstance().getLocalPackageInfo();
        if (pinfo != null) {
            if (ex != null) {
                String errorStr = ex.getLocalizedMessage();
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.getMessage();
                }
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.toString();
                }
                exceptionStr.append("Exception: " + errorStr + "\n");
                StackTraceElement[] elements = ex.getStackTrace();
                if (elements != null) {
                    for (int i = 0; i < elements.length; i++) {
                        exceptionStr.append(elements[i].toString() + "\n");
                    }
                }
            } else {
                exceptionStr.append("no exception. Throwable is null\n");
            }
            return exceptionStr.toString();
        } else {
            return "";
        }
    }

    // *************************************************************************
    /**
     * 【】(保存错误报告到sd卡)
     *
     * @param errorReason
     */
    // *************************************************************************
    private void saveExceptionToSdcard(String errorReason) {
        try {
            LogUtil.e("AppUncaughtExceptionHandler", "执行了一次");
            /**
             * 插入错误报告到数据库
             */
            // ErrorBean bean = new ErrorBean();
            // bean.setType(0);
            // bean.setError(errorReason.replace("'", ""));
            // ErrorOperator.getInstance().insert(bean);

            long timestamp = System.currentTimeMillis() / 1000;
            String time = mFormatter.format(new Date());
            String fileName = "Crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = SdcardConfig.LOG_FOLDER;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(errorReason.getBytes());
                fos.close();
            }
        } catch (Exception e) {
            LogUtil.e("AppUncaughtExceptionHandler",
                    "an error occured while writing file..." + e.getMessage());
        }
    }
}

