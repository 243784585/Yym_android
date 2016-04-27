package com.shengtao.chat.chatUI.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.shengtao.R;
import com.shengtao.application.UIApplication;
import com.shengtao.chat.chatUI.Constant;
import com.shengtao.chat.chatUI.DemoHXSDKHelper;
import com.shengtao.chat.chatUI.db.UserDao;
import com.shengtao.chat.chatUI.domain.User;
import com.shengtao.chat.chatUI.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* 聊天的基础类
*
* 可 提供注册和登录，单独一个人不认识的人聊天
*
* 单独和一个人聊天不会加成好友
*
* */
public class BaseChatActivity extends BaseActivity {


    public static  String currentUsername;
    public static  String currentPassword;
    public static String toName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 和一个陌生人聊天
     * @param username  用户名
     * @param pwd   密码
     * @param toname  对方的用户名
     */
    public void ChatToSomeone(String username,String pwd,String toname){

        if(!DemoHXSDKHelper.getInstance().isLogined()){
            currentUsername = username;
            currentPassword = pwd;
            toName=toname;

            if (TextUtils.isEmpty(currentUsername)) {
                Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            progressShow = true;
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
            pd.setMessage(getString(R.string.Is_landing));
            pd.show();

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
                    if (!BaseChatActivity.this.isFinishing() && pd.isShowing()) {
                        pd.dismiss();
                    }
                    // 进入主页面
                    startActivity(new Intent(BaseChatActivity.this, ChatActivity.class).putExtra("userId", toName));

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



        startActivity(new Intent(this, ChatActivity.class).putExtra("userId", toName));
    }


    /**
     * 注册 环信
     *
     * @param username,pwd
     * 用户名，密码
     * 用户注册的时候这里就进行注册
     * 之前要注册的时候做验证，这里不进行用户名密码等验证
     */
    public void RegisterEase(String username,String pwd) {

        currentUsername=username;
        currentPassword=pwd;

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer(currentUsername, currentPassword);
                        UIApplication.getContext().setUserName(currentUsername);
                    } catch (final EaseMobException e) {

                        Log.i("ChatDome", e.toString());
                    }
                }
            }).start();
        }
    }


    private boolean progressShow;
    private boolean autoLogin = false;

    /**
     * 登录
     * @param username 用户名
     * @param pwd 密码
     */
    public void login(String username,String pwd) {

        new Thread(new Runnable() {
            public void run() {
                int sleepTime=1500;
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
                    startActivity(new Intent(BaseChatActivity.this, MainActivity.class));
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }

                }
            }
        }).start();



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
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

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
                if (!BaseChatActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // 进入主页面
                Intent intent = new Intent(BaseChatActivity.this, MainActivity.class);
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

}
