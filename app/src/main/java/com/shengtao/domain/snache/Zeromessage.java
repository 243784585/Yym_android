package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/28.
 * Description:0元商品
 */
public class Zeromessage extends BaseEnitity implements OpenTimeComparator{

    private  String status;
    private  String id;
    private  String lucky_id;
    private  String open_time;
    private  String goods_current_num;
    private  String zgoods_name;
    private  String share;


    public Zeromessage(JSONObject jsonObject) {
        status = jsonObject.optString("status");
        id = jsonObject.optString("id");
        open_time = jsonObject.optString("open_time");
        goods_current_num = jsonObject.optString("goods_current_num");
        zgoods_name = jsonObject.optString("goods_name");
        lucky_id = jsonObject.optString("lucky_id");
        share = jsonObject.optString("share");
    }

    public String getShare() {
        return share;
    }

    public String getLucky_id() {
        return lucky_id;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLucky_id(String lucky_id) {
        this.lucky_id = lucky_id;
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

}
