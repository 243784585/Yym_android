package com.shengtao.domain;


import java.util.HashMap;
import java.util.Map;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：TT
 * @date： 15/12/16 09:31
 * @package： com.baixi.domain
 * @classname： Config.java
 * @description： 对Config.java类的功能描述
 */
public class Config {
    /**
     * 一页请求的个数
     */
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static Map<String, Object> DEFAULT_CACHE = new HashMap<>();


    /**
     * 保存键值队
     *
     * @param key
     * @param value
     */
    public static void setObject(String key, Object value) {
        DEFAULT_CACHE.put(key, value);
    }

    /**
     * 获取键值对Value值
     *
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        return DEFAULT_CACHE.get(key);
    }

    /*
    * http请求地址头
    * */
//120.25.145.15
//        public static String HTTP_URL_HEAD = "http://192.168.8.114/xingluo/rest/app_lydb/";//http://120.25.145.15/xingluo/rest/app_lydb/
    public static String HTTP_URL_HEAD = "http://112.126.75.199/xingluo/rest/app_lydb/";//http://zhaoplace.vicp.cc/xingluo/rest/app_lydb/
//    public static String HTTP_URL_HEAD = "http://192.168.8.123/lydb/rest/app_lydb/";//http://zhaoplace.vicp.cc/xingluo/rest/app_lydb/
    /*
     * http请求地址头
     */
    public static String HTTP_MODULE_LOGIN = HTTP_URL_HEAD;
    public static String HTTP_MODULE_SNACHE = HTTP_URL_HEAD;
    public static String HTTP_MODULE_MINE = HTTP_URL_HEAD;
    public static String HTTP_MODULE_PUBLISH = HTTP_URL_HEAD;
    public static String HTTP_MODULE_DISCOVER = HTTP_URL_HEAD;
    public static String HTTP_MODULE_SHOPPING = HTTP_URL_HEAD;
    /**
     * HTTP请求头
     */

    public static String HTTP_HRADER = "18816782663-623447281a";
//   public static String HTTP_HRADER = "15869936889-c315b468cd";
//    public static String HTTP_HRADER = "13545681271-e16f293353";

    /**
     * 分享链接
     */

//    public static String HTTP_SHARE_URL = "http://app.etripbon.com/appdown.htm?user_id=" + Session.GetString("id");
    /**
     * 七牛
     */
    public static String HTTP = "http://7xp1b8.com1.z0.glb.clouddn.com/";
    /**
     * 微博SCODE
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    /**
     * 微博重定向地址
     */
    public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    /**
     * 微博APP_KEY
     */
    public static final String APP_KEY_WEI_BO = "1865428524";
    /**
     * 微信第三方登录APP_ID APP_SECRET_WECHAT
     */
    // public static final String APP_ID_WECHAT = "wxf2d76319486d46c0";
    // public static final String APP_ID_WECHAT = "wxd930ea5d5a258f4f";
    public static final String APP_ID_WECHAT = "wxd81212f3f3939d91";
    public static final String APP_SECRET_WECHAT = "ba3f45ae3ac3810112bbdaa7a20c29ec";
    /**
     * QQ第三方登录的APP_ID
     */
    public static final String APP_ID_QQ = "1105052158";
    public static final String APP_KEY_QQ = "rOS4DzXYVz74dgEN";

    /**
     * 短信验证码的key 和 secret
     */
    public static String SMSKEY = "db9fd655b6e5";
    public static String SMSSECRET = "911fbf704bdd964ccada0ad9d4c737a6";

    //外网http请求头
    public static String HTTP_URL_HEADED = HTTP_URL_HEAD;
    /**
     * HTTP请求头
     */

    /**
     * 爱贝云计费的appid,private_key,public_key
     * */
    public static final String APP_ID_AIBEI = "3003969474";
    public static final String PUBLIC_KEY_AIBEI = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHOMoNLvRQ5nGA4XcvCZFMUM7GPa8r+HLxkrX+2y3sqEE7psL45EpYf/vUQD4hQvmZ5IR0GA0/4vjPl9KVEZZ/1uhiUXezS/pdhbkRli26J3sZCVXGoW58zeortUdSnVskY+Zoj8JOMXoPAHGr+Htp8bo8viM6nC4nEx1njGxYZwIDAQAB";
    /**
     * 分享的标题
     */
    public static String sharetitle = "1圆梦一个只需要1元就可以完成梦想的购物天堂";
}
