package com.shengtao.mine.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.application.UIApplication;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.chat.chatUI.utils.CommonUtils;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.main.MainActivity;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.SMSUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.SessionSms;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by hxhuang on 2015/12/16 0016.
 * 找回密码
 */
public class RetrievePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvGetCode;
    private EditText etCode;
    private EditText etPhone;
    private Button btnSubmit;
    private boolean authorization = false;//判断是第三方登录的绑定页还是重置密码
    private String screen_name;
    private String profile_image_url;
    private Boolean wx;
    private String headimgurl;
    private String nickname;
    private String uid;
    private int type;
    private ProgressDialog pd;
    private boolean progressShow;
    Handler handler;
    public static String currentUsername;
    public static String currentPassword;


    @Override
    protected int setLayout() {
        return R.layout.activity_retrieve_password;
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        if(true == authorization){
            return "绑定";
        }else{
            return "找回密码";
        }
    }

    @Override
    protected void initView() {

        //第三方传来返回的信息
        authorization = getIntent().getBooleanExtra("authorization", false);
        uid = getIntent().getStringExtra("openid");
//        type = getIntent().getIntExtra("type", -1);
//        wx = getIntent().getBooleanExtra("wx", false);
//        if(wx){
//            nickname = getIntent().getStringExtra("nickname");
//            headimgurl = getIntent().getStringExtra("headimgurl");
//        }else{
//            screen_name = getIntent().getStringExtra("screen_name");
//            profile_image_url = getIntent().getStringExtra("profile_image_url");
//        }

        etCode = (EditText) findViewById(R.id.et_code);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvGetCode = (TextView) findViewById(R.id.tv_getCode);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(authorization == true){
                    bindUserLogin();
                    return;
                }
                Intent intent = new Intent(RetrievePasswordActivity.this, ResetPasswordActivity.class);
                intent.putExtra("phone", SessionSms.GetString("verifyPhone").toString().trim());
                startActivity(intent);
                finish();
            }
        };

    }



    @Override
    protected void doBusiness() {
        btnSubmit.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Editable text = etPhone.getText();
        if(text == null || text.length() == 0 ){
            ToastUtil.makeText(this, "请输入您的手机号");
            CommonUtil.getFocus(etPhone);
            return;
        }else if(!text.toString().matches("^1[3-5,7,8][0-9]{9}$")){
            ToastUtil.makeText(this, "请填写正确的手机号码");
            CommonUtil.getFocus(etPhone);
            return;
        }
        switch (v.getId()){
            case R.id.btn_submit:
                if(authorization == true){
                    //绑定手机功能
                    //0，判断是否注册过该应用。1，返回数据，2弹出一个窗口，3点击确定进入主界面
                    if(etCode.getText() != null && etCode.getText().length() > 0) {
                        SMSUtil.submitVerificationCode(text.toString().trim(), etCode.getText().toString().trim(), handler);
                    }else{
                        ToastUtil.makeText(this, "请输入验证码");
                        CommonUtil.getFocus(etCode);
                    }
//                setSession();
                }else{
                    if(etCode.getText() != null && etCode.getText().length() > 0) {
                        SMSUtil.submitVerificationCode(text.toString().trim(), etCode.getText().toString().trim(), handler);
                    }else{
                        ToastUtil.makeText(this, "请输入验证码");
                        CommonUtil.getFocus(etCode);
                    }
                }
                break;
            case R.id.tv_getCode:
                if(authorization == true){
                    //绑定页面直接发验证码而不用验证用户是否存在
                    if(SMSUtil.doVerifyTime(text.toString().trim())) {
                        SMSUtil.getVerificationCode(text.toString().trim(), tvGetCode);
                    }
                }else{
                    AsyncHttpTask.post(Config.HTTP_MODULE_MINE + "user/checkPhone",new RequestParams("phone",text.toString().trim()),new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            if("4".equals(response.optString("code"))){
                                //存在的用户,可以找回密码
                                if(SMSUtil.doVerifyTime(text.toString().trim())) {
                                    SMSUtil.getVerificationCode(text.toString().trim(), tvGetCode);
                                }
                            }else{
                                ToastUtil.makeText(RetrievePasswordActivity.this,"用户未注册");
                            }
                        }
                    });
                }
                break;
        }
    }

    private void bindUserLogin(){
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        RequestParams params = new RequestParams();
        params.put("openid",uid);
        params.put("phone", etPhone.getText().toString());
        LogUtil.e("TestData","uid:"+uid+"phone:"+etPhone.getText().toString());
//        if(wx){
//            params.put("nickname",nickname);
//            params.put("headicon",headimgurl);
//            Log.d("TestData", "nickname" + nickname + "上传绑定" + "headimgurl" + headimgurl);
//        }else{
//            params.put("nickname",screen_name);
//            params.put("headicon", profile_image_url);
//            Log.d("TestData", "screen_name" + screen_name + "上传绑定" + "profile_image_url" + profile_image_url);
//        }
        Log.d("TestData", "nickname" + nickname + "上传绑定" + "headimgurl" + headimgurl + "screen_name" + screen_name + "上传绑定" + "profile_image_url" + profile_image_url);
        AsyncHttpTask.post(Config.HTTP_URL_HEAD+"user/bindPhone", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes, super.getCharset());
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    Log.d("TestData", "code-----" + code );

                    if (code.equals("0")) {
                        setSession(json);
                        login(Session.GetString("mobile"), Session.GetString("password"));
                        Log.d("TestData", "0-----" + code + "0" + "0" + headimgurl + "screen_name" + screen_name + "上传绑定" + "profile_image_url" + profile_image_url);
                    } else {
                        String error = json.optString("error");
                        ToastUtil.makeText(RetrievePasswordActivity.this, error);
                        Log.d("TestData", "code-----" + code + "0" + "0" + headimgurl + "screen_name" + screen_name + "上传绑定" + "profile_image_url" + profile_image_url);
                    }
                    pd.dismiss();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.showTextToast("手机已经存在");
                pd.dismiss();
            }
        });
    }

    private void setSession(JSONObject jsonObject) {
        try {
            LogUtil.d("TestData",jsonObject.toString());
            JSONObject attributes = jsonObject.getJSONArray("attributes").getJSONObject(0);

            Session.SetString("token",jsonObject.optString("token"));          //token
            Session.SetString("id",attributes.optString("id"));             //id
            Session.SetString("headimg",attributes.optString("head_img"));       //头像
            Session.SetString("client_name",attributes.optString("client_name"));    //客户名
            Session.SetString("mobile",attributes.optString("mobile"));         //手机
            Session.SetString("popularize_id",attributes.optString("popularize_id"));  //推广ID
            Session.SetString("rmb",attributes.optString("rmb"));            //抢币
            Session.SetString("integrate_all",attributes.optString("integrate_all"));  //积分
            Session.SetString("ship_address",attributes.optString("ship_address"));  //详细地址
            Session.SetString("city_id",attributes.optString("city_id"));  //城市名
            Session.SetString("ship_name",attributes.optString("ship_name"));  //收货人名称
            Session.SetString("ship_phone",attributes.optString("ship_phone"));  //收货人手机
            Session.SetString("client_qq",attributes.optString("client_qq"));    //qq号
            Session.SetString("login_time",attributes.optString("login_time"));    //登录时间
            Session.SetString("password", attributes.optString("password"));    //密码

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 环信登录
     *
     * @param username 用户名
     * @param pwd      密码
     */
    public void login(String username, String pwd) {

        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = username;
        currentPassword = pwd;

        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        progressShow = true;


        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名密码
                UIApplication.getContext().setUserName(currentUsername);
                UIApplication.getContext().setPassword(currentPassword);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            UIApplication.getContext().logout(null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        UIApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                if (!RetrievePasswordActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }

                // 进入主页面
                Intent intent = new Intent(RetrievePasswordActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
