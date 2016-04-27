package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;
import com.shengtao.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/23.
 * Description:
 */
public class NewPublishGoods extends BaseEnitity {

    private int code;
    private String error;
    private String attributes;
    private List<NewPublishedGood> newpublishedGoodList = new ArrayList<>();
    private  String time;

    public NewPublishGoods(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.optString("code"))) {
                JSONObject info = jsonObject.optJSONObject("info");
                time = info.optString("time");

                JSONArray list = info.optJSONArray("list");
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");
                int j = list.length();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject json = list.optJSONObject(i);
                    newpublishedGoodList.add(new NewPublishedGood(json));
                }
            }else{
                ToastUtil.showTextToast(jsonObject.optString("error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<NewPublishedGood> getNewpublishedGoodList() {
        return newpublishedGoodList;
    }

    public void setNewpublishedGoodList(List<NewPublishedGood> newpublishedGoodList) {
        this.newpublishedGoodList = newpublishedGoodList;
    }
}
