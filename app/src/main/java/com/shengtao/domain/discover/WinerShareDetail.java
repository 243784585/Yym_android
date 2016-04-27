package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * Created by Scoot on 2015/12/28.
 * 获奖晒单详情
 */
public class WinerShareDetail extends BaseEnitity {
    public String shareid;
    public String head_img;
    public String client_name;
    public String share_time;
    public String zgoods_name;
    public String share_title;
    public String share_content;
    public String urlGroup;

    public WinerShareDetail(JSONObject jsonObject){
            shareid =jsonObject.optString("shareid");
            head_img =jsonObject.optString("head_img");
            client_name =jsonObject.optString("client_name");
            share_time =jsonObject.optString("share_time");
            zgoods_name =jsonObject.optString("zgoods_name");
            share_title =jsonObject.optString("share_title");
            share_content=jsonObject.optString("share_content");
            urlGroup = jsonObject.optString("urlGroup");
    }

    public String getShareid() {
        return shareid;
    }

    public void setShareid(String shareid) {
        this.shareid = shareid;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getShare_time() {
        return share_time;
    }

    public void setShare_time(String share_time) {
        this.share_time = share_time;
    }

    public String getZgoods_name() {
        return zgoods_name;
    }

    public void setZgoods_name(String zgoods_name) {
        this.zgoods_name = zgoods_name;
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

    public String getUrlGroup() {
        return urlGroup;
    }

    public void setUrlGroup(String urlGroup) {
        this.urlGroup = urlGroup;
    }
}
