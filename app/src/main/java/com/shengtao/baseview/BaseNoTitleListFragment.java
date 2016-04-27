package com.shengtao.baseview;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shengtao.R;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

public abstract class BaseNoTitleListFragment<T extends BaseEnitity> extends BaseWorkerFragment implements OnItemClickListener, OnItemLongClickListener {

    protected final static int MSG_BACK_GET_LIST = 0x101;
    protected final static int MSG_BACK_LOAD_MORE_LIST = 0x102;
    protected final static int MSG_BACK_REFERSH_LIST = 0x103;

    protected final static int MSG_UI_GET_LIST_FAILED = 0x104;
    protected final static int MSG_UI_GET_LIST_SUCCESS = 0x105;
    protected final static int MSG_UI_LOAD_MORE_LIST_SUCCESS = 0x106;
    protected final static int MSG_UI_LOAD_MORE_LIST_FAILED = 0x107;
    protected final static int MSG_UI_REFERSH_LIST_SUCCESS = 0x108;
    protected final static int MSG_UI_REFERSH_LIST_FAILED = 0x109;

    /**
     * 刷新加载的listview
     */

    protected PullToRefreshListView prlv_details;
//	protected LoadMoreFtView mFootview;

    protected TipsLayoutNormal mTlLoading;

    protected BaseAdapter mAdapter;

    protected LoadingDialog mLoadingDialog;

    /**
     * 当前页面的序号
     */
    protected int mCurPageIndex = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_base_no_title_list, null);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        mTlLoading = (TipsLayoutNormal) view.findViewById(R.id.tl_loading);
        prlv_details = (PullToRefreshListView) view.findViewById(this.getPullToRefreshListViewResouceId());
        prlv_details.setMode(PullToRefreshBase.Mode.BOTH);

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
                mCurPageIndex = 1;
                sendEmptyBackgroundMessage(MSG_BACK_REFERSH_LIST);
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
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
        sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
    }

    /**
     * 获取下拉刷新控件资源ID
     *
     * @return
     */
    protected abstract int getPullToRefreshListViewResouceId();

    // *************************************************************************

    /**
     * 【】(设置是否可以刷新)
     *
     * @param able
     */
    // *************************************************************************
    protected void setRefreshAble(boolean able) {
        prlv_details.setPullToRefreshEnabled(able);
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
     * 【】(设置适配器)
     */
    // *************************************************************************
    protected abstract void setAdapter();

    // *************************************************************************

    /**
     * 【】(获取数据列表)
     *
     * @return
     */
    // *************************************************************************
    protected abstract List<T> getDataList();

    // *************************************************************************

    /**
     * 【】(获取请求列表的请求参数)
     *
     * @return
     */
    // *************************************************************************
    protected abstract RequestParams getRequestParam();

    // *************************************************************************

    /**
     * 【】(获取刷新的参数)
     *
     * @return
     */
    // *************************************************************************
    protected abstract RequestParams getRefreshRequestParam();

    // *************************************************************************

    /**
     * 【】(解析请求列表的结果)
     *
     * @param result
     */
    // *************************************************************************
    protected abstract void parseRequestListResult(String result);

    // *************************************************************************

    /**
     * 【】(解析刷新请求列表的结果)
     *
     * @param result
     */
    // *************************************************************************
    protected abstract void parseRefreshRequestListResult(String result);

    // *************************************************************************
    /**
     * 【】(获取列表)
     */
    // *************************************************************************

    /**
     * 获取当前请求HTTP数据的URI
     *
     * @return
     */
    protected abstract String getUri();

    protected void getList(final int type) {
        RequestParams request = null;
        if (type == MSG_BACK_REFERSH_LIST) {
            request = getRefreshRequestParam();
        } else {
            request = getRequestParam();
        }
        AsyncHttpTask.post(getUri(), request, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "UTF-8");
                    Message msg = new Message();
                    msg.obj = data;
                    if (type == MSG_BACK_GET_LIST) {
                        msg.what = MSG_UI_GET_LIST_SUCCESS;
                    } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                        msg.what = MSG_UI_LOAD_MORE_LIST_SUCCESS;
                    } else if (type == MSG_BACK_REFERSH_LIST) {
                        msg.what = MSG_UI_REFERSH_LIST_SUCCESS;
                    }
                    sendUiMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    Message msg = new Message();
                    if (type == MSG_BACK_GET_LIST) {
                        msg.what = MSG_UI_GET_LIST_FAILED;
                    } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                        msg.what = MSG_UI_LOAD_MORE_LIST_FAILED;
                    } else if (type == MSG_BACK_REFERSH_LIST) {
                        msg.what = MSG_UI_REFERSH_LIST_FAILED;
                    }
                    sendUiMessage(msg);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        switch (msg.what) {
            case MSG_BACK_GET_LIST:
                mCurPageIndex = 1;
                getList(MSG_BACK_GET_LIST);
                break;
            case MSG_BACK_LOAD_MORE_LIST:
                mCurPageIndex++;
                getList(MSG_BACK_LOAD_MORE_LIST);
                break;
            case MSG_BACK_REFERSH_LIST:
                getList(MSG_BACK_REFERSH_LIST);
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        if (msg.obj == null) {
            mTlLoading.hide();
        }
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
                mTlLoading.hide();
                parseRequestListResult(msg.obj.toString());
                prlv_details.onRefreshComplete();
                setAdapter();
                break;
            case MSG_UI_LOAD_MORE_LIST_FAILED:
                prlv_details.onRefreshComplete();
                break;
            case MSG_UI_REFERSH_LIST_SUCCESS:
                mTlLoading.hide();
                prlv_details.onRefreshComplete();
                parseRefreshRequestListResult(msg.obj.toString());

                if (getDataList().size() >= Config.DEFAULT_PAGE_SIZE
                        && mCurPageIndex == 1) {
                    mCurPageIndex++;
                }
                setAdapter();
                break;
            case MSG_UI_REFERSH_LIST_FAILED:
                prlv_details.onRefreshComplete();
//                ToastUtil.makeText(getActivity(),getResources().getString(R.string.common_text_refresh_failed));
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
