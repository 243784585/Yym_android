package com.shengtao.mine.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxhuang on 2015/12/22 0022.
 */
public class City implements Serializable {

    private String city;
    private List<District> districtList = new ArrayList<District>();

    public City(String city) {
        this.city = city;
    }

    public City(){}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }
}
