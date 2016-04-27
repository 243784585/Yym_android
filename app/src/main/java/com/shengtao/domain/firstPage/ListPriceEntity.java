package com.shengtao.domain.firstPage;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.firstPage
 * Created by TT on 2016/2/1.
 * Description:首页最新揭晓
 */
public class ListPriceEntity extends BaseEnitity {
    private String id;
    private String goods_headurl;
    private String goods_name;
    private String goods_rmb;
    private String status;
    private String client_name;
    private String head_img;
    private String client_join_num;
    private String open_time;

    public ListPriceEntity(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            goods_headurl = jsonObject.getString("goods_headurl");
            goods_name = jsonObject.getString("goods_name");
            goods_rmb = jsonObject.optString("goods_rmb");
            status = jsonObject.getString("status");
            client_name = jsonObject.getString("client_name");
            head_img = jsonObject.getString("head_img");
            client_join_num = jsonObject.getString("client_join_num");
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

    public String getGoods_rmb() {
        return goods_rmb;
    }

    public void setGoods_rmb(String goods_rmb) {
        this.goods_rmb = goods_rmb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getClient_join_num() {
        return client_join_num;
    }

    public void setClient_join_num(String client_join_num) {
        this.client_join_num = client_join_num;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }
}
