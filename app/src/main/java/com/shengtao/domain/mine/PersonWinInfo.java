package com.shengtao.domain.mine;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.mine
 * Created by TT on 2015/12/25.
 * Description:
 */
public class PersonWinInfo extends BaseEnitity {

    private  String id;
    private  String goods_headurl;
    private  String goods_name;
    private  String all_join_num;
    private  String client_join_num;
    private  String lucky_id;
    private  String open_time;

    public PersonWinInfo(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            goods_headurl = jsonObject.getString("goods_headurl");
            goods_name = jsonObject.getString("goods_name");
            all_join_num = jsonObject.getString("all_join_num");
            client_join_num = jsonObject.getString("client_join_num");
            lucky_id = jsonObject.getString("lucky_id");
            open_time = jsonObject.getString("open_time");
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

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public void setGoods_headurl(String goods_headurl) {
        this.goods_headurl = goods_headurl;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getAll_join_num() {
        return all_join_num;
    }

    public void setAll_join_num(String all_join_num) {
        this.all_join_num = all_join_num;
    }

    public String getClient_join_num() {
        return client_join_num;
    }

    public void setClient_join_num(String client_join_num) {
        this.client_join_num = client_join_num;
    }

    public String getLucky_id() {
        return lucky_id;
    }

    public void setLucky_id(String lucky_id) {
        this.lucky_id = lucky_id;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }
}
