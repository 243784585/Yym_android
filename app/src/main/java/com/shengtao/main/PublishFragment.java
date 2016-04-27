package com.shengtao.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;
import com.shengtao.R;
import com.shengtao.adapter.snache.NewPublishAdapter2;
import com.shengtao.baseview.BaseListFragment;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.NewPublishGoods;
import com.shengtao.domain.snache.NewPublishedGood;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.foundation.SubmitType;
import com.shengtao.snache.activity.PrizeDetailsActivity;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.StringUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新揭晓
 */

public class PublishFragment extends BaseListFragment implements OnClickListener {

    private View PublishMainView;
    private Button btnMyGroup;
    private Button btnLocalGroup;
    private Button btnRGroup;
    NewPublishGoods newGoods;
    private int count = 1;
    private ListView gvNewPublish;
    private List<NewPublishedGood> pList = new ArrayList();
    private NewPublishAdapter2 mNewPublishAdapter;
    private List datas;

    static PublishFragment newInstance(String s) {
        PublishFragment newFragment = new PublishFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtil.e("Position",prlv_details.getRefreshableView().getFirstVisiblePosition()+"");
            sendEmptyBackgroundMessage(MSG_BACK_REFERSH_LIST);
            if(prlv_details.getRefreshableView().getFirstVisiblePosition()>5){
//                prlv_details.getRefreshableView().setSelection(0);
            }
        };
    };

    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    Runnable refresh = new Runnable() {
        @Override
        public void run() {
            mNewPublishAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workThread = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                //进行添加到购物车的请求
                final int position = msg.what;
                final String id = msg.obj.toString();
                AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/newSingleOpenGoods", new RequestParams("id", id), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String result = response.toString();
                        if (TextUtils.isEmpty(result))
                            return;
                        if ("0".equals(response.optString("code"))) {
                            JSONObject info = response.optJSONObject("info");

                            pList.set(position, new NewPublishedGood(info.optJSONArray("list").optJSONObject(0)));
                            mUiHandler.post(refresh);
                        } else if ("3".equals(response.optString("code"))) {
                            sendMessageDelayed(Message.obtain(workThread,position,id),500);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        LogUtil.e("HttpRequestError(" + statusCode + "):", responseString);
                    }
                });
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PublishMainView = inflater.inflate(R.layout.fragment_publish, container, false);
        initViewData(PublishMainView);
        mNewPublishAdapter = new NewPublishAdapter2(getActivity(),pList);
        prlv_details.setAdapter(mNewPublishAdapter);
        return PublishMainView;
    }
//    @Override
//    protected void initViewData(View view) {
//        this.mTlLoading = (TipsLayoutNormal) PublishMainView.findViewById(R.id.tl_loading);
//        /**
//         * 获取列表
//         */
//        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
////        sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
//        super.initViewData(view);
//    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.gv_new_publish;
    }

    @Override
    protected void setAdapter() {
        if (pList.size() == 0){
            showEmptyView("暂无数据，请重新选择");
        }
        mNewPublishAdapter.notifyDataSetChanged();
    }

    @Override
    protected List getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        LogUtil.e("pageNum", mCurPageIndex+"");
        return new RequestParams("pageNum",mCurPageIndex);
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        return getRequestParam();
    }


    @Override
    protected void parseRequestListResult(String result) {
        if (StringUtil.isEmpty(result)) {
            return;
        }
        List results;
        String errMsg = null;
        try {
            JSONObject json = new JSONObject(result);
            newGoods = new NewPublishGoods(json);
            results = newGoods.getNewpublishedGoodList();

            //第二页
            if (results.size() > 0) {
                pList.addAll(results);
            } else {
//                ToastUtil.loadMoreTips(getActivity());
            }
        } catch (Exception e) {
            if (!TextUtils.isEmpty(errMsg))
                ToastUtil.makeText(getActivity(), errMsg);
            e.printStackTrace();
        }
    }

    @Override
    protected void parseRefreshRequestListResult(String result) {
        //下拉刷新和第一次请求
        L.i("TravelFragment", result);
        if (StringUtil.isEmpty(result)) {
            return;
        }
        SparseArray<PublishFragment.MyCountDownTimer> sa = mNewPublishAdapter.getIsCountdown();
        if(sa.size() > 0){
            for(int i=0,len=sa.size();i<len;i++){
                sa.valueAt(i).cancel();
            }
            sa.clear();
        }
        List results;
        try {
            JSONObject json = new JSONObject(result);
            newGoods = new NewPublishGoods(json);
            results = newGoods.getNewpublishedGoodList();
            pList.clear();
            pList.addAll(results);
            for (int i = 0, len = pList.size(); i < len; i++) {
                if ("1".equals(pList.get(i).getStatus())) {
                    sa.put(i, new MyCountDownTimer(Long.parseLong(pList.get(i).getOpen_time()) - Long.parseLong(newGoods.getTime()), 1, pList.get(i).getId(), i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "goods/newOpenGoods";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        NewPublishedGood item = mNewPublishAdapter.getItem(position-1);
        Intent intent = new Intent(getActivity(), PrizeDetailsActivity.class);
        intent.putExtra("banner", item.getGoods_headurl());
        intent.putExtra("singleGoodsId", item.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    private Handler workThread;

    public class MyCountDownTimer extends CountDownTimer {
        /**
         *
         * @param millisInFuture
         *            表示以毫秒为单位 倒计时的总数
         *
         *            例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         *            表示 间隔 多少微秒 调用一次 onTick 方法
         *
         *            例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */

        public TextView tv_time;
        private String id;
        public int position;

        public MyCountDownTimer(long millisInFuture, long countDownInterval,String id,int position) {
            super(millisInFuture, countDownInterval);
            this.id = id;
            this.position = position;
            start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(tv_time !=null) {
                int fen = (int) (millisUntilFinished/1000/60);
                int haomiao = (int)millisUntilFinished % 100;
                int miao = (int) ((millisUntilFinished - (fen * 60 * 1000)) / 1000);
                tv_time.setText(((fen>=10)?fen+"分":"0"+fen+"分") + ((miao>=10)?miao+"秒":"0"+miao+"秒")+((haomiao>=10)?haomiao:"0"+haomiao));
            }
        }

        @Override
        public void onFinish() {
            cancel();
            if(tv_time!=null){
                tv_time.setText("揭晓中");
            }
            workThread.obtainMessage(position,id).sendToTarget();
        }

    }
}

