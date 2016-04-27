package com.shengtao.mine.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2015/12/22 0022.
 */
public class Province implements Serializable {

    private String provice;
    private List<City> cityList = new ArrayList<City>();

    public Province(String provice) {
        this.provice = provice;
    }

    public Province(){}

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
