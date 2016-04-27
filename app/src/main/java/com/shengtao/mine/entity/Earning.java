package com.shengtao.mine.entity;

import java.io.Serializable;

/**
 * Created by hxhuang on 2016/1/4 0004.
 */
public class Earning implements Serializable{

    private String time;
    private String value;

    public Earning(String time, String value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }
}
