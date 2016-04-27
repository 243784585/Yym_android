package com.shengtao.snache.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseListActivity;
import com.shengtao.baseview.ITipsLayoutListener;
import com.shengtao.baseview.TipsLayoutNormal;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.CommonWinmessage;
import com.shengtao.domain.snache.OpenTimeComparator;
import com.shengtao.domain.snache.WinMsg;
import com.shengtao.domain.snache.Zeromessage;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/15.
 * Description:中奖消息
 */
public class MessageWinning extends BaseListActivity{

    private PullToRefreshListView lvMsgWin;
    private MsgWinningAdapter msgWinningAdapter;
    private ArrayList<CommonWinmessage> cList;
    private ArrayList<Zeromessage> zList;
    private ArrayList<Object> oList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        lvMsgWin = (PullToRefreshListView) findViewById(R.id.lv_msg_win);
    }

    //使上下拉操作无效
    @Override
    protected void initViewData() {
        this.mTlLoading = (TipsLayoutNormal) findViewById(R.id.tl_loading);
        this.prlv_details = (PullToRefreshListView) findViewById(this.getPullToRefreshListViewResouceId());
        this.prlv_details.setMode(PullToRefreshBase.Mode.BOTH);


        prlv_details.setOnItemClickListener(this);
        prlv_details.getRefreshableView().setOnItemLongClickListener(this);

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

    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.lv_msg_win;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_winning_message;
    }

    @Override
    protected Object getAvtionTitle() {
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(getResources().getColor(R.color.yym_common_red));
        textView.setText("中奖消息");
        return textView;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void setAdapter() {
        if (msgWinningAdapter == null) {
            // 在这里填充adapter
            msgWinningAdapter = new MsgWinningAdapter();
            super.prlv_details.setAdapter(msgWinningAdapter);
        }
        if (zList.size() == 0 && cList.size() == 0){
            showEmptyView("暂无数据，请重新选择");
        }
        msgWinningAdapter.notifyDataSetChanged();
    }

    @Override
    protected <T extends BaseEnitity> List<T> getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        return new RequestParams("type", "1");
    }

    @Override
    protected RequestParams getRefreshRequestParam() {
        return getRequestParam();
    }

    @Override
    protected void parseResult(String result, boolean isRefresh) {
        if (TextUtils.isEmpty(result)) {
            showEmptyView("暂时没有商品哦哦...");
            return;
        }
        //集合
        List<CommonWinmessage> comResults = new ArrayList<>();
        List<Zeromessage> zeroResults = new ArrayList<>();
//        if(cList==null){
//            //存储集合
//            cList = new ArrayList();
//        }
        cList = new ArrayList();
        zList = new ArrayList();
        oList = new ArrayList();
//        if(cList==null){
//            //存储集合
//            zList = new ArrayList();
//        }
        try {
            JSONObject json = new JSONObject(result);
            WinMsg data = new WinMsg(json);
            comResults = data.getCommsgList();
            zeroResults = data.getZeroList();
            if (isRefresh) {
                cList.clear();
                zList.clear();
            }
            if (zeroResults.size() > 0 || comResults.size() > 0) {
                zList.addAll(zeroResults);
                cList.addAll(comResults);
                oList.addAll(cList);
                oList.addAll(zList);

                //冒泡排序
                Collections.sort(oList, new Comparator() {
                    @Override
                    public int compare(Object t1, Object t2) {
                        return (int) (Long.parseLong(((OpenTimeComparator)t2).getOpen_time()) - Long.parseLong(((OpenTimeComparator)t1).getOpen_time()));
                    }
                });
                LogUtil.e("position", oList.size() + "");
            } else {
                ToastUtil.loadMoreTips(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD+"getMessage/getMessage";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent = new Intent(MessageWinning.this,MessageWinningDetails.class);
        if (msgWinningAdapter.getItem(position-1) instanceof Zeromessage) {
            Zeromessage zMessage = (Zeromessage) msgWinningAdapter.getItem(position-1);
            //传递给详情里面的是时间，名称，幸运号码，多少期，中奖宝物信息
            intent.putExtra("open_time", zMessage.getOpen_time());
            intent.putExtra("goods_current_num", zMessage.getGoods_current_num());
            intent.putExtra("zgoods_name", zMessage.getZgoods_name());
            intent.putExtra("lucky_id", zMessage.getLucky_id());
        } else {
            CommonWinmessage cMessage = (CommonWinmessage) msgWinningAdapter.getItem(position-1);
            intent.putExtra("open_time", cMessage.getOpen_time());
            intent.putExtra("goods_current_num", cMessage.getGoods_current_num());
            intent.putExtra("zgoods_name", cMessage.getGoods_name());
            intent.putExtra("lucky_id", cMessage.getLucky_id());
        }
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    class MsgWinningAdapter extends BaseAdapter {
        private Zeromessage zMessage;
        private CommonWinmessage cMessage;

        @Override
        public int getCount() {
            return oList.size();
        }

        @Override
        public Object getItem(int position) {
//            if (position < zList.size()) {
//                return zList.get(position);
//            } else {
//                return cList.get(position - zList.size());
//            }
            return oList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_msg_winning,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String i = null;
            if(getItem(position) instanceof Zeromessage){
                zMessage = (Zeromessage) getItem(position);
                i = DateTimeUtil.timestamp5Time(zMessage.getOpen_time());
            }else{
                cMessage = (CommonWinmessage) getItem(position);
                i = DateTimeUtil.timestamp5Time(cMessage.getOpen_time());
            }
//            if (position < zList.size()) {
//                zMessage = (Zeromessage) getItem(position);
//                i = DateTimeUtil.timestamp3Time(zMessage.getOpen_time());
//            } else {
//                cMessage = (CommonWinmessage) getItem(position);
//                i = DateTimeUtil.timestamp3Time(cMessage.getOpen_time());
//            }
            viewHolder.tv_goods_name.setText(Html.fromHtml("<font color='#ff4447'>【"+ i + "】恭喜您中奖了!</font>　您的信息与收货地址已发送给商家，如收货地址为空或有误请尽快联系在线客服！如无特殊情况，商家将在三个工作日内发货，敬请期待！感谢您对嗨抢一路的支持！"));
            return convertView;
        }
    }

    public class ViewHolder{
        private TextView tv_goods_name;

        public ViewHolder(View view){
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
        }
    }

}
