package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/23.
 * Description:奖品详情里该期参与人的信息
 */
public class PrizeDetailsJoinUser extends BaseEnitity {
    private int code;
    private String error;
    private String attributes;
    private List<PrizeDetailsJoinUserInfo> PrizeDetailsJoinUserList = new ArrayList<>();

    public PrizeDetailsJoinUser(){

    }

    public PrizeDetailsJoinUser(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {
                JSONArray jsonArray= jsonObject.getJSONArray("info");
                error = jsonObject.getString("error");
                attributes = jsonObject.getString("attributes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    PrizeDetailsJoinUserList.add(new PrizeDetailsJoinUserInfo(json));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<PrizeDetailsJoinUserInfo> getPrizeDetailsJoinUserList() {
        return PrizeDetailsJoinUserList;
    }

    public void setPrizeDetailsJoinUserList(List<PrizeDetailsJoinUserInfo> prizeDetailsJoinUserList) {
        PrizeDetailsJoinUserList = prizeDetailsJoinUserList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
