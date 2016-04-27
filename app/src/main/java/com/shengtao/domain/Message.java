package com.shengtao.domain;

import com.shengtao.foundation.BaseEnitity;

/**
 * Created by TT on 2015/12/12.
 * explain:消息的实体类
 */
public class Message extends BaseEnitity{
    private String img;
    private String title;
    private String desc;

    public Message() {
    }

    public Message(String img, String title, String desc) {
        this.img = img;
        this.title = title;
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
