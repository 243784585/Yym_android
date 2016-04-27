package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * Created by Scoot on 2015/12/26.
 * 零元商品以及往期所有揭晓详情
 */
public class GoodsDetail extends BaseEnitity{

    private String zgoodsId;
    private String current_join_num;
    private String open_time;
    private String zgoods_headurl;
    private String zgoods_name;
    private String zgoods_rmb;
    private String now;
    private String isGet;

    public GoodsDetail(JSONObject jsonObject){
        try {
            zgoodsId =jsonObject.optString("zgoodsId");
            current_join_num =jsonObject.optString("current_join_num");
            open_time =jsonObject.optString("open_time");
            zgoods_headurl =jsonObject.optString("zgoods_headurl");
            zgoods_name =jsonObject.optString("zgoods_name");
            zgoods_rmb =jsonObject.optString("zgoods_rmb");
            now =jsonObject.optString("now");
            isGet =jsonObject.optString("isGet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getZgoodsId() {
        return zgoodsId;
    }

    public void setZgoodsId(String zgoodsId) {
        this.zgoodsId = zgoodsId;
    }

    public String getCurrent_join_num() {
        return current_join_num;
    }

    public void setCurrent_join_num(String current_join_num) {
        this.current_join_num = current_join_num;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getZgoods_headurl() {
        return zgoods_headurl;
    }

    public void setZgoods_headurl(String zgoods_headurl) {
        this.zgoods_headurl = zgoods_headurl;
    }

    public String getZgoods_name() {
        return zgoods_name;
    }

    public void setZgoods_name(String zgoods_name) {
        this.zgoods_name = zgoods_name;
    }

    public String getZgoods_rmb() {
        return zgoods_rmb;
    }

    public void setZgoods_rmb(String zgoods_rmb) {
        this.zgoods_rmb = zgoods_rmb;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getIsGet() {
        return isGet;
    }

    public void setIsGet(String isGet) {
        this.isGet = isGet;
    }
}
