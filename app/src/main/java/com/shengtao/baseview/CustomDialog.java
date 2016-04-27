package com.shengtao.baseview;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.shengtao.R;
import com.shengtao.utils.AVLoadingIndicatorView;


/**
 * 异步初始化加载页面
 * 
 * @author dane55
 * 
 */
public class CustomDialog extends ProgressDialog {
	private AnimationDrawable mAnimation;
	private AVLoadingIndicatorView mImageView;

	public CustomDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
//		initData();
	}

	private void initData() {

//		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});	}

	private void initView() {
		setContentView(R.layout.progress_laytou);
		mImageView = (AVLoadingIndicatorView) findViewById(R.id.loadingIv);
	}
}
