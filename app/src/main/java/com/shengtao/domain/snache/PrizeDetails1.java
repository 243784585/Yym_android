package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/23.
 * Description:
 */
public class PrizeDetails1 extends BaseEnitity {


    private  String id;
    private  String istenGoods;
    private  JSONObject currentGoods;
    private  String CurrentNum;
    private  JSONObject upGoodsInfo;
    private  String goodsName;
    private  String goodsHeadurl;
    private  String newGoodsid;
    private  String newGoodsCurrentNum;
    private  String openTime;
    private  String status;
    private  String AllJoinNum;
    private  String CurrentJoinNum;
    private  String client_join_num;
    private  String open_time;
    private  String lucky_id;
    private  String client_name;
    private  String head_img;
    private  String ip_address;
    private  String address_info;
    private  String Goodsid;
    private  String AllNum;
    private  String upclientid;
    private long time;

    public PrizeDetails1(JSONObject jsonObject) {
        try {
            status = jsonObject.optString("status");
            CurrentNum = jsonObject.optString("CurrentNum");


            //判断status的状态从而判断解析那些数据
            if ("0".equals(status)){
                CurrentJoinNum = jsonObject.optString("CurrentJoinNum");
                AllJoinNum = jsonObject.optString("AllJoinNum");

                if(!"1".equals(CurrentNum)) {
                    upGoodsInfo = jsonObject.optJSONObject("upGoodsInfo");
                    Goodsid = upGoodsInfo.optString("Goodsid");
                    open_time = upGoodsInfo.optString("open_time");
                    lucky_id = upGoodsInfo.optString("lucky_id");
                    client_name = upGoodsInfo.optString("client_name");
                    head_img = upGoodsInfo.optString("head_img");
                    ip_address = upGoodsInfo.optString("ip_address");
                    address_info = upGoodsInfo.optString("address_info");
                    client_join_num = upGoodsInfo.optString("client_join_num");
                }
            }
            else if("1".equals(status)){
                openTime = jsonObject.optString("openTime");
            }else{
                currentGoods = jsonObject.optJSONObject("currentGoods");
                if(currentGoods!=null){
                    newGoodsid = currentGoods.optString("newGoodsid");
                    newGoodsCurrentNum = currentGoods.optString("newGoodsCurrentNum");
                }else{
                    //最后一期就跟当前期一样吧
                    newGoodsCurrentNum = jsonObject.optString("CurrentNum");
                }
                upGoodsInfo = jsonObject.optJSONObject("upGoodsInfo");
                Goodsid = upGoodsInfo.optString("Goodsid");
                client_join_num = upGoodsInfo.optString("client_join_num");
                open_time = upGoodsInfo.optString("open_time");
                lucky_id = upGoodsInfo.optString("lucky_id");
                client_name = upGoodsInfo.optString("client_name");
                head_img = upGoodsInfo.optString("head_img");
                ip_address = upGoodsInfo.optString("ip_address");
                address_info = upGoodsInfo.optString("address_info");
            }
            goodsName = jsonObject.optString("goodsName");
            goodsHeadurl = jsonObject.optString("goodsHeadurl");
            AllNum = jsonObject.optString("AllNum");//最大期数
            time = jsonObject.optLong("time");
//            long systemTime = SystemClock.elapsedRealtime();
//            Calendar calendar = Calendar.getInstance(Locale.CHINESE);
//            LogUtil.e("time","服务器时间---"+time+"   系统时间---"+calendar.getTimeInMillis());
            id = jsonObject.optString("id");
            istenGoods = jsonObject.optString("istenGoods");
            upclientid = upGoodsInfo.optString("upclientid");//获奖者id

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUpclientid() {
        return upclientid;
    }

    public void setUpclientid(String upclientid) {
        this.upclientid = upclientid;
    }

    public String getAllNum() {
        return AllNum;
    }

    public void setAllNum(String allNum) {
        AllNum = allNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIstenGoods() {
        return istenGoods;
    }

    public void setIstenGoods(String istenGoods) {
        this.istenGoods = istenGoods;
    }

    public JSONObject getCurrentGoods() {
        return currentGoods;
    }

    public void setCurrentGoods(JSONObject currentGoods) {
        this.currentGoods = currentGoods;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsHeadurl() {
        return goodsHeadurl;
    }

    public void setGoodsHeadurl(String goodsHeadurl) {
        this.goodsHeadurl = goodsHeadurl;
    }

    public String getNewGoodsid() {
        return newGoodsid;
    }

    public void setNewGoodsid(String newGoodsid) {
        this.newGoodsid = newGoodsid;
    }

    public String getNewGoodsCurrentNum() {
        return newGoodsCurrentNum;
    }

    public void setNewGoodsCurrentNum(String newGoodsCurrentNum) {
        this.newGoodsCurrentNum = newGoodsCurrentNum;
    }
    public long getTime() {
        return time;
    }

    public String getCurrentNum() {
        return CurrentNum;
    }

    public void setCurrentNum(String currentNum) {
        CurrentNum = currentNum;
    }

    public JSONObject getUpGoodsInfo() {
        return upGoodsInfo;
    }

    public void setUpGoodsInfo(JSONObject upGoodsInfo) {
        this.upGoodsInfo = upGoodsInfo;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAllJoinNum() {
        return AllJoinNum;
    }

    public void setAllJoinNum(String allJoinNum) {
        AllJoinNum = allJoinNum;
    }

    public String getCurrentJoinNum() {
        return CurrentJoinNum;
    }

    public void setCurrentJoinNum(String currentJoinNum) {
        CurrentJoinNum = currentJoinNum;
    }

    public String getClient_join_num() {
        return client_join_num;
    }

    public void setClient_join_num(String client_join_num) {
        this.client_join_num = client_join_num;
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

    public String getGoodsid() {
        return Goodsid;
    }

    public void setGoodsid(String goodsid) {
        Goodsid = goodsid;
    }
}
