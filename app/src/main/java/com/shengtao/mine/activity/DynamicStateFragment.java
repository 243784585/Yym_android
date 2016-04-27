package com.shengtao.mine.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfgCircle;
import com.shengtao.application.UIApplication;
import com.shengtao.domain.Config;
import com.shengtao.domain.mine.PersonDState;
import com.shengtao.domain.mine.PersonDStateInfo;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.BaseFragment;
import com.shengtao.foundation.IToastBlock;
import com.shengtao.main.MainActivity;
import com.shengtao.snache.activity.PrizeDetailsActivity;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;
import com.shengtao.utils.VisitorMode;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * @package com.baixi.mine.activity
 * Created by TT on 2015/12/16.
 * Description:他的个人中心-动态的fragment
 */
@SuppressLint("ValidFragment")
public class DynamicStateFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView lvDynamicState;
    private ArrayList<PersonDStateInfo> dList;
    private MsgWinningAdapter msgWinningAdapter;
    private boolean isSnach = false;
    private String id;


    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Object o[] = (Object[]) msg.obj;
            ((removeNumber)o[1]).reset();
            String url = Config.HTTP_URL_HEAD + "goods/AddShoppingCart";
            RequestParams reqParams = new RequestParams();
            //1判断是否点击的是十元商品
            reqParams.put("id", ((PersonDStateInfo) o[0]).getId());
            if ("1".equals(((PersonDStateInfo) o[0]).getGoods_10())) {
                reqParams.put("number", msg.what * 10);
                LogUtil.e("log1",msg.what + "");
            } else {
//                reqParams.put("id", ((SResult) o[0]).getId());
                reqParams.put("number", msg.what);
                LogUtil.e("log1", msg.what + "");
            }
            LogUtil.e("numbernow", reqParams.toString());
            AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String result = response.toString();
                    if (TextUtils.isEmpty(result))
                        return;
                    if ("0".equals(response.optString("code"))) {
                        //2加入成功
                        //发送动画消息,增加数量并显示
                        Session.SetInt("cartnum", Integer.parseInt(response.optString("info")));
                        Message msg1 = MainActivity.mHandler2.obtainMessage();
                        msg1.what = 1;
                        MainActivity.mHandler2.sendMessage(msg1);

                        Message msg = OtherPersonCenter.mHandler.obtainMessage();
                        msg.obj = view;
                        msg.what = 2;
                        OtherPersonCenter.mHandler.sendMessage(msg);//成功后更新下当前购物车数量，防止数量不一致的产生
//                        LogUtil.e("numbernow", (removeNumber)o[1] + "");
                    } else if ("4".equals(response.optString("code"))) {
                        ToastUtil.showTextToast(response.optString("error"));
                    } else {
                        ToastUtil.showTextToast(response.optString("error"));
                    }
                }
            });
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inflect_dynamicstate, null);
        initView(view);
        return view;
    }

    //获取对象
    public static DynamicStateFragment newInstance() {
        DynamicStateFragment df = new DynamicStateFragment();
        return df;
    }

    public void initView(View view){
        //获取从activity传来的信息
        Bundle bundle = getArguments();
        dList = (ArrayList<PersonDStateInfo>) bundle.getSerializable("dList");
        id = bundle.getString("id");
        isSnach = bundle.getBoolean("snach",false);
        LogUtil.e("log",dList);
        //获取动态的资源id
        lvDynamicState = (PullToRefreshListView) view.findViewById(R.id.lv_dynamic_state);

        lvDynamicState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PersonDStateInfo item = msgWinningAdapter.getItem(position-1);
                Intent intent = new Intent(getActivity(),PrizeDetailsActivity.class);
                intent.putExtra("singleGoodsId",item.getId());
                intent.putExtra("banner",item.getGoods_headurl());
                startActivity(intent);
            }
        });
        initRefresh();
        inflater(view);
    }
    public void inflater(View view){
        msgWinningAdapter = new MsgWinningAdapter();
        lvDynamicState.setAdapter(msgWinningAdapter);
    }

    /**
     * 初始化上下拉刷新操作
     */
    public void initRefresh(){
        lvDynamicState.setMode(PullToRefreshBase.Mode.BOTH);
        lvDynamicState.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            int currentPage = 1;
            RequestParams params = new RequestParams();
            String url = !isSnach?Config.HTTP_URL_HEAD + "goods/otherJoinLog":Config.HTTP_URL_HEAD + "user/myJoinLog";

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);

                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            dList.clear();
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvDynamicState.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvDynamicState.onRefreshComplete();
                        return;
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ++currentPage;
                if (!isSnach) {
                    params.put("dbAppClientid", id);
                }
                params.put("pageNum", currentPage);
                AsyncHttpTask.post(url, params, new AsyncHttpResponseHandler() {
                    //加载成功
                    @Override
                    public void onSuccess(int statusCode, Header[] motuHeaders, byte[] bytes) {
                        try {
                            String data = new String(bytes, super.getCharset());
                            LogUtil.d(data);
                            showUIData(data);
                            lvDynamicState.onRefreshComplete();
                            msgWinningAdapter.notifyDataSetChanged();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //获取数据失败
                    @Override
                    public void onFailure(int statusCode, Header[] motuHeaders, byte[] bytes, Throwable throwable) {
                        lvDynamicState.onRefreshComplete();
                        return;
                    }
                });
            }
        });
    }


    @Override
    public IToastBlock getToastBlock() {
        return null;
    }

    protected void showUIData(Object obj) {
        //解析方法
        String result = obj.toString();
        LogUtil.e("log", result);
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json = null;
        ArrayList<PersonDStateInfo> results;
        try {
            json = new JSONObject(result);
            PersonDState personDState = new PersonDState(json);
            results = personDState.getPersonDStateList();//获取动态的数据集合

            if(results.size()>0){
                dList.addAll(results);
            }else{
                ToastUtil.showTextToast("没有更多了");
            }
            msgWinningAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MsgWinningAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dList.size();
        }

        @Override
        public PersonDStateInfo getItem(int position) {
            return dList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = View.inflate(getActivity(),R.layout.item_dynamicstate,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final PersonDStateInfo item = getItem(position);
            String status = item.getStatus();//判断当前状态，0，正在进行 1是正在揭晓，2以上是有中奖者


            if("0".equals(status)){
                viewHolder.ll_is_publishing.setVisibility(View.VISIBLE);
                viewHolder.ll_published.setVisibility(View.GONE);

                if("0".equals(item.getGoods_10())){
                    viewHolder.ico_characteristic.setVisibility(View.GONE);
                }else{
                    viewHolder.ico_characteristic.setVisibility(View.VISIBLE);
                }


                int cha = Integer.parseInt(item.getAll_join_num())-Integer.parseInt(item.getCurrent_join_num());
                viewHolder.tv_current_join_num.setText(Html.fromHtml("<font color=black>剩余</font>") + " " + cha);
                Double j = (Double.parseDouble(item.getAll_join_num())-cha) / Double.parseDouble(item.getAll_join_num()) * 100;
                viewHolder.pro_buy_count.setProgress(j > 99 ? 99 : j < 1 && j!=0? 1 : j.intValue());
                viewHolder.tv_snachecode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final AlertDialog dialog = builder.create();
                        View v = View.inflate(getActivity(), R.layout.check_snach_no, null);
                        v.findViewById(R.id.tv_onclick).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        ((TextView) v.findViewById(R.id.pop_title)).setText(item.getGoods_name());//晒单标题
                        ((TextView) v.findViewById(R.id.pop_number)).setText("参与" + item.getAll_join_num() + "次，夺宝号码");//晒单参与次数
                        String[] buyCode = item.getOrder_code().split(",");
                        ((GridView) v.findViewById(R.id.gv_snach_no)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.arrayadapter_textview, R.id.tv, buyCode));

                        dialog.setView(v);
                        dialog.show();
                    }
                });

                SpannableStringBuilder style=new SpannableStringBuilder("(第" + item.getGoods_current_num() + "期)    " + item.getGoods_name());
                //SpannableStringBuilder实现CharSequence接口
                style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_goods_name.setText(style);

                viewHolder.tv_all_join_num.setText("共需" + item.getAll_join_num() + "人次");
                viewHolder.tv_nember.setText(Html.fromHtml(item.getRmb_num()+"<font color=black>人次</font>"));
                ImageLoader.getInstance().displayImage(item.getGoods_headurl(), viewHolder.iv_goods_headurl, UIApplication.getAvatar());

                //跟买
                viewHolder.tv_genbuy.setOnClickListener(new removeNumber() {
                    @Override
                    public void reset() {
                        number = 0;
                    }

                    private int number = 0;
                    @Override
                    public void onClick(final View view) {
                        if(VisitorMode.isVistor(getActivity())){
                            return;
                        }
                        if(CommonUtil.isEmpty(Session.GetString("city_id"))){
                            //未填写收货地址,不允许加入清单
                            VisitorMode.alertToAddress(getActivity());
                            return;
                        }
                        //跟买的时候跟加入购物车是一样的流程
                        //进行添加到购物车的请求
                        ++number;
                        //发送动画消息
                        Message msg = OtherPersonCenter.mHandler.obtainMessage();
                        msg.obj = view;
                        msg.what =1;
                        OtherPersonCenter.mHandler.sendMessage(msg);
                        //1进行添加到购物车的请求
                        handler1.removeCallbacksAndMessages(null);
                        Message msg1 = new Message();
                        msg1.what = number;
                        msg1.obj = new Object[]{item,this};
                        handler1.sendMessageDelayed(msg1, 500);

                    }
                });
            }else{
                viewHolder.ll_is_publishing.setVisibility(View.GONE);
                viewHolder.ll_published.setVisibility(View.VISIBLE);


                if("0".equals(item.getGoods_10())){
                    viewHolder.ico_characteristic1.setVisibility(View.GONE);
                }else{
                    viewHolder.ico_characteristic1.setVisibility(View.VISIBLE);
                }

                if("null".equals(item.getClient_name())){
                    viewHolder.ll_countdown.setVisibility(View.GONE);
                    viewHolder.tv_countdown.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ll_countdown.setVisibility(View.VISIBLE);
                    viewHolder.tv_countdown.setVisibility(View.GONE);
                    viewHolder.tv_winner.setText(item.getClient_name());
                    viewHolder.tv_luck_nummber.setText(item.getLucky_id());
                    viewHolder.tv_this_number.setText(item.getClient_join_num() + Html.fromHtml("<font color=black>人次</font>"));
                }

                viewHolder.tv_snachecode2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final AlertDialog dialog = builder.create();
                        View v = View.inflate(getActivity(), R.layout.check_snach_no, null);
                        v.findViewById(R.id.tv_onclick).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        ((TextView) v.findViewById(R.id.pop_title)).setText(item.getGoods_name());//晒单标题
                        ((TextView) v.findViewById(R.id.pop_number)).setText("参与" + item.getAll_join_num() + "次，夺宝号码");//晒单参与次数
                        String[] buyCode = item.getOrder_code().split(",");
                        ((GridView) v.findViewById(R.id.gv_snach_no)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.arrayadapter_textview, R.id.tv, buyCode));

                        dialog.setView(v);
                        dialog.show();
                    }
                });

                if(!CommonUtil.isEmpty(item.getOpen_time())){
                    viewHolder.tv_time.setText(DateTimeUtil.getTimeFormatText(Long.parseLong(item.getOpen_time())/1000));
                }
                if(!CommonUtil.isEmpty(item.getAll_join_num())&&!CommonUtil.isEmpty(item.getRmb_num())){

                    SpannableStringBuilder style=new SpannableStringBuilder("(第" + item.getGoods_current_num() + "期)    " + item.getGoods_name());
                    //SpannableStringBuilder实现CharSequence接口
                    style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    viewHolder.tv_goods_name2.setText(style);

                    viewHolder.tv_all_join_num2.setText("共需" + item.getAll_join_num() + "人次");
                    viewHolder.tv_nember2.setText(Html.fromHtml(item.getRmb_num() + "<font color=black>人次</font>"));
                }
                ImageLoader.getInstance().displayImage(item.getGoods_headurl(), viewHolder.iv_goods_headurl2, ImageLoaderCfgCircle.options);
                ImageLoader.getInstance().displayImage(item.getHead_img(), viewHolder.iv_head_image, UIApplication.getAvatar());
            }

            return convertView;
        }
    }

    interface removeNumber extends View.OnClickListener{
        void reset();
    }

    public class ViewHolder{
        private LinearLayout ll_is_publishing;//进行中状态
        private LinearLayout ll_published;//正在揭晓状态
        private LinearLayout ll_countdown;//正在揭晓状态.并且显示揭晓中

        private TextView tv_genbuy;
        private ImageView iv_goods_headurl;
        private ImageView iv_goods_headurl2;
        private ImageView iv_split;
        private TextView tv_goods_name;
        private TextView tv_goods_name2;
        private TextView tv_all_join_num;
        private TextView tv_all_join_num2;
        private TextView tv_current_join_num;
        private ProgressBar pro_buy_count;
        private TextView tv_nember;
        private TextView tv_nember2;
        private TextView tv_snachecode;
        private TextView tv_snachecode2;

        private TextView tv_winner;
        private LinearLayout tv_countdown;
        private TextView tv_luck_nummber;
        private TextView tv_this_number;
        private TextView tv_time;
        private CircleImageView iv_head_image;
        private ImageView ico_characteristic;
        private ImageView ico_characteristic1;

        public ViewHolder(View view){
            ll_is_publishing = (LinearLayout) view.findViewById(R.id.ll_is_publishing);
            ll_published = (LinearLayout) view.findViewById(R.id.ll_published);
            ll_countdown = (LinearLayout) view.findViewById(R.id.ll_countdown);

            tv_genbuy = (TextView) view.findViewById(R.id.tv_genbuy);
            iv_goods_headurl = (ImageView) view.findViewById(R.id.iv_goods_headurl);
            iv_goods_headurl2 = (ImageView) view.findViewById(R.id.iv_goods_headurl2);
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
            tv_goods_name2 = (TextView) view.findViewById(R.id.tv_goods_name2);
            tv_all_join_num = (TextView) view.findViewById(R.id.tv_all_join_num);
            tv_all_join_num2 = (TextView) view.findViewById(R.id.tv_all_join_num2);
            tv_current_join_num = (TextView) view.findViewById(R.id.tv_current_join_num);
            pro_buy_count = (ProgressBar) view.findViewById(R.id.pro_buy_count);
            tv_nember = (TextView) view.findViewById(R.id.tv_nember);
            tv_nember2 = (TextView) view.findViewById(R.id.tv_nember2);
            tv_snachecode = (TextView) view.findViewById(R.id.tv_snachecode);
            tv_snachecode2 = (TextView) view.findViewById(R.id.tv_snachecode2);
            iv_split = (ImageView) view.findViewById(R.id.iv_split);

            if(isSnach) {
                iv_split.setVisibility(View.GONE);
                tv_genbuy.setVisibility(View.GONE);
            }

            tv_winner = (TextView) view.findViewById(R.id.tv_winner);
            tv_luck_nummber = (TextView) view.findViewById(R.id.tv_luck_nummber);
            tv_this_number = (TextView) view.findViewById(R.id.tv_this_number);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_countdown = (LinearLayout) view.findViewById(R.id.tv_countdown);
            iv_head_image = (CircleImageView) view.findViewById(R.id.iv_head_image);
            ico_characteristic = (ImageView) view.findViewById(R.id.ico_characteristic);
            ico_characteristic1 = (ImageView) view.findViewById(R.id.ico_characteristic1);

        }
    }

}
