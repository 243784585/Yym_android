package com.shengtao.domain.mine;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.mine
 * Created by TT on 2015/12/24.
 * Description:
 */
public class PersonDStateInfo extends BaseEnitity {

    private String id;
    private String goods_current_num;
    private String all_join_num;
    private String current_join_num;
    private String status;
    private String goods_name;
    private String goods_headurl;
    private String rmb_num;
    private String order_code;
    private String client_name;
    private String head_img;
    private String lucky_id;

    private String client_join_num;
    private String open_time;
    private String goods_10;

    public PersonDStateInfo(JSONObject jsonObject) {

        try {
            id = jsonObject.optString("id");
            goods_current_num = jsonObject.optString("goods_current_num");
            all_join_num = jsonObject.optString("all_join_num");
            current_join_num = jsonObject.optString("current_join_num");
            status = jsonObject.optString("status");
            goods_10 = jsonObject.optString("goods_10");
            goods_name = jsonObject.optString("goods_name");
            goods_headurl = jsonObject.optString("goods_headurl");
            rmb_num = jsonObject.optString("rmb_num");
            order_code = jsonObject.optString("order_code");
            client_name = jsonObject.optString("client_name");
            head_img = jsonObject.optString("head_img");
            lucky_id = jsonObject.optString("lucky_id");
            client_join_num = jsonObject.optString("client_join_num");
            open_time = jsonObject.optString("open_time");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGoods_10() {
        return goods_10;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_current_num() {
        return goods_current_num;
    }

    public String getOrder_code() {
        return order_code;
    }

    public String getAll_join_num() {
        return all_join_num;
    }


    public String getCurrent_join_num() {
        return current_join_num;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getGoods_headurl() {
        return goods_headurl;
    }

    public String getRmb_num() {
        return rmb_num;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getLucky_id() {
        return lucky_id;
    }

    public String getClient_join_num() {
        return client_join_num;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }


}
