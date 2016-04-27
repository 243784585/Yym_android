package com.shengtao.adapter.snache;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.domain.Config;
import com.shengtao.domain.snache.NewPublishedGood;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package com.shengtao.adapter.snache
 * Created by TT on 2015/12/23.
 * Description:
 */
public class NewPublishAdapter extends BaseAdapter {

    private Context context;

    private List datas;
    private String time;
//    private Map<Integer,CountDownTimer> isCountdown = new HashMap<Integer,CountDownTimer>();
    private Map<Integer,Object> isCountdown = new HashMap<Integer,Object>();
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");
    private Date date = new Date();

    public NewPublishAdapter(Context context, List datas,String time) {
        this.context = context;
        this.datas = datas;
        this.time = time;
    }

//    public Map<Integer, Object> getIsCountdown() {
//        return isCountdown;
//    }

    @Override
    public int getCount() {
        LogUtil.e("collection",datas.size()+"");
        return datas.size();
    }

    @Override
    public NewPublishedGood getItem(int position) {
        return (NewPublishedGood) datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewvher vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_new_publish, null);
            vh = new Viewvher(convertView, position);
            convertView.setTag(vh);
        } else {
            vh = (Viewvher) convertView.getTag();
        }
        LogUtil.e("hehe", vh.position + "");
        NewPublishedGood newPublishedGood = getItem(position);

        //商品的状态0表示正在参与中，1表示正在开奖中，>=2表示已经开奖
        String states = newPublishedGood.getStatus();
        if ("0".equals(states)) {
            vh.iv_newpub.setVisibility(View.GONE);
        } else if ("1".equals(states)) {
            vh.iv_newpub.setVisibility(View.VISIBLE);
            vh.ll_is_released.setVisibility(View.VISIBLE);
            vh.rl_published.setVisibility(View.GONE);


//                vh.tv_winner.setText(newPublishedGood.getClient_name());//获奖者
//                vh.tv_number.setText(newPublishedGood.getClient_join_num());//本期夺宝次数
//                vh.tv_open_time.setText(newPublishedGood.getOpen_time());//开奖时间
//                ImageLoader.getInstance().displayImage(newPublishedGood.getHead_img(), vh.iv_client_name, ImageLoaderCfg.options);//商品头像
            if (isCountdown.get(position) == null) {

                isCountdown.put(position, new MyCountDownTimer(Long.parseLong(newPublishedGood.getOpen_time()) - Long.parseLong(time), 1, vh.tv_time, newPublishedGood.getId(), position, vh.position).start());

//                vh.tv_time.setText("揭晓中");
//                handler.sendMessageDelayed(handler.obtainMessage(0, new Object[]{newPublishedGood.getId(), position}),n);
//                isCountdown.put(position,position);
            } else if (((MyCountDownTimer) isCountdown.get(position)).vhFlag != vh.position) {
                LogUtil.e("wohuanle", vh.position + "====" + ((MyCountDownTimer) isCountdown.get(position)).vhFlag);
                ((MyCountDownTimer) isCountdown.get(position)).setTv_time(vh.tv_time).vhFlag = vh.position;
            }
        } else {
            vh.iv_newpub.setVisibility(View.GONE);
            vh.ll_is_released.setVisibility(View.GONE);
            vh.rl_published.setVisibility(View.VISIBLE);

            vh.tv_winner.setText(Html.fromHtml("<font color=#999999>中 奖 者 : </font>" + newPublishedGood.getClient_name()));//获奖者
            vh.tv_number.setText("本期夺宝: " + newPublishedGood.getClient_join_num());//本期夺宝次数
            vh.tv_open_time.setText("揭晓时间: " + DateTimeUtil.getTimeFormatText(Long.parseLong(newPublishedGood.getOpen_time()) / 1000));//开奖时间
            ImageLoader.getInstance().displayImage(newPublishedGood.getHead_img(), vh.iv_client_name, ImageLoaderCfg.options);//商品头像
        }

        vh.tv_price.setText("价    格: ￥" + newPublishedGood.getGoods_rmb());//商品价格

        if(!newPublishedGood.getGoods_headurl().contains("imageView2")){
            newPublishedGood.setGoods_headurl(newPublishedGood.getGoods_headurl()+"?imageView2/2/w/360");
        }
        ImageLoader.getInstance().displayImage(newPublishedGood.getGoods_headurl(), vh.iv_good_icon, ImageLoaderCfg.options);//商品头像
        vh.tv_goods_name.setText(newPublishedGood.getGoods_name());//商品名称

        return convertView;
    }

    private class Viewvher {

        public int position;

        private ImageView iv_good_icon;//商品图
        private TextView tv_goods_name;//商品名
        private TextView tv_price;//商品价格
        private RelativeLayout rl_published;//已揭晓布局
        private TextView tv_winner;//获奖者
        private TextView tv_number;//夺宝数
        private TextView tv_open_time;//开奖时间
        private CircleImageView iv_client_name;//获奖者头像
        private LinearLayout ll_is_released;//倒计时布局
        private TextView tv_time;//倒计时
        private ImageView iv_newpub;//正在揭晓图

        public Viewvher(View view,int position) {
            this.position = position;

            iv_good_icon = (ImageView) view.findViewById(R.id.iv_good_icon);
            tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_winner = (TextView) view.findViewById(R.id.tv_winner);
            tv_number = (TextView) view.findViewById(R.id.tv_number);
            tv_open_time = (TextView) view.findViewById(R.id.tv_open_time);
            iv_client_name = (CircleImageView) view.findViewById(R.id.iv_client_name);
            ll_is_released = (LinearLayout) view.findViewById(R.id.ll_is_released);
            rl_published = (RelativeLayout) view.findViewById(R.id.rl_published);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_newpub = (ImageView) view.findViewById(R.id.iv_newpub);
        }
    }

    class MyCountDownTimer extends CountDownTimer {
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

        private TextView tv_time;
        private String id;
        public int position;
        public int vhFlag;

        public MyCountDownTimer(long millisInFuture, long countDownInterval,TextView tv_time,String id,int position,int vhFlag) {
            super(millisInFuture, countDownInterval);
            this.tv_time = tv_time;
            this.id = id;
            this.position = position;
            this.vhFlag = vhFlag;
        }

        public MyCountDownTimer setTv_time(TextView tv_time) {
            this.tv_time = tv_time;
            return this;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            /*int fen = (int) (millisUntilFinished / 1000 / 60);
            int haomiao = (int) millisUntilFinished % 1000;
//            long i = millisUntilFinished;
//            long j = fen*60*1000;
            int miao = (int) ((millisUntilFinished - (fen * 60 * 1000)) / 1000);
            tv_time.setText(fen + ":" + miao + ":" + haomiao);*/
            date.setTime(millisUntilFinished);
            tv_time.setText(sdf.format(date));
        }

        @Override
        public void onFinish() {
            cancel();
            tv_time.setText("揭晓中");
            LogUtil.e("doRequest", "onFinish:" + Thread.currentThread().getName());
            doRequest();
        }

        private void doRequest(){
            LogUtil.e("doRequest","1");
            //notifyDataSetChanged();
            //进行添加到购物车的请求
            AsyncHttpTask.post(Config.HTTP_URL_HEAD + "goods/newSingleOpenGoods", new RequestParams("id", id), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String result = response.toString();
                    LogUtil.e("callback", "status:" + response.optString("code"));
                    if (TextUtils.isEmpty(result))
                        return;
                    if ("0".equals(response.optString("code"))) {
                        JSONObject info = response.optJSONObject("info");
                        /*JSONArray list = info.optJSONArray("list");
                        String name = list.optJSONObject(0).optString("client_name");
                        String open_time = list.optJSONObject(0).optString("open_time");
                        String goods_headurl = list.optJSONObject(0).optString("goods_headurl");
                        String head_img = list.optJSONObject(0).optString("head_img");
                        String client_join_num = list.optJSONObject(0).optString("client_join_num");*/

                        datas.set(position, new NewPublishedGood(info.optJSONArray("list").optJSONObject(0)));
                        LogUtil.e("callback", "onsuccess:" + Thread.currentThread().getName());
                        notifyDataSetChanged();
                    } else if ("3".equals(response.optString("code"))) {
                        doRequest();
                        LogUtil.e("faildcallback:", Thread.currentThread().getName());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    LogUtil.e("HttpRequestError(" + statusCode + "):", responseString);
                }
            });
        }

        /*@Override
        public void onUITick(String timer) {
            timer);
        }*/
    }
}
