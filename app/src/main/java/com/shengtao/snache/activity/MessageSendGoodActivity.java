package com.shengtao.snache.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseListActivity;
import com.shengtao.baseview.ITipsLayoutListener;
import com.shengtao.baseview.TipsLayoutNormal;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.Commonmessage;
import com.shengtao.domain.snache.OpenTimeComparator;
import com.shengtao.domain.snache.SendMsg;
import com.shengtao.domain.snache.Zeromessage;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseEnitity;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/15.
 * Description:发货消息
 */
public class MessageSendGoodActivity extends BaseListActivity {

    private PullToRefreshListView lvMsgSendGood;
    private ArrayList<OpenTimeComparator> sendGoodsMsg = new ArrayList<>();
    private MsgSendGoodAdapter mMsgSendGoodAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
//        lvMsgSendGood = (PullToRefreshListView) findViewById(R.id.lv_msg_sendgood);
//        lvMsgSendGood.setAdapter(new MsgSendGoodAdapter());
    }


    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void onResume() {
        if(sendGoodsMsg != null)sendGoodsMsg.clear();
        sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
        super.onResume();
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
        this.mTlLoading.show(TipsLayoutNormal.TIPS_STATUS_LOAD_ING);
//        sendEmptyBackgroundMessage(MSG_BACK_GET_LIST);
    }


    @Override
    protected int getPullToRefreshListViewResouceId() {
        return R.id.lv_msg_sendgood;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_send_good_msg;
    }

    @Override
    protected Object getAvtionTitle() {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.yym_common_red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText("发货消息");
        return textView;
    }

    @Override
    protected void setAdapter() {
        if (mMsgSendGoodAdapter == null) {
            // 在这里填充adapter
            mMsgSendGoodAdapter = new MsgSendGoodAdapter();
            super.prlv_details.setAdapter(mMsgSendGoodAdapter);
//            super.prlv_details.setMode(null);
        }
        if (sendGoodsMsg.size() == 0){
            showEmptyView("暂无数据，请重新选择");
        }
        mMsgSendGoodAdapter.notifyDataSetChanged();
    }

    @Override
    protected <T extends BaseEnitity> List<T> getDataList() {
        return null;
    }

    @Override
    protected RequestParams getRequestParam() {
        return new RequestParams("type", "2");
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
        LogUtil.e("heihei",result);
        //集合
//        List<Commonmessage> comResults = new ArrayList<>();
//        List<Zeromessage> zeroResults = new ArrayList<>();
//        cList = new ArrayList();
//        zList = new ArrayList();

        try {
            JSONObject json = new JSONObject(result);
            SendMsg data = new SendMsg(json);
//            comResults = data.getCommsgList();
//            zeroResults = data.getZeroList();
            if (isRefresh) {
//                zList.clear();
//                cList.clear();
                sendGoodsMsg.clear();
            }
            if (data.getZeroList().size() > 0 || data.getCommsgList().size() > 0) {
//                zList.addAll(zeroResults);
//                cList.addAll(comResults);
                sendGoodsMsg.addAll(data.getCommsgList());
                sendGoodsMsg.addAll(data.getZeroList());
            } else {
                ToastUtil.loadMoreTips(this);
            }
            Collections.sort(sendGoodsMsg, new Comparator<OpenTimeComparator>() {
                @Override
                public int compare(OpenTimeComparator t1, OpenTimeComparator t2) {
                    return (int) (Long.parseLong(t2.getOpen_time()) - Long.parseLong(t1.getOpen_time()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEAD + "getMessage/getMessage";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    class MsgSendGoodAdapter extends BaseAdapter {

        private Zeromessage zMessage;
        private Commonmessage cMessage;

        @Override
        public int getCount() {
            return sendGoodsMsg.size();
        }

        @Override
        public Object getItem(int position) {
            /*if (position < zList.size()) {
                return zList.get(position);
            } else {
                return cList.get(position - zList.size());
            }*/
            return sendGoodsMsg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_msg_sendgood, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (getItem(position) instanceof Zeromessage) {
                zMessage = (Zeromessage) getItem(position);

//                String i = DateTimeUtil.timestamp3Time(zMessage.getOpen_time());
//                String str = "["+i+"]" + "  您的宝物" +"(第"+ zMessage.getGoods_current_num() + "期)" + (zMessage.getZgoods_name()+"已发货，请收到宝物后点击确认收货并晒单");
//                SpannableStringBuilder style=new SpannableStringBuilder(str);
//                //SpannableStringBuilder实现CharSequence接口
//                style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
//                viewHolder.tv_title.setText(style);
                //判断是否晒单，同时显示文字
                viewHolder.btn_show.setText("0".equals(zMessage.getShare()) ? "晒单" : "已晒单");
                String status = zMessage.getStatus();
                //3是显示确认收货，不能点击晒带4不能点击收货可以点击晒单状态5不显示收货
                switch (status) {
                    case "3":
                        String str = "<font color='gray'>["+DateTimeUtil.timestamp5Time(zMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + zMessage.getGoods_current_num() + "期) " + (zMessage.getZgoods_name()+"已发货，请收到宝物后点击确认收货并晒单");
//                        SpannableStringBuilder style=new SpannableStringBuilder(str);
                        //SpannableStringBuilder实现CharSequence接口
//                        style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
                        viewHolder.tv_title.setText(Html.fromHtml(str));
                        viewHolder.btn_confirm_receivegood.setText("确认收货");
                        break;
                    case "4":
                        viewHolder.btn_confirm_receivegood.setTextColor(getResources().getColor(R.color.common_black));
                        viewHolder.btn_confirm_receivegood.setText("已确认收货");

                        String str1 = "<font color='gray'>["+DateTimeUtil.timestamp5Time(zMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + zMessage.getGoods_current_num() + "期) " + (zMessage.getZgoods_name()+" 已收货，请晒单");
//                        SpannableStringBuilder style1=new SpannableStringBuilder(str1);
                        //SpannableStringBuilder实现CharSequence接口
//                        style1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tv_title.setText(Html.fromHtml(str1));
                        break;
                    case "5":
                        viewHolder.btn_confirm_receivegood.setVisibility(View.GONE);

                        String str5 = "<font color='gray'>["+DateTimeUtil.timestamp5Time(zMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + zMessage.getGoods_current_num() + "期) " + (zMessage.getZgoods_name()+" 已收货，请晒单");
//                        SpannableStringBuilder style5=new SpannableStringBuilder(str5);
                        //SpannableStringBuilder实现CharSequence接口
//                        style5.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tv_title.setText(Html.fromHtml(str5));
                        break;
                }

            }else {
                cMessage = (Commonmessage) getItem(position);

                //判断是否晒单，同时显示文字
                viewHolder.btn_show.setText("0".equals(cMessage.getShare()) ? "晒单" : "已晒单");
                String status = cMessage.getStatus();
                //3是显示确认收货，不能点击晒带4不能点击收货可以点击晒单状态5不显示收货
                switch (status) {
                    case "3":
                        String str = "<font color='gray'>["+DateTimeUtil.timestamp5Time(cMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + cMessage.getGoods_current_num() + "期) " + (cMessage.getGoods_name()+"已发货，请收到宝物后点击确认收货并晒单");
//                        SpannableStringBuilder style=new SpannableStringBuilder(str);
                        //SpannableStringBuilder实现CharSequence接口
//                        style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
                        viewHolder.tv_title.setText(Html.fromHtml(str));
                        viewHolder.btn_confirm_receivegood.setText("确认收货");
                        break;
                    case "4":
                        viewHolder.btn_confirm_receivegood.setTextColor(getResources().getColor(R.color.common_black));
                        viewHolder.btn_confirm_receivegood.setText("已确认收货");

                        String str1 = "<font color='gray'>["+DateTimeUtil.timestamp5Time(cMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + cMessage.getGoods_current_num() + "期) " + (cMessage.getGoods_name()+" 已收货，请晒单");
//                        SpannableStringBuilder style1=new SpannableStringBuilder(str1);
                        //SpannableStringBuilder实现CharSequence接口
//                        style1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tv_title.setText(Html.fromHtml(str1));

                        break;
                    case "5":
                        viewHolder.btn_confirm_receivegood.setVisibility(View.GONE);

                        String str5 = "<font color='gray'>["+DateTimeUtil.timestamp5Time(cMessage.getOpen_time())+"]</font>" + "  您的宝物 (第" + cMessage.getGoods_current_num() + "期) " + (cMessage.getGoods_name()+" 已收货，请晒单");
//                        SpannableStringBuilder style5=new SpannableStringBuilder(str5);
                        //SpannableStringBuilder实现CharSequence接口
//                        style5.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tv_title.setText(Html.fromHtml(str5));
                        break;
                }
            }

            //进入晒单页面
            viewHolder.btn_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MessageSendGoodActivity.this, ShowListActivity.class);
                    String status = null;
                    String share = null;
                    if (mMsgSendGoodAdapter.getItem(position) instanceof Zeromessage) {
                        Zeromessage item = (Zeromessage) mMsgSendGoodAdapter.getItem(position);
                        intent.putExtra("singlegoodsid", item.getId());
                        intent.putExtra("isZero",true);
                        status = item.getStatus();
                        share = item.getShare();
                    } else {
                        Commonmessage item = (Commonmessage) mMsgSendGoodAdapter.getItem(position);
                        intent.putExtra("singlegoodsid", item.getId());
                        intent.putExtra("isZero",false);
                        status = item.getStatus();
                        share = item.getShare();
                    }
                    if("4".equals(status)){
                        if("0".equals(share)) {
                            startActivity(intent);
                        }else{
                            ToastUtil.showTextToast("亲!已经晒过单了哟");
                        }
                    }else{
                        ToastUtil.showTextToast("请确认收货后再来晒单页面哦~");
                    }
                }
            });

            //点击收货
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.btn_confirm_receivegood.setOnClickListener(new View.OnClickListener() {

                private Commonmessage citem;
                private Zeromessage zitem;

                @Override
                public void onClick(View v) {
                    String url = Config.HTTP_URL_HEAD + "getMessage/affirmSend";
                    RequestParams requestParams = new RequestParams();
                    if (mMsgSendGoodAdapter.getItem(position) instanceof Zeromessage) {
                        zitem = (Zeromessage) mMsgSendGoodAdapter.getItem(position);
                        if("3".equals(zitem.getStatus())){
                            requestParams.put("id", zitem.getId());

                            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                                //加载成功
                                @Override
                                public void onSuccess(int statusCode, Header[] LydbHeaders, byte[] bytes) {
                                    try {
                                        String data = new String(bytes, super.getCharset());
                                        LogUtil.d(data);
                                        JSONObject jsonObject = new JSONObject(data.toString());
                                        String code = jsonObject.optString("code");
                                        if ("0".equals(code)) {
                                            //收货成功，
                                            // 1。title文字变化 2.可晒单
                                            ToastUtil.showTextToast("收货成功了哦~");
                                            finalViewHolder.btn_confirm_receivegood.setText("已确认收货");
                                            zitem.setStatus("4");
//                                    finalViewHolder.btn_show.setEnabled(true);

                                            finalViewHolder.btn_confirm_receivegood.setTextColor(getResources().getColor(R.color.common_black));

                                            String str = "<font color='gray'>[" + DateTimeUtil.timestamp5Time(zitem.getOpen_time()) + "]</font>" + "  您的宝物 (第" + zitem.getGoods_current_num() + "期) " + (zitem.getZgoods_name() + " 已收货，请晒单");
//                                            SpannableStringBuilder style = new SpannableStringBuilder(str);
                                            //SpannableStringBuilder实现CharSequence接口
//                                            style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            finalViewHolder.tv_title.setText(Html.fromHtml(str));

                                        } else {
                                            ToastUtil.showTextToast("收货失败啦,请刷新过再试~");
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //获取数据失败
                                @Override
                                public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                                    // 网络请求出错
                                    return;
                                }
                            });
                        }

                    } else {
                        citem = (Commonmessage) mMsgSendGoodAdapter.getItem(position);
                        if("3".equals(citem.getStatus())){
                            requestParams.put("id", citem.getId());

                            AsyncHttpTask.post(url, requestParams, new AsyncHttpResponseHandler() {
                                //加载成功
                                @Override
                                public void onSuccess(int statusCode, Header[] LydbHeaders, byte[] bytes) {
                                    try {
                                        String data = new String(bytes, super.getCharset());
                                        LogUtil.d(data);
                                        JSONObject jsonObject = new JSONObject(data.toString());
                                        String code = jsonObject.optString("code");
                                        if ("0".equals(code)) {
                                            //收货成功
                                            // 1。title文字变化 2.可晒单
                                            ToastUtil.showTextToast("收货成功了哦~");
                                            finalViewHolder.btn_confirm_receivegood.setText("已确认收货");
                                            citem.setStatus("4");
//                                    finalViewHolder.btn_show.setEnabled(true);

                                            finalViewHolder.btn_confirm_receivegood.setTextColor(getResources().getColor(R.color.common_black));

                                            String str = "<font color='gray'>[" + DateTimeUtil.timestamp5Time(citem.getOpen_time()) + "]</font>" + "  您的宝物！第" + citem.getGoods_current_num() + "期 " + (citem.getGoods_name() + " 已收货，请晒单");
//                                            SpannableStringBuilder style = new SpannableStringBuilder(str);
                                            //SpannableStringBuilder实现CharSequence接口
//                                            style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                                            finalViewHolder.tv_title.setText(style);
                                            finalViewHolder.tv_title.setText(Html.fromHtml(str));

                                        } else {
                                            ToastUtil.showTextToast("收货失败啦,请刷新过再试~");
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //获取数据失败
                                @Override
                                public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                                    // 网络请求出错
                                    return;
                                }
                            });
                        }
                    }
                }
            });
            return convertView;
        }
    }

    public class ViewHolder {
        private TextView btn_confirm_receivegood;//收货
        private TextView btn_show;//晒单
        private TextView tv_title;//标题

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            btn_confirm_receivegood = (TextView) view.findViewById(R.id.btn_confirm_receivegood);
            btn_show = (TextView) view.findViewById(R.id.btn_show);
        }
    }
}
