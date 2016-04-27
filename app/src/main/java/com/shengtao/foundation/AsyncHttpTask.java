package com.shengtao.foundation;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.utils.Constants;
import com.shengtao.utils.Session;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/14 18:32
 * @tilte：
 * @description： Http异步任务
 */
public class AsyncHttpTask {
    //异步Http调用客户端
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    static {
        asyncHttpClient.setTimeout(Constants.HTTP_TIMEOUT);   //设置链接超时，如果不设置，默认为10s
    }

    /**
     * 用一个完整url获取一个string对象
     *
     * @param httpUri
     * @param httpResponseHandler
     */
    public static void get(String httpUri, AsyncHttpResponseHandler httpResponseHandler) {
        addHeader();
        asyncHttpClient.get(httpUri, httpResponseHandler);
    }

    /**
     * @param urlString
     * @param params
     * @param httpResponseHandler
     */
    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler httpResponseHandler) {   //url里面带参数
        Log.e("AsyncHttpClient_get", Session.GetString("token"));
        addHeader();
        asyncHttpClient.get(urlString, params, httpResponseHandler);
    }

    /**
     * 不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param httpResponseHandler
     */
    public static void get(String urlString, JsonHttpResponseHandler httpResponseHandler) {   //
        addHeader();
        asyncHttpClient.get(urlString, httpResponseHandler);
    }

    /**
     * @param urlString
     * @param params
     * @param httpResponseHandler
     */
    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler httpResponseHandler) {   //带参数，获取json对象或者数组
        Log.e("AsyncHttpClient------", Session.GetString("token"));
        addHeader();
        asyncHttpClient.get(urlString, params, httpResponseHandler);
    }

    /**
     * 下载数据使用，会返回byte数据
     *
     * @param uString
     * @param httpResponseHandler
     */
    public static void get(String uString, BinaryHttpResponseHandler httpResponseHandler)   //
    {
        addHeader();
        asyncHttpClient.get(uString, httpResponseHandler);
    }

    /*************************************POST************************************************/

    /**
     * 用一个完整url获取一个string对象
     *
     * @param httpUri
     * @param httpResponseHandler
     */
    public static void post(String httpUri, AsyncHttpResponseHandler httpResponseHandler) {
        addHeader();
        asyncHttpClient.post(httpUri, httpResponseHandler);
    }

    /**
     * @param urlString
     * @param params
     * @param httpResponseHandler
     */
    public static void post(String urlString, RequestParams params, AsyncHttpResponseHandler httpResponseHandler)   //url里面带参数
    {
//        Log.e("AsyncHttpClient_post", Session.GetString("token"));
        addHeader();
        asyncHttpClient.post(urlString, params, httpResponseHandler);
    }


    /**
     * 不带参数，获取json对象或者数组
     *
     * @param urlString
     * @param httpResponseHandler
     */
    public static void post(String urlString, JsonHttpResponseHandler httpResponseHandler)   //
    {
        addHeader();
        asyncHttpClient.post(urlString, httpResponseHandler);
    }

    /**
     * @param urlString
     * @param params
     * @param httpResponseHandler
     */
    public static void post(String urlString, RequestParams params, JsonHttpResponseHandler httpResponseHandler)   //带参数，获取json对象或者数组
    {
        addHeader();
        asyncHttpClient.post(urlString, params, httpResponseHandler);
    }

    /**
     * 下载数据使用，会返回byte数据
     *
     * @param uString
     * @param httpResponseHandler
     */
    public static void post(String uString, BinaryHttpResponseHandler httpResponseHandler)   //
    {
        addHeader();
        asyncHttpClient.post(uString, httpResponseHandler);
    }

    /**
     * @return
     */
    public static AsyncHttpClient getClient() {
        return asyncHttpClient;
    }

    private static void addHeader() {


        asyncHttpClient.addHeader("token", Session.GetString("token"));

//      asyncHttpClient.addHeader("token", Config.HTTP_HRADER);

    }
}
