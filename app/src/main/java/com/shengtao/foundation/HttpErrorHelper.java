package com.shengtao.foundation;

import android.content.Context;

import com.shengtao.R;
import com.shengtao.utils.StringUtil;

import org.json.JSONObject;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/16 09:56
 * @package： com.motu.foundation
 * @classname： HttpErrorHelper.java
 * @description： 对HttpErrorHelper.java类的功能描述
 */
public class HttpErrorHelper {
    public static String getRequestErrorReason(String data) {

        Context context = BaseApplication.getInstance();

        String errorReason = context.getString(R.string.common_text_error_net);

        try {
            JSONObject json = new JSONObject(data);
            if (json != null) {
                int status = json.getInt("status");
                switch (status) {
                    case 400:
                    case 403:
                        errorReason = context.getString(R.string.common_text_server_error);
                        break;
                    case 404:
                        /**
                         * 客户端出错
                         */
                        errorReason = context
                                .getString(R.string.common_text_client_error);
                        break;
                    case 500:
                    case 503:
                        /**
                         * 服务端出错
                         */
                        errorReason = context
                                .getString(R.string.common_text_server_error);
                        break;
                    case 999999:
                        errorReason = json.getString("info");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return errorReason;
    }

    public static boolean isRequestError(String data, HttpError error) {
        //return false;
        if (error != null) {
            return true;
        }

        if (StringUtil.isEmpty(data))
            return true;

        boolean isError = false;

        try {
            JSONObject json = new JSONObject(data);
            if (json != null) {
                String status = json.getString("code");
                if (!status.equals("200")) {
                    isError = true;
                }
            }
        } catch (Exception e) {
            isError = true;
            e.printStackTrace();
        }

        return isError;
    }
}
