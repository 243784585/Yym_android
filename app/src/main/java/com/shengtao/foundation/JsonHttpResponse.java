package com.shengtao.foundation;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.shengtao.utils.LogUtil;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hxhuang on 2015/12/29 0029.
 */
public abstract class JsonHttpResponse extends JsonHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            if(0==response.getInt("code")){
                success(headers,response);
                return;
            }else if(7 == response.getInt("code")){
                Session.ClearSession();
            }
            //返回code为非0
            ToastUtil.showTextToast(response.optString("error"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        LogUtil.e("HttpRequestError("+statusCode+"):",responseString);
    }

    /**
     * 请求接口成功,code为0
     * @param json 后台返回的数据
     */
    protected abstract void success(Header[] headers, JSONObject json);


}
