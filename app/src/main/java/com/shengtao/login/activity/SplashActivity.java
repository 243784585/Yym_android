package com.shengtao.login.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

import com.iapppay.sdk.main.IAppPay;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shengtao.R;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.Session;
import com.shengtao.utils.SessionSms;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * 闪屏-广告页面
 *
 */

public class SplashActivity extends Activity {

	private ImageView ivRoot;
	private Button btnSkip;
	private NoNetReceiver noNetReceiver;
	private String ADVERTISEMENT = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_splash);
		ivRoot = (ImageView) findViewById(R.id.splash_root);
//		btnSkip = (Button) findViewById(R.id.btn_skip);

		noNetReceiver= new NoNetReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		this.registerReceiver(noNetReceiver, filter);

//		String url = "http://m.etripbon.com";
//		ivRoot.loadUrl(url);
//		String url = "http://m.etripbon.com";
//		ImageLoader.getInstance().displayImage(url,ivRoot);
//		String url = "http://m.etripbon.com";
//		ImageLoader.getInstance().displayImage(url, ivRoot);

//		getAllBb();
		doBuss();
		//爱贝支付初始化
		IAppPay.init(this, IAppPay.PORTRAIT, Config.APP_ID_AIBEI);
		//清空session
		AsyncHttpTask.get(Config.HTTP_URL_HEAD + "user/rmbInfo", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if("7".equals(response.optString("code"))){
					Session.ClearSession();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(noNetReceiver);
		super.onDestroy();
	}

	private void doBuss(){
		boolean isGuide = SessionSms.GetBoolean("is_guide_showed",
				false);
		if(!isGuide){
			ivRoot.setImageDrawable(getResources().getDrawable(R.drawable.main_splash));
//			btnSkip.setVisibility(View.GONE);
		}else{
//			btnSkip.setVisibility(View.VISIBLE);
			//看情况打开
//			if(!NetStatusCheckUtil.isNetworkStatus(this)){
//				Toast.makeText(this,"请检查网络连接情况再登录",Toast.LENGTH_LONG).show();
////				ivRoot.setBackgroundResource(R.drawable.no_net_bg);
//				return;
//			}
//			getBanner();
//			ivRoot.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					try {
//						if (!ADVERTISEMENT.contains("http")) {
//							ADVERTISEMENT = "http://" + ADVERTISEMENT;
//						}
//						Uri uri = Uri.parse(ADVERTISEMENT);
//						Intent it = new Intent(Intent.ACTION_VIEW, uri);
//						startActivity(it);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
		}

		// 渐变动画
		AlphaAnimation animAlpha = new AlphaAnimation(1, 1);
		animAlpha.setDuration(3000);
		animAlpha.setFillAfter(true);


//		// 缩放动画
//		ScaleAnimation animScale = new ScaleAnimation(0,1.4f,0,1.4f);
//		animScale.setFillAfter(true);
//		animAlpha.setDuration(2000);

		// 动画集合
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(animAlpha);
		ivRoot.startAnimation(set);

		set.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 动画结束
				boolean isGuideShowed = SessionSms.GetBoolean("is_guide_showed",
						false);
				if (!isGuideShowed) {
					// 跳新手引导
					startActivity(new Intent(getApplicationContext(),
							WelcomeActivity.class));
				} else {
					// 跳主页面
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
				}
				finish();
			}
		});

//		btnSkip.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				boolean isGuideShowed = PrefUtils.getBoolean("is_guide_showed",
//						false, getApplicationContext());
//				if (!isGuideShowed) {
//					// 跳新手引导
//					startActivity(new Intent(getApplicationContext(),
//							WelcomeActivity.class));
//				} else {
//					// 跳主页面
//					startActivity(new Intent(getApplicationContext(),
//							MainActivity.class));//你看
//					//我猜是这里
//				}
//				finish();
//			}
//		});


//		ivRoot.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Uri uri = Uri.parse("http://m.etripbon.com");
//				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//				startActivity(intent);
//				finish();
//			}
//		});

	}


	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

//	/**
//	 * 获取广告地址
//	 */
//	private void getBanner() {
////        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
//		AsyncHttpTask.get(Config.HTTP_URL_HEAD + "general/get_banner", new RequestParams(), new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//				try {
//					String data = new String(responseBody, "UTF-8");
//					JSONObject jsonObject = new JSONObject(data);
//					JSONObject jsonInfo = jsonObject.getJSONObject("info");
//					JSONObject jsonTravel = jsonInfo.optJSONObject("banner_start");
//					String content = jsonTravel.getString("content");
//					ADVERTISEMENT = content;
//					ivRoot.setClickable(true);
//					String url = jsonTravel.getString("cover");
//					Log.e("url", url);
//					ImageLoader.getInstance().displayImage(url, ivRoot);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//			}
//		});
//	}

	//获取全局的帮币规则
//	private void getAllBb() {
//		AsyncHttpTask.get(Config.HTTP_URL_HEAD + "general/get_bb_type", new RequestParams(), new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//				try {
//					String data = new String(responseBody, "UTF-8");
//					JSONObject jsonObject = new JSONObject(data);
//					JSONArray array = jsonObject.getJSONArray("info");
//					if (array.length() > 0) {
//						for (int i = 0; i < array.length(); i++) {
//
//							JSONObject object = array.optJSONObject(i);
//							if("注册".equals(object.optString("typename"))){
//								Session.SetString("BBRegist", object.optString("typecode"));
//							}
//							if("评论游记".equals(object.optString("typename"))){
//								Session.SetString("BBCommentTravel",object.optString("typecode"));
//							}
//							if("发布游记".equals(object.optString("typename"))){
//								Session.SetString("BBPushTravel",object.optString("typecode"));
//							}
//							if("发布攻略".equals(object.optString("typename"))){
//								Session.SetString("BBPushGuide",object.optString("typecode"));
//							}
//							if("发布帖子".equals(object.optString("typename"))){
//								Session.SetString("BBPushPost",object.optString("typecode"));
//							}
//							if("回答问题".equals(object.optString("typename"))){
//								Session.SetString("BBQuestion",object.optString("typecode"));
//							}
//							if("旅游帮签到".equals(object.optString("typename"))){
//								Session.SetString("BBSign",object.optString("typecode"));
//							}
//							if("评论活动".equals(object.optString("typename"))){
//								Session.SetString("BBCommentAction", object.optString("typecode"));
//							}
//							if("回复帖子".equals(object.optString("typename"))){
//								Session.SetString("BBCommentPost",object.optString("typecode"));
//							}
//						}
//					}
//
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//			}
//		});
//	}

	public class NoNetReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Toast.makeText(context, intent.getAction(), 1).show();
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo activeInfo = manager.getActiveNetworkInfo();

			if(activeInfo!=null){
				doBuss();
			}
		}  //如果无网络连接activeInfo为null
	}



}
