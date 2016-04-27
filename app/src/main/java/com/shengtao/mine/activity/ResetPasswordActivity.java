package com.shengtao.mine.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
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
import com.shengtao.utils.MD5Util;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/16 0016.
 *
 * desc:重置密码
 */
public class ResetPasswordActivity extends BaseActivity{

    private Button btnLogin;
    private EditText etLoginPassword;
    ProgressDialog pd;

    private final String HTTP_RESETPWD_URL = Config.HTTP_URL_HEAD + "user/passwordReset";

    @Override
    protected int setLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected String getAvtionTitle() {
        return "重置密码";


    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected void initView() {


        btnLogin = (Button) findViewById(R.id.btn_login_reset);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password_reset);
        pd = new ProgressDialog(this);
    }

    @Override
    protected void doBusiness() {
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login_reset){
            Editable text = etLoginPassword.getText();
                //重置密码功能
            if(text != null && text.length() > 0){
                if(text.length() >= 6){
                    resetPwd(text.toString().trim());
                }else{
                    ToastUtil.makeText(this,"密码必须大于6位");
                }
            }else{
//                Toast.makeText(this,R.string.Password_cannot_be_empty,Toast.LENGTH_SHORT).show();
                ToastUtil.showTextToast("密码不能为空！");
            }
        }
    }

    /**
     * 提交重置密码信息
     * @param password
     */
    private void resetPwd(final String password) {
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        RequestParams reqParams = new RequestParams();
        reqParams.add("phone",getIntent().getStringExtra("phone").trim());
        reqParams.add("newPassword", MD5Util.Md5(password));

        AsyncHttpTask.post(HTTP_RESETPWD_URL, reqParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String code = response.getString("code");
                    if("0".equals(code)){
                        //重置密码成功
                        ToastUtil.makeText(ResetPasswordActivity.this,"重置密码成功");
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.putExtra("just_login",true);
                        intent.putExtra("mobile",getIntent().getStringExtra("phone").trim());
                        intent.putExtra("password",MD5Util.Md5(password));
                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtil.makeText(ResetPasswordActivity.this,response.optString("error"));
                        startActivity(new Intent(ResetPasswordActivity.this, RetrievePasswordActivity.class));
                        finish();
                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
