package com.shengtao.domain.snache;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.shengtao.domain.snache
 * Created by TT on 2015/12/28.
 * Description:中奖消息
 */
public class WinMsg extends BaseEnitity{
    private int code;
    private String error;
    private String attributes;
    private List<CommonWinmessage> commsgList = new ArrayList<>();
    private List<Zeromessage> zeroList = new ArrayList<>();
    private  JSONArray comArray;
    private  JSONArray zeroArray;

    public WinMsg(JSONObject jsonObject) {
            if ("0".equals(jsonObject.optString("code"))) {

                JSONObject info= jsonObject.optJSONObject("info");
                error = jsonObject.optString("error");
                attributes = jsonObject.optString("attributes");

                comArray = info.optJSONArray("message");
                zeroArray = info.optJSONArray("zeromessage");

                for(int i =0;i<comArray.length();i++){
                    JSONObject comObj = comArray.optJSONObject(i);
                    commsgList.add(new CommonWinmessage(comObj));
                }

                for(int i =0;i<zeroArray.length();i++){
                    JSONObject zeroObj = zeroArray.optJSONObject(i);
                    zeroList.add(new Zeromessage(zeroObj));
                }
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<CommonWinmessage> getCommsgList() {
        return commsgList;
    }

    public void setCommsgList(List<CommonWinmessage> commsgList) {
        this.commsgList = commsgList;
    }

    public List<Zeromessage> getZeroList() {
        return zeroList;
    }

    public void setZeroList(List<Zeromessage> zeroList) {
        this.zeroList = zeroList;
    }

    public JSONArray getComArray() {
        return comArray;
    }

    public void setComArray(JSONArray comArray) {
        this.comArray = comArray;
    }

    public JSONArray getZeroArray() {
        return zeroArray;
    }

    public void setZeroArray(JSONArray zeroArray) {
        this.zeroArray = zeroArray;
    }
}
