package com.shengtao.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.shengtao.R;
import com.shengtao.adapter.WelcomePagerAdapter;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.SessionSms;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity implements
		OnClickListener {
	private boolean mStopSelf = false;
	private static final int MSG_UI_TO_THE_END = 0;
	private static final int MSG_UI_LEAVE_FROM_END = 1;

	private ViewPager mVpWelcome;
	private int[] mIconList = { R.drawable.bg_welcome_1,
			R.drawable.bg_welcome_2, R.drawable.bg_welcome_3,R.drawable.bg_welcome_4 };

	private List<View> mGuideViewList = new ArrayList<View>();

	private int mOffset;
	private int mCurPos = 0;
	private ImageView ivStartMain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_welcome);
		initView();
		initData();
	}

	private void initView() {
		mVpWelcome = (ViewPager) findViewById(R.id.vp_welcome);
		ivStartMain = (ImageView) findViewById(R.id.iv_start_main);
	}

	private void initData() {
		mStopSelf = getIntent().getBooleanExtra("stopself", false);

		for (int i = 0; i < mIconList.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(mIconList[i]);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_XY);
			mGuideViewList.add(iv);
		}

		WelcomePagerAdapter adapter = new WelcomePagerAdapter(mGuideViewList);

		mVpWelcome.setAdapter(adapter);

		mVpWelcome.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				if (arg0 == mIconList.length - 1) {// 到最后一张了

				} else if (arg0 == mIconList.length - 1) {

				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

				if (arg0 == mIconList.length - 1) {

//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					ivStartMain.setVisibility(View.VISIBLE);

					ivStartMain.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							startActivity(new Intent(WelcomeActivity.this,
									MainActivity.class));
							// 记录已经浏览过新手引导
							SessionSms.SetBoolean("is_guide_showed", true
							);
							finish();
						}
					});
				}else{
					ivStartMain.setVisibility(View.GONE);
				}
			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {

	}
}
