package com.shengtao.mine.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.SMSUtil;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/15 0015.
 */
public class RegisterActivity extends BaseActivity{

    private Button btnGetCode;
    private CheckBox cbIsAgreed;
    private TextView tvAgreenment;
    private EditText etPhone;
    private String phone;
    /**0代表注册第一步,1代表注册第二步*/
    private int curIndex = 0;


    private TextView tvAlert;
    private EditText etCode;
    private TextView tvAutoTimeout;
    private Button btnNext;

    Handler handler;
    private CountDownTimer countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        doBusiness();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(RegisterActivity.this, SetPwdActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }

        };
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_register_first;
    }

    @Override
    protected String getAvtionTitle() {
        return "注册";
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void initView() {
        btnGetCode = (Button) findViewById(R.id.btn_get_code);
        cbIsAgreed = (CheckBox) findViewById(R.id.cb_is_agree);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvAgreenment = (TextView) findViewById(R.id.tv_user_agreement);

        tvAgreenment.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);    //添加下划线
//        tvAgreenment.setText("用户协议");


        tvAlert = (TextView) findViewById(R.id.tv_alert);
        etCode = (EditText) findViewById(R.id.et_code);
        tvAutoTimeout = (TextView) findViewById(R.id.tv_auto_timeout);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void doBusiness() {
        btnGetCode.setOnClickListener(this);
        tvAgreenment.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tvAutoTimeout.setOnClickListener(this);
        tvAutoTimeout.setClickable(false);
    }

    private String getUri(){
        return Config.HTTP_MODULE_MINE + "user/checkPhone";
    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        params.add("phone",phone);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_get_code:
                //获取验证码
                String phone = etPhone.getText().toString();
                //是否已输入手机号
                if(phone != null && !phone.isEmpty()) {
//                    if(phone.matches("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$")) {
                    if(phone.matches("^1[3-5,7,8][0-9]{9}$")) {
                        //是否已勾选同意用户协议
                        if (cbIsAgreed.isChecked()) {

                            this.phone = phone;

                            //先请求后台服务器,判断手机号是否可以注册
                            AsyncHttpTask.post(getUri(), getRequestParams(), new JsonHttpResponse() {
                                @Override
                                protected void success(Header[] headers, JSONObject json) {
                                    if(SMSUtil.doVerifyTime(RegisterActivity.this.phone.trim())) {
                                        countDown = SMSUtil.getVerificationCode(RegisterActivity.this.phone.trim(), tvAutoTimeout);
                                        tvAlert.setText("验证码已发至" + RegisterActivity.this.phone.trim());
                                        findViewById(R.id.rl_register_1).setVisibility(View.GONE);
                                        findViewById(R.id.rl_register_2).setVisibility(View.VISIBLE);
                                        curIndex = 1;
                                    }
                                }
                            });




                        } else {
                            ToastUtil.makeText(this, R.string.login_register_read_common);
                        }
                    }else{
                        ToastUtil.makeText(this, "请输入正确手机号");
                    }
                }else{
                    ToastUtil.makeText(this, "请输入手机号码");
                }
                break;
            case R.id.tv_user_agreement:
                ToastUtil.makeText(this, "you win!");
                break;
            case R.id.btn_next:
                //下一步(提交验证码)
                if(etCode.getText()!= null && etCode.getText().length() > 0) {
                    //提交验证码
                    SMSUtil.submitVerificationCode(this.phone, etCode.getText().toString().trim(), handler);
                }else{
                    ToastUtil.makeText(this, R.string.smssdk_code_empty);
                }
                break;
            case R.id.tv_auto_timeout:
                //重新获取验证码
                SMSUtil.getVerificationCode(this.phone, tvAutoTimeout);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if(curIndex == 0){
            new CommonDialog(this) {
                @Override
                protected void afterConfirm() {
                    finish();
                }
            }.setTitle("确定退出注册吗?",R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                    .setButtonStyle(R.color.dialog_btn_color,15f,TypedValue.COMPLEX_UNIT_SP)
                    .setCancle("取消")
                    .setConfirm("确定")
                    .show();
        }else {
            //回到注册页面的第一步,并停止倒计时
            findViewById(R.id.rl_register_1).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_register_2).setVisibility(View.GONE);
            countDown.cancel();
            curIndex = 0;
        }
    }
}
