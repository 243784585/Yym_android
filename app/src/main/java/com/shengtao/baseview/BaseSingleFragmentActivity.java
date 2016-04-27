package com.shengtao.baseview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.shengtao.R;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragmentActivity;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.LYDBHeader;
import com.shengtao.utils.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 单个界面基类
 * Created by TT on 15/9/16.
 */
public abstract class BaseSingleFragmentActivity<T extends BaseEnitity> extends BaseWorkerFragmentActivity {


    protected final static int MSG_UI_GET_DATA_FAILED = 0x104;

    protected final static int MSG_UI_GET_DATA_SUCCESS = 0x105;

    protected final static int MSG_UI_GEt_DATA_LOADING = 0x111;
    protected TipsLayoutNormal mTlLoading;
    //自定义ActionBar，标题栏
    private LYDBHeader myHeader;

    protected CustomDialog dialog;

    private NoNetReceiver noNetReceiver;

    private NetworkInfo activeInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResouceId());
        initNoNet();
//        if(activeInfo!=null){
            if (getType() == 0) {
                //初始化加载数据
                initData();
            }
//        }
        setActionBar();
    }

    protected void initData() {

        this.mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);
        dialog = new CustomDialog(this);
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
        return new ToastBlack(this);
    }

    /**
     * 判断有无网络
     */
    public void initNoNet(){
        //判断有无网络
        noNetReceiver= new NoNetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(noNetReceiver, filter);
    }

    /**
     *检测网络状态
     */
    public class NoNetReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //Toast.makeText(context, intent.getAction(), 1).show();
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            activeInfo = manager.getActiveNetworkInfo();

            if(activeInfo ==null){
                showNoNetView("当前无网络哦");
            }
        }  //如果无网络连接activeInfo为null
    }

    // *************************************************************************
    /**
     * 【】(显示网络失败页面)
     *
     * @param tips
     */
    // *************************************************************************
    protected void showNoNetView(String tips) {
        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED_NET_ERROR, tips);
    }

    /**
     * 显示空数据提示页面
     *
     * @param tips
     */
    protected void showEmptyView(String tips) {
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_SUCCESS_NO_DATA, tips);
    }

    /**
     * 设置标题栏
     */
    protected void setActionBar() {

        if (getHeaderType() == 1) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("left", "left");
            hashMap.put("center", this.getAvtionTitle());
            myHeader = (LYDBHeader) findViewById(R.id.header);

            //设置ActionBar右侧事件
            if (getHeaderRight() != null) {
                hashMap.put("right", getHeaderRight());
                myHeader.headerRight.setOnClickListener(this);
            }
            myHeader.setHeaderView(this, hashMap);
        }
    }

    /**
       0:初始化加载数据
       1:初始化不加载数据
     */
    public int getType() {
        return 0;
    }

    /**
     * 设置自定义ActionBar的描述
     *
     * @return
     */
    protected abstract String getAvtionTitle();


    public Object getHeaderRight() {
        return null;
    }

    /**
     * 重写并更改返回值为1,则设置标题栏
     * Header类型
     *
     * @ 1:LydbHeader
     * 0:自定义
     */

    public int getHeaderType() {
        return 0;
    }


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
        RequestParams request = getRequestParams();

        SubmitType submit = this.getSubmitType();

        if (submit == SubmitType.GET) {

            Log.e("getUri", getUri());
            AsyncHttpTask.get(getUri(), request, new AsyncHttpResponseHandler() {
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
        } else if (submit == SubmitType.POST) {

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

    protected abstract RequestParams getRequestParams();

    private void success(int statusCode, Header[] motuHeaders, byte[] bytes, String charset) {
        try {
            String data = new String(bytes, charset);
            LogUtil.d(data);
            if (TextUtils.isEmpty(data))
                data = "";
            Message msg = new Message();
            msg.obj = data;
            msg.what = MSG_UI_GET_DATA_SUCCESS;
            sendUiMessageDelayed(msg, 100);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void failure(int statusCode, Header[] motuHeaders, byte[] bytes, String charset) {
        if (bytes == null) {

        }
//            String data = new String(bytes, charset);
        Message msg = new Message();
//            msg.obj = HttpErrorHelper.getRequestErrorReason(data);
        msg.what = MSG_UI_GET_DATA_FAILED;
        sendUiMessage(msg);
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
//                mTlLoading.show(TipsLayout.TIPS_STATUS_LOAD_FAILED);
                if(dialog != null)
                    dialog.dismiss();
                mTlLoading.hide();
                break;
            case MSG_UI_GET_DATA_SUCCESS:
                showUIData(msg.obj);
                if(getClasName() == null && mTlLoading!=null){
                    mTlLoading.hide();
                }
                break;
        }
    }

    public String getClasName(){

        return null;
    }
    /**
     * 获取数据
     *
     * @param request
     */
    protected void getHttpResponseList2(RequestParams request) {
//        request = getRequestParams();
        if (mTlLoading == null) {
            mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);
        }
        SubmitType submit = this.getSubmitType();
        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
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
        } else if (submit == SubmitType.POST) {

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


    /**
     * 获取数据
     *
     * @param request
     */
    protected void getHttpResponseList3(RequestParams request) {
//        RequestParams request = getRequestParams();

        SubmitType submit = this.getSubmitType();
        if (getType() == 0) {
            mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
        }
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
        } else if (submit == SubmitType.POST) {

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

    @Override
    protected void onDestroy() {
        unregisterReceiver(noNetReceiver);
        super.onDestroy();
    }
}
