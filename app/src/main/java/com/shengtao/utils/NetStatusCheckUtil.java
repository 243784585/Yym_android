package com.shengtao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * @Title: NetStatusCheckUtil.java
 * @Package com.framework.util
 * @Description: 网络状态监测
 * @author cjl
 * @date 2014年12月25日 下午4:57:12
 * @version V1.0
 */

public class NetStatusCheckUtil {

	// wifi为0,3g为1
	public final static int WIFI = 0;
	public final static int G3 = 1;

	// 检查网络连接的是wifi还是3G
	public static int checkNetworkInfo(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); // mobile 3G
																	// Data
																	// Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		if (wifi == State.CONNECTED) {
			return WIFI;
		} else {
			return G3;
		}
	}

	/**
	 * 判断是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkStatus(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		State wifi = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (mobile == State.CONNECTED || wifi == State.CONNECTED) {
			return true;
		}
		return false;
	}

}
