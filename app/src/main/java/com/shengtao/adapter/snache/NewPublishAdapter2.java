package com.shengtao.adapter.snache;

import android.content.Context;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.application.UIApplication;
import com.shengtao.domain.snache.NewPublishedGood;
import com.shengtao.main.PublishFragment;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.adapter.snache
 * Created by TT on 2015/12/23.
 * Description:
 */
public class NewPublishAdapter2 extends BaseAdapter {

    private Context context;

    private List datas;
    private SparseArray<PublishFragment.MyCountDownTimer> isCountdown = new SparseArray<PublishFragment.MyCountDownTimer>();

    public NewPublishAdapter2(Context context, List datas) {
        this.context = context;
        this.datas = datas;
    }

    public SparseArray<PublishFragment.MyCountDownTimer> getIsCountdown() {
        return isCountdown;
    }

    @Override
    public int getCount() {
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


    private PublishFragment.MyCountDownTimer preCount;
    private PublishFragment.MyCountDownTimer nextCount;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewvher vh = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.test, null);
            vh = new Viewvher(convertView);
            convertView.setTag(vh);
        } else {
            vh = (Viewvher) convertView.getTag();
        }
        NewPublishedGood newPublishedGood =  getItem(position);


        //商品的状态0表示正在参与中，1表示正在开奖中，>=2表示已经开奖
        String states = newPublishedGood.getStatus();
        if("0".equals(states)){
            vh.iv_newpub.setVisibility(View.GONE);
        }else if("1".equals(states)){
            vh.iv_newpub.setVisibility(View.VISIBLE);
            vh.ll_is_released.setVisibility(View.VISIBLE);
            vh.rl_published.setVisibility(View.GONE);


            for(int i=0,len=isCountdown.size();i<len;i++){
                if(isCountdown.valueAt(i).tv_time == null?false:isCountdown.valueAt(i).tv_time.hashCode() == vh.tv_time.hashCode()){
                    isCountdown.valueAt(i).tv_time = null;
                }
            }
            isCountdown.get(position).tv_time = vh.tv_time;

        }else{
            vh.iv_newpub.setVisibility(View.GONE);
            vh.ll_is_released.setVisibility(View.GONE);
            vh.rl_published.setVisibility(View.VISIBLE);

//            vh.tv_winner.setText(Html.fromHtml("<font color=#999999>中 奖 者 : </font>"+ newPublishedGood.getClient_name()) );//获奖者
            vh.tv_winner.setText(newPublishedGood.getClient_name());//获奖者
            vh.tv_number.setText(newPublishedGood.getClient_join_num());//本期夺宝次数
            vh.tv_open_time.setText("揭晓时间: " + DateTimeUtil.getTimeFormatText(Long.parseLong(newPublishedGood.getOpen_time()) / 1000));//开奖时间
            ImageLoader.getInstance().displayImage(newPublishedGood.getHead_img(), vh.iv_client_name, UIApplication.getAvatar());//商品头像
        }

        vh.tv_price.setText(Html.fromHtml("价　　格: <font color='#ff4447'>￥" + newPublishedGood.getGoods_rmb() + "</font>"));//商品价格

        if(!newPublishedGood.getGoods_headurl().contains("imageView2")){
            newPublishedGood.setGoods_headurl(newPublishedGood.getGoods_headurl() + "?imageView2/2/w/360");
        }
        LogUtil.e("slist", newPublishedGood.getGoods_headurl());
        ImageLoader.getInstance().displayImage(newPublishedGood.getGoods_headurl(), vh.iv_good_icon, ImageLoaderCfg.options);//商品头像
        vh.tv_goods_name.setText(newPublishedGood.getGoods_name());//商品名称

        return convertView;
    }

    private static class Viewvher {

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

        public Viewvher(View view) {
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


}
