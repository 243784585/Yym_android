package com.shengtao.domain.shopping;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * Created by Scoot on 2016/1/5.
 */
public class ShoppingCartGoodsDetail extends BaseEnitity {

    private String id;
    private String goodsname;
    private String goodsallnum;
    private String goodsid;
    private String goodscurrentnum;
    private String number;
    private String goodssingleid;
    private String headurl;
    private boolean ischecked;//判断是否选中
    private String isten;




    public ShoppingCartGoodsDetail(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            goodsname = jsonObject.getString("goodsname");
            goodsallnum = jsonObject.getString("goodsallnum");
            goodsid = jsonObject.getString("goodsid");
            goodscurrentnum = jsonObject.getString("goodscurrentnum");
            number = jsonObject.getString("number");
            goodssingleid = jsonObject.getString("goodssingleid");
            headurl = jsonObject.getString("headurl");
            isten =jsonObject.getString("isten");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getGoodssingleid() {
        return goodssingleid;
    }

    public void setGoodssingleid(String goodssingleid) {
        this.goodssingleid = goodssingleid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGoodscurrentnum() {
        return goodscurrentnum;
    }

    public void setGoodscurrentnum(String goodscurrentnum) {
        this.goodscurrentnum = goodscurrentnum;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsallnum() {
        return goodsallnum;
    }

    public void setGoodsallnum(String goodsallnum) {
        this.goodsallnum = goodsallnum;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getIsten() {
        return isten;
    }

    public void setIsten(String isten) {
        this.isten = isten;
    }
}
