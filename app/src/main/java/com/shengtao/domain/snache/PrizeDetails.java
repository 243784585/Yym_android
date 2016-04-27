package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/22.
 * Description：商品详情
 */
public class PrizeDetails extends BaseEnitity{
    private int code;
    private String error;
    private String attributes;
    private  PrizeDetails1 prizeDetails1;

    public PrizeDetails(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.optString("code"))) {


                JSONObject json= jsonObject.optJSONObject("info");
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                prizeDetails1 = new PrizeDetails1(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public PrizeDetails1 getPrizeDetails1() {
        return prizeDetails1;
    }

    public void setPrizeDetails1(PrizeDetails1 prizeDetails1) {
        this.prizeDetails1 = prizeDetails1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
