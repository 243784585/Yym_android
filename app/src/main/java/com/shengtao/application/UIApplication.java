package com.shengtao.application;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.shengtao.R;
import com.shengtao.chat.applib.controller.HXSDKHelper;
import com.shengtao.chat.chatUI.DemoHXSDKHelper;
import com.shengtao.chat.chatUI.db.InviteMessgeDao;
import com.shengtao.chat.chatUI.domain.InviteMessage;
import com.shengtao.chat.chatUI.domain.User;
import com.shengtao.foundation.BaseApplication;
import com.shengtao.foundation.FragmentActivityManager;
import com.shengtao.foundation.SdcardConfig;
import com.shengtao.login.activity.LoginActivity;
import com.shengtao.snache.activity.CommonQuestionActivity;
import com.shengtao.utils.Constants;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * 扩展Android Application
 *
 * @author dane55
 */
public class UIApplication extends BaseApplication {
    private static UIApplication uiApplication;

    // login user name
    public final String PREF_USERNAME = "username";

    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;


//    public ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    public static InviteMessgeDao dao;
    @Override
    public void onCreate() {
        if (Constants.Config.DEVELOPER_MODE
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyDeath().build());
        }
        super.onCreate();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        initImageLoader(getApplicationContext());
        SdcardConfig.getInstance().initSdcard();
        uiApplication = this;
        FragmentActivityManager.initActivityManager();

        //初始化环信
        hxSDKHelper.onInit(this);

        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

        EMContactManager.getInstance().setContactListener(new MyContactListener());
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        EMChat.getInstance().setAppInited();
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);

        hxSDKHelper.notifyForRecevingEvents();

        dao = new InviteMessgeDao(this);

        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);

//        startService(new Intent(this, StartService.class));
    }

    public static UIApplication getContext() {
        return uiApplication;
    }

    /**
     * 初始化ImageLoader的与配置
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        //初始化页面管理
        FragmentActivityManager.initActivityManager();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .imageDownloader(new BaseImageDownloader(context)) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getDefaultOptions() {
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(R.drawable.default_user_icon)
                        // 加载异常时显示图片
                .showImageOnFail(R.drawable.default_user_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(5))
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(R.drawable.default_user_icon)
                .build();
    }

    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(R.drawable.default_user_icon)
                        // 加载异常时显示图片
                .showImageOnFail(R.drawable.default_user_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(5))
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(R.drawable.default_user_icon)
                .build();
    }


    public static DisplayImageOptions getOptionsForRound() {
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(R.drawable.default_user_icon)
                        // 加载异常时显示图片
                .showImageOnFail(R.drawable.default_user_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(60))
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(R.drawable.default_user_icon)
                .build();
    }


    public static DisplayImageOptions getAvatar(){
        return new DisplayImageOptions.Builder()
                // 初始化默认图片
                .showImageOnLoading(R.drawable.default_user_icon)
                        // 加载异常时显示图片
                .showImageOnFail(R.drawable.default_user_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(5))
                        // Uri地址为空时显示图片
                .showImageForEmptyUri(R.drawable.default_user_icon)
                .build();
    }
    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param user
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public static void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);
    }



    //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();

            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            }
        }
    }

    public static void notication(String content){
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "新消息";
//      notification.defaults = Notification.DEFAULT_SOUND;
//      notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        Intent intent = new Intent(getContext(), CommonQuestionActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notification.setLatestEventInfo(getContext(), "消息通知", "" + content, pendingIntent);
        manager.notify(R.drawable.app_icon, notification);
    }

    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Log.e("add"," // 保存增加的联系人");
        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Log.e("add"," 被删除");

        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
            Log.e("add", " 接到邀请的消息");
            dao.deleteMessage(username);
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
            dao.saveMessage(msg);
            notication(username + "申请加为好友");
        }

        @Override
        public void onContactAgreed(String username) {
            //同意好友请求
            Log.e("add", " //同意好友请求");
//            InviteMessage message = new InviteMessage();
//            message.setFrom(username);
//            notication(username + "您的好友请求");
        }

        @Override
        public void onContactRefused(String username) {
            // 拒绝好友请求
            Log.e("add"," // 拒绝好友请求");
            notication(username + "拒绝您的好友请求");
        }

    }


    /**
     * 连接监听listener
     *
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

            // in case group and contact were already synced, we supposed to notify sdk
//            //we are ready to receive the events
//            if(groupSynced && contactSynced){
//                new Thread(){
//                    @Override
//                    public void run(){
//                        HXSDKHelper.getInstance().notifyForRecevingEvents();
//                    }
//                }.start();
//            }else{
//                if(!groupSynced){
//                    asyncFetchGroupsFromServer();
//                }
//
//                if(!contactSynced){
//                    asyncFetchContactsFromServer();
//                }
//
//                if(!HXSDKHelper.getInstance().isBlackListSyncedWithServer()){
//                    asyncFetchBlackListFromServer();
//                }
//            }
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    chatHistoryFragment.errorItem.setVisibility(View.GONE);
//                }
//            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
                showAccountRemovedDialog();
            } else if (error == EMError.CONNECTION_CONFLICT) {
                // 显示帐号在其他设备登陆dialog
                showConflictDialog();
            }
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        UIApplication.getContext().logout(null);
        String st = getResources().getString(R.string.Logoff_notification);
        // clear up global variables
        try {
//            if (conflictBuilder == null)
//                conflictBuilder = new android.app.AlertDialog.Builder(this);
//                conflictBuilder.setTitle(st);
//                conflictBuilder.setMessage(R.string.connect_conflict);
//                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    conflictBuilder = null;
//
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    Session.ClearSession();
//                }
//            });
//            conflictBuilder.setCancelable(false);
//            conflictBuilder.create().show();
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//            ToastUtil.makeText(getApplicationContext(), "账号退出");
            isConflict = true;
        } catch (Exception e) {
            EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
        }
    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        UIApplication.getContext().logout(null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        // clear up global variables
        try {
            if (accountRemovedBuilder == null)
                accountRemovedBuilder = new android.app.AlertDialog.Builder(this);
            accountRemovedBuilder.setTitle(st5);
            accountRemovedBuilder.setMessage(R.string.em_user_remove);
            accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    accountRemovedBuilder = null;
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Session.ClearSession();
                }
            });
            accountRemovedBuilder.setCancelable(false);
            accountRemovedBuilder.create().show();
            isCurrentAccountRemoved = true;
        } catch (Exception e) {
            EMLog.e("", "---------color userRemovedBuilder error" + e.getMessage());
        }
    }
}
