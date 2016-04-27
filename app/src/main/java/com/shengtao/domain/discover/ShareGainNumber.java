package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2016/1/18.
 */
public class ShareGainNumber extends BaseEnitity {

    private int code;
    private String error;
    private String attributes;
    private String db_code;

    public ShareGainNumber(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.optString("code"))) {
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                JSONObject info = jsonObject.optJSONObject("info");
                db_code = info.optString("db_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getDb_code() {
        return db_code;
    }

    public void setDb_code(String db_code) {
        this.db_code = db_code;
    }
}
