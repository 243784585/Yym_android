package com.shengtao.domain.shopping;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Scoot on 2016/1/13.
 */
public class ShoppingCartBuyLine extends BaseEnitity {
    private int code;
    private String error;
    private String attributes;

    public ShoppingCartBuyLine(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                JSONObject json = jsonObject.getJSONObject("info");
                json.optString("goodsInfo");
                json.optString("joinNum");
                json.optString("CurrentNum");
                json.optString("goodsName");
                JSONArray jsonArray = json.optJSONArray("buyCode");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String buyCode = jsonArray.optString(i);
                }
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
}
