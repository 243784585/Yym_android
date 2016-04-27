package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain
 * Created by TT on 2015/12/22.
 * Description：商品
 */
public class Goods extends BaseEnitity{
    private int code;
    private String error;
    private String attributes;
    private List<PublishedGood> publishedGoodList = new ArrayList<>();

    public Goods(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray = jsonObject.getJSONArray("info");
                error = jsonObject.getString("error");
                attributes = jsonObject.getString("attributes");
                int j = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    publishedGoodList.add(new PublishedGood(json));
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

    public List<PublishedGood> getPublishedGoodList() {
        return publishedGoodList;
    }

    public void setPublishedGoodList(List<PublishedGood> publishedGoodList) {
        this.publishedGoodList = publishedGoodList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
