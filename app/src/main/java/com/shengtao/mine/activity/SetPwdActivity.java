package com.shengtao.mine.activity;

import android.content.Intent;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.login.activity.LoginActivity;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.MD5Util;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/15 0015.
 * 设置密码
 */
public class SetPwdActivity extends BaseActivity{

    private EditText etLoginPassword;
    private EditText etInvitationCode;      //邀请码  例:    476000
    private Button btnSubmit;
    private String phone;

    private final String HTTP_REGISTER_URL = Config.HTTP_URL_HEAD + "user/register";


    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "注册";
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_register_setpwd;
    }

    @Override
    protected void initView() {
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        etInvitationCode = (EditText) findViewById(R.id.et_invitation_code);
        btnSubmit = (Button) findViewById(R.id.btn_submit_register);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    @Override
    protected void doBusiness() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit_register:
                //用户注册提交
                btnSubmit.setClickable(false);
                register();
                break;
        }
    }

    /**
     * 提交注册信息
     */
    private void register() {
        final Editable pwd = etLoginPassword.getText();
        if( pwd != null && pwd.length() > 0){
            if(pwd.length() >= 6){
                RequestParams reqParams = new RequestParams();
                reqParams.add("phone",phone);
                reqParams.add("password", MD5Util.Md5(pwd.toString().trim()));
                if(etInvitationCode.getText() != null && etInvitationCode.getText().length() > 0){
                    reqParams.add("popularizeId",etInvitationCode.getText().toString().trim());
                }

                AsyncHttpTask.post(HTTP_REGISTER_URL, reqParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(0==response.optInt("code")){
                            Intent intent = new Intent(SetPwdActivity.this, LoginActivity.class);
                            intent.putExtra("just_login",true);
                            intent.putExtra("mobile",phone);
                            intent.putExtra("password",MD5Util.Md5(pwd.toString()));
                            ToastUtil.makeText(SetPwdActivity.this,"注册成功");
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else{
                ToastUtil.makeText(this, "密码长度为6-20位之间");
            }
        }else{
            ToastUtil.makeText(this, "密码不能为空");
        }
    }

    @Override
    public void onBackPressed() {
        new CommonDialog(this) {
            @Override
            protected void afterConfirm() {
                finish();
            }
        }.setSubTitle("确定退出注册吗?",R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                .setButtonStyle(R.color.dialog_btn_color,15f,TypedValue.COMPLEX_UNIT_SP)
                .setCancle("取消")
                .setConfirm("确定")
                .show();
    }
}
