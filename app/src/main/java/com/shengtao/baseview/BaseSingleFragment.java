package com.shengtao.baseview;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragment;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.LogUtil;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public abstract class BaseSingleFragment<T extends BaseEnitity> extends
		BaseWorkerFragment implements OnItemClickListener,
		OnItemLongClickListener {

	protected final static int MSG_UI_GET_DATA_FAILED = 0x104;

	protected final static int MSG_UI_GET_DATA_SUCCESS = 0x105;

	protected final static int MSG_UI_GEt_DATA_LOADING = 0x111;
	/**
	 * 刷新加载的listview
	 */
	protected ITipLayout mTlLoading;
	protected LoadingDialog mLoadingDialog;


	/**
	 * 当前页面的序号
	 */
	protected int mCurPageIndex = 1;

	protected void initViewData(View view) {

		this.mTlLoading = (TipsLayoutNormal) view.findViewById(R.id.tl_loading);

		this.mTlLoading.setITipsLayoutListener(new ITipsLayoutListener() {
			@Override
			public void onTipLayoutClick(int btnId) {
				switch (btnId) {
					case R.id.layout_load_faile:
						mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
						sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
						break;
				}
			}
		});

		/**
		 * 获取列表
		 */
		this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
		sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
	}

	// *************************************************************************
	/**
	 * 【】(显示空页面)
	 *
	 * @param tips
	 */
	// *************************************************************************
	protected void showEmptyView(String tips) {
		mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_SUCCESS_NO_DATA, tips);
	}

	// *************************************************************************
	/**
	 * 【】(获取请求列表的请求参数)
	 *
	 * @return
	 */
	// *************************************************************************
	protected abstract RequestParams getRequestParam();

	/**
	 * 获取请求提交方式
	 *
	 * @return
	 */
	protected abstract SubmitType getSubmitType();


	/**
	 * 获取当前请求HTTP数据的URI
	 *
	 * @return
	 */
	protected abstract String getUri();


	protected void getList(final int type) {
		RequestParams request = null;
		request = getRequestParam();
		SubmitType submit = this.getSubmitType();

		if (submit == SubmitType.GET) {

			AsyncHttpTask.get(this.getUri(), request, new AsyncHttpResponseHandler() {
				//加载成功
				@Override
				public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {

					success(statusCode, motuHeaders, bytes, super.getCharset());

				}

				//获取数据失败
				@Override
				public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
					// 网络请求出错
					failure(statusCode, motuHeaders, bytes, getCharset());
				}
			});
		}else if (submit == SubmitType.POST){

			AsyncHttpTask.post(getUri(), request, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
					success(statusCode, headers, bytes, super.getCharset());
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
					failure(statusCode, headers, bytes, getCharset());
				}
			});
		}
	}

	private void success(int statusCode, Header[] motuHeaders, byte[] bytes, String charset) {
		try {
			String data = new String(bytes, charset);
			LogUtil.d(data);
			if (TextUtils.isEmpty(data))
				data = "";
			Message msg = new Message();
			msg.obj = data;
			msg.what = MSG_UI_GET_DATA_SUCCESS;
			sendUiMessage(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void failure(int statusCode, Header[] motuHeaders, byte[] bytes, String charset) {
		try {
			if (bytes == null) {

			}
			/*String data = new String(bytes, charset);
			LogUtil.d(data);*/
			Message msg = new Message();
			//msg.obj = HttpErrorHelper.getRequestErrorReason(data);
			msg.what = MSG_UI_GET_DATA_FAILED;
			sendUiMessage(msg);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void showUIData(Object obj);

	@Override
	public void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
			case MSG_UI_GEt_DATA_LOADING:
				getList(MSG_UI_GET_DATA_SUCCESS);
				break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		if (msg.obj == null) {
			mTlLoading.hide();
			return;
		}
		switch (msg.what) {
			case MSG_UI_GET_DATA_FAILED:
				//这里有Bug,如果页面不在一开始进行加载呢
				mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED);
				mTlLoading.hide();
				break;
			case MSG_UI_GET_DATA_SUCCESS:
				mTlLoading.hide();
				showUIData(msg.obj);
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}
}
