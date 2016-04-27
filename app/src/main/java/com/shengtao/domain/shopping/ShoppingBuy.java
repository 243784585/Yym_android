package com.shengtao.domain.shopping;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2016/1/20.
 */
public class ShoppingBuy extends BaseEnitity {

    private int code;
    private String error;
    private String buynumber;
    private String userRMB;
    private ArrayList<ShoppingBuyDetail> shoppingBuyDetails = new ArrayList<>();

    public ShoppingBuy(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {
                error = jsonObject.optString("error");
                JSONObject info = jsonObject.optJSONObject("info");
                buynumber = info.optString("buynumber");
                userRMB = info.optString("userRMB");
                JSONArray jsonArray = jsonObject.getJSONArray("attributes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    shoppingBuyDetails.add(new ShoppingBuyDetail(json));
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

    public String getUserRMB() {
        return userRMB;
    }

    public void setUserRMB(String userRMB) {
        this.userRMB = userRMB;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBuynumber() {
        return buynumber;
    }

    public void setBuynumber(String buynumber) {
        this.buynumber = buynumber;
    }

    public ArrayList<ShoppingBuyDetail> getShoppingBuyDetails() {
        return shoppingBuyDetails;
    }

    public void setShoppingBuyDetails(ArrayList<ShoppingBuyDetail> shoppingBuyDetails) {
        this.shoppingBuyDetails = shoppingBuyDetails;
    }
}
