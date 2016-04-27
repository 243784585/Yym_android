package com.shengtao.utils;

import android.graphics.Bitmap;

import com.shengtao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 相关配置参数
 */
public class Constants {

    public final static int COLUMN_COUNT = 2; // 显示列数

    public final static int PICTURE_COUNT_PER_LOAD = 30; // 每次加载30张图片

    public final static int PICTURE_TOTAL_COUNT = 10000;   //允许加载的最多图片数

    public final static int HANDLER_WHAT = 1;

    public final static int MESSAGE_DELAY = 200;

    public final static int HTTP_TIMEOUT = 200;

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    public static class Extra {
        public static final String IMAGES = "com.motu.application.IMAGES";
        public static final String IMAGE_POSITION = "com.motu.application.IMAGE_POSITION";
    }

    /**
     * 获取显示图片的相关设置
     *
     * @return
     */
    public final static DisplayImageOptions getImageLoaderOption() {
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(R.drawable.ic_stub)
                        // 加载异常时显示图片
                .showImageOnFail(R.drawable.ic_error)
                        //.cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(R.drawable.ic_empty)
                .build();
    }


    public final DisplayImageOptions getImageLoaderOption(int ic_stub, int ic_error, int ic_empty, int rounded) {
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(ic_stub)
                        // 加载异常时显示图片
                .showImageOnFail(ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(rounded))
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(ic_empty)
                .build();
    }

}
