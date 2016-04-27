package com.shengtao.foundation;

import android.os.Environment;

import com.shengtao.utils.SDCardUtil;

import java.io.File;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/16 10:27
 * @package： com.motu.base
 * @classname： SdcardConfig.java
 * @description： 对SdcardConfig.java类的功能描述
 */
public class SdcardConfig {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment
            .getExternalStorageDirectory().toString();

    /**
     * 根目录
     */
    public static final String ROOT_FOLDER = SDCARD_FOLDER + "/moto/";

    /**
     * 缓存目录
     */
    public static final String CACHE_FOLDER = ROOT_FOLDER + "cache/";

    /**
     * 网页缓存目录
     */
    public static final String WEB_CACHE_FOLDER = ROOT_FOLDER + "webCache/";

    /**
     * 数据库缓存目录
     */
    public static final String DB_CACHE_FOLDER = ROOT_FOLDER + "dbCache/";

    /**
     * 城市的数据库地址
     */
    public static final String ADDRESS_DB_CACHE_FILE = ROOT_FOLDER
            + "dbCache/address.db";

    /**
     * 区号的数据库地址
     */
    public static final String PREFIX_DB_CACHE_FILE = ROOT_FOLDER
            + "dbCache/prefix.db";

    /**
     * 相片目录
     */
    public static final String PHOTO_FOLDER = ROOT_FOLDER + "photo/";

    /**
     * 日志目录
     */
    public static final String LOG_FOLDER = ROOT_FOLDER + "log/";

    /**
     * 用户目录
     */
    public static final String USER_FOLDER = ROOT_FOLDER + "user/";

    private static SdcardConfig sSdcardConfig;

    public static synchronized SdcardConfig getInstance() {
        if (sSdcardConfig == null) {
            sSdcardConfig = new SdcardConfig();
        }
        return sSdcardConfig;
    }

    // *************************************************************************
    /**
     * 【】(sd卡初始化)
     */
    // *************************************************************************
    public void initSdcard() {
        if (!SDCardUtil.hasSDCard())
            return;

        File logFile = new File(LOG_FOLDER);
        if (!logFile.exists())
            logFile.mkdirs();

        File cacheFile = new File(CACHE_FOLDER);
        if (!cacheFile.exists())
            cacheFile.mkdirs();

        File webCacheFile = new File(WEB_CACHE_FOLDER);
        if (!webCacheFile.exists())
            webCacheFile.mkdirs();

        File photoFile = new File(PHOTO_FOLDER);
        if (!photoFile.exists())
            photoFile.mkdirs();

        File dbFile = new File(DB_CACHE_FOLDER);
        if (!dbFile.exists())
            dbFile.mkdirs();

        File userFile = new File(USER_FOLDER);
        if (!userFile.exists())
            userFile.mkdirs();
    }
}