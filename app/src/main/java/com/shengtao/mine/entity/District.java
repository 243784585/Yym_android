package com.shengtao.mine.entity;

import java.io.Serializable;

/**
 * Created by hxhuang on 2015/12/22 0022.
 */
public class District implements Serializable {

    private String district;

    public District(String district) {
        this.district = district;
    }

    public District(){}

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
