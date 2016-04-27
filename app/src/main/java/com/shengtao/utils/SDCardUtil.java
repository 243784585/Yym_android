package com.shengtao.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @Title: SDCardUtil.java
 * @Package com.framework.util
 * @Description: sd卡的工具类
 * @author cjl
 * @date 2014年12月25日 下午5:13:35
 * @version V1.0
 */

public class SDCardUtil {

	/**
	 * 判断是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	/**
	 * SDCard剩余大小
	 * 
	 * @return 字节
	 */
	public static long getAvailableExternalMemorySize() {
		if (hasSDCard()) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 是否有足够的空间（30M）
	 * 
	 * @return
	 */
	public static boolean hasEnoughSpace() {
		return getAvailableExternalMemorySize() > 30 * 1024 * 1024;
	}

	/**
	 * 是否有足够的空间
	 * 
	 * @param minSize
	 *            最小值
	 * @return
	 */
	public static boolean hasEnoughSpace(long minSize) {
		return getAvailableExternalMemorySize() > minSize;
	}

	/**
	 * SDCard总容量大小
	 * 
	 * @return 字节
	 */
	public static long getTotalExternalMemorySize() {
		if (hasSDCard()) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				return totalBlocks * blockSize;

			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 这个是手机内存的可用空间大小
	 * 
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 这个是手机内存的总空间大小
	 * 
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}
}
