package com.shengtao.application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shengtao.R;

public class ImageLoaderCfgCircleHeadImg {
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.default_user_icon)
	.showImageForEmptyUri(R.drawable.default_user_icon)
	.showImageOnFail(R.drawable.default_user_icon)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)//是否识别图片的方向
//	.displayer(new FadeInBitmapDisplayer(500)).build();//图片渐现动画
	.displayer(new RoundedBitmapDisplayer(20)).build();//图片设置圆角
}
