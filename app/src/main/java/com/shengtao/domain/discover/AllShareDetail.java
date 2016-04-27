package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * Created by Scoot on 2015/12/28.
 *
 * 所有晒单详情
 */
public class AllShareDetail extends BaseEnitity {

    private String userId;
    private String client_name;
    private String head_img;
    private String share_time;
    private String share_title;
    private String share_content;
    private String GROUP_CONCAT;
    private String goods_current_num;
    private String goods_name;
    private String all_join_num;
    private String lucky_id;
    private String open_time;

    public AllShareDetail() {
    }

    public AllShareDetail(JSONObject jsonObject) {
        try {
            userId = jsonObject.getString("userId");
            client_name = jsonObject.getString("client_name");
            head_img = jsonObject.getString("head_img");
            share_time = jsonObject.getString("share_time");
            share_title = jsonObject.getString("share_title");
            share_content = jsonObject.getString("share_content");
            GROUP_CONCAT = jsonObject.getString("GROUP_CONCAT(iu.img_url)");
            open_time = jsonObject.getString("open_time");
            goods_current_num = jsonObject.getString("goods_current_num");
            goods_name = jsonObject.getString("goods_name");
            all_join_num = jsonObject.getString("all_join_num");
            lucky_id = jsonObject.getString("lucky_id");
            open_time = jsonObject.getString("open_time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_time() {
        return share_time;
    }

    public void setShare_time(String share_time) {
        this.share_time = share_time;
    }

    public String getGROUP_CONCAT() {
        return GROUP_CONCAT;
    }

    public void setGROUP_CONCAT(String GROUP_CONCAT) {
        this.GROUP_CONCAT = GROUP_CONCAT;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_current_num() {
        return goods_current_num;
    }

    public void setGoods_current_num(String goods_current_num) {
        this.goods_current_num = goods_current_num;
    }

    public String getAll_join_num() {
        return all_join_num;
    }

    public void setAll_join_num(String all_join_num) {
        this.all_join_num = all_join_num;
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
