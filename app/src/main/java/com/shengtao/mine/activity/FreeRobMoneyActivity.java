package com.shengtao.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseSingleFragmentActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.foundation.SubmitType;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.Session;
import com.shengtao.utils.ShareDialog;
import com.shengtao.utils.ShareUtils;
import com.shengtao.utils.ShareUtils1;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by hxhuang on 2015/12/18 0018.
 * desc:免费抢币
 */
public class FreeRobMoneyActivity extends BaseSingleFragmentActivity {

    private Intent intent;

    private UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_sign_in).setOnClickListener(this); //签到
        findViewById(R.id.ll_invite_friends).setOnClickListener(this);  //邀请好友
        findViewById(R.id.ll_receiver_address).setOnClickListener(this);  //填写收货地址
        findViewById(R.id.ll_want_exchange).setOnClickListener(this);  //我要兑换
        findViewById(R.id.ll_earnings_detail).setOnClickListener(this);  //收益明细
        findViewById(R.id.ll_rmb_rule).setOnClickListener(this);  //抢币规则
    }

    @Override
    protected String getAvtionTitle() {
        return "免费抢币";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_free_rob_money;
    }

    @Override
    protected String getUri() {
        return Config.HTTP_MODULE_MINE + "user/rmbInfo";
    }

    @Override
    protected SubmitType getSubmitType() {
        return SubmitType.GET;
    }

    @Override
    protected RequestParams getRequestParams() {
        return null;
    }

    @Override
    protected void showUIData(Object obj) {
        try {
            JSONObject json = new JSONObject(obj.toString());
            JSONObject info = json.optJSONObject("info");
            intent = new Intent(this, ExchangeRmbActivity.class);
            if(info != null) {
                String integrate = info.optString("integrate");
                String integrateAll = info.optString("integrateAll");
                String integrateToday = info.optString("integrateToday");
                intent.putExtra("integrate", integrate);
                intent.putExtra("integrateAll", integrateAll);
                intent.putExtra("integrateToday", integrateToday);
                ((TextView) findViewById(R.id.tv_integral)).setText(integrate);//积分金额
                ((TextView) findViewById(R.id.tv_integral_all)).setText("累计收益（积分）: " + integrateAll);//累计收益(积分)
                ((TextView) findViewById(R.id.tv_integral_today)).setText("今日收益（积分）: " + integrateToday);//今天收益(积分)
                ((TextView) findViewById(R.id.tv_rmb_invite)).setText(info.optString("rmbInvite"));//邀请获得的抢币
                ((TextView) findViewById(R.id.tv_rmb_today)).setText("今日收益（抢币）: " + info.optInt("rmbToday"));//今日收益(抢币)
                Session.SetString("integrate_all",integrate);
                Session.SetString("rmb",info.optString("rmb"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_sign_in://签到
                AsyncHttpTask.get(Config.HTTP_MODULE_MINE + "user/sign", new JsonHttpResponse() {
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        //弹出签到对话框
                        new CommonDialog(FreeRobMoneyActivity.this){

                            @Override
                            protected void afterConfirm() {
                                dismiss();
                                sendEmptyBackgroundMessage(MSG_UI_GEt_DATA_LOADING);
                            }
                        }.setTitle("恭喜您获得10个积分!", R.color.black, 16f, TypedValue.COMPLEX_UNIT_SP)
                                .setSubTitle("(100积分=1个抢币)", R.color.yym_common_gray, 11f, TypedValue.COMPLEX_UNIT_SP)
                                .setButtonStyle( R.color.dialog_btn_color, 15f, TypedValue.COMPLEX_UNIT_SP)
                                .setConfirm("确定")
                                .setCancelable(false)
                                .show();
                    }
                });
                break;
            case R.id.ll_invite_friends://邀请好友
                //分享配置
                ShareUtils.configPlatforms(FreeRobMoneyActivity.this, "https://www.baidu.com", "lala");
                ShareDialog shareDialog1 = new ShareDialog(FreeRobMoneyActivity.this, FreeRobMoneyActivity.this);
                shareDialog1.show();
                mController = UMServiceFactory.getUMSocialService("com.umeng.share");
                break;
            case R.id.ll_receiver_address://收货地址
                startActivity(new Intent(this,ReceiverAddressActivity.class));
                break;
            case R.id.ll_want_exchange://我要兑换
                startActivity(intent);
                break;
            case R.id.ll_earnings_detail://收益明细
                startActivity(new Intent(this,IntegeralEarningDetailActivity.class));
                break;
            case R.id.ll_rmb_rule://抢币规则
                startActivity(new Intent(this,UseRuleActivity.class));
                break;
            case R.id.ll_wechat:
                ShareUtils1.shareActive(this, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wechat_friend:
                ShareUtils1.shareActive(this,  SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                ShareUtils.shareActive(this, SHARE_MEDIA.QQ,"");
                break;
            case R.id.ll_qzone:
                ShareUtils.shareActive(this, SHARE_MEDIA.QZONE,"");
                break;
            case R.id.ll_sina:
                ShareUtils.promtoteSinaActive(this);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();//每次重新获取焦点时刷新UI
    }
}