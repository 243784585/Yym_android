package com.shengtao.utils;

import android.util.Log;

import com.shengtao.domain.mine.PersonDStateInfo;

import java.util.ArrayList;

/**
 * @Title: LogUtil.java
 * @Package com.framework.util
 * @Description: 日志工具
 * @author cjl
 * @date 2014年12月25日 下午4:57:12
 * @version V1.0
 */

public class LogUtil {

	private static final String TAG = "Log";

	private static boolean isDebug = true;

	/**
	 * 是否处于调试模式
	 * 
	 * @param debug
	 */
	public static boolean isDebug() {
		return isDebug;
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg, ArrayList<PersonDStateInfo> dList) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

}
