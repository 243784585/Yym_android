package com.shengtao.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 分享
 * Created by TT on 2015/1/3.
 */
public class ShareUtils1 {

    public static UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public static void haha(Context context,Activity activity){

        //wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        //添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
        wxHandler.addToSocialSDK();
        wxHandler.setTargetUrl("http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");
        //添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 设置分享内容
        mController.setShareContent("嗨抢一个只需要1元就可以完成梦想的购物天堂");
        mController.setShareMedia(new UMImage(context, R.drawable.sharelogo)); // 设置分享图片内容

        //QQ分享
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, Config.APP_ID_QQ, Config.APP_KEY_QQ);
        qqSsoHandler.addToSocialSDK();
        qqSsoHandler.setTargetUrl("");
        //空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, Config.APP_ID_QQ, Config.APP_KEY_QQ);
        qZoneSsoHandler.addToSocialSDK();

        //打开分享面板
//        mController.openShare(MessageActivity.this,false);
    }

    /**
     * 邀请好友分享
     * @param context
     * @param share
     * @param ActiveId
     */
    public static void shareActive(final Context context, final SHARE_MEDIA share) {
//        mController.getConfig().closeToast();
//        mController.setShareContent("嗨抢一个只需要1元就可以完成梦想的购物天堂");
//        mController.setShareMedia(new UMImage(UIApplication.getContext(), R.drawable.sharelogo
//        ));
//        mController.setAppWebSite(share, "http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");
        mController.postShare(context, share,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                        if (eCode == 200) {
                            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();

                        } else {
                            String eMsg = "";
                            if (eCode == -101) {
                                eMsg = "没有授权";
                            }
                        }
                    }
                });
    }

    public static void shareCircle(final Context context, final SHARE_MEDIA share, final String zgoodsId){
        // 添加微信朋友圈
        mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                        if (eCode == 200) {
                            String url = Config.HTTP_URL_HEADED + "Zerogoods/SharegetZeroCode";
                            RequestParams reqParams = new RequestParams();
                            reqParams.put("zgoodsId", zgoodsId);
                            AsyncHttpTask.post(url, reqParams, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    String result = response.toString();
                                    if (TextUtils.isEmpty(result))
                                        return;
                                    if ("0".equals(response.optString("code"))) {
                                        //添加成功之后需要重新请求一次数据
                                    } else if ("7".equals(response.optString("code"))) {
                                        ToastUtil.showTextToast("获得过夺宝码了哦");
                                    } else {
                                    }
                                }
                            });

                        } else {
                            String eMsg = "";
                            if (eCode == -101) {
                                eMsg = "没有授权";
                            }
                        }
                    }
                });
    }
}
