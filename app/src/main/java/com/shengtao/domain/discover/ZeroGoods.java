package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/28.
 *
 * 零元商品展示封装
 */
public class ZeroGoods extends BaseEnitity {

    private int code;
    private String error;
    private JSONArray jsonArray;
    private ArrayList<GoodsDetail> goodsDetails = new ArrayList<>();
    private  String info;

    public ZeroGoods(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                info = jsonObject.optString("info");
                error = jsonObject.getString("error");
                jsonArray = jsonObject.getJSONArray("attributes");
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

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

