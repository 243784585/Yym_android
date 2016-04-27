package com.shengtao.utils;

import android.content.Context;
import android.widget.Toast;

import com.shengtao.application.UIApplication;


public class ToastUtil {

    /**
     * 开关
     */
    private static boolean isDebug = true;

    /**
     * @param message
     * 吐出的信息
     * @param mContext
     * 文本对象
     */
//	public static void makeText(Context mContext, String message) {
//		if (isDebug) {
//			Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
//		}
//	}

//    private static Toast toast;

    /**
     * 可以连续弹吐司，不用等上个吐司消失
     *
     * @param text
     */
//    public static void makeText(Context mContext, String text) {
//        if (isDebug) {
//
//        }
//        if (toast == null) {
//            toast = Toast.makeText(mContext, text, 0);
//        }
//        toast.setText(text);
//        toast.show();
//    }

//        private static Toast toast;

//        public static void makeText(Context context, String content) {
////            if (isDebug) {
//            if (toast == null) {
//            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
//        }
//        toast.setText(content);
//        toast.show();
////                if (toast != null) {
////                    toast.cancel();
////                }
////                toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
////                toast.show();
//////            }
//        }


    private static Toast toast = null;

    public static void makeText(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void makeText(Context context, int content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    //连续吐司
    public static void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(UIApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showTextToast(int msg) {
        if (toast == null) {
            toast = Toast.makeText(UIApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void loadMoreTips(Context context) {
        ToastUtil.showTextToast("没有更多数据了...");
    }
}
