package com.shengtao.baseview;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.BaseWorkerFragment;
import com.shengtao.foundation.SubmitType;

import org.apache.http.Header;

import java.util.List;

public abstract class BaseGirdViewFragment<T extends BaseEnitity> extends BaseWorkerFragment implements OnItemClickListener, OnItemLongClickListener {

    protected final static int MSG_BACK_GET_LIST = 100;
    protected final static int MSG_BACK_LOAD_MORE_LIST = 101;
    protected final static int MSG_BACK_REFERSH_LIST = 102;

    protected final static int MSG_UI_GET_LIST_FAILED = 104;
    protected final static int MSG_UI_GET_LIST_SUCCESS = 105;
    protected final static int MSG_UI_LOAD_MORE_LIST_SUCCESS = 106;
    protected final static int MSG_UI_LOAD_MORE_LIST_FAILED = 107;
    protected final static int MSG_UI_REFERSH_LIST_SUCCESS = 108;
    protected final static int MSG_UI_REFERSH_LIST_FAILED = 109;

    /**
     * 刷新加载的listview
     */
    protected PullToRefreshGridView prlv_details;
    protected TipsLayoutNormal mTlLoading;
    protected BaseAdapter mAdapter;

    /**
     * 当前页面的序号
     */
    protected int mCurPageIndex = 1;

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

    protected void initViewData(View view) {

        this.mTlLoading = (TipsLayoutNormal) view.findViewById(R.id.tl_loading);
        this.prlv_details = (PullToRefreshGridView) view.findViewById(this.getPullToRefreshListViewResouceId());
        this.prlv_details.setMode(PullToRefreshBase.Mode.BOTH);


        prlv_details.setOnItemClickListener(this);
        prlv_details.getRefreshableView().setOnItemLongClickListener(this);
        //绑定刷新
        this.prlv_details.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                sendEmptyBackgroundMessage(MSG_BACK_REFERSH_LIST);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                Log.e("indexPage=", mCurPageIndex + "");
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
        mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_FAILED, tips);
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

    protected abstract SubmitType getSubmitType();

    protected void getList(final int type) {

        System.out.println("加载");
        RequestParams request = null;
        if (type == MSG_BACK_REFERSH_LIST) {
            request = getRefreshRequestParam();
        } else {
            request = getRequestParam();
        }
        System.out.println(request.toString());
        if (getSubmitType() == SubmitType.GET) {
            AsyncHttpTask.get(getUri(), request, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        String data = new String(bytes, "UTF-8");
                        System.out.println("onFinish(String data, H==========" + data);

                        Message msg = new Message();
                        msg.obj = data;
                        if (type == MSG_BACK_GET_LIST) {
                            msg.what = MSG_UI_GET_LIST_SUCCESS;
                        } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                            msg.what = MSG_UI_LOAD_MORE_LIST_SUCCESS;
                        } else if (type == MSG_BACK_REFERSH_LIST) {
                            msg.what = MSG_UI_REFERSH_LIST_SUCCESS;
                        } else if (type == MSG_BACK_GET_LIST) {
                            msg.what = MSG_UI_GET_LIST_FAILED;
                        } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                            msg.what = MSG_UI_LOAD_MORE_LIST_FAILED;
                        } else if (type == MSG_BACK_REFERSH_LIST) {
                            msg.what = MSG_UI_REFERSH_LIST_FAILED;
                        }
                        sendUiMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.e("fail", "fail");
//                    try {
//                        String data = new String(bytes, super.getCharset());
//                        LogUtil.d(data);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
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
        } else if (getSubmitType() == SubmitType.POST) {
            AsyncHttpTask.post(getUri(), request, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        String data = new String(bytes, "UTF-8");
                        System.out.println("onFinish(String data, H==========" + data);

                        Message msg = new Message();
                        msg.obj = data;
                        if (type == MSG_BACK_GET_LIST) {
                            msg.what = MSG_UI_GET_LIST_SUCCESS;
                        } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                            msg.what = MSG_UI_LOAD_MORE_LIST_SUCCESS;
                        } else if (type == MSG_BACK_REFERSH_LIST) {
                            msg.what = MSG_UI_REFERSH_LIST_SUCCESS;
                        } else if (type == MSG_BACK_GET_LIST) {
                            msg.what = MSG_UI_GET_LIST_FAILED;
                        } else if (type == MSG_BACK_LOAD_MORE_LIST) {
                            msg.what = MSG_UI_LOAD_MORE_LIST_FAILED;
                        } else if (type == MSG_BACK_REFERSH_LIST) {
                            msg.what = MSG_UI_REFERSH_LIST_FAILED;
                        }
                        sendUiMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.e("fail", "fail");
//                    try {
//                        String data = new String(bytes, super.getCharset());
//                        LogUtil.d(data);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
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


    }

    @Override
    public void handleBackgroundMessage(Message msg) {
//        if(){
//            return;
//        }
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
                mCurPageIndex = 1;
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
                parseRequestListResult(msg.obj.toString());
                prlv_details.onRefreshComplete();
                mTlLoading.hide();
                setAdapter();
                break;
            case MSG_UI_LOAD_MORE_LIST_FAILED:
                prlv_details.onRefreshComplete();
                break;
            case MSG_UI_REFERSH_LIST_SUCCESS:
                prlv_details.onRefreshComplete();
                parseRefreshRequestListResult(msg.obj.toString());
                mTlLoading.hide();
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
    }
}
