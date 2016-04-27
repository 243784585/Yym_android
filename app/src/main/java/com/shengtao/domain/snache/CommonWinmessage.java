package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/28.
 * Description:中奖普通商品
 */
public class CommonWinmessage extends BaseEnitity implements OpenTimeComparator{

    private  String status;
    private  String id;
    private  String open_time;
    private  String goods_current_num;
    private  String zgoods_name;
    private  String isnomal;
    private  String lucky_id;
    private  String goods_name;

    public CommonWinmessage(JSONObject jsonObject) {
        status = jsonObject.optString("status");
        id = jsonObject.optString("id");
        open_time = jsonObject.optString("open_time");
        lucky_id = jsonObject.optString("lucky_id");
        goods_current_num = jsonObject.optString("goods_current_num");
        goods_name = jsonObject.optString("goods_name");
        isnomal = jsonObject.optString("isnomal");
    }

    public String getLucky_id() {
        return lucky_id;
    }

    public void setLucky_id(String lucky_id) {
        this.lucky_id = lucky_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public void setGoods_current_num(String goods_current_num) {
        this.goods_current_num = goods_current_num;
    }

    public void setZgoods_name(String zgoods_name) {
        this.zgoods_name = zgoods_name;
    }

    public String getIsnomal() {
        return isnomal;
    }

    public void setIsnomal(String isnomal) {
        this.isnomal = isnomal;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getGoods_current_num() {
        return goods_current_num;
    }

    public String getZgoods_name() {
        return zgoods_name;
    }

}
