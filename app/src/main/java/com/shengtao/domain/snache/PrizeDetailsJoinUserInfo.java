package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONObject;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/23.
 * Description:奖品详情里该期参与人的信息1
 */
public class PrizeDetailsJoinUserInfo extends BaseEnitity {

    private  String userId;
    private  String client_name;
    private  String head_img;
    private  String rmb_num;
    private  String create_time;
    private  String ip_address;
    private  String address_info;

    public PrizeDetailsJoinUserInfo(){}

    public PrizeDetailsJoinUserInfo(JSONObject jsonObject) {
        try {
            userId = jsonObject.getString("userId");
            client_name = jsonObject.getString("client_name");
            head_img = jsonObject.getString("head_img");
            rmb_num = jsonObject.getString("rmb_num");
            create_time = jsonObject.getString("create_time");
            ip_address = jsonObject.getString("ip_address");
            address_info = jsonObject.getString("address_info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRmb_num() {
        return rmb_num;
    }

    public void setRmb_num(String rmb_num) {
        this.rmb_num = rmb_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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
}
