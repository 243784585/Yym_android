package com.shengtao.discover.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.PastResultReward;
import com.shengtao.domain.discover.PastResultRewardDeatil;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.ToastUtil;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/23.
 * <p/>
 * 查看往期奖品详情
 */
public class WinnerDetailActivity extends BaseSingleFragmentActivity {

    public static UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private Context mContext = null;
    private RequestParams requestParams;

    private ImageView iv_head_image_ed;
    private TextView tv_goods_name, tv_price, textView2, reward_number, tv1, tv_share_obtain, pop_number, pop_number1, pop_null, pop_number2,pop_title,tv_now_opentime,tv_open_time;
    private LinearLayout ll_luck, ll_join;
    private ArrayList<PastResultRewardDeatil> pastResultRewardDeatils;
    private ArrayList<String> dbCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "奖品详情";
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.reward_detail;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_URL_HEADED + "Zerogoods/ZeroSingleDetail";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        requestParams = new RequestParams();
        requestParams.put("zgoodsId", getIntent().getExtras().get("pastGoods"));
        return requestParams;
    }

    @Override
    protected void showUIData(Object obj) {
        String result = obj.toString();
        if (TextUtils.isEmpty(result))
            return;
        JSONObject json;
        try {
            json = new JSONObject(result);
            PastResultReward pastResultReward = new PastResultReward(json);
            dbCode = pastResultReward.getInfo();
            pastResultRewardDeatils = pastResultReward.getPastResultRewardDeatils();
            inFlater();//填充数据
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void inFlater() {
        ll_luck.setVisibility(View.VISIBLE);
        if (pastResultRewardDeatils.size() > 0) {
            ImageLoader.getInstance().displayImage(pastResultRewardDeatils.get(0).getZgoods_headurl(), iv_head_image_ed);
            tv_goods_name.setText(pastResultRewardDeatils.get(0).getZgoods_name());
            tv_price.setText(pastResultRewardDeatils.get(0).getZgoods_rmb());
            textView2.setText(pastResultRewardDeatils.get(0).getCurrent_join_num());
            reward_number.setText(pastResultRewardDeatils.get(0).getLucky_id());

            tv_now_opentime.setText("2.活动时间: " + DateTimeUtil.timestamp4Time(pastResultRewardDeatils.get(0).getStar_time(), "1") + "---" + DateTimeUtil.timestamp4Time(pastResultRewardDeatils.get(0).getOpen_time(),"1"));
            tv_open_time.setText("3.开奖时间:" + DateTimeUtil.timestamp4Time(pastResultRewardDeatils.get(0).getOpen_time(), "1"));
        }
    }

    private void initView() {
        ll_luck = (LinearLayout) findViewById(R.id.ll_luck);
        iv_head_image_ed = (ImageView) findViewById(R.id.iv_head_image_ed);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_now_opentime = (TextView) findViewById(R.id.tv_now_opentime);
        tv_open_time = (TextView) findViewById(R.id.tv_open_time);
        tv_price = (TextView) findViewById(R.id.tv_price);//价格
        textView2 = (TextView) findViewById(R.id.textView2);//参与人数
        reward_number = (TextView) findViewById(R.id.reward_number);//中奖号码
        tv1 = (TextView) findViewById(R.id.tv1);//中奖号码
        tv1.setOnClickListener(this);
        tv_share_obtain = (TextView) findViewById(R.id.tv_share_obtain);//分享获得更多夺宝码
        tv_share_obtain.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
               if (dbCode.size() == 0){
                   ToastUtil.showTextToast("您未参与此次夺宝");
               }else{
                   diaLog();
               }
                break;
        }
    }

    //弹窗
    private void diaLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WinnerDetailActivity.this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(WinnerDetailActivity.this, R.layout.check_number_pop, null);
        pop_title = (TextView) view.findViewById(R.id.pop_title);//晒单标题
//        pop_number = (TextView) findViewById(R.id.pop_number);//晒单参与次数
        pop_number1 = (TextView) view.findViewById(R.id.pop_number1);//晒单码1
        pop_number2 = (TextView) view.findViewById(R.id.pop_number2);//晒单码2
        pop_null = (TextView)view.findViewById(R.id.pop_null);//没有参与夺宝
        ll_join = (LinearLayout)view. findViewById(R.id.ll_join);//参与
        if (pastResultRewardDeatils.size() > 0){
            pop_title.setText(pastResultRewardDeatils.get(0).getZgoods_name());
//            pop_number.setText(pastResultRewardDeatils.get(0).getCurrent_join_num());
        }

        //参与过的情况就显示夺宝码，否则显示空的状态
        if (!CommonUtil.isEmpty(dbCode.get(0))){
            pop_number1.setText(dbCode.get(0));
        }else {
            ll_join.setVisibility(View.GONE);
            pop_null.setVisibility(View.VISIBLE);
            pop_number1.setVisibility(View.GONE);

        }
        if(dbCode.size()==2){//判断是否显示第二个参与码
            if (!CommonUtil.isEmpty(dbCode.get(1))){
                pop_number2.setVisibility(View.VISIBLE);
                pop_number2.setText(dbCode.get(1));
            }else {
                ll_join.setVisibility(View.GONE);
                pop_null.setVisibility(View.VISIBLE);
                pop_number2.setVisibility(View.GONE);
            }
        }

        TextView tvClick = (TextView) view.findViewById(R.id.tv_onclick);
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();

    }


}
