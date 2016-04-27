package com.shengtao.mine.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.JsonHttpResponse;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.SMSUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/31 0031.
 */
public class ChangeMobileActivity extends BaseActivity{

    private EditText etMobile;
    private EditText etCode;
    private TextView tvGetCode;
    private String phone;

    Handler handler;


    @Override
    protected int setLayout() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_submit).setOnClickListener(this) ;
        tvGetCode = (TextView) getViewAndSetOnClick(R.id.tv_getCode,this);
        etCode = (EditText) findViewById(R.id.et_code);
        etMobile = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "修改手机号";
    }

    private String getUri(){
        return Config.HTTP_MODULE_MINE + "user/updateInfo";
    }

    private RequestParams getRequestParams(){
        return new RequestParams("mobile",etMobile.getText().toString().trim());
    }

    @Override
    protected void doBusiness() {
        handler  = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //验证码验证成功,请求后台修改手机号
                AsyncHttpTask.post(getUri(), getRequestParams(), new JsonHttpResponse() {
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        new CommonDialog(ChangeMobileActivity.this){

                            @Override
                            protected void afterConfirm() {
                                Session.SetString("mobile", etMobile.getText().toString().trim());
                                finish();
                            }
                        }.setTitle("更改成功", R.color.black, 16f, TypedValue.COMPLEX_UNIT_SP)
                                .setButtonStyle(R.color.dialog_btn_color, 15f, TypedValue.COMPLEX_UNIT_SP)
                                .show();
                    }
                });
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.showSoftInput(etMobile);
    }

    @Override
    public void onClick(View view) {
        Editable text = etMobile.getText();
        if(text == null || text.length() == 0 ){
            ToastUtil.makeText(this, "请输入您的手机号");
            CommonUtil.getFocus(etMobile);
            return;
        }else if(text.length() != 11){
            ToastUtil.makeText(this, "请填写正确的手机号码");
            CommonUtil.getFocus(etMobile);
            return;
        }

        switch (view.getId()){
            case R.id.btn_submit://提交
                if(etCode.getText() != null && etCode.getText().length() > 0) {
                    SMSUtil.submitVerificationCode(text.toString().trim(), etCode.getText().toString().trim(), handler);
                    //下一步执行成功回调(handler中)
                }else{
                    ToastUtil.makeText(this, "请输入验证码");
                    CommonUtil.getFocus(etCode);
                }
                break;
            case R.id.tv_getCode://获取验证码
                phone = etMobile.getText().toString().trim();
                AsyncHttpTask.post(Config.HTTP_MODULE_MINE + "user/checkPhone",new RequestParams("phone",phone),new JsonHttpResponse(){
                    @Override
                    protected void success(Header[] headers, JSONObject json) {
                        if (SMSUtil.doVerifyTime(phone)) {
                            SMSUtil.getVerificationCode(phone, tvGetCode);
                        }
                    }
                });
                break;
        }
    }

}
