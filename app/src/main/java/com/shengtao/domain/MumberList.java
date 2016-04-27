package com.shengtao.domain;

import java.io.Serializable;

/**
 * Created by TT on 2015/9/22.
 */
public class MumberList implements Serializable {
    private  int id;
    private String user_imgurl;
    private String user_name;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public MumberList() {
    }

    public MumberList(int id, String user_imgurl) {
        this.id = id;
        this.user_imgurl = user_imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_imgurl() {
        return user_imgurl;
    }

    public void setUser_imgurl(String user_imgurl) {
        this.user_imgurl = user_imgurl;
    }




}
