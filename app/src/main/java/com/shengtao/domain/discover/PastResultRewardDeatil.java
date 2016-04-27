package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Scoot on 2015/12/30.
 */
public class PastResultRewardDeatil extends BaseEnitity {
    private String zgoodsId;
    private String current_join_num;
    private String open_time;
    private String zgoods_headurl;
    private String zgoods_name;
    private String zgoods_rmb;
    private String lucky_id;
    private String star_time;

    public PastResultRewardDeatil (JSONObject jsonObject){
        try {
            zgoodsId =jsonObject.getString("zgoodsId");
            current_join_num =jsonObject.getString("current_join_num");
            open_time =jsonObject.getString("open_time");
            star_time =jsonObject.getString("star_time");
            zgoods_headurl =jsonObject.getString("zgoods_headurl");
            zgoods_name =jsonObject.getString("zgoods_name");
            zgoods_rmb =jsonObject.getString("zgoods_rmb");
            lucky_id =jsonObject.getString("lucky_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getStar_time() {
        return star_time;
    }

    public void setStar_time(String star_time) {
        this.star_time = star_time;
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

    public String getLucky_id() {
        return lucky_id;
    }

    public void setLucky_id(String lucky_id) {
        this.lucky_id = lucky_id;
    }

    public String getZgoods_rmb() {
        return zgoods_rmb;
    }

    public void setZgoods_rmb(String zgoods_rmb) {
        this.zgoods_rmb = zgoods_rmb;
    }

}
