package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/29.
 *
 * 往期揭晓所有商品封装
 */
public class PastResult extends BaseEnitity {
    private int code;
    private String error;
    private String attributes;
    private ArrayList<GoodsDetail> goodsDetails = new ArrayList<>();

    public PastResult(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = jsonObject.getJSONArray("info");
                error = jsonObject.getString("error");
                attributes = jsonObject.getString("attributes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    goodsDetails.add(new GoodsDetail(json));
                }
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

    public ArrayList<GoodsDetail> getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(ArrayList<GoodsDetail> goodsDetails) {
        this.goodsDetails = goodsDetails;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}