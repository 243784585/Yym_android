package com.shengtao.baseview;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragmentActivity;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.LogUtil;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * 单个界面基类
 * Created by TT on 15/9/16.
 */
public abstract class BaseMyActionFragmentActivity<T extends BaseEnitity> extends BaseWorkerFragmentActivity {


    protected final static int MSG_UI_GET_DATA_FAILED = 0x104;

    protected final static int MSG_UI_GET_DATA_SUCCESS = 0x105;

    protected final static int MSG_UI_GEt_DATA_LOADING = 0x111;
    protected ITipLayout mTlLoading;
    //自定义ActionBar，标题栏
    private String tag = "tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResouceId());

        if (getType() == 0) {
            //初始化加载数据
            initData();
        } else {
//            initData();
            //
        }

    }

    protected void initData() {
        this.mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);

        /**
         * 获取列表
         */
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
        sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);

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
    }

    @Override
    public IToastBlock getmToastBlack() {
        return null;
    }


    /*
       0:初始化加载数据
       1:初始化不加载数据
     */
    public int getType() {
        return 0;
    }

    /**
     * @return
     */
    protected abstract RequestParams getRequestParam();


    /**
     * 获取当前ListActivity布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutResouceId();

    /**
     * 获取当前请求HTTP数据的URI
     *
     * @return
     */
    protected abstract String getUri();

    /**
     * 获取请求提交方式
     *
     * @return
     */
    protected abstract SubmitType getSubmitType();

    /**
     * 获取数据
     *
     * @param type
     */
    protected void getHttpResponseList(final int type) {
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
//        try {
            if (bytes == null) {

            }
//            String data = new String(bytes, charset);
//            LogUtil.d(data);
//            Message msg = new Message();
//            msg.obj = HttpErrorHelper.getRequestErrorReason(data);
//            msg.what = MSG_UI_GET_DATA_FAILED;
//            sendUiMessage(msg);
//            return;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * @param msg
     */
    @Override
    public void handleBackgroundMessage(Message msg) {
        switch (msg.what) {
//            case MSG_UI_GET_DATA_FAILED:
//                getHttpResponseList(MSG_UI_GET_DATA_FAILED);
//                break;
//            case MSG_UI_GET_DATA_SUCCESS:
//                getHttpResponseList(MSG_UI_GET_DATA_SUCCESS);
//                break;

            case MSG_UI_GEt_DATA_LOADING:
                getHttpResponseList(MSG_UI_GET_DATA_SUCCESS);
                LogUtil.e(tag, "请求成功");
                break;
        }
    }

    protected abstract void showUIData(Object obj);

    /**
     * 接收数据 显示
     *
     * @param msg
     */
    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_UI_GET_DATA_FAILED:
                //这里有Bug,如果页面不在一开始进行加载呢
                mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED);
                mTlLoading.hide();
                break;
            case MSG_UI_GET_DATA_SUCCESS:
                if(getType() == 0){
                    mTlLoading.hide();
                }
                showUIData(msg.obj);
                break;
        }
    }
}
