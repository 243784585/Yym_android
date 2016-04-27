package com.shengtao.domain.shopping;

import com.shengtao.foundation.BaseEnitity;
import com.shengtao.utils.CommonUtil;

import org.json.JSONObject;

/**
 * Created by Scoot on 2016/1/20.
 */
public class ShoppingBuyDetail extends BaseEnitity {
    private String buyNum;
    private String buyCode;
    private String CurrentNum;
    private String goodsName;

    public ShoppingBuyDetail(JSONObject jsonObject) {
        try {
            buyNum = jsonObject.optString("buynum");
            buyCode = CommonUtil.isEmpty(jsonObject.optString("buycode"))?"-1":jsonObject.optString("buycode");
            CurrentNum = jsonObject.optString("CurrentNum");
            goodsName = jsonObject.optString("goodsName");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getBuyCode() {
        return buyCode;
    }

    public void setBuyCode(String buyCode) {
        this.buyCode = buyCode;
    }

    public String getCurrentNum() {
        return CurrentNum;
    }

    public void setCurrentNum(String currentNum) {
        CurrentNum = currentNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
