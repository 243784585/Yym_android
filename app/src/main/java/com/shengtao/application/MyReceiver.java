package com.shengtao.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.shengtao.main.MainActivity;
import com.shengtao.snache.activity.BgNoticeActivity;
import com.shengtao.snache.activity.MessageSendGoodActivity;
import com.shengtao.snache.activity.MessageWinning;
import com.shengtao.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * @Description
 * @Created by YCH on 2015/10/31.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle, context));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            String result = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            if (TextUtils.isEmpty(result))
                //showEmptyView("暂时没有帖子哦...");
                return;
            try {
                JSONObject json = new JSONObject(result);
                String type = json.optString("type");
                String content = json.optString("content");

//                Message msg = MainActivity.mHandler1.obtainMessage();
//                msg.obj=type;
//                MainActivity.mHandler1.sendMessage(msg);
//                Session.SetString("type", type);
//
//                Message msg2 = MainActivity.mHandler2.obtainMessage();
//                msg2.obj=content;
//                MainActivity.mHandler2.sendMessage(msg2);
//                Session.SetString("content", content);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            Log.d(TAG, "type"+JPushInterface.EXTRA_NOTI_TYPE);
            Log.d(TAG, "title"+JPushInterface.EXTRA_NOTIFICATION_TITLE);

//            Session.SetBoolean("isMessage", true);
            String notifaction = bundle.getString(JPushInterface.EXTRA_ALERT);
            if(notifaction.endsWith("中奖了")){
                Session.SetInt("iswinMessage", Session.GetInt("iswinMessage")+1);
            }else if(notifaction.endsWith("收货了") || notifaction.endsWith("发货了")){
                Session.SetInt("issendMessage", Session.GetInt("issendMessage") + 1);
            }else if (notifaction.endsWith("系统公告")){
                Session.SetBoolean("isSysMessage", true);
            }
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);
            String notifaction = bundle.getString(JPushInterface.EXTRA_ALERT);
            if(notifaction.endsWith("中奖了")){
                Intent i = new Intent(context, MessageWinning.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }else if(notifaction.endsWith("收货了")){
                Intent i = new Intent(context, MessageSendGoodActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);
            }else if(notifaction.endsWith("发货了")){
                Intent i = new Intent(context, MessageSendGoodActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);
            }else if(notifaction.endsWith("系统公告")){
                Intent i = new Intent(context,BgNoticeActivity.class);
                i.putExtra("title","系统公告");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);
            }else{
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle, Context context) {
        StringBuilder sb = new StringBuilder();

//        String result = bundle.get("cn.jpush.android.ALERT").toString();
//        Log.e("------------",result);
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("key:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("key:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("key:" + key + ", value:" + bundle.getString(key));
            }

            if (key.equals("cn.jpush.android.ALERT")) {
                Log.e("-------------", bundle.getString(key));
//                notifitionMessage(context,bundle.getString(key));
            }
        }
        return sb.toString();
    }

//    private static void notifitionMessage(Context context,String value){
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Notification notification = new Notification();
//        notification.icon = R.mipmap.ic_launcher;
//        notification.tickerText = "消息通知";
//        notification.contentIntent = pendingIntent;
////      notification.defaults = Notification.DEFAULT_SOUND;
////      notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
//
//
////        notification.setLatestEventInfo(context, "消息通知", value, pendingIntent);
//        manager.notify(0, notification);
//    }
//    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
//    }
}
