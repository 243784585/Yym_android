package com.shengtao.mine.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iapppay.sdk.main.IAppPayOrderUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.utils.CircleImageView;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by hxhuang on 2015/12/17 0017.
 * desc:充值页面
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {


    private CircleImageView ivHeadImage;
    private TextView tvQb;
    private TextView tvUserName;
    private EditText etInputAmount;                     //自定义输入金额输入框
    private RadioButton radios[] = new RadioButton[5];  //充值金额单选按钮数组
    private ImageView ivPay[] = new ImageView[2];       //支付方式按钮数组

    @Override
    protected int setLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        ivHeadImage = (CircleImageView) findViewById(R.id.iv_head_image_recharge);
        tvQb = (TextView) findViewById(R.id.tv_qb_recharge);
        tvUserName = (TextView) findViewById(R.id.tv_username_recharge);
        etInputAmount = (EditText) findViewById(R.id.et_input_amount);

        radios[0] = (RadioButton) getViewAndSetOnClick(R.id.rb_20yuan, this);
        radios[1] = (RadioButton) getViewAndSetOnClick(R.id.rb_50yuan, this);
        radios[2] = (RadioButton) getViewAndSetOnClick(R.id.rb_100yuan, this);
        radios[3] = (RadioButton) getViewAndSetOnClick(R.id.rb_200yuan, this);
        radios[4] = (RadioButton) getViewAndSetOnClick(R.id.rb_500yuan, this);

        ivPay[0] = (ImageView) findViewById(R.id.ico_alipay);
        ivPay[1] = (ImageView) findViewById(R.id.ico_weixin);


        getViewAndSetOnClick(R.id.cb_alipay,this).setPadding(30,0,30,0);
        getViewAndSetOnClick(R.id.cb_weixin,this).setPadding(30,0,30,0);
        findViewById(R.id.btn_now_recharge).setOnClickListener(this);//立即充值按钮设置点击事件
        etInputAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    for (int i = 0, len = radios.length; i < len; i++) {
                        radios[i].setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    protected String getAvtionTitle() {
        return "充值";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void doBusiness() {
        tvUserName.setText("昵称: " + Session.GetString("client_name"));
        ImageLoader.getInstance().displayImage(Session.GetString("headimg"),ivHeadImage);
        String qb = "抢币: "+Session.GetString("rmb");
        SpannableStringBuilder style=new SpannableStringBuilder(qb);
        style.setSpan(new ForegroundColorSpan(Color.RED), 4, qb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvQb.setText(style);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_20yuan:
                selectThis(0, 0);
                break;
            case R.id.rb_50yuan:
                selectThis(1,0);
                break;
            case R.id.rb_100yuan:
                selectThis(2,0);
                break;
            case R.id.rb_200yuan:
                selectThis(3,0);
                break;
            case R.id.rb_500yuan:
                selectThis(4,0);
                break;
            case R.id.cb_alipay:
                //支付宝支付
                selectThis(0,1);
                break;
            case R.id.cb_weixin:
                //微信支付
                selectThis(1,1);
                break;
            case R.id.btn_now_recharge:
                //充值按钮
                recharge();
                break;
        }
    }

    private String rmb;
    private String tradeNum;

    /** 充值 */
    private void recharge(){
        Editable text = etInputAmount.getText();
        rmb = null;      //充值金额
        if(text == null || text.length()==0){
            //非自定义
            RadioButton curItem = null;
            for(int i=0,len=radios.length;i<len;i++){
                if(radios[i].isChecked()){
                    curItem = radios[i];
                }
            }
            if(curItem != null){
                rmb = curItem.getText().toString().replace("元","").trim();
            }else{
                //没有选择充值金额
                ToastUtil.makeText(this,"请选择充值金额");
                return;
            }
        }else{
            //自定义充值
            rmb=text.toString().trim();
        }


        ImageView curIv = null;
        for(int i=0,len=ivPay.length;i<len;i++){
            if(ivPay[i].isSelected()){
                curIv = ivPay[i];
            }
        }
        if(curIv != null){
            switch (curIv.getId()){
                case R.id.ico_alipay:
                    //支付宝支付
                    getTransid(401);
                    break;
                case R.id.ico_weixin:
                    //维信支付
                    getTransid(403);
                    break;
            }
        }else{
            //没有选择支付方式
            ToastUtil.makeText(this,"请选择支付方式");
        }
    }

    /**控制按钮选中某个,其他不选中(type=0代表充值面额,非0代表支付方式)*/
    private void selectThis(int position,int type){
        if(type == 0) {
            for (int i = 0, len = radios.length; i < len; i++) {
                if (i != position) {
                    radios[i].setChecked(false);
                } else {
                    radios[i].setChecked(true);
                    etInputAmount.setText("");
                    etInputAmount.clearFocus();
                }
            }
        }else{
            for(int i=0,len=ivPay.length;i<len;i++){
                if(i!=position){
                    ivPay[i].setSelected(false);
                }else{
                    ivPay[i].setSelected(true);
                }
            }
        }
    }



    /**
     * 从后台获取transid
     */
    public void getTransid(final int payType){
        RequestParams reqParams = new RequestParams("cporderid", tradeNum = new Date().getTime()+Session.GetString("mobile"));
        reqParams.put("price", Float.parseFloat(rmb));//Float.parseFloat(rmb)*0.01
        AsyncHttpTask.post(Config.HTTP_URL_HEADED + "apppay/depositForAndriod", reqParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                if (TextUtils.isEmpty(result))
                    return;
                if ("0".equals(response.optString("code"))) {
                    IAppPay.startPay(RechargeActivity.this, new StringBuilder("transid=").append(response.optString("info")).append("&appid=").append(Config.APP_ID_AIBEI).toString().trim(), mIPayResultCallback, payType);
                }
            }
        });
    }

    /**支付回调*/
    IPayResultCallback mIPayResultCallback  = new IPayResultCallback() {

        //@Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {

            String hehe = URLDecoder.decode(signvalue);
            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    boolean isPaySuccess = IAppPayOrderUtils.checkPayResult(signvalue, Config.PUBLIC_KEY_AIBEI);
                    if(isPaySuccess){
                        doRequest();
                    }
                    break;
                case IAppPay.PAY_ERROR:
                    ToastUtil.showTextToast("支付失败");
                    break;
                case IAppPay.PAY_CANCEL:
                    ToastUtil.showTextToast("支付取消");
                    break;
            }
        }

    };

    public void doRequest(){
        RequestParams params = new RequestParams("rmb", rmb);//1
        params.put("tradeNum", tradeNum);
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "user/updateRMB", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                if(0 == json.optInt("code")) {
                    tradeNum = null;
                    Session.SetString("rmb", Integer.parseInt(Session.GetString("rmb")) + Integer.parseInt(rmb));
                    rmb = null;
                    doBusiness();
                    ToastUtil.showTextToast("充值成功");
                    return;
                }else if(8== json.optInt("code")){
                    ToastUtil.showTextToast("订单号。。。。不存在，一会儿删了");
                    return;
                }
                ToastUtil.showTextToast("充值抢币处理中...");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doRequest();
            }
        });
    }
}
