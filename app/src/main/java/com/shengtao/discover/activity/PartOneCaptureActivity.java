package com.shengtao.discover.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.application.ImageLoaderCfg;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.domain.discover.GoodsDetail;
import com.shengtao.domain.discover.ZeroCapture;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.ShareUtils;
import com.shengtao.utils.ShareUtils1;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2016/1/1.
 * <p/>
 * 参与零元夺宝
 */
public class PartOneCaptureActivity extends BaseSingleFragmentActivity {

    public static UMSocialService mController;

    private RequestParams requestParams;
    private ArrayList<GoodsDetail> goodsDetails;
    private LinearLayout ll_join;
    private ArrayList<String> dbCode;
    private ImageView iv_head_image_ed;
    private TextView now_opentime, opentime, tv_goods_name, tv_price, textView2, tv_open_time1, tv1, tv_share_obtain, pop_number, pop_number1, pop_number2, pop_null, pop_title;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected void initData() {
        num = getIntent().getIntExtra("num", 0);
        super.initData();
    }

    private void initView() {
        iv_head_image_ed = (ImageView) findViewById(R.id.iv_head_image_ed);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_price = (TextView) findViewById(R.id.tv_price);//价格
        textView2 = (TextView) findViewById(R.id.textView2);//参与人数
        tv_open_time1 = (TextView) findViewById(R.id.tv_open_time1);//开奖时间
        now_opentime = (TextView) findViewById(R.id.tv_now_opentime);//文字介绍里的活动时间
        opentime = (TextView) findViewById(R.id.tv_open_time);//文字介绍里的开奖时间

        tv1 = (TextView) findViewById(R.id.tv1);//中奖号码
        tv1.setOnClickListener(this);


        tv_share_obtain = (TextView) findViewById(R.id.tv_share_obtain);//分享获得更多夺宝码
        tv_share_obtain.setOnClickListener(this);
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.setShareContent(Config.sharetitle);

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
        return Config.HTTP_URL_HEADED + "Zerogoods/getZeroCode";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.POST;
    }

    @Override
    protected RequestParams getRequestParams() {
        requestParams = new RequestParams();
        requestParams.put("zgoodsId", getIntent().getExtras().get("goodsDetail"));
        return requestParams;
    }

    @Override
    protected void showUIData(Object obj) {
        String result = obj.toString();
        System.out.println(result + "全部参数");
        if (TextUtils.isEmpty(result)) {
            return;
        }
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            LogUtil.e("obj", json.toString());
            ZeroCapture zeroCapture = new ZeroCapture(json);
            dbCode = zeroCapture.getDbCodes();
            goodsDetails = zeroCapture.getGoodsDetail();
            initFlater();//填充数据
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initFlater() {
        if (goodsDetails.size() > 0) {
            if (!goodsDetails.get(0).getZgoods_headurl().contains("imageView2")) {
                goodsDetails.get(0).setZgoods_headurl(goodsDetails.get(0).getZgoods_headurl() + "?imageView2/2/w/640");
            }
            ImageLoader.getInstance().displayImage(goodsDetails.get(0).getZgoods_headurl(), iv_head_image_ed, ImageLoaderCfg.options);
            tv_goods_name.setText(goodsDetails.get(0).getZgoods_name());
            tv_price.setText(goodsDetails.get(0).getZgoods_rmb());
            textView2.setText(num + "");

            //时间处理
            Long k = Long.parseLong(goodsDetails.get(0).getOpen_time()) - Long.parseLong(goodsDetails.get(0).getNow());
            MyCountDownTimer mc2 = new MyCountDownTimer(k, 1, tv_open_time1);
            mc2.start();

            now_opentime.setText("2.活动时间: " + DateTimeUtil.timestamp4Time(goodsDetails.get(0).getNow(), "1") + "---" + DateTimeUtil.timestamp4Time(goodsDetails.get(0).getOpen_time(),"1"));
            opentime.setText("3.开奖时间:" + DateTimeUtil.timestamp4Time(goodsDetails.get(0).getOpen_time(), "1"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                diaLog();
                break;
            case R.id.tv_share_obtain:
                ShareUtils.configPlatforms(PartOneCaptureActivity.this, "https://www.baidu.com", "lala");
                String zgoodsId = (String) getIntent().getExtras().get("goodsDetail");
                ShareUtils1.shareCircle(this, SHARE_MEDIA.WEIXIN_CIRCLE,zgoodsId);

//                String appId = "wxd81212f3f3939d91";
//                String appSecret = "ba3f45ae3ac3810112bbdaa7a20c29ec";
//                UMWXHandler wxCircleHandler = new UMWXHandler(getApplicationContext(), appId, appSecret);
//                wxCircleHandler.setTargetUrl("http://120.25.145.15/oneDream.html");
//                wxCircleHandler.setToCircle(true);
//                wxCircleHandler.setTitle(Config.sharetitle);
//                wxCircleHandler.addToSocialSDK();

                break;
        }
    }

    //弹窗
    private void diaLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartOneCaptureActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(PartOneCaptureActivity.this, R.layout.check_number_pop, null);
        pop_title = (TextView) view.findViewById(R.id.pop_title);//晒单标题
        pop_number1 = (TextView) view.findViewById(R.id.pop_number1);//晒单码1
        pop_number2 = (TextView) view.findViewById(R.id.pop_number2);//晒单码2
        pop_null = (TextView) view.findViewById(R.id.pop_null);//没有参与夺宝
        ll_join = (LinearLayout) view.findViewById(R.id.ll_join);//参与

        if (goodsDetails.size() > 0) {
            pop_title.setText(goodsDetails.get(0).getZgoods_name());
        }
        switch (dbCode.size()) {
            case 0:
                ll_join.setVisibility(View.GONE);
                pop_null.setVisibility(View.VISIBLE);
                pop_number1.setVisibility(View.GONE);
                break;
            case 1:
                pop_number1.setText(dbCode.get(0));
                break;
            case 2:
                pop_number1.setText(dbCode.get(0));
                pop_number2.setText(dbCode.get(1));
                pop_number2.setVisibility(View.VISIBLE);
                break;
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

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         * <p/>
         * 例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         * <p/>
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        private TextView tv_open_time1;

        public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView tv_open_time1) {
            super(millisInFuture, countDownInterval);
            this.tv_open_time1 = tv_open_time1;
        }


        @Override
        public void onTick(long millisUntilFinished) {
            int tian = (int) (millisUntilFinished / (1000 * 60 * 60 * 24));
            int shi = (int) ((millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int fen = (int) ((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60));
            int miao = (int) ((millisUntilFinished % (1000 * 60)) / 1000);
            int haomiao = (int) millisUntilFinished % 1000;
            tv_open_time1.setText("还剩" + tian + "天" + shi + "时" + fen + "分" + miao + "秒");

        }

        @Override
        public void onFinish() {

        }
    }
}
