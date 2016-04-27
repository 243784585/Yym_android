package com.shengtao.login.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.shengtao.chat.chatUI.Constant;
import com.shengtao.chat.chatUI.db.UserDao;
import com.shengtao.chat.chatUI.domain.User;
import com.shengtao.chat.chatUI.utils.CommonUtils;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.main.MainActivity;
import com.shengtao.mine.activity.RegisterActivity;
import com.shengtao.mine.activity.RetrievePasswordActivity;
import com.shengtao.utils.CommonDialog;
import com.shengtao.utils.CommonUtil;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.MD5Util;
import com.shengtao.utils.Session;
import com.shengtao.utils.ShareUtils;
import com.shengtao.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

import static com.shengtao.R.id;

public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private Button btnLogin;
    private TextView tvForgetPwd;
    private TextView tv2Register;
    private EditText etUser;
    private EditText etPassword;
    public static String currentUsername;
    public static String currentPassword;

    private String uid;
    private int type;

    private boolean progressShow;
    ProgressDialog pd;
    private final String HTTP_LOGIN_URL = Config.HTTP_MODULE_LOGIN + "user/login";

    private boolean just_login = false;
    private String cancelType;

    //从第三方获取到的头像和昵称
    private String profile_image_url;
    private String screen_name;

    private String nickname;
    private String headimgurl;
    private Boolean wx = false;

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            cancelType = getIntent().getStringExtra("type");
        } catch (Exception e) {
            cancelType = "0";
        }

        if ("1".equals(cancelType)) {
            myDialog();
            return;
        }

        //SDK配置
        ShareUtils.configPlatforms(LoginActivity.this, "http://www.baidu.com");
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        btnLogin = (Button) findViewById(id.btn_login);
        tvForgetPwd = (TextView) findViewById(id.tv_forget_password);
        tv2Register = (TextView) findViewById(id.tv_to_register);
        etUser = (EditText) findViewById(id.et_user);
        etPassword = (EditText) findViewById(id.et_password);

        just_login = getIntent().getBooleanExtra("just_login", false);
        pd = new ProgressDialog(this);

        //后台环信注册好后解开
        if (just_login) {
            String i = getIntent().getStringExtra("mobile");
            String j = getIntent().getStringExtra("password");

//            if(doVerify()){
//                pd.setCanceledOnTouchOutside(false);
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        progressShow = false;
//                    }
//                });
//                pd.setMessage(getString(R.string.Is_landing));
//                pd.show();
//
//                    RequestParams reqParams = new RequestParams();
//                    reqParams.add("phone",etUser.getText().toString());
//                    reqParams.add("password", MD5Util.Md5(etPassword.getText().toString()));
//
//                    AsyncHttpTask.post(HTTP_LOGIN_URL, reqParams, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            showUIData(response);
//                        }
//                    });
//                }


                RequestParams reqParams = new RequestParams();
                reqParams.add("phone",i);
                reqParams.add("password", j);

                AsyncHttpTask.post(HTTP_LOGIN_URL, reqParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        showUIData(response);
                    }
                });
            }
            return;


    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "个人中心";
    }


    @Override
    protected void doBusiness() {
        btnLogin.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tv2Register.setOnClickListener(this);
        findViewById(R.id.iv_qq).setOnClickListener(this);
        findViewById(R.id.iv_weibo).setOnClickListener(this);
        findViewById(R.id.iv_wechat).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:
                //点击了登录按钮
                if(doVerify()){
                    InputMethodManager systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    systemService.hideSoftInputFromWindow(etPassword.getWindowToken(),0);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressShow = false;
                        }
                    });
                    pd.setMessage(getString(R.string.Is_landing));
                    pd.show();

                    RequestParams reqParams = new RequestParams();
                    reqParams.add("phone",etUser.getText().toString());
                    reqParams.add("password", MD5Util.Md5(etPassword.getText().toString()));

                    AsyncHttpTask.post(HTTP_LOGIN_URL, reqParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            showUIData(response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            ToastUtil.showTextToast("连接超时，请重新登录");
                            pd.dismiss();
                        }
                    });
                }
                break;
            case R.id.tv_forget_password:
                //点击了忘记密码
                startActivity(new Intent(this,RetrievePasswordActivity.class));
                break;
            case R.id.tv_to_register:
                //点击了免费注册
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.iv_qq:
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.Is_landing));
                pd.show();
                login(SHARE_MEDIA.QQ);
                break;
            case R.id.iv_weibo:
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.Is_landing));
                pd.show();
                login(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_wechat:
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.Is_landing));
                pd.show();
                login(SHARE_MEDIA.WEIXIN);
                break;
        }
    }


    /**
     * 授权。如果授权成功，则获取用户信息
     *
     * @param platform
     */
    private void login(final SHARE_MEDIA platform) {

        try {
            ShareUtils.mController.doOauthVerify(LoginActivity.this, platform, new SocializeListeners.UMAuthListener() {
                @Override
                public void onError(SocializeException e, SHARE_MEDIA platform) {
                    Toast.makeText(LoginActivity.this, "授权错误异常", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

                @Override
                public void onComplete(Bundle value, final SHARE_MEDIA platform) {
                    pd.dismiss();
                    if (value != null) {
                        uid = value.getString("uid");
                        LogUtil.e("TestData", " --- uid"+uid);
//                        Toast.makeText(LoginActivity.this, "授权成功.", Toast.LENGTH_SHORT).show();
                        ShareUtils.mController.getPlatformInfo(LoginActivity.this, platform, new SocializeListeners.UMDataListener() {
                            @Override
                            public void onStart() {
//                                Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                pd.dismiss();
                                if (status == 200 && info != null) {
                                    StringBuilder sb = new StringBuilder();
                                    Set<String> keys = info.keySet();
                                    for (String key : keys) {
                                        sb.append(key + "=" + info.get(key).toString() + "\r\n");
                                        Log.d("TestData", sb.toString());
                                    }
                                    Log.d("TestData", info.toString());
                                    //解析的时候，微信是这个字段
                                    if (platform == SHARE_MEDIA.WEIXIN) {
                                        nickname = info.get("nickname").toString();
                                        headimgurl = info.get("headimgurl").toString();//这里是获取数据的，而且已经获取到了
                                        wx = true;
                                        Log.d("TestData", "wx" + wx + "nickname" + nickname + "----headimgurl" + headimgurl);
                                    } else {
                                        //qq呵微博是这个字段,所以拿这个区分下
                                        screen_name = info.get("screen_name").toString();
                                        profile_image_url = info.get("profile_image_url").toString();//
                                        wx = false;
                                        Log.d("TestData", "wx" + wx + "screen_name" + screen_name + "----profile_image_url" + profile_image_url);
                                    }

                                    switch (platform) {
                                        case QQ:
                                            type = 1;
                                            break;
                                        case WEIXIN:
                                            type = 2;
                                            break;
                                        case SINA:
                                            type = 3;
                                            break;
                                    }
                                    login_extra();
                                    Log.d("TestData", sb.toString() + "最火");
                                    //现在是这一步，目的是将这个数据传给绑定页面，进行http请求
                                } else {
                                    Log.d("TestData", "发生错误：" + status);
                                }
                            }
                        });


                    } else {
                        Toast.makeText(LoginActivity.this, "授权失败，请点击注册", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
                    pd.dismiss();
                }

                @Override
                public void onStart(SHARE_MEDIA platform) {
//                    Toast.makeText(LoginActivity.this, "获取平台数据开始... ", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }

    private void login_extra() {
        RequestParams extraParams = new RequestParams();
        extraParams.put("openid", uid);
        if (wx) {//这个是用来区分qq,微博，和微信的
            extraParams.put("name", nickname);
            extraParams.put("headurl", headimgurl);
            Log.d("TestData", "wx1" + wx + "nickname" + nickname + "----headimgurl" + headimgurl);
        } else {
            extraParams.put("name", screen_name);
            extraParams.put("headurl", profile_image_url);
            Log.d("TestData", "wx1" + wx + "screen_name" + screen_name + "----profile_image_url" + profile_image_url);
        }

        //现在走到了绑定这步
        AsyncHttpTask.post(Config.HTTP_URL_HEAD + "user/thirdLogin", extraParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
//                    Toast.makeText(LoginActivity.this, "验证完成", Toast.LENGTH_SHORT).show();
                    String result = new String(bytes, super.getCharset());
                    JSONObject json = new JSONObject(result);
                    //打印json
                    LogUtil.d("TestData", json.toString());//zheyang? zouyibian
                    String code = json.optString("code");
                    if ("0".equals(code)) { //验证授权成功
                        LogUtil.d("TestData", 0 + "");//你的手机号注册了吗，我的数据关联太多不能删，我把你的删掉吧test
//                        Toast.makeText(LoginActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
                        showUIData(json);
//                        login(Session.GetString("phone"), Session.GetString("password"));
                    } else if ("5".equals(code)) { //验证授权失败
                        LogUtil.d("TestData", 5 + "");//zheyang?
                        //跳转绑定注册账号页面
//                        Toast.makeText(LoginActivity.this, "正在绑定", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
                        intent.putExtra("authorization", true);
                        intent.putExtra("openid", uid);
                        startActivity(intent);
                    } else if ("1".equals(code)) {
                        //第一次三方登录生成数据表成功
                        LogUtil.d("TestData", 1 + "");//zheyang?
                        //跳转绑定陌途注册账号页面
//                        Toast.makeText(LoginActivity.this, "正在绑定", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
                        intent.putExtra("authorization", true);
                        intent.putExtra("openid", uid);
                        startActivity(intent);
                    } else if ("3".equals(code)) {
                        //重新绑定
                        LogUtil.d("TestData", 3 + "");//zheyang?
                        login_extra();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 环信登录
     * @param username 用户名
     * @param pwd      密码
     */
    public void login(String username, String pwd) {
        Log.e("------------", username + "||" + pwd);
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = username;
        currentPassword = pwd;
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
                    // 处理好友和群组
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!just_login) {
//                                pd.dismiss();
                            }
                            Session.ClearSession();
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
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    if (!just_login) {
//                            pd.dismiss();
                    }
                }
                pd.dismiss();
//              //登录成功,跳转到主页面
                startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                pd.dismiss();
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!just_login) {
//                        pd.dismiss();
                        }
                        Session.ClearSession();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);
        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 存入内存
        UIApplication.getContext().setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showUIData(JSONObject json) {
        try {
            String code = json.getString("code");
            Toast.makeText(this, code, Toast.LENGTH_SHORT);
            if("0".equals(code)){
                //登录成功,保存信息到Session
                JSONObject attributes = json.getJSONArray("attributes").getJSONObject(0);
                int info = json.optInt("info");

                Session.SetString("token",json.optString("token"));          //token
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
                Session.SetString("client_qq","null".equals(attributes.optString("client_qq"))?"":attributes.optString("client_qq"));    //qq号
                Session.SetString("password",attributes.optString("password"));
                Session.SetInt("cartnum",info);

                //登录环信
                if(just_login){
                    login(Session.GetString("mobile"), getIntent().getStringExtra("password"));
                }else{
                    login(Session.GetString("mobile"), Session.GetString("password"));//MD5Util.Md5(etPassword.getText().toString()
                }
                JPushInterface.setAlias(this, Session.GetString("id"), null);

            }else if("8".equals(code)){
                ToastUtil.makeText(this,"密码错误");
                pd.dismiss();
            }else {
                ToastUtil.makeText(this,json.optString("error"));
                pd.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //做数据校验
    private boolean doVerify(){
        boolean flag = false;
        String user = etUser.getText().toString();
        String password = etPassword.getText().toString();
        if(user != null && !user.isEmpty()){
            //用户名
            if(user.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")){
                if(password != null && !password.isEmpty()){
                    if(password.length() >= 6){
                        flag = true;
                    }else{
                        ToastUtil.makeText(this,"密码长度不符合规则");
                        CommonUtil.getFocus(etPassword);
                    }
                }else{
                    ToastUtil.makeText(this,R.string.Password_cannot_be_empty);
                    CommonUtil.getFocus(etPassword);
                }

            }else{
                ToastUtil.makeText(this, "请输入正确的手机号");
                //用户名获取焦点
                CommonUtil.getFocus(etUser);
            }
        }else{
            ToastUtil.makeText(this, R.string.login_register_phone_empty);
            //用户名获取焦点
            CommonUtil.getFocus(etUser);
        }
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = ShareUtils.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (requestCode == 1001) {
            Toast.makeText(LoginActivity.this, "onActivityResult", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 异地登录弹出框
     */
    public void myDialog() {
        Session.ClearSession();
        JPushInterface.setAlias(this, "", null);//取消极光推送的消息
        new CommonDialog(this) {
            @Override
            protected void afterConfirm() {
                dismiss();
            }
        }.setTitle("消息提示",R.color.black,16f, TypedValue.COMPLEX_UNIT_SP)
                .setSubTitle("账号在异地登录..", R.color.black,11f,TypedValue.COMPLEX_UNIT_SP)
                .setButtonStyle(R.color.dialog_btn_color,15f,TypedValue.COMPLEX_UNIT_SP)
                .setConfirm("确定")
                .show();
    }
}
