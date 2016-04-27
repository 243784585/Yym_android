package com.shengtao.domain;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/22.
 * Description:
 */
public class PublishedGood extends BaseEnitity {

    private  String id;
    private  String goods_current_num;
    private  String open_time;
    private  String lucky_id;
    private  String client_join_num;
    private  String share;
    private  String ip_address;
    private  String address_info;
    private  String client_name;
    private  String head_img;
    private  String goods_name;
    private  String share_title;
    private  String share_content;
    private  String share_time;
    private  String allImgUrl;

    public PublishedGood(JSONObject jsonObject) {

        try {
            id = jsonObject.getString("id");
            goods_current_num = jsonObject.getString("goods_current_num");
            open_time = jsonObject.getString("open_time");
            lucky_id = jsonObject.getString("lucky_id");
            client_join_num = jsonObject.getString("client_join_num");
            share = jsonObject.getString("share");
            ip_address = jsonObject.getString("ip_address");
            address_info = jsonObject.getString("address_info");
            client_name = jsonObject.getString("client_name");
            head_img = jsonObject.getString("head_img");
            goods_name = jsonObject.getString("goods_name");
            share_title = jsonObject.getString("share_title");
            share_content = jsonObject.getString("share_content");
            share_time = jsonObject.getString("share_time");
            allImgUrl = jsonObject.getString("allImgUrl");
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

    public String getGoods_current_num() {
        return goods_current_num;
    }

    public void setGoods_current_num(String goods_current_num) {
        this.goods_current_num = goods_current_num;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getLucky_id() {
        return lucky_id;
    }

    public void setLucky_id(String lucky_id) {
        this.lucky_id = lucky_id;
    }

    public String getClient_join_num() {
        return client_join_num;
    }

    public void setClient_join_num(String client_join_num) {
        this.client_join_num = client_join_num;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getAddress_info() {
        return address_info;
    }

    public void setAddress_info(String address_info) {
        this.address_info = address_info;
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getShare_time() {
        return share_time;
    }

    public void setShare_time(String share_time) {
        this.share_time = share_time;
    }

    public String getAllImgUrl() {
        return allImgUrl;
    }

    public void setAllImgUrl(String allImgUrl) {
        this.allImgUrl = allImgUrl;
    }
}
