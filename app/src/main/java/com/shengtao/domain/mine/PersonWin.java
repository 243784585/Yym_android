package com.shengtao.domain.mine;

import com.shengtao.foundation.BaseEnitity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @package com.shengtao.domain.mine
 * Created by TT on 2015/12/25.
 * Description:中奖纪录
 */
public class PersonWin extends BaseEnitity{

    private int code;
    private String error;
    private String attributes;
    private ArrayList<PersonWinInfo> personWinList = new ArrayList<>();
    private JSONArray jsonArray;
    private  String head_url;
    private  String name;
    private  String userid;

    public PersonWin(JSONObject jsonObject) {
        if("0".equals(jsonObject.optString("code"))) {
            try {
                JSONObject info = jsonObject.optJSONObject("info");
                if(info != null) {
                    head_url = info.optString("head_url");
                    name = info.optString("name");
                    userid = info.optString("userid");
                    error = jsonObject.optString("error");
                    attributes = jsonObject.optString("attributes");
                    jsonArray = info.optJSONArray("list");
                }else {
                    jsonArray = jsonObject.optJSONArray("info");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    personWinList.add(new PersonWinInfo(json));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getAttributes() {
        return attributes;
    }

    public ArrayList<PersonWinInfo> getPersonWinList() {
        return personWinList;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }

}
