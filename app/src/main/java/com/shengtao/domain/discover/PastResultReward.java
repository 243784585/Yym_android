package com.shengtao.domain.discover;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Scoot on 2015/12/30.
 * 往期揭晓查看详情
 */
public class PastResultReward extends BaseEnitity {
    private int code;
    private String error;
    private ArrayList<String> info = new ArrayList<>();
    private ArrayList<PastResultRewardDeatil> pastResultRewardDeatils = new ArrayList<>();

    public PastResultReward(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.optString("code"))) {
                JSONArray array = jsonObject.optJSONArray("info");
                if (array !=null) {
                    for (int i = 0, len = array.length(); i < len; i++) {
                        JSONObject object = array.optJSONObject(i);
                        info.add(object.optString("db_code"));
                    }
                }

                error = jsonObject.optString("error");
                JSONArray attributes = jsonObject.optJSONArray("attributes");
                for (int i = 0; i < attributes.length(); i++) {
                    JSONObject json1 = attributes.getJSONObject(i);
                    pastResultRewardDeatils.add(new PastResultRewardDeatil(json1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<PastResultRewardDeatil> getPastResultRewardDeatils() {
        return pastResultRewardDeatils;
    }

    public void setPastResultRewardDeatils(ArrayList<PastResultRewardDeatil> pastResultRewardDeatils) {
        this.pastResultRewardDeatils = pastResultRewardDeatils;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<String> info) {
        this.info = info;
    }
}
