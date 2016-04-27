package com.shengtao.utils;

import android.content.Context;

/**
 * 缓存工具类
 */
public class CacheUtils {

	/**
	 * 写缓存 以url为key,以json为value,保存在sp中
	 */
	public static void setCache(String url, String json, Context ctx) {
		// 有时候,也可以将缓存写在本地文件中,以Md5(url)为文件名, 以json为文件内容
		Session.SetString(url, json);
	}

	/**
	 * 读缓存
	 */
	public static String getCache(String url, Context ctx) {
		// MD5(url),判断该文件是否存在,如果存在,说明有缓存
		return Session.GetString(url, null);
	}
}
