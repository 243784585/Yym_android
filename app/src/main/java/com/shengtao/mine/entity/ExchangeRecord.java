package com.shengtao.mine.entity;

import java.io.Serializable;

/**
 * Created by hxhuang on 2016/1/4 0004.
 * 积分兑换记录对象
 */
public class ExchangeRecord implements Serializable{

    private String time;
    private String amount;
    private String state;

    public ExchangeRecord(String time, String amount, String state){
        this.time = time;
        this.amount = amount;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public String getState() {
        return state;
    }
}
