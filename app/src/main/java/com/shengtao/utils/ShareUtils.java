package com.shengtao.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.application.UIApplication;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 分享
 * Created by TT on 2015/10/3.
 */
public class ShareUtils {

    public static UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public static void configPlatforms(Activity activity,String url) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());//设置新浪SSO handler
        addQQQZonePlatform(activity, url, null);
        addWXPlatform(activity, url, null);
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
//        mController.openShare(activity, false);ShareUtils
    }

    public static void configPlatforms(Activity activity,String url,String title) {
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());//设置新浪SSO handler
        addQQQZonePlatform(activity, url, null);
        addWXPlatform(activity, url, title);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
//        mController.openShare(activity, false);
    }

    //我的推广
    public static void configPlatforms(Activity activity,String url,String nickname, String cover) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());//设置新浪SSO handler
        addQQQZonePlatform(activity, url, null);
        addWXPlatform(activity, url, nickname, cover);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
//      mController.openShare(activity, false);
    }

    //游记
    public static void configPlatforms(Activity activity,String url,String title,String nickname,String city,String cover) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());//设置新浪SSO handler
        addQQQZonePlatform(activity, url, title);
        addWXPlatform(activity, url, title,nickname,city,cover);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
//        mController.openShare(activity, false);
    }

    //活动
    public static void configPlatforms(Activity activity,String url,String title,String time,String from,String to,String cover) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());//设置新浪SSO handler
        addQQQZonePlatform(activity, url, title);
        addWXPlatform(activity, url, title,time,from,to,cover);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
//        mController.openShare(activity, false);
    }
    public static void addQQQZonePlatform(Activity activity,String url,String title) {
        String appId = "1105052158";//100424468
        String appKey = "rOS4DzXYVz74dgEN";//c7394704798a158208a74ab60104f0ba

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, appId, appKey);
        qqSsoHandler.setTargetUrl("http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");
        if(title != null){
            qqSsoHandler.setTitle(title);
        }
        //"http://app.etripbon.com/appdown.htm"
        qqSsoHandler.addToSocialSDK();

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId, appKey);
        qZoneSsoHandler.setTargetUrl("http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");
        qZoneSsoHandler.addToSocialSDK();
    }

    public static void addWXPlatform(Context context,String url,String title) {
        String appId = "wx9d75dd3790c426a8";//wxd81212f3f3939d91
        String appSecret = "1895683fc68add032edc3ace17e71c2a";//ba3f45ae3ac3810112bbdaa7a20c29ec

        // 设置分享内容
        mController.setShareContent("嗨抢一个只需要1元就可以完成梦想的购物天堂");
        mController.setShareMedia(new UMImage(context, R.drawable.sharelogo)); // 设置分享图片内容

        UMWXHandler wxHandler = new UMWXHandler(context, Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
        wxHandler.addToSocialSDK();
        wxHandler.setTargetUrl("http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");

        UMWXHandler wxCircleHandler = new UMWXHandler(context, Config.APP_ID_WECHAT, Config.APP_SECRET_WECHAT);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.setTitle("嗨抢一个只需要1元就可以完成梦想的购物天堂");
        wxCircleHandler.addToSocialSDK();
        wxCircleHandler.setTargetUrl("http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");

        // 添加微信分享文字
//        WeiXinShareContent WeixinShareContent1 = new WeiXinShareContent();
//        WeixinShareContent1.setTitle("嗨抢一个只需要1元就可以完成梦想的购物天堂");
//        WeixinShareContent1.setTargetUrl("https://www.baidu.com");
//        mController.setShareMedia(WeixinShareContent1);

        // 朋友圈分享文字
//        CircleShareContent CircleShareContent1 = new CircleShareContent();
//        CircleShareContent1.setTitle(title);
//        CircleShareContent1.setTargetUrl(url);
//        mController.setShareMedia(CircleShareContent1);
//        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
//                SHARE_MEDIA.DOUBAN);

    }

    //我的推广
    public static void addWXPlatform(Context context,String url,String nickname, String cover) {
        String appId = "wxd81212f3f3939d91";//wxcfdb2e08decacd35
        String appSecret = "ba3f45ae3ac3810112bbdaa7a20c29ec";//6d54c68436b594e44c2748362dcb489d

        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.setTargetUrl(url);
        wxHandler.setTitle("一边旅行，一边赚钱，就是这么任性");
        wxHandler.addToSocialSDK();

        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setTargetUrl(url);
        wxCircleHandler.setTitle("一边旅行，一边赚钱，就是这么任性");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 添加微信分享文字
        WeiXinShareContent WeixinShareContent1 = new WeiXinShareContent();
        WeixinShareContent1.setTitle("一边旅行，一边赚钱，就是这么任性");
        WeixinShareContent1.setTargetUrl(url);
        // 设置分享文字内容
        WeixinShareContent1.setShareContent("Hi，我是" + nickname + "，欢迎加入我的旅游帮，我在一大帮等你哦！");
        // 设置分享图片
        WeixinShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));
        mController.setShareMedia(WeixinShareContent1);


        // 朋友圈分享文字
        CircleShareContent CircleShareContent1 = new CircleShareContent();
        CircleShareContent1.setTitle("一边旅行，一边赚钱，就是这么任性");
        CircleShareContent1.setTargetUrl(url);
        CircleShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));

        CircleShareContent1.setShareContent("Hi，我是" + nickname + "，欢迎加入我的旅游帮，我在一大帮等你哦！");
        mController.setShareMedia(CircleShareContent1);
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
                SHARE_MEDIA.DOUBAN);

    }


    public static void addWXPlatform(Context context,String url,String title,String nickname,String city,String cover) {
        String appId = "wxd81212f3f3939d91";//wxcfdb2e08decacd35
        String appSecret = "ba3f45ae3ac3810112bbdaa7a20c29ec";//6d54c68436b594e44c2748362dcb489d

        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.setTargetUrl(url);
        if(title != null){
            wxHandler.setTitle(title);
        }
        wxHandler.addToSocialSDK();

        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setTargetUrl(url);
        if(title != null)
            wxCircleHandler.setTitle(title);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 添加微信分享文字
        WeiXinShareContent WeixinShareContent1 = new WeiXinShareContent();
        WeixinShareContent1.setTitle(title);
        WeixinShareContent1.setTargetUrl(url);
        // 设置分享文字内容
        WeixinShareContent1.setShareContent("我是"+nickname+"，这是我在"+city+"游玩的游记，给个赞吧!");
        // 设置分享图片
        WeixinShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));
        mController.setShareMedia(WeixinShareContent1);


        // 朋友圈分享文字
        CircleShareContent CircleShareContent1 = new CircleShareContent();
        CircleShareContent1.setTitle(title);
        CircleShareContent1.setTargetUrl(url);
        CircleShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));

        CircleShareContent1.setShareContent("我是"+nickname+"，这是我在"+city+"游玩的游记，给个赞吧!");
        mController.setShareMedia(CircleShareContent1);
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
                SHARE_MEDIA.DOUBAN);
    }


    public static void addWXPlatform(Context context,String url,String title,String time,String from, String to,String cover) {
        String appId = "wxd81212f3f3939d91";//wxcfdb2e08decacd35
        String appSecret = "ba3f45ae3ac3810112bbdaa7a20c29ec";//6d54c68436b594e44c2748362dcb489d

        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.setTargetUrl(url);
        if(title != null){
            wxHandler.setTitle(title);
        }
        wxHandler.addToSocialSDK();

        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setTargetUrl(url);
        if(title != null)
            wxCircleHandler.setTitle(title);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 添加微信分享文字
        WeiXinShareContent WeixinShareContent1 = new WeiXinShareContent();
        WeixinShareContent1.setTitle(title);
        WeixinShareContent1.setTargetUrl(url);
        // 设置分享文字内容
        WeixinShareContent1.setShareContent("嗨抢一个只需要1元就可以完成梦想的购物天堂");
        // 设置分享图片
        WeixinShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));
        mController.setShareMedia(WeixinShareContent1);

        // 朋友圈分享文字
        CircleShareContent CircleShareContent1 = new CircleShareContent();
        CircleShareContent1.setTitle(title);
        CircleShareContent1.setTargetUrl(url);
        CircleShareContent1.setShareImage(new UMImage(UIApplication.getContext(),
                cover));
        CircleShareContent1.setShareContent("一大帮AA活动：" + DateTimeUtil.formatTime(time, "yyyy-MM-dd") + " 从" + from + "出发，我们去" + to + "玩哦，走起！");

//        CircleShareContent1.setShareContent("快来参与我的一大帮吧~");
        mController.setShareMedia(CircleShareContent1);
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
                SHARE_MEDIA.DOUBAN);
    }


    /**
     * @param context
     * @param share
     */
    public static void promote(final Context context, final SHARE_MEDIA share,String nickname , String cover) {
        mController.setShareContent("Hi，我是" + nickname + "，欢迎加入我的旅游帮，我在一大帮等你哦！");
//        mController.setShareImage(new UMImage(context, cover));
        mController.setShareMedia(new UMImage(UIApplication.getContext(),
                cover));
//        mController.setShareMedia(new UMImage(context, bitmap));
        mController.setAppWebSite("http://m.etripbon.com/index.php/user/user_yidabang?user_id=" + Session.GetString("id"));
        mController.postShare(context, share,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
//                        Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(context, "分享失败[" + eCode + "] " +
//                                    eMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    /**
//     * 游记详情分享到新浪
//     * @param context
//     * @param bitmap
//     */
//    public static void promtoteSina(final Context context, final String travelID) {
//        mController.getConfig().closeToast();
//        mController.setShareContent(Constant.CONTENT + " " + "http://m.etripbon.com/index.php/travel/travel_show?id=" + travelID);// +
//        mController.postShare(context, SINA,
//                new SocializeListeners.SnsPostListener() {
//                    @Override
//                    public void onStart() {
////                        Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                        if (eCode == 200) {
//                            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//                            TravelDetailsActivity.shareCreate(5, travelID);
//                        } else {
//                            String eMsg = "";
//                            if (eCode == -101) {
//                                eMsg = "没有授权";
//                            }
////                            Toast.makeText(context, "分享失败[" + eCode + "] " +
////                                    eMsg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
    /**
     * 活动详情分享到新浪
     * @param context
     * @param bitmap
     */
    public static void promtoteSinaActive(final Context context) {
        // 设置分享内容
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");


//        UMVideo umVideo = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        umVideo.setThumb("http://www.baidu.com/img/bdlogo.png");
//        umVideo.setTitle("友盟社会化组件视频");
//        SinaShareContent sinaShareContent = new SinaShareContent(umVideo);
        // 设置分享到腾讯微博的文字内容
//        sinaShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
        // 设置分享到腾讯微博的多媒体内容
//        mController.setShareMedia(sinaShareContent);




        mController.getConfig().closeToast();
        mController.setShareContent(" " + "http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");// +
        mController.postShare(context, SHARE_MEDIA.SINA,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "分享失败[" + eCode + "] " +
                                    eMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//
//
//    /**
//     * 推广分享到新浪
//     * @param context
//     * @param bitmap
//     */
//    public static void promtoteSina(final Context context, Bitmap bitmap) {
//        mController.getConfig().closeToast();
//        mController.setShareContent(Constant.CONTENT + " " + "http://app.etripbon.com/appdown.htm");// +
//        mController.setShareImage(new UMImage(context, bitmap));
//        mController.postShare(context, SINA,
//                new SocializeListeners.SnsPostListener() {
//                    @Override
//                    public void onStart() {
////                        Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                        if (eCode == 200) {
//                            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String eMsg = "";
//                            if (eCode == -101) {
//                                eMsg = "没有授权";
//                            }
////                            Toast.makeText(context, "分享失败[" + eCode + "] " +
////                                    eMsg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
    /**
     * 游记详情分享
     * @param context
     * @param share
     * @param travelID
     */
    public static void share(final Context context, final SHARE_MEDIA share, final String travelID, String nickname, String city, String cover) {


        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(UIApplication.getContext(),
                cover));

        mController.getConfig().closeToast();
        // 设置分享内容
        mController.setShareContent("我是"+nickname+"，这是我在"+city+"游玩的游记，给个赞吧!");
        mController.setAppWebSite(share, "http://m.etripbon.com/index.php/travel/travel_show?id=" + travelID);
        mController.postShare(context, share,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
//                        Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(context, "分享失败[" + eCode + "] " +
//                                    eMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//
    /**
     * 邀请好友分享
     * @param context
     * @param share
     * @param ActiveId
     */
    public static void shareActive(final Context context, final SHARE_MEDIA share, final String zgoodsId) {
        mController.getConfig().closeToast();
        mController.setShareContent("嗨抢一个只需要1元就可以完成梦想的购物天堂");
        mController.setShareMedia(new UMImage(UIApplication.getContext(), R.drawable.sharelogo
                ));
        mController.setAppWebSite(share, "http://dbback.stzero.cn/oneDream.html?from=timeline&isappinstalled=1");
        mController.postShare(context, share,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                        if (eCode == 200) {
                            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                            if(CommonUtil.isEmpty("zgoodsId")){
                                AsyncHttpTask.post(Config.HTTP_URL_HEAD + "Zerogoods/SharegetZeroCode", new RequestParams("zgoodsId", zgoodsId), new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    }
                                });
                            }
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
