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
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragmentActivity;
import com.shengtao.foundation.HttpErrorHelper;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.LYDBHeader;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 19:51
 * @package： com.motu.baseui
 * @description： 对BaseListActivity.java类的功能描述
 */
public abstract class BaseListActivity extends BaseWorkerFragmentActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    protected final String DEFAULT_ERROR_MSG = "请求数据异常";
    protected final static int MSG_BACK_GET_LIST = 0x101;                   //初始化
    protected final static int MSG_BACK_LOAD_MORE_LIST = 0x102;             //加载更多
    protected final static int MSG_BACK_REFERSH_LIST = 0x103;               //刷新

    protected final static int MSG_UI_GET_LIST_FAILED = 0x104;              //初始化失败
    protected final static int MSG_UI_GET_LIST_SUCCESS = 0x105;             //初始化成功
    protected final static int MSG_UI_LOAD_MORE_LIST_SUCCESS = 0x106;       //加载更多失败
    protected final static int MSG_UI_LOAD_MORE_LIST_FAILED = 0x107;        //加载更多成功
    /**
     * 刷新失败
     */
    protected final static int MSG_UI_REFERSH_LIST_SUCCESS = 0x108;         //刷新失败
    /**
     * 刷新成功
     */
    protected final static int MSG_UI_REFERSH_LIST_FAILED = 0x109;          //刷新成功

    private NetworkInfo activeInfo;
    private NoNetReceiver noNetReceiver;

    //自定义ActionBar，标题栏
    private LYDBHeader myHeader = null;
    protected TextView mTvTitle;
    protected PullToRefreshListView prlv_details;
    protected ITipLayout mTlLoading;
    protected BaseAdapter mAdapter;


    /**
     * 当前页面的序号
     */
    protected int mCurPageIndex = 1;
    private boolean net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutResouceId());
//        initNoNet();
        this.initViewData();
        this.setSetting();

        if (getHeaderType() == 1) {
            this.setActionBar();
        }
    }

    protected void setSetting() {
        ILoadingLayout startLabels = prlv_details.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = prlv_details.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    protected void initViewData() {

        this.mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);
        this.prlv_details = (PullToRefreshListView) findViewById(this.getPullToRefreshListViewResouceId());
        this.prlv_details.setMode(PullToRefreshBase.Mode.BOTH);


        prlv_details.setOnItemClickListener(this);
        prlv_details.getRefreshableView().setOnItemLongClickListener(this);
        //绑定刷新
        this.prlv_details.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /**
             * 下拉刷新
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                sendEmptyBackgroundMessage(MSG_BACK_REFERSH_LIST);
//                mAdapter.notifyDataSetChanged();
            }

            /**
             * 下拉加载更多
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                sendEmptyBackgroundMessage(MSG_BACK_LOAD_MORE_LIST);
            }
        });

        this.mTlLoading.setITipsLayoutListener(new ITipsLayoutListener() {
            @Override
            public void onTipLayoutClick(int btnId) {
                switch (btnId) {
                    case R.id.layout_load_faile:
                        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
                        sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
                        break;
                }
            }
        });

        /**
         * 获取列表
         */
//        if(net){
            this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
            sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
//        }else{
//            showNoNetView("当前无网络哦");
//        }
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

            if(activeInfo !=null){
                net = true;
            }else{
                net = false;
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
        this.mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);
        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED_NET_ERROR, tips);
    }

    /**
     * @return 1:MotuHeader
     * 0:自定义
     */
    public int getHeaderType() {
        return 1;
    }

    /**
     * 获取下拉刷新控件资源ID
     *
     * @return
     */
    protected abstract int getPullToRefreshListViewResouceId();

    /**
     * 设置刷新控件的项长按事件
     */
    protected void setOnItemLongClickListener() {

        this.prlv_details.getRefreshableView().setOnItemLongClickListener(this);

    }


    /**
     * 设置刷新按钮的点击事件
     */
    protected void setOnItemClickListener() {

        this.prlv_details.setOnItemClickListener(this);
    }

    /**
     * 获取当前ListActivity布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutResouceId();

    /**
     * 显示空数据提示页面
     *
     * @param tips
     */
    protected void showEmptyView(String tips) {
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_SUCCESS_NO_DATA, tips);
    }

    /**
     * 返回上一页
     *
     * @param v
     */
    public void onBack(View v) {
        super.finishAnimationActivity();
    }

    /**
     *
     */
    private void setActionBar() {
        try {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("left", "left");
            hashMap.put("center", this.getAvtionTitle());
            myHeader = (LYDBHeader) findViewById(R.id.header);
            //设置ActionBar右侧事件
            if (getHeaderRight() != null) {
                hashMap.put("right", getHeaderRight());
            }
            myHeader.setHeaderView(this, hashMap);
            myHeader.headerRight.setOnClickListener(this);
        } catch (Exception e) {

        }
    }

    /**
     * @return
     */
    protected Object getHeaderRight() {
        return null; //你重写这个方法，就可以设置你的按钮资源
    }

    /**
     * 设置自定义ActionBar的描述
     *
     * @return
     */
    protected abstract Object getAvtionTitle();

    /**
     *
     */
    protected abstract void setAdapter();

    /**
     * @return
     */
    protected abstract <T extends BaseEnitity> List<T> getDataList();


    /**
     * @return
     */
    protected abstract RequestParams getRequestParam();

    /**
     * @return
     */
    protected abstract RequestParams getRefreshRequestParam();

    /**
     * @param result
     */
    protected void parseRequestListResult(String result) {
        parseResult(result, false);
    }

    /**
     * @param result
     */
    protected void parseRefreshRequestListResult(String result) {
        parseResult(result, true);
    }

    /**
     * 格式化结果
     *
     * @param result
     * @param isRefresh
     */
    protected abstract void parseResult(String result, boolean isRefresh);

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
    protected void getHttpResponseList1(final int type) {
        RequestParams request = null;
        if (type == MSG_BACK_REFERSH_LIST) {
            request = getRefreshRequestParam();
        } else {
            request = getRequestParam();
        }

        AsyncHttpTask.get(this.getUri(), request, new AsyncHttpResponseHandler() {
            //加载成功
            @Override
            public void onSuccess(int statusCode, Header[] yymHeaders, byte[] bytes) {
                try {
                    String data = new String(bytes, super.getCharset());
                    LogUtil.d(data);
                    if (TextUtils.isEmpty(data))
                        data = "";
                    Message msg = new Message();
                    msg.obj = data;
                    if (type == MSG_BACK_GET_LIST) {
                        msg.what = MSG_UI_GET_LIST_SUCCESS;
                    } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                        msg.what = MSG_UI_LOAD_MORE_LIST_SUCCESS;
                    } else if (type == MSG_BACK_REFERSH_LIST) {
                        msg.what = MSG_UI_REFERSH_LIST_SUCCESS;
                    }
                    sendUiMessageDelayed(msg, 100);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //获取数据失败
            @Override
            public void onFailure(int statusCode, Header[] yymHeaders, byte[] bytes, Throwable throwable) {
                // 网络请求出错

//                try {
//                    String data = new String(bytes, super.getCharset());
//                    LogUtil.d(data);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                try {
                    Message msg = new Message();
                    if (type == MSG_BACK_GET_LIST) {
                        msg.what = MSG_UI_GET_LIST_FAILED;
                    } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                        msg.what = MSG_UI_LOAD_MORE_LIST_FAILED;
                    } else if (type == MSG_BACK_REFERSH_LIST) {
                        msg.what = MSG_UI_REFERSH_LIST_FAILED;
                    }
                    if (bytes == null) {
                        msg.obj = DEFAULT_ERROR_MSG;
                    } else {
                        String data = new String(bytes, super.getCharset());
                        LogUtil.d(data);

                        msg.obj = HttpErrorHelper.getRequestErrorReason(data);
                    }
                    sendUiMessage(msg);
                    return;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void success(final int type, byte[] bytes, String charSet) {
        try {
            String data = new String(bytes, charSet);
            LogUtil.d(data);
            if (TextUtils.isEmpty(data))
                data = "";
            Message msg = new Message();
            msg.obj = data;
            if (type == MSG_BACK_GET_LIST) {
                msg.what = MSG_UI_GET_LIST_SUCCESS;
            } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                msg.what = MSG_UI_LOAD_MORE_LIST_SUCCESS;
            } else if (type == MSG_BACK_REFERSH_LIST) {
                msg.what = MSG_UI_REFERSH_LIST_SUCCESS;
            }
            //sendUiMessageDelayed(msg, 100);
            sendUiMessage(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void failure(final int type, byte[] bytes, String charSet) {

        try {
            Message msg = new Message();
            if (type == MSG_BACK_GET_LIST) {
                msg.what = MSG_UI_GET_LIST_FAILED;
            } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                msg.what = MSG_UI_LOAD_MORE_LIST_FAILED;
            } else if (type == MSG_BACK_REFERSH_LIST) {
                msg.what = MSG_UI_REFERSH_LIST_FAILED;
            }
            if (bytes == null) {
                msg.obj = DEFAULT_ERROR_MSG;
            } else {
                String data = new String(bytes, charSet);
                LogUtil.d(data);
                msg.obj = HttpErrorHelper.getRequestErrorReason(data);
            }
            sendUiMessage(msg);
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void getHttpResponseList(final int type) {
        SubmitType submit = this.getSubmitType();
        RequestParams request = null;
        if (type == MSG_BACK_REFERSH_LIST) {
            request = getRefreshRequestParam();
        } else {
            request = getRequestParam();
        }
        Log.e("geturi", this.getUri());
        if (submit == SubmitType.GET) {
            //
            AsyncHttpTask.get(this.getUri(), request, new AsyncHttpResponseHandler() {
                //加载成功
                @Override
                public void onSuccess(int statusCode, Header[] yymHeaders, byte[] bytes) {
                    success(type, bytes, super.getCharset());
                }

                //获取数据失败
                @Override
                public void onFailure(int statusCode, Header[] yymHeaders, byte[] bytes, Throwable throwable) {
                    failure(type, bytes, super.getCharset());
                }
            });
        }
        if (submit == SubmitType.POST) {
            //
            AsyncHttpTask.post(this.getUri(), request, new AsyncHttpResponseHandler() {
                //加载成功
                @Override
                public void onSuccess(int statusCode, Header[] yymHeaders, byte[] bytes) {
                    success(type, bytes, super.getCharset());
                }

                //获取数据失败
                @Override
                public void onFailure(int statusCode, Header[] yymHeaders, byte[] bytes, Throwable throwable) {
                    failure(type, bytes, super.getCharset());
                }
            });
        }

    }

    /**
     * @param msg
     */
    @Override
    public void handleBackgroundMessage(Message msg) {
        switch (msg.what) {
            case MSG_BACK_GET_LIST:
                mCurPageIndex = 1;
                getHttpResponseList(MSG_BACK_GET_LIST);
                break;
            case MSG_BACK_LOAD_MORE_LIST:
                ++mCurPageIndex;
                getHttpResponseList(MSG_BACK_LOAD_MORE_LIST);
                break;
            case MSG_BACK_REFERSH_LIST:
                mCurPageIndex = 1;
                getHttpResponseList(MSG_BACK_REFERSH_LIST);
                break;
        }
    }


    /**
     * 获取网络数据到U线程
     *
     * @param msg
     */
    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_UI_GET_LIST_SUCCESS:
                parseRequestListResult(msg.obj.toString());
                mTlLoading.hide();
                setAdapter();
                break;
            case MSG_UI_GET_LIST_FAILED:
                mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED);
                break;
            case MSG_UI_LOAD_MORE_LIST_SUCCESS:
                parseRequestListResult(msg.obj.toString());
                prlv_details.onRefreshComplete();
                setAdapter();
                break;
            case MSG_UI_LOAD_MORE_LIST_FAILED:
                prlv_details.onRefreshComplete();
                break;
            case MSG_UI_REFERSH_LIST_SUCCESS:
                prlv_details.onRefreshComplete();
                parseRefreshRequestListResult(msg.obj.toString());

               /* if (getDataList().size() >= Config.DEFAULT_PAGE_SIZE
                        && mCurPageIndex == 1) {
                    mCurPageIndex++;
                }*/
                setAdapter();
                break;
            case MSG_UI_REFERSH_LIST_FAILED:
                prlv_details.onRefreshComplete();
                showToast(R.string.common_text_refresh_failed);
                break;
        }
    }

    /**
     * 获取自定义的Toast
     *
     * @return
     */
    @Override
    public IToastBlock getmToastBlack() {
        return new ToastBlack(this);
    }

//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(noNetReceiver);
//        super.onDestroy();
//    }
}
