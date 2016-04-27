package com.shengtao.domain.discover;

import com.shengtao.domain.snache.PublishedGood;
import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scoot on 2015/12/26.
 *0元夺宝
 */
public class ZeroCapture extends BaseEnitity {

    private int code;
    private String error;

    private ArrayList<GoodsDetail> goodsDetail = new ArrayList<>();
    private ArrayList<String> dbCodes = new ArrayList<>();

    public ZeroCapture(JSONObject jsonObject) {
        try {
            if ("0".equals(jsonObject.getString("code"))) {

                JSONArray jsonArray= jsonObject.getJSONArray("info");
                int j =jsonArray.length();
                for(int i = 0; i < j; i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    String db_code = json.getString("db_code");
                    dbCodes.add(db_code);
                }
                error = jsonObject.getString("error");
                JSONArray attributes = jsonObject.getJSONArray("attributes");
                for(int i = 0; i < attributes.length(); i++){
                    JSONObject json1 = attributes.getJSONObject(i);
                    goodsDetail.add(new GoodsDetail(json1));
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

    public ArrayList<GoodsDetail> getGoodsDetail() {
        return goodsDetail;
    }

    public ArrayList<String> getDbCodes() {
        return dbCodes;
    }

    public void setDbCodes(ArrayList<String> dbCodes) {
        this.dbCodes = dbCodes;
    }

    public void setGoodsDetail(ArrayList<GoodsDetail> goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

