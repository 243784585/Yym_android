package com.shengtao.domain.shopping;

import com.shengtao.domain.discover.AllShareDetail;
import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2016/1/5.
 */
public class ShoppingCartGoods extends BaseEnitity {
    private int code;
    private String error;
    private String info;
    private ArrayList<ShoppingCartGoodsDetail> shoppingCartGoodsDetails = new ArrayList<>();

    public ShoppingCartGoods(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = jsonObject.getJSONArray("attributes");
                error = jsonObject.getString("error");
                info = jsonObject.getString("info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    shoppingCartGoodsDetails.add(new ShoppingCartGoodsDetail(json));
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ShoppingCartGoodsDetail> getShoppingCartGoodsDetails() {
        return shoppingCartGoodsDetails;
    }

    public void setShoppingCartGoodsDetails(ArrayList<ShoppingCartGoodsDetail> shoppingCartGoodsDetails) {
        this.shoppingCartGoodsDetails = shoppingCartGoodsDetails;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
